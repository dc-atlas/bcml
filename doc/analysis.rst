Using BCML pathways for analysis
================================

BCML files can be converted into formats suitable for use with common pathway analysis algorithms such as the Fisher's Exact Test[Grosu2002]_, Gene Set Enrichment Analysis[Subramanioan2005]_, and impact analysis[Tarca2009]_ (in the form implemented by the SPIA R package).

For most of the analysis algorithms, an export to gene list will be sufficient; for impact analysis there is a specialized workflow.

Exporting data to gene lists
----------------------------

Gene lists (which can then be used for Fisher's Test or GSEA) can be simply exported using ``bcml_export`` as :ref:`following the procedure to export identifiers <exporting>`, without ising the `--var`` argument, and keeping or disabling the filter as appropriate. Once the gene list has been obtained, it can be used according to the selected analysis procedure.

Exporting data for impact analysis
----------------------------------

Exporting data for use with impact analysis requires different arguments to be passed to ``bcml_export``. Specifically, it must be invoked with different ``--db`` and ``--method`` switches::

        bcml_export --method SPIA --organism <organism> --srcSBGN <source SBGN file>
                    --outFile <destination file> --var SPIA --disableFilter

Notice also the use of ``--disableFilter``, as SPIA requires the full pathway to work effectively. 

The export will produce an R file. This needs to be sourced in R before starting the analysis, as follows:

.. code-block:: r

        >>> source("mypathwayfile.r")

.. warning:: SPIA needs to be installed, or the operation will fail. Also, the oepration will write a file to SPIA's installation directory: make sure your user account has sufficient rights to write there.

.. note:: You will need the BCML-bundled version of SPIA (spia.r) in order to run analysis on the BCML-exported pathways.
