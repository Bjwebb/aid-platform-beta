package client

import play.api.libs.json.Json

object CountriesApi extends BaseXSupport {

  def projectBudgetByYear(code: String) = {
   val budgets = withSession { session =>
     session.execute(s"SET BINDINGS $$country_code=$code")
     session.run("country_summary/project_budget_by_year")
   }

   Json.parse(budgets)
  }

  def sectorGroups(code: String) = {
  }

  def countries = withSession(_.run("dfid_countries"))

}
