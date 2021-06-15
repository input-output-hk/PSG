==========================
PSG Services Documentation
==========================

.. toctree::
   :maxdepth: 3
   :caption: Guides
   :titlesonly:
   :hidden:


   New User guide <guides/new_user_guide.md>
   PSG Services guide <guides/psg_services_grpc_guide.md>
   Mail Service with gpg <guides/mail_service_guide_gpg.md>
   Mail Service with Mailvelope <guides/mail_service_guide_ui.md>
   Mail metadata verification <guides/mail_service_metadata_check.md>


.. raw:: html

   <p align="center">
        <a href='https://psg-services-draft.readthedocs.io/en/latest/?badge=latest'>
            <img src='https://readthedocs.org/projects/psg-services-draft/badge/?version=latest' alt='Documentation Status' />
        </a>
   </p>

PSG services core functionality:
################################

* `Submit metadata and attachments to the blockchain using mail clients from UI <https://psg-services-draft.readthedocs.io/en/latest/guides/mail_service_guide_ui.html>`_
* `Submit metadata and attachments to the blockchain using the gpg command-line tool <https://psg-services-draft.readthedocs.io/en/latest/guides/mail_service_guide_gpg.html>`_
* `Verify transaction metadata info via PSG Self Serve UI <https://psg-services-draft.readthedocs.io/en/latest/guides/mail_service_metadata_check.html>`_
* Submit metadata to the blockchain using Metadata service gRPC API
* Store file in AWS and get URL to file and file's hash via StoreAndHash service gRPC API
* Verify transaction metadata info via AuthMailMetadata gRPC API

**Pay attention that currently, our PROD environment is working using testnet**.