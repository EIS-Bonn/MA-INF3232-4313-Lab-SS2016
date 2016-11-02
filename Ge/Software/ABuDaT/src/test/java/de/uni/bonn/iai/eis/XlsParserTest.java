package de.uni.bonn.iai.eis;

import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class XlsParserTest {
    @Test
    public void parse() throws Exception {

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("Seznam-prijemcu-(List-of-Beneficiaries)-12_2014.xlsx")) {

            XlsParser xlsParser = new XlsParser();
            List<String> parse = xlsParser.parse(resourceAsStream, 4);

        }
    }

    @Test
    public void parseSheet() throws Exception {

    }

}