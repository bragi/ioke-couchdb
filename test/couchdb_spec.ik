
use("ispec")
use("ioke-json-1.0.1.jar")
use("ioke-couchdb-1.0.jar")

describe(CouchDB,
  describe("database",
    it("creates and destroys the database",
      database = CouchDB database("http://127.0.0.1:5984/ioke-couchdb-test-1")
      database exists? should be false
      database create!
      database exists? should be true
      database destroy!
      database exists? should be false
    )
  )
  
  describe("database!", {pending: true},
    it("creates database when it does not exist",
      database = Origin mimic
      database stub!(exists?: false)
      database mock!(:create!)
      CouchDB stub!(database: database)
      CouchDB database!("http://127.0.0.1:5984/ioke-couchdb-test-2")
    )
  )
)

describe(CouchDB Database,
  before(
    database = CouchDB database!("http://127.0.0.1:5984/ioke-couchdb-test-2")
  )
  after(
    database destroy!
  )
  
  it("should save and load object",
    object = Origin with(name: "Test subject")
    database saveObject(object)
    database loadObject(object _id) name should == "Test subject"
  )
)