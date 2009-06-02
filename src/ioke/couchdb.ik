Resource do(
  success? = method(
    result => 200 && result < 300
  )
  
  missing? = method(
    result == 404
  )
)

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
  create! = method(
    Resource mimic(url: url) put success?
  )
  
  destroy! = method(
    Resource mimic(url: url) delete success?
  )
  
  exists? = method(
    Resource mimic(url: url) get success?
  )
  
  createObject = method(object,
    Resource mimic(url: url, representation: object toJson) put success?
  )

  loadObject = method(id,
    JSON fromText(Resource mimic(url: "#{url}/#{id}") get representation)
  )
  
  updateObject = method(object,
    Resource mimic(url: "#{url}/#{object _id}", representation: object toJson) post success?
  )

  saveObject = method(object,
    if(object cell?(:_id),
      updateObject(object),
      
      createObject(object)
    )
  )
)
