.. _nativeasset:

Native Assets Service - Documentation
#####################################

The native assets service wraps the work of creating native assets on Cardano blockchain in a gRpc defined service.

Native Assets functionality comes in 3 forms: gRPC service (similar to Metadata Service), ClI Client and JAVA SDK library.

Currently there are 2 set of APIs.
  - Custodial Native Assets APis
  - Non-custodial Native Assets APIs
Both provides a set of methods for working with native assets.

Additionally, you can get an initial overview of the API by checking `Native Assets Client <https://psg-releases.s3.us-east-2.amazonaws.com/native-assets-client-0.3.1.zip>`_ - commandline tool.

**Custodial Native Assets - Supported operations:**
 - Create, get, delete and list policies
 - Import existing policy
 - Create, get, delete and list native assets
 - Mint (native asset)
 - Transfer (native asset)
 - Burn (native asset)
 - Send an airdrop of NFTs
 - Check airdrop status

 **Non-Custodial Native Assets(Multisig Native Assets)  - Supported operations:**
  - Create, get and list policies
  - Mint (native asset)
  - Transfer (native asset)
  - Burn (native asset)
  - Add signature/witness to the Transaction
  - Send the Transaction
  - List the Transactions

 *Client side helpers*
  - Generation of public-key
  - Generation of the Address
  - Generation of key pair (public-key,private key)
This section describes the service API and how to use it.

**Native Assets Guides:**

.. toctree::
   :maxdepth: 2
   :titlesonly:

   Native Assets Service - gRPC API <guides/native_assets_api.md>
   Native Assets Service - JAVA SDK <guides/native_assets_sdk.md>
   Native Assets Service - CLI <guides/native_assets_client.md>

   Multisig Native Assets Service - gRPC API <guides/multisig_native_assets_api.md>
   Multisig Native Assets Service - JAVA SDK <guides/multisig_native_assets_sdk.md>
   Multisig Native Assets Service - CLI <guides/multisig_native_assets_client.md>

.. raw:: html

   <p align="center">
        <a href='https://psg-services.readthedocs.io/en/latest/?badge=latest'>
            <img src='https://readthedocs.org/projects/psg-services/badge/?version=latest' alt='Documentation Status' />
        </a>
   </p>


