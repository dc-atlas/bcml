Creating pathways in BCML
=========================

As a pathway described in BCML is simply a text file, writing a BCML pathway requires no more than a common text editor. However, to ensure the writing of proper files and to increase efficiency, an XML editor is recommended. Also, an XML editor will make use of the BCML schema directly, preventing the creation of invalid elements. This will have the added advantage of reducing, if not eliminating completely, typing and validation errors.

Examples of XML editors that can be used to write BCML pathways include:

 * `XMLSpy <http://www.altova.com/xml-editor/>`_ (commercial)
 * `XMLSpear <http://www.donkeydevelopment.com>`_ (freeware)
 *  Eclipse IDE (open source)
 *  Microsoft Visual Studio (free Express version, commercial professional and better).

Describing how to write a SBGN compliant pathway is outside the scope of the document: in any case, you should use the `SBGN Process description specification <http://precedings.nature.com/documents/3721/version/1>`_ as a reference on how to structure your pathway. More detailed information on BCML itself and what it supports are in the schema documentation. Each of the objects of SBGN PD has a homonym correspondent in BCML.

Writing annotations
-------------------

Annotations are an extension of the schema that permit to associate specific findings and other facts to an element of a pathway. For example, you may want to associate the type of experiment where a specific interaction was recorded, or the Entrez Gene IDs related to a macromolecule.

The SBGN extension schema provides support for these kinds of annotations. For example, to annotate a macromolecule with a cell type in *Homo sapiens*:

.. code-block:: xml

        <Macromolecule ID="my_gene">
        ...
                <Finding>
                        <Organism>Homo sapiens</Organism>
                        <CellType>Cancer cells</CellType>
                </Finding>
        </Macromolecule>

Annotating findings enables the description of generic pathways. The generic pathways can be transformed to specific pathway; the elements not belonging to an organism or cell type of interest, can be removed with the bcml_filter tool.

Other valid information that can be added in Findings include for example PubMed IDs (using the ``PMID`` tag).

Additional annotations outside Findings can be used to indicate gene or protein identifiers, for example:

.. code-block:: xml

        <Macromolecule ID="my_gene">
        ...
                <Organism name="HS">
                        <annotation DB="EntrezGeneID" ID="148022"/>
                </Organism>
        </Macromolecule>

In this case, we indicate the ``EntrezGeneID`` 148022 associated to ``HS`` (which is *Homo sapiens*) for this macromolecule. Annotation of the macromolecules with known database symbols is a facilitator of analysis techniques and enables representation of experiments within the pathway.
The full list and description of available annotation features is in the schema documentation.


.. comment: providing a few of the SBGN sample pathways in BCML format would enhance the perception of the user regarding BCML capabilities.
