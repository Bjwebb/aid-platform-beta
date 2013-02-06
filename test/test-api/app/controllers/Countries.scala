package controllers

import play.api.mvc._
import play.api.libs.json.Json

object Countries extends Controller {

val countries = Json.obj(
      "countries" -> Json.arr(
        Json.obj(
          "code" -> "ET",
          "name" -> "Ethiopia",
          "budget" -> 238000000
        ),
        Json.obj(
          "code" -> "PK",
          "name" -> "Pakistan",
          "budget" -> 267000000
        ),
        Json.obj(
          "code" -> "IN",
          "name" -> "India",
          "budget" -> 264000000
        ),
        Json.obj(
          "code" -> "BD",
          "name" -> "Bangladesh",
          "budget" -> 210000000
        ),
        Json.obj(
          "code" -> "NG",
          "name" -> "Nigeria",
          "budget" -> 185000000
        )
      ))


  def allCountries = Action {
    Ok (
        countries
      )
  }

  def country(code: String) = Action {
    Ok (
        code
      )
  }


}