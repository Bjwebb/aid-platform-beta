package controllers

import play.api.mvc._
import play.api.libs.json._

object Countries extends Controller {

  val countries: JsValue = Json.parse("""
    {
      "country" : [
      {
         "code" : "ET",
         "name" : "Ethiopia",
         "budget" : 238000000
      },
      {
         "code" : "PK",
         "name" : "Pakistan",
         "budget" : 26700000
      },
      {
         "code" : "IN",
         "name" : "India",
         "budget" : 264000000
      }
    ]

   }
   """)
  def allCountries = Action {
    Ok (
          (countries\"country")
      )
  }

  def country(code: String) = Action {
    Ok (
         (countries\"country")(0)
      )
  }


}