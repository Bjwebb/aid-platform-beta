package uk.gov.dfid.loader

import org.neo4j.cypher.ExecutionEngine
import uk.gov.dfid.common.DataLoadAuditor
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import reactivemongo.bson._
import concurrent.Await
import concurrent.duration.Duration
import reactivemongo.bson.handlers.DefaultBSONHandlers._
import concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.BSONLong
import reactivemongo.bson.BSONDateTime
import reactivemongo.api.DefaultDB
import reactivemongo.bson.BSONString

class ProjectAggregator(engine: ExecutionEngine, db: DefaultDB, auditor: DataLoadAuditor) {

  private val format = DateTimeFormat.forPattern("yyyy-MM-ddd")

  def collectPartnerTransactions = {

    val results = db.collection("funded-projects").find(
      BSONDocument(),
      BSONDocument("funded" -> BSONInteger(1))
    ).toList

    val projects = Await.result(results, Duration Inf)
    val whereClause = projects.map(
      _.getAs[BSONString]("funded").map(_.value).get
    ).mkString("WHERE n.`iati-identifier` IN ['", "', '", "']")

    engine.execute(
      s"""
        | START n=node:entities(type="iati-activity")
        | MATCH n-[:transaction]-txn,
        |       txn-[:value]-value,
        |       txn-[:`transaction-date`]-date,
        |       txn-[:`transaction-type`]-type,
        |       txn-[r?:`receiver-org`]-receiver,
        |       txn-[p?:`provider-org`]-provider
        | $whereClause
        | RETURN n.`iati-identifier`            as id,
        |        COALESCE(txn.description?, "") as description,
        |        value.value                    as value,
        |        COALESCE(receiver.`receiver-org`?, txn.`receiver-org`?, "") as `receiver-org`,
        |        COALESCE(provider.`provider-org`?,"") as `provider-org`,
        |        COALESCE(provider.`provider-activity-id`?,"") as `provider-activity-id`,
        |        date.`iso-date`                as date,
        |        type.code                      as type
      """.stripMargin).foreach { row =>

      val project          = row("id").asInstanceOf[String]
      val value            = row("value").asInstanceOf[Long]
      val date             = DateTime.parse(row("date").asInstanceOf[String], format)
      val transaction      = row("type").asInstanceOf[String]
      val receiver         = row("receiver-org").asInstanceOf[String]
      val provider         = row("provider-org").asInstanceOf[String]
      val providerActivity = row("provider-activity-id").asInstanceOf[String]
      val component        = ""
      val description      = row("description").asInstanceOf[String]

      println(s"inserting: $project $value $description")
      db.collection("transactions").insert(
        BSONDocument(
          "project"                -> BSONString(project),
          "component"              -> BSONString(component),
          "description"            -> BSONString(description),
          "receiver-org"           -> BSONString(receiver),
          "provider-org"           -> BSONString(provider),
          "provider-activity-id"   -> BSONString(providerActivity),
          "value"                  -> BSONLong(value),
          "date"                   -> BSONDateTime(date.getMillis),
          "type"                   -> BSONString(transaction)
        )
      )
    }
  }

  def collectTransactions = {

    auditor.info("Collecting Project Transactions")

    Await.ready(db.collection("transactions").drop, Duration Inf)

    engine.execute(
      """
        | START  txn = node:entities(type="transaction")
        | MATCH  project-[:`related-activity`]-component-[:transaction]-txn,
        |        component-[:`reporting-org`]-org,
        |        txn-[:value]-value,
        |        txn-[:`transaction-date`]-date,
        |        txn-[:`transaction-type`]-type,
        |        txn-[r?:`receiver-org`]-receiver
        | WHERE  project.type = 1
        | AND    org.ref      = "GB-1"
        | RETURN project.ref                    as project,
        |        component.`iati-identifier`    as component,
        |        COALESCE(txn.description?, "") as description,
        |        COALESCE(component.title?, "") as title,
        |        COALESCE(receiver.`receiver-org`?, txn.`receiver-org`?, "") as `receiver-org`,
        |        value.value                    as value,
        |        date.`iso-date`                as date,
        |        type.code                      as type
      """.stripMargin).foreach { row =>

      val project     = row("project").asInstanceOf[String]
      val value       = row("value").asInstanceOf[Long]
      val date        = DateTime.parse(row("date").asInstanceOf[String], format)
      val transaction = row("type").asInstanceOf[String]
      val component   = row("component").asInstanceOf[String]
      val description = row("description").asInstanceOf[String]
      val receiver    = row("receiver-org").asInstanceOf[String]
      val title       = row("title").asInstanceOf[String]

      db.collection("transactions").insert(
        BSONDocument(
          "project"       -> BSONString(project),
          "component"     -> BSONString(component),
          "description"   -> BSONString(description),
          "receiver-org"  -> BSONString(receiver),
          "title"         -> BSONString(title),
          "value"         -> BSONLong(value),
          "date"          -> BSONDateTime(date.getMillis),
          "type"          -> BSONString(transaction)
        )
      )
    }

    auditor.success("Collected Project Transactions")
  }

