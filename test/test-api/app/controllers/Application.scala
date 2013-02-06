package controllers

import play.api.mvc._
import play.api.libs.json.Json

object Application extends Controller {

  def index = Action {
    Ok(Json.obj(
      "top-countries" -> Json.arr(
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
        )
      )
    ))
  }

}