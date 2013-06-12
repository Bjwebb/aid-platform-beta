require 'lib/client'
require 'nokogiri'

@query_root = "/Users/james/Projects/dfid/xqueries/"
@session = BaseXClient::Session.new("localhost", 1984, "admin", "admin")

['BD'].each do |country_code|

  project_query = @session.query <<-query
    let $db            := db:open('iati')
    let $bd_activities := $db//iati-activity/recipient-country[@code='#{country_code}']/..
    let $bd_projects   := for $id in distinct-values($bd_activities/related-activity[@type='1']/data(@ref))
                          let $bd_project     := $db//iati-activity/iati-identifier[text()=$id]/..
                          let $project_budget := <budget> 
                                                   {
                                                     sum(for $component in $db//iati-activity[@hierarchy=2]
                                                         where $component/related-activity[@ref = $id]
                                                         return sum($component/budget/value)) 
                                                   }
                                                 </budget>
                          return <project>
                                   { $bd_project/title[1] }
                                   { $bd_project/description[1] }
                                   { $project_budget }
                                 </project>
    return <results>
             <count>{ count($bd_projects) }</count>
             <data>{ $bd_projects }</data>
           </results>
  query

  budgets_by_year_query = @session.query <<-query
    let $budgets := for $budget in //iati-activity[recipient-country/@code='#{country_code}'][reporting-org/@ref='GB-1']/budget
                    let $date       := $budget/period-start/@iso-date
                    let $date_parts := tokenize($date, '-')
                    let $month      := number($date_parts[position() = 2])
                    let $year       := if ($month < 4)
                                       then number($date_parts[position() = 1]) - 1
                                       else number($date_parts[position() = 1])
                    let $fy         := concat("FY", substring(string($year), 3), "/", substring(string($year+1), 3))
                    group by $fy
                    return <financial-year label="{ $fy }"> 
                      { sum($budget/value/text()) } 
                    </financial-year>
    return <results>
       <count>{ count($budgets) }</count>
       <data>{ $budgets }</data>
     </results>
  query

  projects        = Nokogiri::XML(project_query.next)
  budgets_by_year = Nokogiri::XML(budgets_by_year_query.next)

  proxy "/countries/#{country_code}/index.html", '/templates/country.html', :locals => { :country_code => country_code, :projects => projects }
end

set :css_dir,    'stylesheets'
set :js_dir,     'javascripts'
set :images_dir, 'images'

configure :build do

end