  def collectProjectDetails = {
    val format = DateTimeFormat.forPattern("yyyy-MM-ddd")

    auditor.info("Getting project start and end dates")

    engine.execute(
      """
        | START n=node:entities(type="iati-activity")
        | MATCH n-[:`reporting-org`]-o,
        |       n-[:`activity-status`]-a,
        |       n-[:`activity-date`]-d
        | WHERE n.hierarchy = 1
        | AND   o.ref = "GB-1"
        | RETURN distinct(n.`iati-identifier`) as id, d.type as type, COALESCE(d.`iso-date`?, d.`activity-date`) as date
      """.stripMargin).foreach { row =>

      val id       = row("id").asInstanceOf[String]
      val dateType = row("type").asInstanceOf[String]
      val date     = DateTime.parse(row("date").asInstanceOf[String], format)

      db.collection("projects").update(
        BSONDocument("iatiId" -> BSONString(id)),
        BSONDocument("$set" -> BSONDocument(
          dateType -> BSONDateTime(date.getMillis)
        )),
        upsert = false, multi = false
      )
    }

    auditor.success("Project dates added")
  }

  def collectProjectSectorGroups = {

    val projectSectorBudgets = db.collection("project-sector-budgets")
    auditor.info("Getting project sector groups")
    try {
      engine.execute(
        """
          | START  n=node:entities(type="iati-activity")
          | MATCH  n-[:`reporting-org`]-o,
          |   	   n-[:`related-activity`]-a,
          |        n-[:`budget`]-b-[:`value`]-v,
          |        n-[:`sector`]-s
          | WHERE  n.hierarchy = 2
          | AND    o.ref = "GB-1"
          | AND	   a.type = 1
          | RETURN a.ref as id, s.code as code, s.sector as name,
          |        COALESCE(s.percentage?, 100) as percentage, sum(v.value) as val,
          |        (COALESCE(s.percentage?, 100) / 100.0 * sum(v.value)) as total
          | ORDER BY id asc, total desc
        """.stripMargin).foreach { row =>

        val id          = row("id").asInstanceOf[String]
        val name        = row("name").asInstanceOf[String]
        val code        = row("code").asInstanceOf[Long]
        val total       = row("total")  match {
          case v: java.lang.Integer => v.toLong
          case v: java.lang.Long    => v.toLong
          case v: java.lang.Double  => v.toLong
        }

        projectSectorBudgets.insert(
          BSONDocument(
            "projectIatiId" -> BSONString(id),
            "sectorName"    -> BSONString(name),
            "sectorCode"    -> BSONLong(code),
            "sectorBudget"  -> BSONLong(total)
          )
        )
      }
      auditor.success("Collected project sector groups")
    } catch {
      case e: Throwable => println(e.getMessage); println(e.getStackTraceString)
    }
  }

