package edu.unibonn.i4matcher.helpers.OPCUA;

import com.sun.xml.xsom.parser.AnnotationParser;
import com.sun.xml.xsom.parser.AnnotationParserFactory;

public class AnnotationFactory implements AnnotationParserFactory {

    /* (non-Javadoc)
     * @see com.sun.xml.xsom.parser.AnnotationParserFactory#create()
     */
    @Override
    public AnnotationParser create() {
        return new XsdAnnotationParser();
    }

}