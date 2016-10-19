package edu.unibonn.i4matcher;

//import com.hp.hpl.jena.query.*;
import edu.unibonn.i4matcher.helpers.Config;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;

import org.apache.jena.query.*;
import org.apache.jena.query.ResultSet;
import virtuoso.jena.driver.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Alina on 10/3/2016.
 */
public class SparqlQuery {
    /**
     * Executes a SPARQL query against a virtuoso url and prints results.
     */
    public SparqlQuery(){
    }
    public String getResult(String query) {
        String json ="";

        VirtGraph set = new VirtGraph (Config.SRV+Config.SRVOPTS, Config.USER, Config.PASS);
        Query sparql = QueryFactory.create(query);
        VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (String.valueOf(sparql), set);
        ResultSet results = vqe.execSelect();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            ResultSetFormatter.outputAsJSON(outputStream, results);
            json = new String(outputStream.toByteArray());
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;

    }

/*
    public static void main(String[] args) throws UnsupportedEncodingException {
//        String url = "jdbc:virtuoso://192.168.0.105:1111";
        //String qry = "SELECT * WHERE { GRAPH ?graph { ?s ?p ?o } } limit 100";
        String qry = "SELECT+%2A+WHERE+%7B+GRAPH+%3Fgraph+%7B+%3Fs+%3Fp+%3Fo+%7D+%7D+limit+100";
        String param = URLDecoder.decode(qry, "UTF-8");
        SparqlQuery sp = new SparqlQuery();
        String a = sp.getResult(param);

    }
*/

}
