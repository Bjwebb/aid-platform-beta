package controllers

import play.api.mvc._
import play.api.libs.json._

object Countries extends Controller {

  val countries: JsValue = Json.parse("""
                                      {
                                        "countries" : {
                                        "ET" : {
                                                   "name" : "Ethiopia",
                                                   "summary" : "Ethiopia has experienced impressive growth and development in recent years.",
                                                   "budget" : 238000000,
                                                   "active-projects" : 26,
                                                   "population" : 67760000,
                                                   "life expectancy" : 59,
                                                   "income-level" : "low"
                                                },
                                        "PK" : {
                                                   "name" : "Pakistan",
                                                   "summary" : "Some 60 million people (one in three) live in poverty. Half of all adults, and two out of every three women, are illiterate",
                                                   "budget" : 26700000,
                                                   "active-projects" : 12,
                                                   "population" : 4760200,
                                                   "life expectancy" : 48,
                                                   "income-level" : "low"
                                                },
                                        "IN" : {
                                                   "name" : "India",
                                                   "summary" : "India is a key strategic partner to the UK and the largest democracy in the Commonwealth",
                                                   "budget" : 264000000,
                                                   "active-projects" : 4,
                                                   "population" : 99800000,
                                                   "life expectancy" : 65,
                                                   "income-level" :  "mid-low"
                                               }
                                      }

                                     }
                                      """)
  def allCountries = Action {
    Ok (
          (countries \ "countries")
       )
  }

  def country(code: String) = Action {
    Ok (
           (countries \ "countries" \ code)
       )
  }


}