package edu.unibonn.i4matcher.helpers;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import virtuoso.jena.driver.VirtGraph;
import java.util.*;

/**
 * Created by phil on 21.09.16.
 */
public class TripleStoreWriter {

    public String write(Model m) {
        // connect to Virtuoso instance
        long ts = System.currentTimeMillis();
        String graph = String.valueOf(ts);
        VirtGraph vg = new VirtGraph( Config.SRV+ graph, Config.SRV + Config.SRVOPTS, Config.USER, Config.PASS);

        // convert triples
        Iterator<Statement> it = m.listStatements();
        while(it.hasNext()) {
            Statement s = it.next();
            vg.add(s.asTriple());
        }

        // close connection
        vg.close();
        System.out.println("Finished loading grapgh");
        return graph;

    }

    /**
     * Test with some dummy data.
     * @param args
     */
/*
    public static void main(String[] args) {
        Model m = ModelFactory.createDefaultModel();
        m.add(ResourceFactory.createResource("http://localhost"),
                ResourceFactory.createProperty("http://xmlns.com/foaf/0.1/name"),
                "localhost");

        TripleStoreWriter tsw = new TripleStoreWriter();
        tsw.write( m);


    }
*/
}