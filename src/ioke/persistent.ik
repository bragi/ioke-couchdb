CouchDB Persistent = Origin mimic

CouchDB Persistent do(
  find = method(id,
    database loadObject(id)
  )
  
  save = method(
    database saveObject(self)
  )
  
  delete = method(
    database deleteObject(self)
  )
)
