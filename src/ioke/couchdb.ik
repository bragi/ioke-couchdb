CouchDB = Origin mimic
CouchDB Database = Origin mimic

CouchDB do(
  database = method("Creates representation of database with given url",
    url,
    CouchDB Database with(url: url)
  )

  database! = method("Creates representation of database with given url, creates the database under given url if it does not exists.",
    url,
    db = database(url)
    unless(db exists?,
      db create!
    )
    db
  )
)

CouchDB Database do(
  all = method(
    collection = get("#{url}/_all_docs")
    if(collection,
      collection["rows"],
      
      list
    )
  )

  create! = method(
    Resource with(url: url) put success?
  )

  destroy! = method(
    Resource with(url: url) delete success?
  )

  exists? = method(
    Resource with(url: url) get success?
  )

  createObject = method(object,
    resource = Resource with(url: url, representation: object toJson)
    resource post
    response = resource toIoke
    object _id = response["id"]
    object _rev = response["rev"]
    resource
  )
  
  deleteObject = method(object,
    Resource with(url: "#{url}/#{object _id}?rev=#{object _rev}") delete
  )
  
  get = method(url,
    resource = Resource with(url: url) get
    if(resource success?,
      resource toIoke,
      nil
    )
  )

  loadObject = method(id,
    get("#{url}/#{id}")
  )

  updateObject = method(object,
    resource = Resource with(url: "#{url}/#{object _id}", representation: object toJson) 
    resource put 
    response = resource toIoke
    object _rev = response["rev"]
    resource
  )

  temporaryView = method(map:, reduce: nil,
    Resource with(url: "#{url}/_temp_view", representation: dict(map: map) toJson) post toIoke["rows"]
  )

  saveObject = method(object,
    if(object cell?(:_id),
      updateObject(object),

      createObject(object)
    )
  )

)
