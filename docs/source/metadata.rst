.. _metadata:

Metadata Service - Documentation
================================

The metadata service wraps the work of posting metadata to the Cardano blockchain in a gRpc defined service.

This allows for the automatic generation of clients using the specified IDL.

Service
-------

The service wraps access to the Cardano wallet backend API. It supports multiple wallets via its configuration file.
It allows a single service instance to be used by various clients as a multi-tenant service.
However, it also works as a microservice integrated with a particular solution.

This section describes the service API and how to use it.

.. toctree::
   :maxdepth: 2
   :titlesonly:

   Metadata Service - API <guides/metadata_service_guide.md>
   Metadata Service - Configuration <guides/metadata_service_server_guide.md>

.. raw:: html

   <p align="center">
        <a href='https://psg-services.readthedocs.io/en/latest/?badge=latest'>
            <img src='https://readthedocs.org/projects/psg-services/badge/?version=latest' alt='Documentation Status' />
        </a>
   </p>


