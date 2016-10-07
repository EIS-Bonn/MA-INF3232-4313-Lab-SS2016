package uni.bonn.eula.controller;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import gate.CorpusController;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.creole.ANNIEConstants;
import gate.creole.SerialAnalyserController;
import gate.creole.gazetteer.FlexibleGazetteer;
import gate.creole.gazetteer.Gazetteer;
import gate.creole.ontology.OInstance;
import gate.creole.ontology.Ontology;
import gate.util.GateException;

public class TermsConditionsApp {
	private CorpusController termsConditions = null;
	

    private FlexibleGazetteer createFlexibleGazetteer(CorpusController rootFinder, Ontology ontforgaz) throws GateException{
 	   
 	   //load the Gazetteer_Ontology_Based
 	   
 	   try {
 	    
 	    Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Gazetteer_Ontology_Based").toURI().toURL());
 	       
 	   //need Tools plugin for flexible Gazetteer
 	   Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tools").toURI().toURL());
 	    
 	   } catch (GateException e) {
 	    // TODO Auto-generated catch block
 	    e.printStackTrace();
 	   } catch (MalformedURLException e) {
 	    // TODO Auto-generated catch block
 	    e.printStackTrace(); 
 	   } 

 	   
 	   FeatureMap params = Factory.newFeatureMap();
 	   params.put("ontology", ontforgaz);
 	   params.put("rootFinderApplication", rootFinder);
 	   System.out.println("Done.....");
 	   Gazetteer ontoRootGazetteer = (Gazetteer)Factory.createResource("gate.clone.ql.OntoRootGaz",params); 
 	   
 	  
 
 	  FeatureMap params2 = Factory.newFeatureMap();
 	  ArrayList<String> inputFeature = new ArrayList<String>();
 	  inputFeature.add("Token.root");
 	  params2.put("gazetteerInst", ontoRootGazetteer);
 	  params2.put("inputFeatureNames",inputFeature);
 	  
 	 FlexibleGazetteer flexibleGazetteer = (FlexibleGazetteer)Factory.createResource("gate.creole.gazetteer.FlexibleGazetteer",params2); 

 	   	  
 	  return flexibleGazetteer;
 
 }
	
	
	
    public  CorpusController createResources (CorpusController rootFinder) throws GateException {
  	  

  	  try {
  	    	  
  	   //Load ANNIE for the Tokeniser and POS Tagger , ...
  	   Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), ANNIEConstants.PLUGIN_DIR).toURL());
	   
  	   //need Tools plugin for the Morphological Analyser 
	   Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tools").toURL());

  	   //load Ontology plugin
  	   File pluginHome = new File (new File(Gate.getGateHome(),"plugins"), "Ontology");
  	   Gate.getCreoleRegister().registerDirectories(pluginHome.toURI().toURL());
  	  
  	   
  	  } catch (GateException e) {
  	   // TODO Auto-generated catch block
  	   e.printStackTrace();
  	  } catch (MalformedURLException e) {
  	   // TODO Auto-generated catch block
  	   e.printStackTrace(); 
  	  } 
  	  
  	  String[] processingResources = {
  			"gate.creole.annotdelete.AnnotationDeletePR",  
  	        "gate.creole.tokeniser.DefaultTokeniser",
  			"gate.creole.splitter.SentenceSplitter",   			  
  	        "gate.creole.POSTagger"};
  	  
  	  termsConditions = (CorpusController)Factory.createResource("gate.creole.SerialAnalyserController");
  	   
  	  for(int pr = 0; pr < processingResources.length; pr++) {
  	         System.out.print("\t* Loading " + processingResources[pr] + " ... ");
  	         ((SerialAnalyserController)termsConditions).add((gate.LanguageAnalyser)Factory.createResource(processingResources[pr]));
  	         System.out.println("done");
  	  }

	   //add my defined Gazetteer
		FeatureMap gazParam = Factory.newFeatureMap();
		gazParam.put("listsURL", getClass().getResource("/gazetteer/lists.def"));
		ProcessingResource annieGaz =(ProcessingResource) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", gazParam);
	   ((SerialAnalyserController)termsConditions).add(annieGaz);
		

		ProcessingResource morphAnalyzer =(ProcessingResource) Factory.createResource("gate.creole.morph.Morph");
		((SerialAnalyserController)termsConditions).add(morphAnalyzer);	   
	   
		
	   // load the Ontology 
		FeatureMap fm = Factory.newFeatureMap ();
		try {
		 fm.put("rdfXmlURL", new File("ODRL21.rdf").toURI().toURL());
		 
		} catch (MalformedURLException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		} 		
	   Ontology odrl = (Ontology)Factory.createResource("gate.creole.ontology.impl.sesame.OWLIMOntology",fm);  
	   odrl.setName("ODRL");

	   
	   // add an Ontology Enhancement Transducer
		FeatureMap transducerParam = Factory.newFeatureMap();
		transducerParam.put("ontology",odrl);
		transducerParam.put("grammarURL", getClass().getResource("/ontologyEnhancement.jape"));
		ProcessingResource ontologyEnhancementTransducer = (ProcessingResource) Factory.createResource("gate.creole.Transducer", transducerParam);
		System.out.print("\t* Loading gate.creole.Transducer" + " ... ");		
	   ((SerialAnalyserController)termsConditions).add(ontologyEnhancementTransducer);
	   System.out.println("done");

	   
	   //add flexible Gazetteer	
	   System.out.print("\t* Loading gate.creole.gazetteer.FlexibleGazetteer" + " ... ");
	   ((SerialAnalyserController)termsConditions).add(createFlexibleGazetteer(rootFinder,odrl));	   
	   System.out.println("done");
	   
	   
	   
	   // add my JAPE rules
		transducerParam = Factory.newFeatureMap();
		transducerParam.put("grammarURL", getClass().getResource("/jape/main.jape"));
		ProcessingResource japeTransducer =(ProcessingResource) Factory.createResource("gate.creole.Transducer", transducerParam);
	   ((SerialAnalyserController)termsConditions).add(japeTransducer);
	   
      
  	  termsConditions.setName("terms and Conditions");
  	  return termsConditions;
  }	
	

}
