require 'json'
require 'hashugar'

class StubCountryPagesApi

  def countries
    response = <<-JSON 
      {
        "success": true,
        "results": [{
          "code": "BD"
        }]
      }
    JSON

    JSON.parse(response)
  end

  def index_page_for(country_code)
    response = <<-JSON 
      {
        "name"        : "Bangladesh",
        "code"        : "BD",
        "description" : "__Bangladesh__ is a country",
        "projects"    : {
          "total"  : 86,
          "active" : 47
        },
        "published_results" : true,
        "budget"            : {
          "total"                     : 100000000,
          "financial_year"            : "2013/2014",
          "percentage_of_dfid_budget" : 23
        },
        "facts" : {
          "life_expectancy" : {
            "value"  : 64,
            "source" : {
              "name" : "World Bank",
              "href" : "http://worldbank.org"
            }
          },
          "population" : {
            "value"  : 10000000,
            "source" : {
              "name" : "World Bank",
              "href" : "http://worldbank.org"
            }
          }
        },
        "sector_groups" : [{
          "name"       : "Health",
          "percentage" : 20.15
        }, {
          "name"       : "Government",
          "percentage" : 18.66
        }, {
          "name"       : "Education",
          "percentage" : 13.93
        }, {
          "name"       : "Multisector",
          "percentage" : 9.95
        }, {
          "name"       : "Environment",
          "percentage" : 5.97
        }, {
          "name"       : "Other",
          "percentage" : 31.34
        }],
        "budgets_by_year" : [{
          "year"   : "FY10/11", 
          "budget" : 238905819
        }, {
          "year"   : "FY11/12", 
          "budget" : 325835034
        }, {
          "year"   : "FY12/13", 
          "budget" : 259560962
        }, {
          "year"   : "FY13/14", 
          "budget" : 271246560
        }, {
          "year"   : "FY14/15", 
          "budget" : 312370710
        }, {
          "year"   : "FY15/16", 
          "budget" : 174666976
        }],
        "locations" : [{
          "name"      : "Ramgarh",
          "longitude" : 91.7417143,
          "latitude"  : 22.9946547,
          "precision" : "Second order administrative division",
          "type"      : "populated place",
          "id"        : "GB-1-107371",
          "title"     : "Sanitation, Hygiene, Education & Water Supply in Bangladesh (SHEWA-B)"
        },{
          "name"      : "Mahalchari",
          "longitude" : 92.029768,
          "latitude"  : 22.9316977,
          "precision" : "Second order administrative division",
          "type"      : "populated place",
          "id"        : "GB-1-107371",
          "title"     : "Sanitation, Hygiene, Education & Water Supply in Bangladesh (SHEWA-B)"
        }]
      }
    JSON

    JSON.parse(response)
  end
end