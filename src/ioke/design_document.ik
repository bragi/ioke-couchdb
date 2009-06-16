CouchDB DesignDocument = Origin mimic
CouchDB DesignDocument View = Origin mimic

CouchDB DesignDocument do(
  asDict = method(
    d = dict(
      _id: _id,
      language: "javascript"
    )
    if(cell?(:rev),
      d[:_rev] = _rev
    )
    v = dict
    views each(view, v[view name] = view asDict)
    d[:views] = v
    d
  )

  create = method(
    d = self asDict
    d inspect println
    resource = database createObject(d)
    self _rev = d _rev
    resource
  )

  delete = method(
    database deleteObject(_id: _id, _rev: _rev)
  )
  
  exists? = method(
    database exists?(_id)
  )

  getView = method(view,
    database loadObject(view url)
  )

  initialize = method(
    self views = []
  )

  url = method(
    "_design/#{name}"
  )

  aliasMethod(:url, :_id)

  view = method(name, map:, reduce: nil,
    v = View with(name: name, map: map, reduce: reduce, url: "#{url}/_view/#{name}")
    views << v
    self cell(name) = fnx(
      getView(v)
    )
  )
)

CouchDB DesignDocument View do(
  asDict = method(
    if(reduce,
      dict(map: mapFunction, reduce: reduceFunction),

      dict(map: mapFunction)
    )
  )

  mapFunction = method(
    "function(doc) {#{map}}"
  )
  
  reduceFunction = method(
    "function(keys, values, rereduce) {#{reduce}}"
  )
)

