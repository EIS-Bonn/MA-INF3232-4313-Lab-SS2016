package edu.unibonn.i4matcher;

//import com.hp.hpl.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import edu.unibonn.i4matcher.model.FileMeta;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Alina on 9/7/2016.
 */
public class Matcher {
    private String matchLevel;
    public Matcher(String value){
        this.matchLevel = value;
    }

    public Model match2Files (LinkedList<FileMeta> files) throws IOException {

        //generate 2 ArrayLists of statements
        ArrayList<Statement> fileMap1 = getStatements(files.get(0).getTtl());
        ArrayList<Statement> fileMap2 = getStatements(files.get(1).getTtl());

        //create empty model for matching results
        Model model = null;
        model = ModelFactory.createDefaultModel();

        //create integration file
        //String matchFileName = "match.ttl";
        //FileWriter matchFile = new FileWriter(matchFileName);

        //matching process (strict, soft, non-strict)

        for(Statement statement1 : fileMap1){
            for(Statement statement2: fileMap2){
                if("strict".equals(matchLevel) && statement1.equals(statement2) ){
                            model.add(statement1);
                }
                if("soft".equals(matchLevel) && (statement1.getSubject().equals(statement2.getSubject()))
                                && (statement1.getPredicate().equals(statement2.getPredicate())) ){

                            model.add(statement1.getSubject(), statement1.getPredicate(), statement1.getObject());
                            model.add(statement1.getSubject(), statement2.getPredicate(), statement2.getObject());
                }
                if("nonstrict".equals(matchLevel) && statement1.getSubject().equals(statement2.getSubject())){

                            model.add(statement1.getSubject(), statement1.getPredicate(), statement1.getObject());
                            model.add(statement1.getSubject(), statement2.getPredicate(), statement2.getObject());
                }
            }
        }

        //model.write(matchFile, "TTL") ;
        return model;
    }

    private ArrayList<Statement> getStatements (byte[] rdfFile){

        try(InputStream inputStream = new ByteArrayInputStream(rdfFile)){

            //create empty model
            Model model = null;
            model = ModelFactory.createDefaultModel();

            // parses in turtle format
            model.read(new InputStreamReader(inputStream), null, "TURTLE");

            //generate ArrayList of statements
            StmtIterator iterator = model.listStatements();
            ArrayList<Statement> listStatement = new ArrayList<Statement>();

            while (iterator.hasNext()) {
                Statement stmt = iterator.nextStatement();
                listStatement.add(stmt);
            }
            return listStatement;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    //

}
