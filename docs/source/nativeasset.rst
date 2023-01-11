.. _nativeasset:

Native Assets Service - Documentation
#####################################

The native assets service wraps the work of creating native assets on Cardano blockchain in a gRpc defined service.

Native Assets functionality comes in two forms: gRPC service (similar to Metadata Service) and JAR library.

You can easily get Native assets JAR to the project from IOG Nexus repository ::

    <dependency>
      <groupId>iog.psg</groupId>
      <artifactId>native-assets-client_2.13</artifactId>
      <version>0.3.12</version>
    </dependency>

Both, the service and the client, provide a set of methods for working with native assets.

Additionally, you can get an initial overview of the API by checking `Native Assets Client <https://psg-releases.s3.us-east-2.amazonaws.com/native-assets-client-0.3.12.zip>`_ - commandline tool.

**Native Assets  - Supported operations:**
 - Create, get, delete and list policies
 - Import existing policy
 - Create, get, delete and list native assets
 - Mint (native asset)
 - Transfer (native asset)
 - Burn (native asset)
 - Send an airdrop of NFTs
 - Check airdrop status

This section describes the service API and how to use it.

**Native Assets Guides:**

.. toctree::
   :maxdepth: 2
   :titlesonly:

   Native Assets Service - API <guides/native_assets_api.md>
   Native Assets Client <guides/native_assets_client.md>

   Multisig Native Assets Service - API <guides/multisig_native_assets_api.md>
   Multisig Native Assets Client SDK HOW TO <guides/multisig_native_assets_sdk.md>
   Multisig Native Assets Client CLI <guides/multisig_native_assets_client.md>

.. raw:: html

   <p align="center">
        <a href='https://psg-services.readthedocs.io/en/latest/?badge=latest'>
            <img src='https://readthedocs.org/projects/psg-services/badge/?version=latest' alt='Documentation Status' />
        </a>
   </p>


