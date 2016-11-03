package de.uni.bonn.iai.eis.web;

import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesETL;
import de.uni.bonn.iai.eis.web.model.AbstractTransformation;
import de.uni.bonn.iai.eis.web.model.Execution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EtlServiceTest {

    @Mock
    private ABuDaTConfiguration aBuDaTConfiguration;

    @Mock
    private LinkedPipesETL linkedPipesETL;

    @Before
    public void setupMocks() throws IOException {
        when(aBuDaTConfiguration.getOutputDir()).thenReturn(Files.createTempDirectory("temp-").toString());
        when(aBuDaTConfiguration.getComponentBaseUrl()).thenReturn("http://example.com/component/");
        when(aBuDaTConfiguration.getPipelineBaseUrl()).thenReturn("http://example.com/pipeline/");

        when(linkedPipesETL.doesPipelineExist(any())).thenReturn(false);
        when(linkedPipesETL.executePipeline(any())).thenReturn("http//example.com/execution/1");

        when(linkedPipesETL.getExecutionStatus(any())).thenReturn("status");
    }

    class Transformation extends AbstractTransformation {
        @Override
        public String getId() {
            return "12345";
        }

        @Override
        public Pipeline createPipeline(String outputFileName, String outputFilePath, ABuDaTConfiguration aBuDaTConfiguration) {
            return new Pipeline();
        }
    }

    @Test
    public void testExecuteTransformation() throws Exception {
        EtlService etlService = new EtlService(aBuDaTConfiguration, linkedPipesETL);

        assertTrue(new File(aBuDaTConfiguration.getOutputDir()).isDirectory());

        Execution execution = etlService.executeTransformation(new Transformation());

        assertEquals("12345", execution.getPipelineId());
        assertEquals("http//example.com/execution/1", execution.getExecutionUri());
        assertEquals("unknown", execution.getExecutionStatus());
        assertTrue(execution.getOutputFilePath().startsWith(aBuDaTConfiguration.getOutputDir()));

        verify(linkedPipesETL).doesPipelineExist(any());
        verify(linkedPipesETL).createPipeline(any());
        verify(linkedPipesETL).updatePipeline(any());
        verify(linkedPipesETL).readPipeline(any());
        verify(linkedPipesETL).executePipeline(any());
    }

    @Test
    public void testGetExecutionStatus() {
        EtlService etlService = new EtlService(aBuDaTConfiguration, linkedPipesETL);

        String uri = "http://example.com";
        String executionStatus = etlService.getExecutionStatus(uri);

        assertEquals("status", executionStatus);
    }
}