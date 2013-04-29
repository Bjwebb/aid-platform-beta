require "rubygems"
require "json"
require "helpers/formatters"
require "helpers/country_helpers"
require "helpers/region_helpers"
require "helpers/frontpage_helpers"
require "helpers/project_helpers"
require "helpers/codelists"
require "helpers/lookups"
require "helpers/sector_helpers"
require "middleman-smusher"

#------------------------------------------------------------------------------
# CONFIGURATION VARIABLES
#------------------------------------------------------------------------------
@api_root_url   = ENV['DFID_API_URL'] || "http://localhost:9000"
@api_access_url = "#{@api_root_url}/access"
@cms_client     = Mongo::MongoClient.new('localhost', 27017)
@cms_db         = @cms_client['dfid']

#------------------------------------------------------------------------------
# IGNORE TEMPLATES AND PARTIALS
#------------------------------------------------------------------------------
ignore "/projectList.html"
ignore "/countries/country.html"
ignore "/countries/projects.html"
ignore "/countries/results.html"
ignore "/projects/summary.html"
ignore "/projects/documents.html"
ignore "/projects/transactions.html"
ignore "/projects/partners.html"
ignore "/sector/categories.html"
ignore "/sector/sectors.html"
ignore "/sector/projects.html"

#------------------------------------------------------------------------------
# GENERATE COUNTRIES
#------------------------------------------------------------------------------
@cms_db['countries'].find({}).each do |country|
  stats    = @cms_db['country-stats'].find_one({ "code" => country["code"] })
  projects = @cms_db['projects'].find({ "recipient" => country['code'] }, :sort => ['totalBudget', Mongo::DESCENDING]).to_a
  locations = @cms_db['locations'].find( { 
    'id' =>  {
      '$in' => projects.map { |p| p['iatiId']}
    } 
  }).to_a.map { |location|
    {
      "name"      => location['name'], 
      "longitude" => location['longitude'], 
      "latitude"  => location['latitude'], 
      "precision" => CodeLists.geographical_precision(location['precision']), 
      "type"      => CodeLists.location_type(location['type']),
      "id"        => location['id'],
      "title"     => location['title']
    }
  }
  
  results = @cms_db['country-results'].aggregate([{ 
        "$match" => {"code" => country["code"]}
        }, {
         "$group" => {
            "_id" => "$pillar",
            "countryResult" => {
              "$addToSet" => "$results"
            },
            "resultTotal" => {
              "$addToSet" => "$total"
            }
        } 
      }])
  
  proxy "/countries/#{country['code']}/index.html",          "/countries/country.html",  :locals => { :country => country, :stats    => stats,    :projects => projects, :results => results, :locations => locations }
  proxy "/countries/#{country['code']}/results/index.html",  "/countries/results.html",  :locals => { :country => country, :projects => projects, :results  => results }
  proxy "/countries/#{country['code']}/projects/index.html", "/countries/projects.html", :locals => { :country => country, :projects => projects, :results => results }
end

#------------------------------------------------------------------------------
# GENERATE REGION PROJECT LIST
#------------------------------------------------------------------------------
@cms_db['regions'].find({}).each do |region|
  projects = @cms_db['projects'].find({"projectType" => "regional", "recipient" => region['code']}, :sort => ['totalBudget', Mongo::DESCENDING]).to_a
  proxy "/regions/#{region['code']}/projects/index.html", "/projectList.html", :locals => {:projects => projects, :name => region['name']}
end

projects = @cms_db['projects'].find({"projectType" => "regional"}, :sort => ['totalBudget', Mongo::DESCENDING]).to_a
proxy "/regions/index.html", "/projectList.html", :locals => {:projects => projects, :name => "Regional"}

#------------------------------------------------------------------------------
# GENERATE GLOBAL PROJECT LIST
#------------------------------------------------------------------------------
projects = @cms_db['projects'].find({"projectType" => "global"}, :sort => ['totalBudget', Mongo::DESCENDING]).to_a
proxy "/global/index.html", "/projectList.html", :locals => {:projects => projects, :name => "Global"}

CodeLists.all_global_recipients.map { |code, name|

  budget = (@cms_db['projects'].aggregate([{
    "$match" => {
        'projectType' => 'global',
        'recipient'   => code
    },
  }, {
    "$group" => {
        "_id" => nil,
        "total" => { "$sum" => "$currentFYBudget" }
    }
  }]).first || { "total" => 0 })['total']

  if budget > 0 then
    projects = @cms_db['projects'].find({ 'projectType' => 'global', 'recipient' => code }).to_a
    proxy "/global/#{code}/projects/index.html", "/projectList.html", :locals => {:projects => projects, :name => name}
  end
}


