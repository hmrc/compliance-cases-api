We use standard HTTP status codes to show whether an API request succeeded or not. They are usually in the range:

- 200 to 299 if it succeeded, including code 202 if it was accepted by an API that needs to wait for further action
- 400 to 499 if it failed because of a client error by your application
- 500 to 599 if it failed because of an error on our server

Errors specific to each API are shown in the Endpoints section, under Response. See our [reference guide](https://developer.service.hmrc.gov.uk/api-documentation/docs/reference-guide#errors) for more on errors.

Responses with HTTP 422 (Unprocessable Entity) status code contain errors relating to particular fields in the request:  

Example error response: Repayment
<pre>
{
  "code": "UNPROCESSABLE_ENTITY",
  "message": "Unprocessable Entity",
  "caseType": "Repayment",
  "errors": [
    {
      "code": "001",
      "message": "Repayment amount must greater than 0",
      "path": "case/repaymentAmount"
    }
  ]
}</pre>

Example error response: Risk
<pre>
{
  "code": "UNPROCESSABLE_ENTITY",
  "message": "Unprocessable Entity",
  "caseType": "Risk",
  "errors": [
    {
      "code": "001",
      "message": "...",
      "path": "case/campaignId"
    }
  ]
}
</pre>

The full list of <code>errors.code</code> and <code>errors.message</code> is provided in the Endpoints section.
  
