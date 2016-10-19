/**
 * Created by phil on 04.09.16.
 */
package edu.unibonn.i4matcher.helpers;

import edu.unibonn.i4matcher.helpers.OPCUA.XML2OWLMapper;
import edu.unibonn.i4matcher.helpers.OPCUA.XSD2OWLMapper;
import net.sf.saxon.CollectionURIResolver;
import net.sf.saxon.Configuration;
import net.sf.saxon.FeatureKeys;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trace.XSLTTraceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class RDFTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RDFTransformer.class);

    static {
        // use Saxon as XSLT transformer
        System.setProperty("java.xml.transform.TransformerFactory",
                "net.sf.saxon.TransformerFactoryImpl");
       System.setProperty("javax.xml.transform.TransformerFactory",
                "net.sf.saxon.TransformerFactoryImpl");
        // and as XPath processor
        System.setProperty("javax.xml.xpath.XPathFactory",
                "net.sf.saxon.xpath.XPathFactoryImpl");
        System.setProperty("javax.xml.xpath.XPathFactory:"
                        + XPathConstants.DOM_OBJECT_MODEL,
                "net.sf.saxon.xpath.XPathFactoryImpl");

        TransformerFactory factory = TransformerFactory.newInstance();
        Configuration saxonConfiguration = new Configuration();
        saxonConfiguration
                .setCollectionURIResolver(new CollectionURIResolver() {
                    @Override
                    public SequenceIterator resolve(String href, String base,
                                                    net.sf.saxon.expr.XPathContext context)
                            throws net.sf.saxon.trans.XPathException {
                        return null;
                    }

                });
        factory.setAttribute(FeatureKeys.CONFIGURATION, saxonConfiguration);
    }
    private static boolean isEqual(InputStream i1, InputStream i2)
            throws IOException {

        ReadableByteChannel ch1 = Channels.newChannel(i1);
        ReadableByteChannel ch2 = Channels.newChannel(i2);

        ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
        ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);

        try {
            while (true) {

                int n1 = ch1.read(buf1);
                int n2 = ch2.read(buf2);

                if (n1 == -1 || n2 == -1) return n1 == n2;

                buf1.flip();
                buf2.flip();

                for (int i = 0; i < Math.min(n1, n2); i++)
                    if (buf1.get() != buf2.get())
                        return false;

                buf1.compact();
                buf2.compact();
            }

        } finally {
            if (i1 != null) i1.close();
            if (i2 != null) i2.close();
        }
    }


    public byte[] transform(InputStream aml, String schema) {

        try {

            TransformerFactory tf = TransformerFactory.newInstance();
            XSLTTraceListener traceListener = new XSLTTraceListener();
            traceListener.setOutputDestination(new PrintStream("/dev/null"));
            tf.setAttribute(FeatureKeys.TRACE_LISTENER, traceListener);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ClassLoader classLoader = getClass().getClassLoader();

            if ("opcua".equals(schema)){

                XSD2OWLMapper mapping = new XSD2OWLMapper(classLoader.getResourceAsStream("opcua.xsd"));
                mapping.setObjectPropPrefix("");
                mapping.setDataTypePropPrefix("has");
                mapping.convertXSD2OWL();

                // This part converts XML instance to RDF data model.
                XML2OWLMapper generator = new XML2OWLMapper(aml, mapping);
                generator.convertXML2OWL();
                generator.writeModel(baos,"ttl");

            }
            else if ("automationML".equals(schema )){
                InputStream xsl = classLoader.getResource(schema + "..turtle.xsl").openStream();
                Transformer transformer = tf.newTransformer(new StreamSource(xsl));
                LOGGER.info("Works");
                StreamSource xmlSource = new StreamSource(aml);
                transformer.transform(xmlSource, new StreamResult(baos));
                xsl.close();
            }

            byte[] formattedOutput = baos.toByteArray();
            baos.close();
            if (aml != null) {
                aml.close();
            }
            return formattedOutput;

        } catch (FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