#------------------------------------------------------------------------------
# GENERATE PROJECTS
#------------------------------------------------------------------------------
@cms_db['projects'].find({}).each do |project|

  id                  = project['iatiId']
  funded_projects     = @cms_db['funded-projects'].find({ 'funding' => id }).to_a
  has_funded_projects = funded_projects.size > 0
  documents           = @cms_db['documents'].find({ 'project' => id}).to_a
  locations           = @cms_db['locations'].find( { 'id' =>  id }).to_a.map { |location|
    {
      "name"      => location['name'], 
      "longitude" => location['longitude'], 
      "latitude"  => location['latitude'], 
      "precision" => CodeLists.geographical_precision(location['precision']), 
      "type"      => CodeLists.location_type(location['type']),
      "id"        => location['id'],
      "title"     => location['title']
    }
  }
  transaction_groups  = @cms_db['transactions'].aggregate([{
    "$match" => {
      "project" => id
    }
  }, {
    "$group" => {
      "_id" => "$type",
      "total" => {
        "$sum" => "$value"
      },
      "transactions" => {
        "$addToSet" => {
          "description"  => "$description",
          "component"    => "$component",
          "date"         => "$date",
          "value"        => "$value",
          "title"        => "$title",
          "receiver-org" => "$receiver-org",
        }
      }
    }
  }, {
    "$sort" => {
      "_id" => 1
    }
  }])

  proxy "/projects/#{id}/index.html",              '/projects/summary.html',      :locals => { :project => project, :has_funded_projects => has_funded_projects, :locations => locations }
  proxy "/projects/#{id}/documents/index.html",    '/projects/documents.html',    :locals => { :project => project, :has_funded_projects => has_funded_projects, :documents => documents }
  proxy "/projects/#{id}/transactions/index.html", '/projects/transactions.html', :locals => { :project => project, :has_funded_projects => has_funded_projects, :transaction_groups => transaction_groups }

  if has_funded_projects then
    proxy "/projects/#{id}/partners/index.html",   '/projects/partners.html',     :locals => { :project => project, :funded_projects => funded_projects }
  end
end

#------------------------------------------------------------------------------
# GENERATE OTHER PROJECTS
#------------------------------------------------------------------------------
@cms_db['other-org-projects'].find({}).each do |project|

  id                  = project['iatiId']
  documents           = @cms_db['documents'].find({ 'project' => id }).to_a
  transaction_groups  = @cms_db['transactions'].aggregate([{
    "$match" => {
      "project" => id
    }
  },{
    "$group" => {
      "_id" => "$type",
      "total" => {
        "$sum" => "$value"
      },
      "transactions" => {
        "$addToSet" => {
          "description" => "$description",
          "component"   => "$component",
          "date"        => "$date",
          "value"       => "$value",
        }
      }
    }
  }])

  proxy "/projects/#{id}/index.html",              '/projects/summary.html',      :locals => { :project => project, :has_funded_projects => false, :locations => [] }
  proxy "/projects/#{id}/documents/index.html",    '/projects/documents.html',    :locals => { :project => project, :has_funded_projects => false, :documents => documents }
  proxy "/projects/#{id}/transactions/index.html", '/projects/transactions.html', :locals => { :project => project, :has_funded_projects => false, :transaction_groups => transaction_groups }

end

