
package uni.bonn.eula;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import uni.bonn.eula.controller.OntologyBasedAnnotController;
import uni.bonn.eula.lib.CategoryEnum;
import uni.bonn.eula.lib.GateResources;
import uni.bonn.eula.lib.StanfordResources;
import gate.*;
import gate.creole.ANNIEConstants;
import gate.creole.SerialAnalyserController;
import gate.creole.gazetteer.Gazetteer;
import gate.creole.ontology.*;
import gate.util.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Gri	zzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8081/";
	
	
	protected static void initialize() throws GateException{
		Gate.setGateHome(new File("C:/Program Files/GATE_Developer_8.1"));
		File gateHome = Gate.getGateHome();
		File pluginsHome = new File(gateHome,"plugins");
		Gate.setPluginsHome(pluginsHome);
		Gate.init();
		
	}
/*
	public static OntoRootApp getInstance() throws GateException{
		if(instance == null){
			instance = new OntoRootApp();
		}

		return instance;
	}
*/		
	
	
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in uni.bonn.eula package
        final ResourceConfig rc = new ResourceConfig().packages("uni.bonn.eula");
        rc.register(MultiPartFeature.class);
        
        HttpServer webServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI),rc);
        
        

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return webServer;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     * @throws GateException 
     */
    public static void main(String[] args) throws IOException, GateException , Exception{
    	
    	final HttpServer server = startServer();
        GateResources gr = GateResources.getInstance();
		gr.initializePipeLines();

        System.out.println("Server started: http://localhost:8081/ ");
        //initialize();
        //StanfordResources sr = StanfordResources.getInstance();
        System.out.println("Hit enter to stop it...");
        System.in.read();
        server.stop();
    }
}

