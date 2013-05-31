package lib

import play.api.mvc.{Request, Controller}
import org.neo4j.graphdb.{Node, GraphDatabaseService}
import org.neo4j.cypher.ExecutionEngine
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json


trait ApiController { self: Controller =>

  val graph: GraphDatabaseService
  val engine: ExecutionEngine = new ExecutionEngine(graph)

  val paging = Form(
    tuple(
      "start" -> default(longNumber, 0L),
      "limit" -> default(longNumber, 100L)
    )
  )

  def single(entity: String, id: String, property: String = "iati-identifier") = {
    engine.execute(
      s"""
        | START    node = node:entities(type="$entity")
        | WHERE    node.`$property`! = '$id'
        | RETURN   node
      """.stripMargin).columnAs[Node]("node").toSeq.headOption
  }

  def list(entity: String, sort: String = "iati-identifier")(implicit request: Request[_]) = {
    val (start, limit) = paging.bindFromRequest.get
    Map("start" -> start, "limit" -> limit, "total" ->engine.execute(
      s"""
      | START    node = node:entities(type="$entity")
      | RETURN   COUNT(DISTINCT(node.`$sort`?)) as total
    """.stripMargin).columnAs[Long]("total").toSeq.head) -> (engine.execute(
      s"""
      | START    node = node:entities(type="$entity")
      | RETURN   node
      | ORDER BY STR(node.`$sort`?)
      | SKIP     $start
      | LIMIT    $limit
    """.stripMargin).columnAs[Node]("node").toSeq)
  }

}