#------------------------------------------------------------------------------
# GENERATE FUNDED PROJECT PAGES
#------------------------------------------------------------------------------
@cms_db['funded-projects'].find({}).to_a.each do |funded_project|

  # format the project model to suit the project templates
  project = {
    'iatiId'            => funded_project['funded'],
    'title'             => funded_project['title'],
    'description'       => funded_project['description'],
    'funds'             => funded_project['funds'],
    'totalBudget'       => funded_project['totalBudget'],    
    'totalProjectSpend' => funded_project['totalSpend'],
    'end-actual'        => funded_project['end-actual'],
    'end-planned'       => funded_project['end-planned'],
    'start-actual'      => funded_project['start-actual'],
    'start-planned'     => funded_project['start-planned'],
    'status'            => funded_project['status']
  }

  # get the other funded projects
  funded_projects = @cms_db['funded-projects'].find({ 
    'funding' => funded_project['funding'],
    'funded'  => { '$ne' => funded_project['funded'] } 
  }).to_a

  # get the parent project
  funding_project    = @cms_db['projects'].find_one({ 'iatiId' =>  funded_project['funding'] })
  documents          = @cms_db['documents'].find({ 'project' => funded_project['funded'] }).to_a
  transaction_groups = @cms_db['transactions'].aggregate([{
    "$match" => {
      "project" => funded_project['funded']
    }
  },{
    "$group" => {
      "_id" => "$type",
      "total" => {
        "$sum" => "$value"
      },
      "transactions" => {
        "$addToSet" => {
          "description"   => "$description",
          "component"     => "$component",
          "date"          => "$date",
          "value"         => "$value",
          "provider-org"  => "$provider-org",
          "provider-activity-id" => "$provider-activity-id"
        }
      }
    }
  }])

  proxy "/projects/#{project['iatiId']}/index.html",              '/projects/summary.html',      :locals => { :project => project, :has_funded_projects => true, :locations => [] }
  proxy "/projects/#{project['iatiId']}/documents/index.html",    '/projects/documents.html',    :locals => { :project => project, :has_funded_projects => true, :documents => documents  }
  proxy "/projects/#{project['iatiId']}/transactions/index.html", '/projects/transactions.html', :locals => { :project => project, :has_funded_projects => true, :transaction_groups => transaction_groups  }
  proxy "/projects/#{project['iatiId']}/partners/index.html",     '/projects/partners.html',     :locals => { :project => project, :has_funded_projects => true, :funded_projects => funded_projects, :funding_project => funding_project  }

end

#------------------------------------------------------------------------------
# GENERATE SECTOR HIERARCHIES
#------------------------------------------------------------------------------
@cms_db['sector-hierarchies'].aggregate([{ 
  "$group" => { 
    "_id"  => "$highLevelCode", 
    "sectorName" => {
      "$first" => "$highLevelName"
    } 
  } 
}]).each do |sector|
  proxy "/sector/#{sector['_id']}/index.html", '/sector/categories.html', :locals => { :sector => sector }
end

@cms_db['sector-hierarchies'].aggregate([{ 
  "$group" => { 
    "_id"          => "$categoryCode", 
    "sectorCode"   => {"$first" => "$highLevelCode"}, 
    "sectorName"   => {"$first" => "$highLevelName"}, 
    "categoryName" => {"$first" => "$categoryName"} 
  } 
}]).each do |sector|

  categoryCode = sector['_id']
  sectorCode   = sector['sectorCode']
  proxy "/sector/#{sectorCode}/categories/#{categoryCode}/index.html", '/sector/sectors.html', :locals => { :sector => sector }

end

@cms_db['sector-hierarchies'].find({}).to_a.each do |sector|

  highLevelCode = sector['highLevelCode']
  categoryCode  = sector['categoryCode']
  sectorCode    = sector['sectorCode']
  
  projectIds = @cms_db['project-sector-budgets'].find({
    "sectorCode" => sectorCode
  }).map { |project| 
    project['projectIatiId'] 
  }

  projects = @cms_db['projects'].find({
    "iatiId" => {
      "$in" => projectIds
    }
  })

  proxy "/sector/#{highLevelCode}/categories/#{categoryCode}/projects/#{sectorCode}/index.html", 'sector/projects.html', :locals => { :sector => sector, :projects => projects }  
end

#------------------------------------------------------------------------------
# DEFINE HELPERS - Import from modules to avoid bloat
#------------------------------------------------------------------------------
helpers do

  include Formatters
  include CountryHelpers
  include FrontPageHelpers
  include Lookups
  include ProjectHelpers
  include CodeLists
  include SectorHelpers
  include RegionHelpers

end

#------------------------------------------------------------------------------
# CONFIGURE DIRECTORIES
#------------------------------------------------------------------------------
set :css_dir   , 'stylesheets'
set :js_dir    , 'javascripts'
set :images_dir, 'images'

activate :livereload

#------------------------------------------------------------------------------
# BUILD SPECIFIC CONFIGURATION
#------------------------------------------------------------------------------
configure :build do
  activate :minify_css
  activate :minify_javascript
  activate :cache_buster
  
  # this takes time, be careful
  # activate :smusher
end