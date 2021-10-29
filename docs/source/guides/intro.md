# PSG Services - Introduction

### PSG Services

The Professional Services Group have leveraged their collective experience
in providing solutions to enterprise clients to produce the PSG Services platform.

We've taken established implementation patterns and exposed only the API necessary to perform the useful function.

For example, when posting metadata to the blockchain, the important ingress details are the data and
the number of blocks the data should be buried under, and the result should be the transaction identifier.

The user of the PSG Metadata Service can happily ignore the state of the wallet or constructing a valid transaction.

PSG Services has two major components, a set of gRpc endpoints that allow the enterprise user to perform the aforementioned tasks
related to the Cardano blockchain, and a user interface to help users pay for and configure those services.


Further details on registering and paying in Ada for the services is available on this site, and you can view walkthrough
videos on our [youtube channel](https://youtube.com/playlist?list=PLnPTB0CuBOByp2KWl22ElFZsb63s72O1v)

Finally, there is a duplicate testnet facing PSG Services platform for evaluation and development purposes.
The testnet and production platforms are identical except that the testnet platform uses testnet nodes.

The connection details are [here](https://psg-services.readthedocs.io/en/latest/guides/psg_services_grpc_guide.html)

**Note**: Please report any issues to **[psg-tech@iohk.io](mailto:psg-tech@iohk.io)**

### Thank you

Thank you for using PSG Services, [feedback](mailto:enterprise.services@iohk.io) is always welcome, and we are
always looking to add new services and find ways of meeting enterprise clients needs.