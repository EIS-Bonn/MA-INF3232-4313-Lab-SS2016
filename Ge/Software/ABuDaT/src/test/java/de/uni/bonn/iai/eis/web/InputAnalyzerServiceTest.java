package de.uni.bonn.iai.eis.web;

import org.junit.Test;

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class InputAnalyzerServiceTest {

    @Test
    public void analyzeNonExistingUrlContent() {
        InputAnalyzerService inputAnalyzerService = new InputAnalyzerService();
        Input input = inputAnalyzerService.analyzeInput("foo", "UTF-8", false);

        assertEquals("foo", input.getUrl());
        assertTrue(input.getColumns().isEmpty());
        assertEquals(",", input.getDelimeter());
    }

    @Test
    public void analyzeCsvInput() throws Exception {
        URL testResource = getClass().getClassLoader().getResource("analyzeCsvInput.csv");

        InputAnalyzerService inputAnalyzerService = new InputAnalyzerService();
        assert testResource != null;
        String urlString = testResource.toString();
        Input input = inputAnalyzerService.analyzeInput(urlString, "UTF-8", false);

        assertEquals(urlString, input.getUrl());
        LinkedList<String> columns = input.getColumns();

        assertNotNull(columns);
        assertEquals(7, columns.size());
        assertEquals(",", input.getDelimeter());
    }

}