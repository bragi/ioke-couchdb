
use("ispec")
use("ioke-couchdb-1.0.jar")

AppleView = CouchDB DesignDocument with(name: "apples")
AppleView do(
  view(:by_color, map: "emit(doc.color, doc)")
)

describe(CouchDB DesignDocument,
  before(
    database = CouchDB database!("http://127.0.0.1:5984/ioke-couchdb-test-views")
    AppleView database = database
  )

  after(
    database destroy!
  )
  
  describe("when defined",
    before(
      testView = CouchDB DesignDocument with(name: "testView")
      testView do(
        view(:by_color, map: "emit(doc.color, doc)")
      )
    )

    it("should have views",
      testView views length should == 1
    )
    
    it("should have map",
      testView views first map should == "emit(doc.color, doc)"
    )
    
    describe("and providing dict representation",
      it("should have views",
        testView asDict at(:views) should not be nil
      )
    )
  )
  
  it("should allow to create and destroy itself",
    AppleView create inspect println ;should be success
    AppleView exists? should be true
    AppleView delete inspect println ;should be success
  )
  
  describe("when created",
    before(
      AppleView create
    )
    it("should allow to query one of views",
      5 times(i, database saveObject(dict(color: "color-#{i}")))
      AppleView by_color length should == 5
    )
  )
)

describe(CouchDB DesignDocument View,
  describe("when giving dict representation",
    before(
      view = CouchDB DesignDocument View with(name: "name", map: "map", reduce: "reduce", url: "url")
    )

    it("should skip name",
      view asDict key?(:name) should be false
    )
    
    it("should have map",
      view asDict key?(:map) should be true
    )

    it("should have reduce",
      view asDict key?(:reduce) should be true
    )
    
    it("should skip reduce when empty",
      view = CouchDB DesignDocument View with(name: "name", map: "map", reduce: nil, url: "url")
      view asDict key?(:reduce) should be false
    )
  )
)