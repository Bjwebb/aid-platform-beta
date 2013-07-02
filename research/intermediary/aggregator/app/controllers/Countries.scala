package controllers

import play.api.mvc._
import play.api.libs.json._
import client._

object Countries extends Controller with BaseXSupport {

  def countries = Action {
    val countries = CountriesApi.countries
    Ok(countries)
  }

  def index(code: String) = Action {

    val budgets = CountriesApi.projectBudgetByYear(code)

    Ok(Json.obj(
      "budgets_by_year" -> budgets
    ))


//    Async {
//
//      WS.url("$ACTIVITY_URL?reporting-org=GB-1&recipient-country=$code").get.map { data =>
//
//        val xml = data.xml
//
//        val response = Json.obj(
//          "name"        -> "Bangladesh",
//          "code"        -> "BD",
//          "description" -> "__Bangladesh__ is a country",
//          "projects"    -> Json.obj(
//            "total"  -> 86,
//            "active" -> 47
//          ),
//          "published_results" -> true,
//          "budget"            -> Json.obj(
//            "total"                     -> 100000000,
//            "financial_year"            -> "2013/2014",
//            "percentage_of_dfid_budget" -> 23
//          ),
//          "facts" -> Json.obj(
//            "life_expectancy" -> Json.obj(
//              "value"  -> 64,
//              "source" -> Json.obj(
//                "name" -> "World Bank",
//                "href" -> "http://worldbank.org"
//              )
//            ),
//            "population" -> Json.obj(
//              "value"  -> 10000000,
//              "source" -> Json.obj(
//                "name" -> "World Bank",
//                "href" -> "http->//worldbank.org"
//              )
//            )
//          ),
//          "sector_groups" -> Json.arr(
//            Json.obj(
//              "name"       -> "Health",
//              "percentage" -> 20.15
//            ),
//            Json.obj(
//              "name"       -> "Government",
//              "percentage" -> 18.66
//            ),
//            Json.obj(
//              "name"       -> "Education",
//              "percentage" -> 13.93
//            ),
//            Json.obj(
//              "name"       -> "Multisector",
//              "percentage" -> 9.95
//            ),
//            Json.obj(
//              "name"       -> "Environment",
//              "percentage" -> 5.97
//            ),
//            Json.obj(
//              "name"       -> "Other",
//              "percentage" -> 31.34
//            )
//          ),
//          "budgets_by_year" -> projectBugetsByYear(xml),
//          "locations" -> Json.arr(
//            Json.obj(
//              "name"      -> "Ramgarh",
//              "longitude" -> 91.7417143,
//              "latitude"  -> 22.9946547,
//              "precision" -> "Second order administrative division",
//              "type"      -> "populated place",
//              "id"        -> "GB-1-107371",
//              "title"     -> "Sanitation, Hygiene, Education & Water Supply in Bangladesh (SHEWA-B)"
//            ),
//            Json.obj(
//              "name"      -> "Mahalchari",
//              "longitude" -> 92.029768,
//              "latitude"  -> 22.9316977,
//              "precision" -> "Second order administrative division",
//              "type"      -> "populated place",
//              "id"        -> "GB-1-107371",
//              "title"     -> "Sanitation, Hygiene, Education & Water Supply in Bangladesh (SHEWA-B)"
//            )
//          )
//        )
//
//        Ok(response)
//      }
//    }
  }
}
