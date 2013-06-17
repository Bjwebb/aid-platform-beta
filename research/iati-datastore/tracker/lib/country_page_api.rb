require 'nokogiri'
require 'open-uri'
require 'date'

class CountryPageApi

  # retrieves the project budgets by year data from the service
  # in the format expected for drawing the graphs
  def project_budgets_by_year(country_code)

    # get the GB-1 budgets for a country
    doc = Nokogiri::XML(open("http://iati-datastore.herokuapp.com/api/1/access/activity.xml?reporting-org=GB-1&recipient-country=#{country_code}"))

    # extract the budgets and sort by year
    budgets = doc.xpath("//budget").sort_by { |b|
      b.xpath("period-start/@iso-date").first.content
    }

    # convert the budgets to FY
    groups = budgets.map { |budget|

      period_start = Date.parse(budget.xpath("period-start/@iso-date").first.content)
      year         = period_start.year
      month        = period_start.month
      day          = period_start.day
      value        = budget.xpath("value").first.content

      if period_start.month < 4 then
        fy_start_year = Date.new(year - 1, month, day).strftime("%y")
        fy_end_year   = period_start.strftime("%y")
      else
        fy_start_year = period_start.strftime("%y")
        fy_end_year   = Date.new(year + 1, month, day).strftime("%y")
      end

      fy = "FY#{fy_start_year}/#{fy_end_year}"
      [fy, value]
    }.group_by { |fy, budget| fy }.map { |fy,budgets|
      fy_budget = budgets.map{ |budget| budget.last.to_i  }.inject(:+)
      [fy, fy_budget]
    }

    Hash[*groups.flatten]
  end

  def sector_groups(country_code)
    total_budget = country_budget(country_code)
    doc          = Nokogiri::XML(open("http://iati-datastore.herokuapp.com/api/1/access/activity.xml?reporting-org=GB-1&recipient-country=#{country_code}"))

    doc.group_by { |activity|
      # get the hierarchy of the activity
      hierarchy = doc.xpath('@hierarchy').first.content.to_i || 1

      if(
    }
  end

  def country_budget(country_code)
    doc = Nokogiri::XML(open("http://iati-datastore.herokuapp.com/api/1/access/activity.xml?reporting-org=GB-1&recipient-country=#{country_code}"))

    current_date = DateTime.now

    if current_date.month < 4 then
      fy_start = "#{current_date.year-1}-04-01"
      fy_end   = "#{current_date.year}-03-31"
    else
      fy_start = "#{current_date.year}-04-01"
      fy_end   = "#{current_date.year+1}-03-31"
    end

    fy_budgets = doc.xpath("//budget").map do |budget|
      period_start = budget.xpath("period-start/@iso-date").first.content

      if (period_start >= fy_start) && (period_start <= fy_end) then
        budget.xpath("value").first.content.to_i
      else
        0
      end
    end

    fy_budgets.inject(:+)
  end

end
