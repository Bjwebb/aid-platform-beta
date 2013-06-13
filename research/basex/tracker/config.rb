require 'lib/client'
require 'lib/country_page_api'
require 'nokogiri'

@country_page_api = CountryPageApi.new

['BD'].each do |country_code|

  # project_query = @session.query <<-query
  #   let $db            := db:open('iati')
  #   let $bd_activities := $db//iati-activity/recipient-country[@code='#{country_code}']/..
  #   let $bd_projects   := for $id in distinct-values($bd_activities/related-activity[@type='1']/data(@ref))
  #                         let $bd_project     := $db//iati-activity/iati-identifier[text()=$id]/..
  #                         let $project_budget := <budget> 
  #                                                  {
  #                                                    sum(for $component in $db//iati-activity[@hierarchy=2]
  #                                                        where $component/related-activity[@ref = $id]
  #                                                        return sum($component/budget/value)) 
  #                                                  }
  #                                                </budget>
  #                         return <project>
  #                                  { $bd_project/title[1] }
  #                                  { $bd_project/description[1] }
  #                                  { $project_budget }
  #                                </project>
  #   return <results>
  #            <count>{ count($bd_projects) }</count>
  #            <data>{ $bd_projects }</data>
  #          </results>
  # query

  proxy "/countries/#{country_code}/index.html", '/templates/country.html', :locals => { :country_code => country_code, :api => @country_page_api }
end

set :css_dir,    'stylesheets'
set :js_dir,     'javascripts'
set :images_dir, 'images'

configure :build do

end