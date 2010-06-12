Adding experimental measurements to a BCML file
===============================================

Experimental measurements coming from various sources, such as fold changes, p-values, concentrations, SNPs,  phosphate binding points of the corresponding proteins, can be easily added to a BCML file for viewing or analysis. The only requirement is that there must be a matching set of identifiers between the BCML file and the experimental measurements.

The mapping, or annotation, operation is divided into two main steps:

 1. Preparation of an experimental measurement file
 2. Incorporation of the measurements into the BCML file

Preparing the experimental measurement file
-------------------------------------------

The experimental measurement file is a simple two-column tab-delimited text file. In the first column there is a list of identifiers, and in the second column a list of experimental measurements. Other lines (such as a header) can be present, but will be ignored during the annotation.

The identifiers in the file must be of the same type that are present in the BCML file, under the ``<Organism name=...>`` sub-tags (for example Entrez Gene IDs).

.. note:: If you are using Excel to export the file, pay attention that numerical values are outputted with US locale, with decimal points, and not commas as in other locales.

Incorporating the experimental measures
---------------------------------------

The experimental measurements are incorporated in the BCML file with the ``bcml_annotate`` program::

        bcml_annotate --srcSBGN <source SBGN file> --db <database>
                      --varName <measurement name> 
                      --varFile <experimental measurement file>
                      --organism <organism> --outFile <destination BCML file>

``measurement name`` indicates the name of the variable that will be shown in the graphs (corresponds to SBGN's StateVariable), while ``database name`` indicates which identifier to use for the mapping (and values should be present in the BCML file). ``organism`` is specified to tell ``bcml_annotate`` which species to annotate for in case of multiple values. 

In case no match is found, no BCML file is produced and ``bcml_annotate`` issues a warning. There are also other less common options to specify the colors, how to handle multiple values for the same entity in the measurements, and so on. Execute ``bcml_annotate`` without arguments for a full list.

The resulting BCML file can then be either manipulated (filtered, etc.) or viewed after conversion to GraphML. In the latter case, entities affected by experimental measurements will appear colored (depending on the degree of the measurement) along with their actual value. The colors can be controlled from the command line or from the configuration file.

Exporting measurements from a BCML file
---------------------------------------

BCML files with existing measurements can be exported to entity lists along with their measurements, as a two-column tab-delimited file. To do, ``bcml_export`` is invoked, like :ref:`when exporting entity lists <exporting>` but adding the `--var` option::

        bcml_export --db <database> --method GeneList --organism <organism>
                    --srcSBGN <source SBGN file> --outFile <destination file>
                    --var <variable name>

``variable`` must be the same name as the one that was used when originally annotating the file, otherwise only an identifier list will be outputted. It is also convenient to use ``---disableFilter`` when exporting measurements data, so that filters will not exclude part of it.

The generated file can be opened and used with any program capable of loading tab-delimited data (including spreadsheet programs).

.. comment: a sample + graphical representation before/after would be useful to understand practically how to assign the measurement to a pathway.
