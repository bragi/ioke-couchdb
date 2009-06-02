Resource do(
  success? = method(
    result >= 200 && result < 300
  )
  
  missing? = method(
    result == 404
  )
  
  toIoke = method(
    JSON fromText(representation)
  )
  
  inspect = method(
    "Resource(result: #{result}, mimeType: #{mimeType}, representation: #{representation})"
  )
)
