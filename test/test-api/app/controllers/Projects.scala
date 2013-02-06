package controllers

import play.api.mvc._
import play.api.libs.json.Json

object Projects extends Controller {

val projects = Json.obj(
      "projects" -> Json.arr(
        Json.obj(
          "name" -> "Malaria bed nets distributed",
          "budget" -> 12200000
        ),
        Json.obj(
          "name" -> "Children vaccinated",
          "budget" -> 12000000
        ),
        Json.obj(
          "name" -> "Access to financial services",
          "budget" -> 11900000
        ),
        Json.obj(
          "name" -> "Hygiene conditions improved",
          "budget" -> 7400000
        ),
        Json.obj(
          "name" -> "Emergency food assistance",
          "budget" -> 6000000
        )
      ))


  def allProjects = Action {
    Ok (
        projects
      )
  }


}