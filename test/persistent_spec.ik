use("ispec")
use("ioke-couchdb-1.0.jar")

Apple = CouchDB Persistent mimic

describe(CouchDB Persistent,
  before(
    CouchDB Persistent database = CouchDB database!(("http://127.0.0.1:5984/ioke-couchdb-test-persistent"))
  )
  after(
    CouchDB Persistent database destroy!
  )
  it("should provide CRUD operations",
    apple = Apple with(color: "green")
    apple save should be true
    
    greenApple = Apple find(apple _id)
    greenApple kind?("Apple") should be true
    greenApple _id should == apple _id
    greenApple color should == "green"
    
    greenApple color = "red"
    greenApple save should be true
    
    redApple = Apple find(greenApple _id)
    redApple color should == "red"
    redApple _id should == apple _id
    
    redApple delete should be true
    
    Apple find(redApple _id) should be nil
  )
)