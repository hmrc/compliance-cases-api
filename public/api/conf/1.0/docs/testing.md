You can use the sandbox environment to [test this API](https://developer.service.hmrc.gov.uk/api-documentation/docs/testing).

[Access a set of mock campaignId to test 422 error codes for caseType Risk](https://github.com/hmrc/compliance-cases-api/tree/master/public/api/conf/1.0/test-data) to use when testing in the sandbox environment.

[Access a set of mock projectId to test 422 error codes for caseType Repayment](https://github.com/hmrc/compliance-cases-api/tree/master/public/api/conf/1.0/test-data) to use when testing in the sandbox environment.

N.b. the last 3 digits of the test-data campaignId and projectId will match the returned error code e.g. CID-00000105 will return code 105. 


Any 422 codes not listed can be tested using real data i.e. Repayment codes 001, 003, 004, 010 & Risk codes 009, 107, 108, 109, 112, 113, 114 will be returned if incorrect dates and amounts are supplied in the Json payload. 