
# compliance-cases-api
Part of the MDTP Protect Connect APIs

## Running the service
To run the service, in the repository's main directory, run ```sbt run```
This service is intended to run on port 7052
You need to be a standard application to be able to use this API.

### Routes
    POST /case

Validates payload with the schema provided; sends it on if payload matches schema. Requires header:

Header | Content
-------|--------
CorrelationId | UUID

Possible responses:

Status | Message | description
-------|--------|----------
202 | - | Message has been received and is passed on.
400 | Invalid payload           | There is something wrong with the request body. response has a sequence of invalid fields
400 | Invalid correlation ID    | Correlation id is invalid
400 | Missing correlation ID    | Correlation id is missing 
401 | Unauthorised              | You are either not a standard application or you are not whitelisted
500 | Internal server error     | Something bad has happened

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
