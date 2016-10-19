package edu.unibonn.i4matcher.helpers.OPCUA;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class URLResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
        URL url = new URL(systemId);
        InputSource inputSource = new InputSource(url.openStream());
        inputSource.setSystemId(url.toExternalForm());
        return inputSource;
    }

}
