# The Metadata Service

The metadata service wraps the work of posting metadata to the Cardano blockchain in a gRpc defined service.

This allows for the automatic generation of clients using the specified IDL.

This document describes the service and how to use it. A working knowledge of gRpc is assumed. 
 

## The Service 

The service wraps access to the cardano wallet backend API. It supports multiple wallets via it's configuration file. 
This is to allow a single service instance to be used by 
multiple clients as a multi tenant service. 
However it also works as a micro service integrated with a particular solution.

### Configuration

The configuration is stored within the docker container, in the `conf` folder of the application however it can be overridden from the commandline. 
 
This is the time interval used by the service to poll the wallet backend for updates on the state of a transaction.   
`txPollInterval = 4 seconds`

This is the number of retries that will happen in a case where communication with the wallet backend is lost.

`max-retry-on-error = 3`

In the case where no `clientId` is provided to the service request, the default wallet name is used to service the request.
 
`default-wallet-name = "metadata-default-wallet"`

In the case where the balance of a wallet falls below this value the low balance alarm is triggered 

`default-minbalance = 1000000`

As mentioned previously the configuration supports multiple wallet configurations as a list.

```
wallets = [
    {
        url = "https://xxxxxx.iog.solutions:8090/v2/"
        name = "metadata-default-wallet"
        id = "695f31xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
        passphrase = "xxxxxxxxxx"
        minbalance = 1000000
        transferamount = 1000000
        mnemonic = "valid menmonic words all the way to twenty"
        monitor-low-balance = true
    },...
```

Most of this wallet configuration is self-explanatory, note that it is NOT necessary to provide the mnemonic for the 
wallet in this file. This facilitates never having to have the menmonic on this machine.

To have the low balance alarm monitor ignore the balance of this wallet set 
  
`monitor-low-balance = false` 

### Deployment

The service is deployed as a docker container or as a Java Application as defined by sbt native packager.

In order to override any of the values in the application configuration (with environment substitution) from the commandline use 

`-DVAR_NAME=NEW_VALE`

...so for example (Note the environment substitution syntax)

```
default-wallet-name = "metadata-default-wallet"
default-wallet-name = ${?DEFAULT_WALLET_NAME}
```

... to override the default wallet name use

`-DDEFAULT_WALLET_NAME=my-new-wallet`

To override other application configuration from the command line e.g. from the docker run command line, use 

`-J-DNAME=VALUE`

### Monitoring

There are two http urls available for monitoring applications, the first is a trivial `alive` url that returns 200 if the service is up.

`wget https://myservice.com:2000/alive` should return 200 OK. 

More usefully 

`wget https://myservice.com:2000/alarms/lowbalance` will return a json response indicating whether all the monitored 
wallets have balances above their low balance thresholds. 

In the case where all wallets are ok the following is returned 

`{"allOk":true,"lowBalancedWallets":[],"errors":[]}`

...otherwise `allOk:false` will be returned along with details of the problems.
 
### The Metadata Service API

#### Submit Metadata

The submit metadata method provides a stream of response updates until all the criteria have been met.
  
Create a request and provide the `clientId` to choose a particular wallet configuration, or provide no client id to use the default wallet.

Provide the depth as a number of blocks. 

Provide the metadata as a string in Json format, but conforming to the schema described 
[here](https://input-output-hk.github.io/cardano-wallet/api/edge/#operation/postTransactionFee)

The result will be a stream of responses, these responses are checked for status, first to make sure the request has a state 'IN_LEDGER' 
and finally to indicate the transaction has been buried under the requisite number of blocks. 

The transaction id (`tx_id`) for the metadata transaction is returned in the first result of the stream.

The stream ends when these criteria are filled or there is a 'problem'. 

In the case of a problem, that problem is described in a `problem` field of the response.` 

In the case where a problem occurs before the criteria are filled, due to a lost connection for example, 
the process can be restarted by 
resending the request but providing only the `tx_id` and the `clientId`.
 
#### Balance
 
Simple returns the available balance of the wallet associated with the `clientId` or default wallet.

#### Get Funding Addresses

In order to add funds to a wallet a target address is required, this call returns a list of unused addresses from the wallet associated with a `clientId`

This is then used to add more funds to a wallet (e.g. from Daedalus) and thus facilitate more metadata transactions. 

#### List Transactions

List all metadata transactions associated with the clientIds wallet between the dates given. 

Note the dates are optional and when not provided, all metadata transactions are returned.   

Note, if a transaction has no metadata, it will not be listed by this call.

 


