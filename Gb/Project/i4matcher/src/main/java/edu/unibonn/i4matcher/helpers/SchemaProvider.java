package edu.unibonn.i4matcher.helpers;

import org.jdom.Namespace;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.*;
import javax.crypto.ExemptionMechanismException;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by phil on 15.10.16.
 */
public class SchemaProvider {
    private String importPath;
    private String exportPath;
    public SchemaProvider(String schema, String path){
        this.importPath= "src/main/resources/output/turtle.xsl";//"file://"+path + "output/turtle.xsl";
        switch (schema) {

            case "automationML":
                this.exportPath = "src/main/resources/extract/aml.xsl";//"file://"+path+"extract/aml.xsl";
                break;
            case "opcua":
                this.exportPath = path+"extract/opcua.xsl";
                break;
        }
    }
    public org.w3c.dom.Document getDoc(){
        org.w3c.dom.Document document = null;
        try {
            // root elements
            Document doc = new Document();
            Namespace xls = Namespace.getNamespace("xsl", "http://www.w3.org/1999/XSL/Transform");
            //Element rootElement = doc.createElementNS("http://www.w3.org/1999/XSL/Transform", "stylesheet");
            Element root = new Element("stylesheet", xls);
            root.setAttribute("version","2.0");
            Element imp = new Element("import", xls);
            imp.setAttribute("href",importPath);
            Element exp = new Element("include", xls);
            exp.setAttribute("href",exportPath);
            root.addContent(imp);
            root.addContent(exp);
            doc.addContent(root);

            XMLOutputter xml = new XMLOutputter();
            xml.setFormat(Format.getPrettyFormat());
            System.out.println(xml.outputString(doc));

            DOMOutputter domOutputer = new DOMOutputter();
            document = domOutputer.output(doc);
        } catch (Exception pce) {
            pce.printStackTrace();
        }
        return document;
    }
    /*
    public static void main(String argv[]) {
        SchemaProvider schemaProvider = new SchemaProvider("automationML","/home/phil/workspace/Integration-I4.0/Project/i4matcher/target/classes/");
        System.out.print(schemaProvider.getDoc());
        //String importPath= "output/turtle.xsl";
        //String exportPath= "extract/aml.xsl";


       //     <xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
         //       version="2.0">
           //     <xsl:import href="src/main/resources/output/turtle.xsl"/>
             //   <xsl:include href="src/main/resources/extract/aml.xsl"/>
            //</xsl:stylesheet>



    }*/
}
