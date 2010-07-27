Installation
============

The  BCML manual is divided into two parts: the XML schema which is needed to write files that are compliant to the specification, and the utilities that are needed to manipulate, validate and export the data.

XML Schema
----------

To use the XML Schema, there is no need of installation. Simply copy the XSD and XML files from the schema archive in a directory of your choice and you will be already ready to use them.

Utility programs
----------------

BCML also offers a series of utilities to perform various operation on BCML schema compliant XML files. In order to use them, you need to have the `Sun Java Runtime Environment (JRE) <http://www.java.com>`_ installed already, at least version 1.6.  For Linux systems, the tools were only tested for Sun Java Virtual Machine.

Once this prerequisite is met, you need to download the utility archive (a ZIP file or a TGZ file, depending on your operating system), unpack it to a directory of your choice. The utility files are in the ``bin`` directory and can be used directly through the command prompt (Windows), a shell (Linux) or the Terminal (OS X). To uninstall the utilities, simply delete the folder.

Adding new annotations
----------------------

Sometimes you may want to add additional annotations to the BCML-supplied definitions to build more complete pathway definitions: for example, adding new cell types. This is done through an additional program, ``bcml_add_ontology``. ``bcml_add_ontology`` is a small program written in Python that simplifies the addition of new terms to the BCML schemas.

``bcml_add_ontology`` has the following requirements:

 * Python (2.5) or greater (http://www.python.org)
 * The lxml Python module, 2.0 or greater (http://codespeak.net/lxml)
 * The datamatrix Python module 0.9 or greater (http://pypi.python.org/pypi/datamatrix/0.9)

Installers for the major platforms are available on the linked web sites. Once the depenencies are satisfied, execute ``bcml_add_ontology`` as follows::

        bcml_add_ontology -o <ontology file> -s <type> <source> <destination>

where ``ontology file`` refers to a text file containing the new terms to add, one per line, ``type`` indicates the category you want to add items to (e.g., ProvenIn, CellType), source is the full path to your ``SBGN-ext.xsd`` file (which contains the definitions) and ``destination`` the new file to be written.

To use the new XSD file, replace the old ``SBGN-ext.xsd`` file with the new one (make a backup copy first). Now you can use any XML editor to build your pathway with the new annotations.

To allow the new schema to be used in the tools (for example validation), you need to rebuild them (see below).

Rebuilding the utility programs
-------------------------------

.. warning:: The following instructions are for experienced users only.

If you make any modification to the schemas, you cannot use the pre-built utilities, and you need to rebuild them. Rebuilding them requires `Apache Maven <http://maven.apache.org/download.html>`_. Once you have installed Maven and its dependencies, download the source archive, and unpack it to a directory of your choice.  Add your modified schema files (e.g. ``SBGN-ext.xsd``) to the ``sbgnpdschema/src/main/xsd/`` directory, overwriting the existing ones. Notice that the names must be the same or rebuilding will fail Notice that the names must be the same or rebuilding will fail.

Lastly, Open a command prompt to that directory and type ``make`` (Linux/Unix) or ``make.bat`` (Windows) to build the new utilities (you willl find them in the ``bin`` directory once building is complete).
