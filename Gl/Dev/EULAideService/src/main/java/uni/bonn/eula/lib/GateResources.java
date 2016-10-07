package uni.bonn.eula.lib;
import java.util.*;
import java.io.*;
import java.net.*;

import uni.bonn.eula.controller.OntoRootApp;
import uni.bonn.eula.controller.TermsConditionsApp;
import uni.bonn.eula.model.*;

import gate.*;
import gate.creole.*;
import gate.gui.MainFrame;
import gate.util.*;
import gate.util.persistence.PersistenceManager;

/**
 * Class to get all data from the document using GATE modules. 
 *
 */
public class GateResources {

	//private SerialAnalyserController serAnCtrlr= null;
	private CorpusController termsConditionPipeLine = null;
	private CorpusController rootFinderPipeLine = null;


	Corpus corpus = null;
	private static GateResources instance = null;

	private HashSet <String> reqAnnots = new HashSet<String>();
	
	
	
	/**
	 * Singleton constructor to initialize GATE. This takes a couple of seconds,
	 * and we do not want to do this for each instance
	 * @throws GateException
	 */
	protected GateResources() throws GateException{
		//Gate.setGateHome(new File(getClass().getResource("/").getPath()));
		Gate.setGateHome(new File("C:/Program Files/GATE_Developer_8.1"));
		File gateHome = Gate.getGateHome();
		File pluginsHome = new File(gateHome,"plugins");
		Gate.setPluginsHome(pluginsHome);
		Gate.init();
		//MainFrame.getInstance().setVisible(true);		
		rootFinderPipeLine = new SerialAnalyserController();			

		termsConditionPipeLine = new SerialAnalyserController();	
		
	}

	/**
	 * Generates a singleton instance
	 * @return
	 * @throws GateException
	 */
	public static GateResources getInstance() throws GateException{
		if(instance == null){
			instance = new GateResources();
		}

		return instance;
	}
	
	
	
	/**
	 * Initialize ANNIE and other processing resources here
	 */
	public void initializePipeLines()
	{
		try {
			
	        OntoRootApp rootFinder = new OntoRootApp();
	        rootFinderPipeLine = rootFinder.createResources();

			TermsConditionsApp mainPipeLine = new TermsConditionsApp();
	        termsConditionPipeLine = mainPipeLine.createResources(rootFinderPipeLine);
			Out.prln("Processing resources are loaded");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private void setCorpus() {
		termsConditionPipeLine.setCorpus(corpus);             
	}
	
	public void execute() throws GateException {
		
		termsConditionPipeLine.execute();
		Out.prln("Main Pipe Line Executed.");
	}
	
	/**
	 * Builds corpus with URL
	 * @param u
	 * @throws ResourceInstantiationException
	 */
	public void buildCorpusWithDoc(URL u) throws ResourceInstantiationException{
		corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");
		//corpus.clear();
		FeatureMap params = Factory.newFeatureMap();
		params.put("sourceUrl", u);
		params.put("preserveOriginalContent", new Boolean(true));
		params.put("collectRepositioningInfo", new Boolean(true));
		Out.prln("Creating Gate document for " + u);
		Document temp = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);
		corpus.add(temp);
		setCorpus();
	}
	
	/**
	 * Builds corpus with file
	 * @param file
	 * @throws ResourceInstantiationException
	 * @throws MalformedURLException
	 */
	public void buildCorpusWithDoc(File file) throws ResourceInstantiationException, MalformedURLException{
		corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");
		corpus.clear();
		FeatureMap params = Factory.newFeatureMap();
		URL u = file.toURI().toURL();
		params.put("sourceUrl", u);
		params.put("preserveOriginalContent", new Boolean(true));
		params.put("collectRepositioningInfo", new Boolean(true));
		Out.prln("Creating doc for " + u);
		Document temp = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);
		corpus.add(temp);
		setCorpus();
	}
	
	public void setRequiredAnnots(HashSet<String> list){
		reqAnnots.clear();
		reqAnnots.addAll(list);
	}
	/**
	 * Default setter
	 * @return
	 */
	public HashSet<Annotation> getAnnotations(){
		if(corpus.iterator().hasNext()){
			Document doc = corpus.iterator().next();
			return new HashSet<Annotation>(doc.getAnnotations().get(reqAnnots));
		}
		else
			return null;
	}
	/**
	 * Gets specific annotations
	 * @param type
	 * @return
	 */
	public AnnotationSet getAnnotations(String type){
		if(corpus.iterator().hasNext()){
			Document doc = corpus.iterator().next();
			
			//System.out.println("document content: " + doc.getContent().toString());
			System.out.println("All Annotation Sets: "  + doc.getAnnotations().getAllTypes());
			return doc.getAnnotations().get(type);
		}
		else
			return null;
	}
	/**
	 * Gets string from annotation
	 * @param annot
	 * @param cat
	 * @return
	 * @throws Exception
	 */
	public String getContentFromCategory(Annotation annot, String cat) throws Exception{	
		if(annot.getFeatures().get(Constants.CATEGORY).equals(cat)){
			return corpus.get(0).getContent().getContent(annot.getStartNode().getOffset().longValue(),annot.getEndNode().getOffset().longValue()).toString();
		}
		
		return null;
	}
	
