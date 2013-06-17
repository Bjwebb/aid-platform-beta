require 'nokogiri'
require 'open-uri'
require 'date'

# get the GB-1 budgets for a country
doc = Nokogiri::XML(open("http://iati-datastore.herokuapp.com/api/1/access/activity.xml?reporting-org=GB-1&recipient-country=BD"))

# extract the budgets
budgets = doc.xpath("//budget").sort_by { |b| b.xpath("period-start/@iso-date").first.content }

groups = budgets.map do |budget|

  period_start = Date.parse(budget.xpath("period-start/@iso-date").first.content)
  value        = budget.xpath("value").first.content

  if period_start.month < 4 then
    fy_start_year = Date.new(period_start.year-1, period_start.month, period_start.day).strftime("%y")
    fy_end_year   = period_start.strftime("%y")
  else
    fy_start_year = period_start.strftime("%y")
    fy_end_year   = Date.new(period_start.year+1, period_start.month, period_start.day).strftime("%y")
  end

  fy = "FY#{fy_start_year}/#{fy_end_year}"
  [fy, value]
end

groups = groups.group_by { |a,b| a }.map { |a,b| 
  fy_budget = b.map{ |c| c.last.to_i }.inject(:+)
  [a, fy_budget]
}
groups = Hash[*groups.flatten]

puts groups.inspect

#puts groups.inspect
