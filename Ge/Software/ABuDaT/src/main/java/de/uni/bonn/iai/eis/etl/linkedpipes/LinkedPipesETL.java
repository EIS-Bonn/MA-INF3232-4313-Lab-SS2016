package de.uni.bonn.iai.eis.etl.linkedpipes;

import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.etl.ETL;
import de.uni.bonn.iai.eis.etl.Pipeline;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openrdf.model.*;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.util.Models;
import org.openrdf.model.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


/**
 * Class acting as a proxy to a linkedpipes etl instance.
 * <p>
 * It is responsible for actions such as creating a pipeline,
 * modifiying a pipeline (update, delete), executing a pipeline and
 * getting executions for a pipeline.
 */
@org.springframework.stereotype.Component
public class LinkedPipesETL implements ETL {

    private static final Logger log = LoggerFactory.getLogger(LinkedPipesETL.class);
    private final RestTemplate restTemplate = new RestTemplate();

    private ABuDaTConfiguration aBuDaTConfiguration;

    @Autowired
    public LinkedPipesETL(ABuDaTConfiguration aBuDaTConfiguration) {
        this.aBuDaTConfiguration = aBuDaTConfiguration;
    }

    /**
     * Creates a new empty pipeline with a given id.
     *
     * @param pipelineId the id
     * @return the generated pipeline
     */
    @Override
    public Pipeline createPipeline(String pipelineId) {
        URI pipelineURI = tryCreateURI(getPipelineURI(pipelineId));
        return createPipelineWithGivenURI(pipelineURI);
    }

    /**
     * Creates a new empty pipeline with a given uri.
     *
     * @param uri the uri of the pipeline
     * @return the generated pipeline
     */
    private Pipeline createPipelineWithGivenURI(URI uri) {
        ResponseEntity<EmptyPipelineResponseEntity> response = restTemplate.postForEntity(uri, null, EmptyPipelineResponseEntity.class);
        log.info("Create Pipeline finished with status: " + response.getStatusCode());

        EmptyPipelineResponseEntity emptyPipeline = response.getBody();
        Pipeline pipeline = new Pipeline();
        pipeline.setId(emptyPipeline.getId());
        pipeline.setUri(emptyPipeline.getUri());

        return pipeline;
    }

    /**
     * Updates a given pipeline in linkedpipes etl.
     *
     * @param pipeline the pipeline to update
     */
    @Override
    public void updatePipeline(Pipeline pipeline) {
        Model model = LinkedPipesRdfHelper.modelFromPipeline(pipeline);
        String body = LinkedPipesRdfHelper.modelToString(model);

        URI uri = tryCreateURI(getPipelineURI(pipeline.getId()));

        ResponseEntity<String> response = restTemplate.exchange(RequestEntity.put(uri).body(body), String.class);
        log.info("Read Pipeline finished with status: " + response.getStatusCode());
    }

    /**
     * Deletes a pipeline in linkedpipes etl.
     *
     * @param pipeline the pipeline to delete
     */
    @Override
    public void deletePipeline(Pipeline pipeline) {
        URI pipelineURI = tryCreateURI(getPipelineURI(pipeline.getId()));

        ResponseEntity<Void> response = restTemplate.exchange(RequestEntity.delete(pipelineURI).build(), Void.class);
        log.info("Delete Pipeline finished with status: " + response.getStatusCode());
    }

    /**
     * Reads a pipeline from linkedpipse etl by its id.
     *
     * @param id the id of the pipeline
     * @return the read pipeline
     */
    public Pipeline readPipeline(String id) {
        String pipelineJSON = readPipelineJSON(id);

        return LinkedPipesRdfHelper.parsePipeline(pipelineJSON);
    }

    /**
     * Reads all pipeline from linkedpipes etl.
     *
     * @return a map of pipelines with keys: pipeline ids and values: pipeline uris
     */
    @Override
    public Map<String, String> readPipelines() {
        URI pipelineBaseURI = tryCreateURI(aBuDaTConfiguration.getPipelineBaseUrl());

        ResponseEntity<String> response = restTemplate.exchange(RequestEntity.get(pipelineBaseURI).build(), String.class);
        log.info("Read pipelines finished with status: " + response.getStatusCode());

        Model model = LinkedPipesRdfHelper.modelFromJsonLd(response.getBody());

        Map<String, String> pipelines = new HashMap<>();

        for (Resource context : model.contexts()) {
            Optional<String> uri = Models.objectString(
                    filterModuleAndAssertSingleResult(model, null, LinkedPipesOntology.PIPELINE_TYPE_IN_READ_PIPELINES_RESPONSE, null, context));

            Optional<String> id = Models.objectString(
                    filterModuleAndAssertSingleResult(model, null, LinkedPipesOntology.LINKEDPIPES_ID, null, context));

            Assert.isTrue(id.isPresent());
            Assert.isTrue(uri.isPresent());

            if (id.isPresent() && uri.isPresent()) {
                pipelines.put(id.get(), uri.get());
            }
        }

        return pipelines;
    }

