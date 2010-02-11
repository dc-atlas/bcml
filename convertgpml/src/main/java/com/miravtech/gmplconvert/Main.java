package com.miravtech.gmplconvert;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.genmapp.gpml._2008a.DataNode;
import org.genmapp.gpml._2008a.DataNodeType;
import org.genmapp.gpml._2008a.Pathway;

import com.miravtech.sbgn.NucleicAcidFeatureType;
import com.miravtech.sbgn.SBGNPDl1;

public class Main {

	
	static JAXBContext jaxbContext;   
	static Unmarshaller unmarshaller;
	static Marshaller marshaller;
	
	/**
	 * @param args
	 * @throws JAXBException 
	 */
	public static void main(String[] args) throws JAXBException {

		
		
		String source = "l:\\Documents and Settings\\r\\My Documents\\dcthera\\gpml tutti";
		String destination = "c:\\temp";
		if (args.length >= 2) {
			source = args[0];
			destination = args[1];
		}
		File srcDir = new File(source);
		File destDir = new File(destination);

		
		jaxbContext = JAXBContext.newInstance("org.genmapp.gpml._2008a:com.miravtech.sbgn:com.miravtech.sbgn_graphics");   
		unmarshaller = jaxbContext.createUnmarshaller();
		marshaller = jaxbContext.createMarshaller();

		
		for (File f : srcDir.listFiles( new GPMLFiles())) {
			File destSBGN = new File(destDir, GPMLFiles.getName(f.getName())+ ".xml" );
			gpmlconvert(f,destSBGN);
		}
	}

	/**
	 * 
	 * Converts the gpml to SBGN files.
	 * 
	 * @param sourceGPML the source file (must exist)
	 * @param destSBGN the destination file (will be overwritten)
	 * @throws JAXBException if any issue occurs with IO or with XML parsing
	 */
	public static void gpmlconvert(File sourceGPML, File destSBGN) throws JAXBException {
		Pathway gpmlpath = (Pathway) unmarshaller.unmarshal(sourceGPML);
		SBGNPDl1 sbgnpath = new SBGNPDl1();
		
		for (DataNode dn : gpmlpath.getDataNode()) {
			if (dn.getType() == DataNodeType.METABOLITE || dn.getType() == DataNodeType.GENE_PRODUCT  ) {
				// a gene product
				NucleicAcidFeatureType nf = new NucleicAcidFeatureType();
				nf.setLabel(dn.getTextLabel());
				nf.setID(dn.getGraphId());
				sbgnpath.getValue().getGlyphs().add(nf);
			}
		}
		
		marshaller.marshal(sbgnpath, destSBGN);
		
	}

}


class GPMLFiles implements FilenameFilter {
	public final static String GMPL = "gpml";
	private static Pattern file = Pattern.compile("(.*)\\."+GMPL);
	@Override
	public boolean accept(File dir, String name) {
		return file.matcher(name.toLowerCase()).matches();
	}
	public static String getName(String name) {
		Matcher m = file.matcher(name.toLowerCase());
		if (!m.matches())
			throw new RuntimeException("The name does not match the pattern");
		return m.group(0);
	}
}
