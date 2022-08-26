# The Metadata Service - Configuration

This document describes the service configuration and deployment.

## Configuration  

The configuration is stored within the docker container in the `conf` folder of the application.
It is possible to override configuration from the command line.

This is the time interval used by the service to poll the wallet backend for updates on the state of a transaction.
`txPollInterval = 4 seconds`

This is the number of retries that will happen if communication with the wallet backend is lost.

`max-retry-on-error = 3`

If no `clientId` is provided to the service request, the default wallet name is used to service the request.

`default-wallet-name = "metadata-default-wallet"`

In the case where the balance of a wallet falls below this value, the low balance alarm is triggered.

`default-minbalance = 1000000`

As mentioned previously, the configuration supports multiple wallet configurations as a list.

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

Most of this wallet configuration is self-explanatory; note that it is NOT necessary to provide the mnemonic for the
wallet in this file. This facilitates never having to have the mnemonic on this machine.

To have the low balance alarm monitor ignore the balance of this wallet set.

`monitor-low-balance = false`

## Deployment

The service is deployed as a docker container or as a Java Application as defined by the sbt native packager.

To override any of the values in the application configuration (with environment substitution) from the command line use

`-DVAR_NAME=NEW_VALUE`

...so for example (Note the environment substitution syntax)

```
default-wallet-name = "metadata-default-wallet"
default-wallet-name = ${?DEFAULT_WALLET_NAME}
```

... to override the default wallet name use

`-DDEFAULT_WALLET_NAME=my-new-wallet`

To override other application configuration from the command line e.g. from the docker run command line, use.

`-J-DNAME=VALUE`

## Monitoring

There are two http urls available for monitoring applications, the first is a trivial `alive` url that returns 200 if the service is up.

`wget https://myservice.com:2001/alive` should return 200 OK.

More usefully

`wget https://myservice.com:2001/alarms/lowbalance` will return a json response indicating whether all the monitored
wallets have balances above their low balance thresholds.

In the case where all wallets are ok the following is returned

`{"allOk":true,"lowBalancedWallets":[],"errors":[]}`

...otherwise, `allOk:false` will be returned along with details of the problems.
