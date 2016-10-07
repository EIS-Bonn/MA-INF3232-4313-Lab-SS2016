package uni.bonn.eula.controller;
import java.util.*;

import org.json.simple.JSONObject;

import java.net.*;

import gate.*;
import gate.util.GateException;

import uni.bonn.eula.model.*;
import uni.bonn.eula.controller.SummaryController.PhrasePositionComparator;
import uni.bonn.eula.lib.*;
import uni.bonn.eula.lib.GateResources.SortedAnnotationList;

public class OntologyBasedAnnotController {
	private LicenseAgreement license = new LicenseAgreement();
	private GateResources gr;

	public OntologyBasedAnnotController() throws Exception{
		gr = GateResources.getInstance();
	}
	/**
	 * Builds Thread and pulls phrases from the thread
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public void buildCorpusExecutePipeline(URL url) throws Exception{

		gr.buildCorpusWithDoc(url);
		gr.execute();
		//license.setTitle("Apache");


		/*		


		//Get the subject
		HashSet<Annotation> subjAnnotSet = gr.getAnnotations(AnnotEnum.SubjectMail.name());
		if(subjAnnotSet.iterator().hasNext()){
			Annotation subjAnnot = subjAnnotSet.iterator().next();
			thread.setSubject(gr.getContentFromAnnot(subjAnnot));
		}

		GateResources.SortedAnnotationList sortedAnnots = new SortedAnnotationList();

		HashSet<Annotation> threadPartAnnotSet = gr.getAnnotations(AnnotEnum.ThreadPart.name());

		for(Annotation a: threadPartAnnotSet){
			sortedAnnots.addSortedExclusive(a);
		}

		//Build meta information
		Meta meta = buildMetaInformation();
		HashSet<String> peopleSet = new HashSet<String>();
		thread.clearThreadParts();
		ThreadPart tp = new ThreadPart();
		for (int i = 0; i < sortedAnnots.size(); ++i) {
			Annotation tpAnnot = (Annotation) sortedAnnots.get(i);
			if(gr.getContentFromCategory(tpAnnot,CategoryEnum.ThreadBody.getCategory()) != null){
				String body = gr.getContentFromCategory(tpAnnot,CategoryEnum.ThreadBody.getCategory());
				body = cleanString(body);
				if(body != null){
					tp.setBody(body);
				}

				thread.addThreadPart(tp);
				tp = new ThreadPart();
			}
			if(gr.getContentFromCategory(tpAnnot,CategoryEnum.FromEmail.getCategory()) != null){
				tp.setSenderEmail(gr.getContentFromCategory(tpAnnot,CategoryEnum.FromEmail.getCategory()));
			}
			if(gr.getContentFromCategory(tpAnnot,CategoryEnum.SentDate.getCategory()) != null){
				tp.setSentTime(gr.getContentFromCategory(tpAnnot,CategoryEnum.SentDate.getCategory()));
			}
			if(gr.getContentFromCategory(tpAnnot,CategoryEnum.SenderName.getCategory()) != null){
				String name = gr.getContentFromCategory(tpAnnot,CategoryEnum.SenderName.getCategory());
				//Add names of all active participants into meta information
				if(name != null && !name.trim().isEmpty()){
					tp.setSenderName(name.trim());
					peopleSet.add(name.trim());
				}
				else
				{
					tp.setSenderName("Someone");
				}

			}
		}
		meta.setPeopleList(peopleSet);
		thread.setMeta(meta);
		gr.freeResources();

		//get all the phrases for the thread
		getPhrases(thread,phrases);
		 */		
	}
	/**
	 * Gets email thread
	 * @return
	 */
	public LicenseAgreement getLicense(){
		return license;
	}
	/**
	 * Gets all the emails URLs, Names, etc.
	 * @return
	 * @throws Exception
	 */
	private Meta buildMetaInformation() throws Exception{
		Meta meta = new Meta();
		//Emails
		meta.setEmailList(buildAnnotsFromKind(AnnotEnum.Address.name(), KindEnum.email.name()));
		//URLs
		meta.setUrlList(buildAnnotsFromKind(AnnotEnum.Address.name(), KindEnum.url.name()));
		//DateTime
		meta.setDateTimeList(buildAnnots(AnnotEnum.Date.name()));
		return meta;
	}

	/**
	 * Gets all noun and verb phrases using Stanford NLP resources like sentence splitter and POS tagger
	 * @return
	 */

	private void getPhrases(LicenseAgreement thread, ArrayList<Phrase> phrases){

		StanfordResources sr = StanfordResources.getInstance();
		int i = 0;

		ArrayList<String> strPhrases = new ArrayList<String>();
		HashSet<String> firstEmailPhrases = new HashSet<String>();
		boolean isFirstEmail = true;
		for(ThreadPart tp: thread.getThreadParts()){
			strPhrases = new ArrayList<String>();
			sr.buildPhrases(strPhrases,tp.getBody());
			buildPhraseList(phrases,strPhrases,thread.getThreadParts().indexOf(tp));
			if(isFirstEmail){
				firstEmailPhrases.addAll(strPhrases);
			}
		}
		thread.setFirstEmailPhrases(firstEmailPhrases);
	}
	/**
	 * Builds phrase list from a list of strings. Adds position of phrase(which email it belongs to) and sets score to 0
	 * @param phrases
	 * @param strPhrases
	 * @param pos
	 */
	public void buildPhraseList(ArrayList<Phrase> phrases, ArrayList<String> strPhrases, int pos){
		for(String s:strPhrases){
			Phrase p = new Phrase();
			p.setPhrase(s);
			p.setPosition(pos);
			p.setScore(0);
			phrases.add(p);
		}
	}
	/**
	 * gets all annotation string for an annotation
	 * @param annot
	 * @return
	 * @throws Exception
	 */
	private HashSet<String> buildAnnots(String annot) throws Exception{
		//HashSet<Annotation> annots = gr.getAnnotations(annot);
		HashSet<Annotation> annots = null;
		HashSet<String> list = new HashSet<String>();

		for(Annotation a : annots){
			String str = cleanString(gr.getContentFromAnnot(a));
			if(str != null){
				list.add(str);
			}
		}
		return list;
	}
	/**
	 * Builds annotations with category
	 * @param annot
	 * @param cat
	 * @return
	 * @throws Exception
	 */
	private HashSet<String> buildAnnotsFromCat(String annot, String cat) throws Exception{
		//HashSet<Annotation> annots = gr.getAnnotations(annot);
		HashSet<Annotation> annots = null;
		HashSet<String> list = new HashSet<String>();

		for(Annotation a : annots){
			String str = cleanString(gr.getContentFromCategory(a, cat));
			if(str != null){
				list.add(str);
			}
		}
		return list;
	}
	/**
	 * Builds annotations with the "kind" feature in GATE's featuremap of an annotation
	 * @param annot
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getAnnotatedTextXML(String annotType) throws Exception {
		
		ArrayList<String> list = new ArrayList<String>();		
		AnnotationSet annots = gr.getAnnotations(annotType);

		System.out.println("Annotation Set Size = " + annots.size());
		
		SortedAnnotationList sortedAnnots = new SortedAnnotationList();		
		for(Annotation an: annots){
			sortedAnnots.addSortedExclusive(an);
		}

		for (int i = 0; i < sortedAnnots.size(); ++i) {
									
			Annotation a = (Annotation) sortedAnnots.get(i);		
			
			String str = gr.getContentFromAnnot(a);
			
			if(str != null){
				list.add(str);
			}				
		}
		return list;
	}

	public ArrayList<HashMap<String, String>> getAnnotatedText(String annotType) throws Exception {
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		AnnotationSet annots = gr.getAnnotations(annotType); 
		
		SortedAnnotationList sortedAnnots = new SortedAnnotationList(); 
		for(Annotation an: annots){
			sortedAnnots.addSortedExclusive(an);
		} 
		
		for (int i = 0; i < sortedAnnots.size(); ++i) {
			
			HashMap<String, String> annotation =  new HashMap<String, String>();
			JSONObject jsonAnnot = new JSONObject();
			Annotation a = (Annotation) sortedAnnots.get(i);
			
			if(a.getFeatures().get("generalAction") != null){
				
				String actionGeneral = a.getFeatures().get("generalAction").toString();				
				annotation.put("generalAction", actionGeneral);					
				
				String str = gr.getContentFromAnnot(a);				
				annotation.put("annotation", str);		
				
				jsonAnnot.putAll(annotation);				
				list.add(jsonAnnot); 
			}
			
			if(a.getFeatures().get("advancedAction") != null){					
				
				String actionAdvance = a.getFeatures().get("advancedAction").toString();				
				annotation.put("advancedAction", actionAdvance);				
				
				String str = gr.getContentFromAnnot(a);				
				annotation.put("annotation", str);		
				
				jsonAnnot.putAll(annotation);				
				list.add(jsonAnnot); 
			}
		} 

		return list;
	}


	private HashSet<String> buildAnnotsFromKind(String annot, String kind) throws Exception{
		//HashSet<Annotation> annots = gr.getAnnotations(annot);
		HashSet<Annotation> annots = null;
		HashSet<String> list = new HashSet<String>();

		for(Annotation a : annots){
			String str = cleanString(gr.getContentFromKind(a, kind));
			if(str != null){
				list.add(str);
			}
		}
		return list;
	}

	/**
	 * Cleans the string. Mainly used to clean body of an email, and if required phrases.
	 * @param str
	 * @return
	 */
	public static String cleanString(String str){
		if(str != null){
			//Removes the phrase Quoted 
			str = str.replaceAll("Quoted.*\n?.*","");
			//These are to remove apostrophes to get better phrases
			str = str.replaceAll("'d", " would");
			str = str.replaceAll("'m", " am");
			str = str.replaceAll("'ll", " will");
			str = str.replaceAll("Did'nt", "Did not");
			str = str.replaceAll("did'nt ", "did not");
			str = str.replaceAll("won't", "will not");
			str = str.replaceAll("Won't", "Will not");
			str = str.replaceAll("Had'nt", "Had not");
			str = str.replaceAll("had'nt", "had not");
			str = str.replaceAll("Can't", "Cannot");
			str = str.replaceAll("can't", "cannot");
			str = str.replaceAll("Should'nt", "Should not");
			str = str.replaceAll("should'nt", "should not");
			str = str.replaceAll("'ve", "have");
			str = str.replaceAll("'ve", "have");
			str = str.replaceAll("'re", "are");
			//Removes all automatic signatures assigned by iPhones and Blackberrys
			str = str.replaceAll("[Ss]ent[ ]+[Ff]rom[ ]+[Mm]y[ ]+.*", "");
			str = str.replaceAll("[Ss]ent[ ]+[Vv]ia[ ]+B.*", "");
			str = str.replaceAll("https://mail.google.com[/.?=&\\w\\d]*", "");
			str = str.replaceAll("[\\d]*[\\d]/[\\d]*[\\d]/[\\d]*[\\d][ ]*Gmail.*", "");
			//Tries to match signature
			str = str.replaceAll("\n((([Rr]egards)|([Th]anks)|([Bb]est)|([W]arm)).*[,.!]?).*([A-Z][a-zA-Z0-9,.-])*[ ]*([A-Z][a-zA-Z0-9,.-]*)*[ ]*.*\n?.*", "");
			//Matches a few unwanted dates
			str = str.replaceAll("[\\w]+[ ]+[\\d]+[ ]*,[ ]*[\\d]+[ ]*at[ ]*[\\d]+[ ]*:[ ]*[\\d]+[ ]*[\\w]*", "");
			//Matches dates like this: Wed, 19 Dec 2012 21:20:40+0800
			str = str.replaceAll("[A-Za-z]{3}[, ]+[0-9]+((th)|(rd))?[ ]*[,]?[ ]*[0-9]+[ ]*(at)?[ ]*.*", "");
			str = str.replaceAll("NOTICE.*", "");
			str = str.replaceAll("<.*>.*", "");
			str = str.replaceAll("\\[.*\\]", "");
			str = str.replaceAll("__+.*", "");
			//removes unwanted characters added by Stanford Parser
			str = str.replaceAll("-RSB-.*", "");
			str = str.replaceAll("-LSB-.*", "");
			str = str.replaceAll("-RRB-.*", "");
			str = str.replaceAll("-LRB-.*", "");
			str = str.replaceAll("Cc.*\n", "");
			str = str.replaceAll("``.*''", "");
			str = str.replaceAll("--\n.*", "");
			//Removes residual dates
			str = str.replaceAll("On[ ]+[A-Za-z]{3}[ ]*,[ ]*[0-9]+[ ]*,.*","");
			str = str.replaceAll("\n.*\\|.*", "");
			str = str.replaceAll("\n", " ");
			return str;
		}
		else
			return null;

	}

	public SummaryXML getSummaryXML() throws Exception{

		ArrayList<Phrase> phrases = new ArrayList<Phrase>();
		SummaryXML summary = new SummaryXML();
		
		ArrayList<String> permissions = getAnnotatedTextXML(AnnotEnum.Permission.name());		
		summary.setPermissions(permissions);

		ArrayList<String> prohibitions = getAnnotatedTextXML(AnnotEnum.Prohibition.name());		
		summary.setProhibitions(prohibitions);

		ArrayList<String> duties = getAnnotatedTextXML(AnnotEnum.Duty.name());		
		summary.setDuties(duties);		


		return summary;
	}
	
	public Summary getSummary() throws Exception{

		ArrayList<Phrase> phrases = new ArrayList<Phrase>();
		Summary summary = new Summary();
		
		ArrayList<HashMap<String, String>> permissions = getAnnotatedText(AnnotEnum.Permission.name());		
		summary.setPermissions(permissions);

		ArrayList<HashMap<String, String>> prohibitions = getAnnotatedText(AnnotEnum.Prohibition.name());		
		summary.setProhibitions(prohibitions);

		ArrayList<HashMap<String, String>> duties = getAnnotatedText(AnnotEnum.Duty.name());		
		summary.setDuties(duties);

		return summary;
	}


}