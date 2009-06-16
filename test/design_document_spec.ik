
use("ispec")
use("ioke-couchdb-1.0.jar")

AppleView = CouchDB DesignDocument with(name: "apples")
AppleView do(
  view(:byColor, map: "emit(doc.color, doc)")
)

describe(CouchDB DesignDocument,
  before(
    database = CouchDB database!("http://127.0.0.1:5984/ioke-couchdb-test-views")
    AppleView database = database
  )
  after(
    database destroy!
  )
  
  it("should allow to create and destroy itself",
    AppleView create inspect println ;should be success
    AppleView delete inspect println ;should be success
  )
  
  describe("when created",
    before(
      AppleView create
    )
    it("should allow to query one of views",
      5 times(i, database saveObject(dict(color: "color-#{i}")))
      AppleView byColor length should == 5
    )
  )
)
