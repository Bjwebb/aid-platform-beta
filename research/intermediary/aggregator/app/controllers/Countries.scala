package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.ws.WS
import scala.xml.Elem
import org.joda.time.DateTime
import concurrent.ExecutionContext.Implicits.global
import play.api.data.{FormError, Form}
import play.api.data.Forms._

object Countries extends Controller {

  private val ACTIVITY_URL = "http://iati-datastore.herokuapp.com/api/1/access/activity.xml"

  def countries = Action {

    val response = Json.obj(
      "success" -> true,
      "results" -> Json.arr(
        Json.obj(
          "code" -> "BD"
        )
      )
    )

    Ok(response)
  }

  def index(code: String) = Action {

    Async {

      WS.url(s"$ACTIVITY_URL?reporting-org=GB-1&recipient-country=$code&per_page=999").get.map { data =>

        val xml = data.xml

        val response = Json.obj(
          "name"        -> "Bangladesh",
          "code"        -> "BD",
          "description" -> "__Bangladesh__ is a country",
          "projects"    -> Json.obj(
            "total"  -> 86,
            "active" -> 47
          ),
          "published_results" -> true,
          "budget"            -> Json.obj(
            "total"                     -> 100000000,
            "financial_year"            -> "2013/2014",
            "percentage_of_dfid_budget" -> 23
          ),
          "facts" -> Json.obj(
            "life_expectancy" -> Json.obj(
              "value"  -> 64,
              "source" -> Json.obj(
                "name" -> "World Bank",
                "href" -> "http://worldbank.org"
              )
            ),
            "population" -> Json.obj(
              "value"  -> 10000000,
              "source" -> Json.obj(
                "name" -> "World Bank",
                "href" -> "http->//worldbank.org"
              )
            )
          ),
          "sector_groups" -> Json.arr(
            Json.obj(
              "name"       -> "Health",
              "percentage" -> 20.15
            ),
            Json.obj(
              "name"       -> "Government",
              "percentage" -> 18.66
            ),
            Json.obj(
              "name"       -> "Education",
              "percentage" -> 13.93
            ),
            Json.obj(
              "name"       -> "Multisector",
              "percentage" -> 9.95
            ),
            Json.obj(
              "name"       -> "Environment",
              "percentage" -> 5.97
            ),
            Json.obj(
              "name"       -> "Other",
              "percentage" -> 31.34
            )
          ),
          "budgets_by_year" -> projectBugetsByYear(xml),
          "locations" -> Json.arr(
            Json.obj(
              "name"      -> "Ramgarh",
              "longitude" -> 91.7417143,
              "latitude"  -> 22.9946547,
              "precision" -> "Second order administrative division",
              "type"      -> "populated place",
              "id"        -> "GB-1-107371",
              "title"     -> "Sanitation, Hygiene, Education & Water Supply in Bangladesh (SHEWA-B)"
            ),
            Json.obj(
              "name"      -> "Mahalchari",
              "longitude" -> 92.029768,
              "latitude"  -> 22.9316977,
              "precision" -> "Second order administrative division",
              "type"      -> "populated place",
              "id"        -> "GB-1-107371",
              "title"     -> "Sanitation, Hygiene, Education & Water Supply in Bangladesh (SHEWA-B)"
            )
          )
        )

        Ok(response)
      }
    }
  }

  private def projectBugetsByYear(xml: Elem) = {

    // sort the budgets by their period start
    val budgets = (xml \\ "budget").sortBy { budget =>
      (budget \\ "period-start" \ "@iso-date").head.text
    }

    // convert the budget nodes into a FYXX/XX and Budget Value pair
    val sortedBudgets = budgets.map { budget =>

      val value            = (budget \ "value").head.text.toLong
      val periodStart      = (budget \\ "period-start" \ "@iso-date").head.text
      val periodStartDate  = DateTime.parse(periodStart)
      val year             = periodStartDate.getYear
      val (fyStart, fyEnd) = if(periodStartDate.getMonthOfYear < 4) {
        s"${year-1}".substring(2) -> s"$year".substring(2)
      } else {
        s"$year".substring(2) -> s"${year+1}".substring(2)
      }

      s"FY$fyStart/$fyEnd" -> value
    }

    // group the collection by their respective FY
    val groupedBudgets = sortedBudgets.groupBy { case (fy, _) => fy }

    // convert the groups into FY -> value pairs by summing
    // up the values
    val aggregatedBudgets = groupedBudgets.map { case (fy, budgets) =>
      Json.obj(
        "year" -> fy,
        "budget" -> budgets.foldLeft(0L)(_ + _._2)
      )
    }.take(6)

    JsArray(aggregatedBudgets.toSeq)
  }


}
