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
    t1 = Time.now
    result = transaction.call(session)
    t2 = Time.now
    puts "Transaction Took #{t2 - t1}ms"
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
    run_for_country(country_code, "country_summary/project_budget_by_year.xq")
  end

  def sector_groups(country_code)

    xml  = run_for_country(country_code, "country_summary/budget_by_sector.xq")
    xml  = xml.xpath('//data/sector').map do |sector|
      [sector.attr('code'), Float(sector.content)]
    end

    top  = xml[0..4]
    tail = ['Other', xml[5..-1].reduce(0) { |sum, item| sum + item.last }]
    out  = top << tail

    Hash[*out.flatten]
  end

  def country_budget(country_code)
    run_for_country(country_code, "country_summary/country_budget.xq").content
  end

private

  def run_for_country(country_code, script)
    with_session do |session|
      result = session.execute "SET BINDINGS $country_code=#{country_code}"
      result = session.execute "RUN #{@@XQ_BASE_PATH}/#{script}"

      Nokogiri::XML(result)
    end
  end

end