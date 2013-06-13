require 'mongo'

@cms_client = Mongo::MongoClient.new('localhost', 27017)
@cms_db     = @cms_client['dfid']

puts @cms_db['transactions'].aggregate([{
  "$match" => {
    "project" => 'GB-1-114050',
    "type"    => 'D',
    "value"   => 2289
  }
}, {
  "$group" => {
    "_id"   => "$type",
    "total" => {
      "$sum" => "$value"
    },
    "transactions" => {
      "$addToSet" => {
        "_id"          => "$_id",
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