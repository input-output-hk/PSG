PSG Services - Documentation
============================

.. toctree::
   :maxdepth: 2
   :caption: Guides
   :titlesonly:
   :hidden:

   Getting Started <start.rst>
   Metadata Service <metadata.rst>
   StoreAndHash Service <storeandhash.rst>
   Mail Service <mail.rst>
   Native Assets Service <nativeasset.rst>
   Features <features.rst>


.. raw:: html

   <p align="center">
        <a href='https://psg-services.readthedocs.io/en/latest/?badge=latest'>
            <img src='https://readthedocs.org/projects/psg-services/badge/?version=latest' alt='Documentation Status' />
        </a>
   </p>

PSG Services
-------------

The Professional Services Group have leveraged their collective experience
in providing solutions to enterprise clients to produce the PSG Services platform.

We've taken established implementation patterns and exposed only the API necessary to perform the useful function.

For example, when posting metadata to the blockchain, the important ingress details are the data and
the number of blocks the data should be buried under, and the result should be the transaction identifier.

The user of the PSG Metadata Service can happily ignore the state of the wallet or constructing a valid transaction.

PSG Services has two major components, a set of gRpc endpoints :ref:`api` that allow the enterprise user to perform the aforementioned tasks
related to the Cardano blockchain, and a user interface :ref:`ui` to help users pay for and configure those services.

Further details and :ref:`pricing` information for the services are available on this site, and you can view walkthrough
videos on our `youtube channel <https://youtube.com/playlist?list=PLnPTB0CuBOByp2KWl22ElFZsb63s72O1v>`_

Finally, there is a duplicate testnet facing PSG Services platform for evaluation and development purposes.
The `testnet <https://psg-testnet.iog.services/>`_ and `production <https://psg.iog.services/>`_ platforms are identical except that the testnet platform uses testnet nodes.

The connection details can be found `here <https://psg-services.readthedocs.io/en/latest/guides/psg_services_grpc_guide.html>`_

**Note**: Please report any issues to `psg-tech@iohk.io <mailto:psg-tech@iohk.io>`_

Thank you
----------

Thank you for using PSG Services, `feedback <mailto:enterprise.services@iohk.io>`_ is always welcome, and we are
always looking to add new services and find ways of meeting enterprise clients needs.

.. _ui:

Self Serve UI
-------------
* `PSG Mainnet <https://psg.iog.services/>`_
* `PSG Testnet <https://psg-testnet.iog.services/>`_

.. _api:

API and Features
----------------

* Submit metadata to Cardano blockchain using :ref:`Metadata Service <metadata>` gRPC API
* Store file in AWS S3 or IPFS and get URL to file and file's hash via :ref:`StoreAndHash Service <storeandhash>` gRPC API
* Create, transfer and burn native assets on Cardano blockchain using :ref:`Native Assets Service <nativeasset>`
* Submit metadata and attachments as a part of email message using :ref:`mail clients from UI and commandline tools <mail>`
* Other :ref:`features <features>`, like checking transaction metadata

.. _pricing:

Price List
------

(API call prices include all network transaction fees)

* Metadata service
    * Submit Metadata: 10 credits
    * List Transactions: 1 credit
* Store And Hash service
    * Store And Hash: 1 credit
* Email
    * Authenticate Email: 15 credits
* Custodial Native Assets service
    * Create / Delete Policy: 5 credits
    * Get Policy / List Policies: 1 credit
    * Import Policy: 5 credits
    * Create / Delete Asset: 5 credits
    * Get Asset / List Assets: 1 credit
    * Fund Policy (per 1 ADA): 25 credits
    * Mint Asset: 150 credits
    * Transfer Asset: 25 credits
    * Burn Asset: 25 credits
    * Perform Airdrop (per NFT): 25 credits
    * Check Airdrop Status: 1 credit
* Non-Custodial Native assets:
    * Create / Delete Policy: 5 credits
    * Get Policy / List Policies: 1 credit
    * Mint Asset: 150 credits
    * Transfer Asset: 150 credits
    * Burn Asset: 150 credits
    * Add witness / List witnesses: 1 credit
    * Send transaction: 25 credits
    * Get transaction/ List transactions: 1 credit
