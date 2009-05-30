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
