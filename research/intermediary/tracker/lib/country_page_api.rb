require 'json'
require 'httparty'

class CountryPagesApi

  def countries
    response = HTTParty.get('http://localhost:9000/countries')
    JSON.parse(response.body)
  end

  def index_page_for(country_code)
    response = HTTParty.get("http://localhost:9000/countries/#{country_code}")
    puts response.body
    JSON.parse(response.body)
  end
end