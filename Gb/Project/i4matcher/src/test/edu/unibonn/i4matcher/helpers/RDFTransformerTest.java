package edu.unibonn.i4matcher.helpers;

import edu.unibonn.i4matcher.helpers.OPCUA.XSD2OWLTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by Alina on 10/15/2016.
 */
public class RDFTransformerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RDFTransformerTest.class);

    @Test
    public void transform() {

        InputStream aml1 = null;

        try {
            aml1 = new FileInputStream("src/test/resources/2.aml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.warn(String.valueOf(e));
        }

        RDFTransformer RDFTransformer = new RDFTransformer();
        byte[] out = RDFTransformer.transform(aml1, "automationML");
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream("src/test/resources/output/aml-test.ttl");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.warn(String.valueOf(e));
        }

        try {
            fos.write(out);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn(String.valueOf(e));
        }

        LOGGER.info("Finish");
    }

}