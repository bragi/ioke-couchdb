CouchDB Persistent = Origin mimic

CouchDB Persistent do(
  all = method(
    database all
  )

  find = method(id,
    database loadObject(id)
  )

  save = method(
    database saveObject(self)
  )

  save! = method(
    save || error!("Record not saved")
  )

  delete = method(
    database deleteObject(self)
  )
)
