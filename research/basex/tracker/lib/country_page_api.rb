require 'lib/client'
require 'nokogiri'

# Module provides mixin support for accessing BaseX
module BaseXSupport

  @@XQ_BASE_PATH = "#{Dir.pwd}/xq"

  # executes the passed block in the context of a session that gets passed in
  # closes the session after block is executed and returns the result of the
  # transaction block
  def with_session(&transaction)
    session = BaseXClient::Session.new("localhost", 1984, "admin", "admin")
    result = transaction.call(session)
    session.close
    result
  end
end

class CountryPageApi 

  # mixin support for accessing BaseX
  include BaseXSupport

  # retrieves the project budgets by year data from the service
  # in the format expected for drawing the graphs
  def project_budgets_by_year(country_code)
    with_session do |session|

      result = session.execute "SET BINDINGS $country_code=#{country_code}"
      result = session.execute "RUN #{@@XQ_BASE_PATH}/country_summary/project_budget_by_year.xq"

      Nokogiri::XML(result)
    end
  end

  def sector_groups
  end

end