.. _mail:

Mail Service - Documentation
############################

The Mail Service simplifies the mechanism of storing custom metadata on Cardano blockchain.

How it works?
-------------

Steps:

* prepare message with metadata and a set of mandatory keys
* send message to predefined email addresses
* Mail Service will process the message and attachments, store it at AWS S3, gets hash from the file and make a transaction with hash metadata
* Mail Service will respond with a link to transaction at Cardano Explorer

This section contains a set of guides on how to use Mail Service via UI (Mailvelope) or via command line.

.. toctree::
   :maxdepth: 2
   :titlesonly:

   Mail Service - UI <guides/mail_service_guide_ui.md>
   Mail Service - Commandline <guides/mail_service_guide_gpg.md>

.. raw:: html

   <p align="center">
        <a href='https://psg-services.readthedocs.io/en/latest/?badge=latest'>
            <img src='https://readthedocs.org/projects/psg-services/badge/?version=latest' alt='Documentation Status' />
        </a>
   </p>