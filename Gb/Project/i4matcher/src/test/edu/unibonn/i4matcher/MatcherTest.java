package edu.unibonn.i4matcher;

import edu.unibonn.i4matcher.helpers.RDFTransformer;
import edu.unibonn.i4matcher.model.FileMeta;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by Alina on 10/15/2016.
 */
public class MatcherTest {
    @Test
    public void match2Files() throws Exception {

        String matchLevel = "strict";
        Matcher matcher = new Matcher(matchLevel);
        RDFTransformer rdfTransformer = new RDFTransformer();
        LinkedList<FileInputStream> files = new LinkedList<>();
        files.add(new FileInputStream("src/test/resources/1.aml")); //Topology - Copy
        files.add(new FileInputStream("src/test/resources/2.aml")); //Topology

        byte[] rdfFile1 = rdfTransformer.transform(files.get(0), "automationML");//opcua
        byte[] rdfFile2 = rdfTransformer.transform(files.get(1), "automationML");//opcua


        ArrayList<Statement> fileMap1 = matcher.getStatements(rdfFile1);
        ArrayList<Statement> fileMap2 = matcher.getStatements(rdfFile2);

        //create empty model for matching results
        Model model = null;
        model = ModelFactory.createDefaultModel();

        //create integration file
        String matchFileName = "src/test/resources/output/match.ttl";
        FileWriter matchFile = new FileWriter(matchFileName);

        //matching process (strict, soft, non-strict)

        for (Statement statement1 : fileMap1) {
            for (Statement statement2 : fileMap2) {
                if ("strict".equals(matchLevel) && statement1.equals(statement2)) {
                    model.add(statement1);
                }
                if ("soft".equals(matchLevel) && (statement1.getSubject().equals(statement2.getSubject()))
                        && (statement1.getPredicate().equals(statement2.getPredicate()))) {

                    model.add(statement1.getSubject(), statement1.getPredicate(), statement1.getObject());
                    model.add(statement1.getSubject(), statement2.getPredicate(), statement2.getObject());
                }
                if ("nonstrict".equals(matchLevel) && statement1.getSubject().equals(statement2.getSubject())) {

                    model.add(statement1.getSubject(), statement1.getPredicate(), statement1.getObject());
                    model.add(statement1.getSubject(), statement2.getPredicate(), statement2.getObject());
                }
            }
        }

        model.write(matchFile, "TTL");
    }

}