    /**
     * Gets all executions for all pipelines from linkedpipes etl.
     *
     * @return a map of executions by id with keys: pipeline uris and values: list of execution uris.
     */
    @Override
    public Map<String, List<String>> getExecutionsByPipeline() {
        String body = readExecutions();

        Model model = LinkedPipesRdfHelper.modelFromJsonLd(body);

        Map<String, List<String>> result = new HashMap<>();

        Model executions = model.filter(null, RDF.TYPE, LinkedPipesOntology.ETL_EXECTUION_TYPE);
        for (Statement executionStatement : executions) {
            Resource executionURI = executionStatement.getSubject();
            Model m1 = model.filter(executionURI, LinkedPipesOntology.PIPELINE_TYPE_IN_EXECUTE_PIPELINES_RESPONSE, null);
            Assert.isTrue(m1.size() == 1);
            String pipelineURI = Models.objectString(m1).get();

            result.putIfAbsent(pipelineURI, new ArrayList<>());
            result.get(pipelineURI).add(executionURI.stringValue());
        }

        return result;
    }

    /**
     * Read all executions from linkedpipes etl.
     *
     * @return the executions in json ld format.
     */
    private String readExecutions() {
        URI uri = tryCreateURI(aBuDaTConfiguration.getExecutionsBaseUrl());

        ResponseEntity<String> response = restTemplate.exchange(RequestEntity.get(uri).build(), String.class);
        log.info("Read executions finished with status: " + response.getStatusCode());

        return response.getBody();
    }

    private Model filterModuleAndAssertSingleResult(Model model, Resource subbject, IRI predicate, Value object, Resource context) {
        Model filter = model.filter(subbject, predicate, object, context);
        Assert.isTrue(filter.size() == 1);
        return filter;
    }

    private String readPipelineJSON(String pipelineID) {
        ResponseEntity<String> response = readPipelineResponse(pipelineID);
        return response.getBody();
    }


    //FIXME use errorhandler to handle real errors, atm it's only used to check if pipeline is present
    private class ErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode() != HttpStatus.OK;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            //noop
        }
    }

    private ResponseEntity<String> readPipelineResponse(String pipelineID) {
        URI pipelineURI = tryCreateURI(getPipelineURI(pipelineID));

        restTemplate.setErrorHandler(new ErrorHandler());

        ResponseEntity<String> response = restTemplate.getForEntity(pipelineURI, String.class);
        log.info("Read pipeline finished with status: " + response.getStatusCode());

        return response;
    }

    @Override
    public String getExecution(String executionURI) {
        URI uri = tryCreateURI(executionURI);

        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        log.info("Get execution finished with status: " + response.getStatusCode());

        return response.getBody();
    }

    /**
     * Delete the execution with the given id.
     *
     * @param executionURI the id of the execution to delete
     */
    @Override
    public void deleteExecution(String executionURI) {
        URI uri = tryCreateURI(executionURI);

        ResponseEntity<Void> response = restTemplate.exchange(RequestEntity.delete(uri).build(), Void.class);
        log.info("Delete execution finished with status: " + response.getStatusCode());
    }

    /**
     * Create the pipeline URI for a pipeline ID.
     *
     * @param pipelineId the pipelineId of the pipeline
     * @return The pipeline URI.
     */
    private String getPipelineURI(String pipelineId) {
        return aBuDaTConfiguration.getPipelineBaseUrl() + "/" + pipelineId;
    }

    /**
     * Execute a Pipeline in LinkedPipes ETL.
     *
     * @param pipeline the pipeline to executeTransformation
     * @return The  LinkedPipes ETL URI of the execution
     */
    @Override
    public String executePipeline(Pipeline pipeline) {
        HttpEntity<MultiValueMap<String, Object>> request = createExecutionRequest(pipeline);

        URI uri = tryCreateURI(aBuDaTConfiguration.getExecutionsBaseUrl());
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);

        HttpStatus statusCode = response.getStatusCode();

        String executionIRI = null;
        String msg = "Execute pipeline finished with status " + statusCode;

        if (statusCode.is2xxSuccessful()) {
            log.info(msg);
            String body = response.getBody();
            try {
                executionIRI = (String) ((JSONObject) new JSONParser().parse(body)).get("iri");
            } catch (ParseException e) {
                log.error("executePipeline: Error parsing result json");
            }
        } else {
            log.error(msg);
        }

        return executionIRI;
    }

    /**
     * Create an execution request for a pipeline.
     *
     * @param pipeline The pipeline to create an execution request for.
     */
    private HttpEntity<MultiValueMap<String, Object>> createExecutionRequest(Pipeline pipeline) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        Model model = LinkedPipesRdfHelper.modelFromPipeline(pipeline);
        String pipelineJson = LinkedPipesRdfHelper.modelToString(model);
        parts.add("pipeline", pipelineJson);

        return new HttpEntity<>(parts, headers);
    }

    public String getExecutionStatus(String executionIRI) {
        String execution = getExecution(executionIRI);

        Model model = LinkedPipesRdfHelper.modelFromJsonLd(execution);

        Model modelFilteredForExecution = model.filter(
                SimpleValueFactory.getInstance().createIRI(executionIRI),
                LinkedPipesOntology.ETL_EXECUTION_STATUS,
                null
        );

        return Models.objectString(modelFilteredForExecution).orElse(null);
    }

    private URI tryCreateURI(String executionURI) {
        URI uri;
        try {
            uri = new URI(executionURI);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unable to create URI", e);
        }
        return uri;
    }

    public boolean doesPipelineExist(String pipelineId) {
        ResponseEntity<String> response = readPipelineResponse(pipelineId);

        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        }
        //TODO if pipeline does not exist, it retuns 500 and json:
        //
//
//        {
//            "exception": {
//            "errorMessage": ""
//            "systemMessage": ""
//            "userMessage": "Pipeline "foo" does not exists."
//            "errorCode": "ERROR"
//        }-
//        }
//        else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
//
//        }

        return false;
    }
}