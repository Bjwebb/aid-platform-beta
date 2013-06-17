require 'lib/country_page_api'
require 'nokogiri'

@country_page_api = CountryPageApi.new

['BD'].each do |country_code|
  proxy "/countries/#{country_code}/index.html", '/templates/country.html', :locals => { :country_code => country_code, :api => @country_page_api }
end

set :css_dir,    'stylesheets'
set :js_dir,     'javascripts'
set :images_dir, 'images'

configure :build do

end