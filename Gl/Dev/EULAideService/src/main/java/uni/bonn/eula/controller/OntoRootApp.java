package uni.bonn.eula.controller;

import java.io.File;
import java.net.MalformedURLException;

import gate.CorpusController;
import gate.Factory;
import gate.Gate;
import gate.creole.ANNIEConstants;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

public class OntoRootApp {

	private CorpusController rootfinder = null;
	private static OntoRootApp instance = null;
	
		
	
    public  CorpusController createResources () throws GateException {
    	  

    	  try {
    	  
    	   //need Tools plugin for the Morphological Analyser 
    	   Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tools").toURL());
    	  
    	   //Load ANNIE for the Tokeniser and POS Tagger
    	   Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), ANNIEConstants.PLUGIN_DIR).toURL());
    	   
    	  
    	   
    	  } catch (GateException e) {
    	   // TODO Auto-generated catch block
    	   e.printStackTrace();
    	  } catch (MalformedURLException e) {
    	   // TODO Auto-generated catch block
    	   e.printStackTrace(); 
    	  } 
    	  
    	  String[] processingResources = {   
    	        "gate.creole.tokeniser.DefaultTokeniser",
    	        "gate.creole.POSTagger",
    	        "gate.creole.morph.Morph"};
    	  
    	  rootfinder = (CorpusController)Factory.createResource("gate.creole.SerialAnalyserController");
    	   
    	  for(int pr = 0; pr < processingResources.length; pr++) {
    	         System.out.print("\t* Loading " + processingResources[pr] + " ... ");
    	         ((SerialAnalyserController)rootfinder).add((gate.LanguageAnalyser)Factory.createResource(processingResources[pr]));
    	         System.out.println("done");
    	       }
    	  rootfinder.setName("Root finder");
    	  return rootfinder;
    }	

}
