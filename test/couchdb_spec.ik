
use("ispec")
use("ioke-couchdb-1.0.jar")

describe(CouchDB,
  describe("database",
    it("creates and destroys the database",
      database = CouchDB database("http://127.0.0.1:5984/ioke-couchdb-test")
      database exists? should be false
      database create!
      database exists? should be true
      database destroy!
      database exists? should be false
    )
  )
  
  describe("database!",
    it("creates database unless it exists",
      
    )
  )
)