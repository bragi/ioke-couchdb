CouchDB DesignDocument = Origin mimic
CouchDB DesignDocument View = Origin mimic

CouchDB DesignDocument do(
  asDict = method(
    d = dict(
      id: _id,
      language: "javascript"
    )
    if(cell?(:rev),
      d[:rev] = _rev
    )
    v = dict
    views each(view, v[view name] = view asDict)
    d
  )

  create = method(
    d = self asDict
    resource = database createObject(d)
    self _rev = d _rev
    resource
  )

  delete = method(
    database deleteObject(_id: _id, _rev: _rev)
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
    mapFunction = "function(doc) {
  #{map}
}"

    if(reduce,
      reduceFunction = "function(keys, values, rereduce) {
  #{reduce}
}"
      dict(map: mapFunction, reduce: reduceFunction),

      dict(map: mapFunction)
    )
  )

)