  def collectPartnerProjects = {

    auditor.info("Collecting Partner Projects")

    Await.ready(db.collection("funded-projects").drop, Duration.Inf)

    engine.execute("""
       | START  n=node:entities(type="iati-activity")
       | MATCH  n-[:`participating-org`]-o,
       |        n-[:`reporting-org`]-ro,
       |        n-[:transaction]-t-[:`transaction-type`]-tt,
       |        n-[:description]-d,
       |        n-[:title]-ttl,
       |        t-[:value]-v,
       |        n-[:`activity-status`]-status,
       |        t-[:`provider-org`]-po
       | WHERE  o.role  = "Funding"
       | AND    o.ref   = "GB-1"
       | AND    tt.code = "IF"
       | RETURN n.`iati-identifier`       as funded      ,
       |        ro.`reporting-org`        as reporting   ,
       |        ttl.title                 as title       ,
       |        d.description             as description ,
       |        po.`provider-activity-id` as funding     ,
       |        SUM(v.value)              as funds       ,
       |        status.code               as status
     """.stripMargin).toSeq.foreach { row =>

      val funded      = row("funded").asInstanceOf[String]
      val reporting   = row("reporting").asInstanceOf[String]
      val title       = row("title").asInstanceOf[String]
      val description = row("description").asInstanceOf[String]
      val funding     = row("funding").asInstanceOf[String]
      val status      = row("status").asInstanceOf[Long]
      val funds       = row("funds") match {
        case v: java.lang.Integer => v.toLong
        case v: java.lang.Long    => v.toLong
      }

      val project = engine.execute(
        s"""
          | START  v=node:entities(type="iati-activity")
          | MATCH  v-[:`related-activity`]-a
          | WHERE  v.`iati-identifier` = '$funding'
          | AND    a.type=1
          | RETURN a.ref as id
        """.stripMargin).toSeq.head("id").asInstanceOf[String]

      // now we need to sum up the project budgets and spend.  this is not specific
      // to dfid itself.  While here we can also grab the status
      // now we need to sum up the project budgets and spend.
      val totalBudget = engine.execute(
        s"""
           | START  funded=node:entities(type="iati-activity")
           | MATCH  funded-[:budget]-budget-[:value]-budget_value
           | WHERE  funded.`iati-identifier` = '$funded'
           | RETURN SUM(budget_value.value) as totalBudget
        """.stripMargin).toSeq.head("totalBudget") match {
        case v: java.lang.Integer => v.toLong
        case v: java.lang.Long    => v.toLong
      }

      val totalSpend = engine.execute(
        s"""
           | START  funded=node:entities(type="iati-activity")
           | MATCH  funded-[:transaction]-transaction-[:value]-transaction_value,
           | transaction-[:`transaction-type`]-type
           | WHERE  funded.`iati-identifier` = '$funded'
           | AND    (type.`code` = 'D' OR type.`code` = 'E')
           | RETURN SUM(transaction_value.value) as totalSpend
        """.stripMargin).toSeq.head("totalSpend") match {
        case v: java.lang.Integer => v.toLong
        case v: java.lang.Long    => v.toLong
      }

      // then we need to get the dates as well
      val dates = engine.execute(
        s"""
          | START  n=node:entities(type="iati-activity")
          | MATCH  d-[:`activity-date`]-n-[:`activity-status`]-a
          | WHERE  n.`iati-identifier` = '$funded'
          | RETURN d.type as type, COALESCE(d.`iso-date`?, d.`activity-date`) as date
        """.stripMargin).toSeq.map { row =>

        val dateType = row("type").asInstanceOf[String]
        val date     = DateTime.parse(row("date").asInstanceOf[String], format)

        dateType -> BSONDateTime(date.getMillis)
      }

      db.collection("funded-projects").insert(
        BSONDocument(
          "funded"      -> BSONString(funded),
          "funding"     -> BSONString(project),
          "title"       -> BSONString(title),
          "reporting"   -> BSONString(reporting),
          "description" -> BSONString(description),
          "funds"       -> BSONLong(funds),
          "totalBudget" -> BSONLong(totalBudget),
          "totalSpend"  -> BSONLong(totalSpend),
          "status"      -> BSONLong(status)
        ).append(dates: _*)
      )

      // put the project budgets in
      engine.execute(
        s"""
          | START  b=node:entities(type="budget")
          | MATCH  v-[:value]-b-[:budget]-n
          | WHERE  n.`iati-identifier` = '$funded'
          | RETURN v.value        as value,
          |        v.`value-date` as date
        """.stripMargin).foreach { row =>

        val value = row("value").asInstanceOf[Long].toInt
        val date = row("date").asInstanceOf[String]

        db.collection("project-budgets").insert(
          BSONDocument(
            "id"    -> BSONString(funded),
            "value" -> BSONInteger(value),
            "date"  -> BSONString(date)
          )
        )
      }

      // ok now we need to work out the sector breakdown for the project
      engine.execute(
        s"""
          | START  n=node:entities(type="iati-activity")
          | MATCH  s-[:sector]-n-[:`budget`]-b-[:`value`]-v
          | WHERE  n.`iati-identifier` = '$funded'
          | RETURN s.code                                                as code,
          |        s.sector?                                             as name,
          |        COALESCE(s.percentage?, 100)                          as percentage,
          |        (COALESCE(s.percentage?, 100) / 100.0 * sum(v.value)) as total
        """.stripMargin).foreach { row =>

        val name = row("name") match {
          case null          => None
          case value: String => Some(value)
        }
        val code        = row("code").asInstanceOf[Long]
        val total       = row("total")  match {
          case v: java.lang.Integer => v.toLong
          case v: java.lang.Long    => v.toLong
          case v: java.lang.Double  => v.toLong
        }

        db.collection("project-sector-budgets").insert(
          BSONDocument(
            "projectIatiId" -> BSONString(funded),
            "sectorCode"    -> BSONLong(code),
            "sectorBudget"  -> BSONLong(total)
          ).append(
            Seq(
              name.map("sectorName" -> BSONString(_))
            ).flatten:_*
          )
        )
      }
    }

    auditor.success("Collected Partner Projects")
  }

  def collectProjectLocations = {

    auditor.info("Collecting project locations")

    // drop the collection as we will build is all from scratch here
    Await.ready(db.collection("locations").drop, Duration.Inf)

    engine.execute(
      """
        | START  location=node:entities(type='location')
        | MATCH  org-[:`reporting-org`]-project-[:location]-location-[:coordinates]-coordinates
        | WHERE  org.ref='GB-1'
        | RETURN project.`iati-identifier` as id,
        |        location.name             as name,
        |        coordinates.precision     as precision,
        |        coordinates.longitude     as longitude,
        |        coordinates.latitude      as latitude
      """.stripMargin).foreach { row =>

      val id        = row("id").asInstanceOf[String]
      val name      = row("name").asInstanceOf[String]
      val precision = row("precision").asInstanceOf[Long]
      val longitude = row("longitude").asInstanceOf[Double]
      val latitude  = row("latitude").asInstanceOf[Double]

      db.collection("locations").insert(BSONDocument(
        "id"        -> BSONString(id),
        "name"      -> BSONString(name),
        "precision" -> BSONLong(precision),
        "longitude" -> BSONDouble(longitude),
        "latitude"  -> BSONDouble(latitude)
      ))
    }

    auditor.success("Collected all project locations")
  }
}
