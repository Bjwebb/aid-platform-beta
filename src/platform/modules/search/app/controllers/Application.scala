package controllers.search

import play.api.mvc._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import uk.gov.dfid.es.ElasticSearch

object Application extends Controller {

  def search = Action(parse.urlFormEncoded) { request =>

    request.body("query").headOption.map { query =>
      val result = ElasticSearch.search(query, scala.util.Properties.envOrElse("DFID_ELASTICSEARCH_PATH", "/dfid/elastic" ))
      Ok(views.html.search(query, result.size , result.toList.map(_.toMap)))
    } getOrElse {
      Ok(views.html.search("", 0 , List.empty))
    }

  }
}