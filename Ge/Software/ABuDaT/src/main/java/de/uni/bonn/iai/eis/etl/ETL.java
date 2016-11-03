package de.uni.bonn.iai.eis.etl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface ETL {
    Pipeline createPipeline(String pipelineId) throws URISyntaxException;

    void updatePipeline(Pipeline pipeline) throws URISyntaxException;

    void deletePipeline(Pipeline pipeline) throws URISyntaxException;

    Pipeline readPipeline(String id) throws URISyntaxException, IOException;

    Map<String, String> readPipelines() throws URISyntaxException, IOException;

    Map<String, List<String>> getExecutionsByPipeline() throws URISyntaxException, IOException;

    String getExecution(String executionIRI) throws URISyntaxException;

    void deleteExecution(String executionURI) throws URISyntaxException;

    String executePipeline(Pipeline pipeline) throws URISyntaxException;
}