	/**
	 * Gets string from annotation
	 * @param annot
	 * @return
	 * @throws Exception
	 */
	public String getContentFromAnnot(Annotation annot) throws Exception{	
		//return (String)annot.getFeatures().get(Constants.STRING);
		return corpus.get(0).getContent().getContent(annot.getStartNode().getOffset().longValue(),annot.getEndNode().getOffset().longValue()).toString();
	}
	/**
	 * Gets string from annotation
	 * @param annot
	 * @param kind
	 * @return
	 * @throws InvalidOffsetException
	 */
	public String getContentFromKind(Annotation annot, String kind) throws InvalidOffsetException{
		if(annot.getFeatures().get(Constants.KIND).equals(kind)){
			return corpus.get(0).getContent().getContent(annot.getStartNode().getOffset().longValue(),annot.getEndNode().getOffset().longValue()).toString();
		}
		return null;
	}
	
	public void freeResources(){
		corpus.cleanup();
		Factory.deleteResource(corpus);
		
	}
	
	/**
	 * Unit test everything here before you push stuff to DAO
	 * @param a
	 * @throws Exception 
	 */
/*	
	public static void main(String a[]) throws Exception
	{
		uni.bonn.eula.model.EmailThread thread = new EmailThread();
		GateResources gr = GateResources.getInstance();
		gr.initialize();
		gr.buildCorpusWithDoc(GateResources.class.getResource("/docs/Gigzolo rehearsal.pdf"));
		gr.execute();
		//Get the subject
		HashSet<Annotation> subjAnnotSet = gr.getAnnotations("SubjectMail");
		if(subjAnnotSet.iterator().hasNext()){
			Annotation subjAnnot = subjAnnotSet.iterator().next();
			thread.setSubject(gr.getContentFromAnnot(subjAnnot));
			Out.prln(gr.getContentFromAnnot(subjAnnot));
		}
		
		Out.prln("\n\n Thread Part \n\n");
		HashSet<Annotation> threadPartAnnotSet = gr.getAnnotations("ThreadPart");
		thread.clearThreadParts();
		ThreadPart tp = new ThreadPart();
		
		StanfordResources stanfordParser = StanfordResources.getInstance();
		
		SortedAnnotationList sortedAnnots = new SortedAnnotationList();
		
		for(Annotation an: threadPartAnnotSet){
			sortedAnnots.addSortedExclusive(an);
		}
		//ThreadPart tp = new ThreadPart();
		for (int i = 0; i < sortedAnnots.size(); ++i) {
			Annotation tpAnnot = (Annotation) sortedAnnots.get(i);
			if(gr.getContentFromCategory(tpAnnot,CategoryEnum.ThreadBody.getCategory()) != null){
				tp.setBody(gr.getContentFromCategory(tpAnnot,CategoryEnum.ThreadBody.getCategory()));
				
				// remove [Quoted  text  hidden]
				String removedQuotedText = tp.getBody().replace("[Quoted  text  hidden]", "");
				stanfordParser.setThreadPart(removedQuotedText);
				stanfordParser.parseThreadPart();

				thread.addThreadPart(tp);

			}
			if(gr.getContentFromCategory(tpAnnot,CategoryEnum.FromEmail.getCategory()) != null){
				tp.setSenderEmail(gr.getContentFromCategory(tpAnnot,CategoryEnum.FromEmail.getCategory()));
			}
			if(gr.getContentFromCategory(tpAnnot,CategoryEnum.SentDate.getCategory()) != null){
				tp.setSentTime(gr.getContentFromCategory(tpAnnot,CategoryEnum.SentDate.getCategory()));
			}
			if(gr.getContentFromCategory(tpAnnot,CategoryEnum.SenderName.getCategory()) != null){
				tp.setSenderName(gr.getContentFromCategory(tpAnnot,CategoryEnum.SenderName.getCategory()));
			}
		}
		
	}
*/	
	/**
	 * References: Gate Resources
	 * @author svalmiki
	 *
	 */
	public static class SortedAnnotationList extends Vector {
		public SortedAnnotationList() {
			super();
		} // SortedAnnotationList

		public boolean addSortedExclusive(Annotation annot) {
			Annotation currAnot = null;
			// overlapping check
			for (int i = 0; i < size(); ++i) {
				currAnot = (Annotation) get(i);
				if (annot.overlaps(currAnot)) {
					return false;
				} // if
			} // for
			long annotStart = annot.getStartNode().getOffset().longValue();
			long currStart;
			// insert
			for (int i = 0; i < size(); ++i) {
				currAnot = (Annotation) get(i);
				currStart = currAnot.getStartNode().getOffset().longValue();
				if (annotStart < currStart) {
					insertElementAt(annot, i);
					/*
					 * Out.prln("Insert start: "+annotStart+" at position: "+i+
					 * " size="+size()); Out.prln("Current start: "+currStart);
					 */
					return true;
				} // if
			} // for
			int size = size();
			insertElementAt(annot, size);
			// Out.prln("Insert start: "+annotStart+" at size position: "+size);
			return true;
		} // addSorted
	} // SortedAnnotationList
} // class StandAloneAnnie