==========================
PSG Services Documentation
==========================

.. toctree::
   :maxdepth: 2
   :caption: Guides
   :titlesonly:
   :hidden:


   New User guide <guides/new_user_guide.md>
   PSG Services <guides/psg_services_grpc_guide.md>
   Metadata Service <guides/metadata_service_guide.md>
   StoreAndHash Service <guides/store_and_hash_service_guide.md>
   Metadata Service (server) <guides/metadata_service_server_guide.md>
   StoreAndHash Service (server) <guides/store_and_hash_service_server_guide.md>

   Mail Service (command-line) <guides/mail_service_guide_gpg.md>
   Mail Service (Mailvelope) <guides/mail_service_guide_ui.md>
   Transaction metadata verification <guides/mail_service_metadata_check.md>


.. raw:: html

   <p align="center">
        <a href='https://psg-services-draft.readthedocs.io/en/latest/?badge=latest'>
            <img src='https://readthedocs.org/projects/psg-services-draft/badge/?version=latest' alt='Documentation Status' />
        </a>
   </p>

Link: `PSG Self Serve UI <https://prod.iog.services/>`_
#######################################################

**Please note, that for now PSG Services use** `Cardano testnet <https://developers.cardano.org/en/testnets/cardano/overview/>`_

If you want to use only gRPC services - skip account creation described in New User Guide.

PSG services core functionality:
################################

* Submit metadata to the blockchain using Metadata service gRPC API
* Store file in AWS and get URL to file and file's hash via StoreAndHash service gRPC API
* Submit metadata and attachments to the blockchain using mail clients from UI
* Submit metadata and attachments to the blockchain using the gpg command-line tool
* Verify transaction metadata info via PSG Self Serve
