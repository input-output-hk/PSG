# The StoreAndHash Service

The StoreAndHash service provides an API for posting data to the AWS storage, getting the hash of the data and URL for further download.

This document describes the service and how to use it.

Working knowledge of gRPC is assumed.

## The Service
The service wraps access to the AWS instance for storing file.
It is possible to specify connection details as well as the custom path to upload the file.

### The StoreAndHash Service API

#### StoreAndHashHttp

The StoreAndHashHttp method expects a stream of requests from client: UploadDetails and one or more Chunk of data to be stored.

As a result,  provides a stream of response: one containing the hash of the file and second - with the URL to file.

UploadDetails in request should contain the following data:
- path to upload the file
- credentials (you can use any option)
    - 'clientId'
    - AWS credentials object

AWS's credentials object expected to have:
* key id
* key secret
* bucket name
* region

Chunk object expects to have data in a form of ByteString.

The result will be a stream of responses:
- Hash object which contains type of hash used (SHA-256), and hash values in bytes and base64 String formats.
- URL to file stored

In the case of a problem, that problem is described in a `problem` field of the response.`

### Monitoring

There are two http urls available for monitoring applications, the first is a trivial `alive` url that returns 200 if the service is up.

`wget https://myservice.com:2000/alive` should return 200 OK.

More usefully

`wget https://myservice.com:2000/alarms/lowbalance` will return a json response indicating whether all the monitored
wallets have balances above their low balance thresholds.

In the case where all wallets are ok the following is returned

`{"allOk":true,"lowBalancedWallets":[],"errors":[]}`

...otherwise `allOk:false` will be returned along with details of the problems.

