package uni.bonn.eula.dao;

import java.net.URL;
import java.util.ArrayList;

import uni.bonn.eula.model.*;
import uni.bonn.eula.controller.*;
public class ThreadDao {
	/**
	 * Gets email thread for the service
	 * @param u
	 * @return
	 * @throws Exception
	 */

	public LicenseAgreement getThreadForDoc(URL u) throws Exception{
		OntologyBasedAnnotController aCtrl = new OntologyBasedAnnotController();
		LicenseAgreement thread = new LicenseAgreement();
		ArrayList<Phrase> list = new ArrayList<Phrase>();
		//aCtrl.buildThreadAndPhraseList(u,thread,list);
		return thread;
	}

	/**
	 * Gets summary for the service
	 * @param u
	 * @return
	 * @throws Exceptions
	 */
	public Summary getSummaryForDoc(URL u) throws Exception{
		OntologyBasedAnnotController OntBaseAnnot = new OntologyBasedAnnotController();

		OntBaseAnnot.buildCorpusExecutePipeline(u);

		Summary summary = new Summary();
		
		summary = OntBaseAnnot.getSummary();

		return summary;		
	}
	
	/**
	 * Gets summary for the service in XML
	 * @param u
	 * @return
	 * @throws Exceptions
	 */
	public SummaryXML getSummaryForDocXML(URL u) throws Exception{
		OntologyBasedAnnotController OntBaseAnnot = new OntologyBasedAnnotController();

		OntBaseAnnot.buildCorpusExecutePipeline(u);

		SummaryXML summary = new SummaryXML();
	
		summary = OntBaseAnnot.getSummaryXML();
	

		return summary;
		
	}
}
