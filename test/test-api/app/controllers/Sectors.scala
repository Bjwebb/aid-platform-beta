package controllers

import play.api.mvc._
import play.api.libs.json.Json

object Sectors extends Controller {

val sectors = Json.obj(
      "sectors" -> Json.arr(
        Json.obj(
          "name" -> "Economic and development policy/planning",
          "budget" -> 1917000000
        ),
        Json.obj(
          "name" -> "Education policy and administrative management",
          "budget" -> 1687000000
        ),
        Json.obj(
          "name" -> "Primary education",
          "budget" -> 1687000000
        ),
        Json.obj(
          "name" -> "Social/ welfare services",
          "budget" -> 1402000000
        ),
        Json.obj(
          "name" -> "Bio-diversity",
          "budget" -> 1391000000
        )
      ))


  def allSectors = Action {
    Ok (
        sectors
      )
  }


}