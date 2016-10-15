package edu.unibonn.i4matcher.helpers;

/**
 * Created by phil on 31.08.16.
 */
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class XSDValidator {
    //create private property
    private Schema schema;

    public XSDValidator(String xsd) throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        System.out.println(xsd);

        FileInputStream fis = new FileInputStream(classLoader.getResource(xsd).getFile());
        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        this.schema = factory.newSchema(new StreamSource(fis));
        fis.close();
        System.out.println("Successfully read schema");
    }
    public boolean validateAgainstXSD(InputStream xml)
    {
        try
        {
            Validator validator = this.schema.newValidator();
            validator.validate(new StreamSource(xml));
            System.out.println("Successfully validated");
            if (xml != null) xml.close();
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
}
