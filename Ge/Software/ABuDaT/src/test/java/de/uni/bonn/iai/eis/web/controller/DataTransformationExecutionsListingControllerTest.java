package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.ValidationResult;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.Execution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(DataTransformationExecutionsListingController.class)
public class DataTransformationExecutionsListingControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getExecutionsForTransformationId() throws Exception {
        ArrayList<Execution> executions = new ArrayList<>();
        executions.add(new Execution());

        given(databaseService.getDataTransformationExecutions("abc")).willReturn(executions);

        DataTransformation dataTransformation = new DataTransformation();
        dataTransformation.setName("name");
        dataTransformation.setDsdLabel("description");
        given(databaseService.getDataTransformation("abc")).willReturn(dataTransformation);

        mvc.perform(get("/transformation/data/executions").param("id", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("data_transformation_executions_listing"))
                .andExpect(model().attribute("executions", is(notNullValue())))
                .andExpect(model().attribute("executions", hasSize(1)));

        verify(databaseService).getDataTransformationExecutions("abc");
    }

    @Test
    public void deleteExecution() throws Exception {
        ArrayList<Execution> executions = new ArrayList<>();
        executions.add(new Execution());

        given(databaseService.getDataTransformationExecutions("abc")).willReturn(executions);

        DataTransformation dataTransformation = new DataTransformation();
        dataTransformation.setName("name");
        dataTransformation.setDsdLabel("description");
        given(databaseService.getDataTransformation("abc")).willReturn(dataTransformation);

        mvc.perform(get("/transformation/data/executions/delete").param("id", "abc").param("execution", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("data_transformation_executions_listing"))
                .andExpect(model().attribute("executions", is(notNullValue())))
                .andExpect(model().attribute("executions", hasSize(1)));

        verify(databaseService).deleteDataTransformationExecution("1");
        verify(databaseService).getDataTransformationExecutions("abc");
    }

    @Test
    public void openExecutionInLinkedpipes() throws Exception {
        String expectedRedirectUrl = "http://localhost:8080/#/pipelines/edit/canvas?pipeline=http://localhost:8080/resources/pipelines/abc&execution=1";

        mvc.perform(get("/transformation/data/executions/show").param("id", "abc").param("execution", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedRedirectUrl));
    }

    @Test
    public void download() throws Exception {
        String path = getClass().getClassLoader().getResource("test_execution_result").getPath();

        Execution execution = new Execution();
        execution.setOutputFilePath(path);

        given(databaseService.getDataTransformationExecution("1")).willReturn(execution);

        mvc.perform(get("/transformation/data/executions/result.ttl").param("uri", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle"))
                .andExpect(content().string("test file content"));

        verify(databaseService).getDataTransformationExecution("1");
    }

    @Test
    public void upload() throws Exception {
        String path = getClass().getClassLoader().getResource("test_execution_result").getPath();

        Execution execution = new Execution();
        execution.setOutputFilePath(path);

        given(databaseService.getDataTransformationExecution("1")).willReturn(execution);

        ArrayList<Execution> executions = new ArrayList<>();
        executions.add(new Execution());
        given(databaseService.getDataTransformationExecutions("abc")).willReturn(executions);

        DataTransformation dataTransformation = new DataTransformation();
        dataTransformation.setName("name");
        dataTransformation.setDsdLabel("description");
        given(databaseService.getDataTransformation("abc")).willReturn(dataTransformation);

        mvc.perform(get("/transformation/data/executions/upload").param("uri", "1").param("id", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("data_transformation_executions_listing"))
                .andExpect(model().attribute("executions", is(notNullValue())))
                .andExpect(model().attribute("executions", hasSize(1)));

        verify(tripleStoreService).upload(any());
        verify(databaseService).getDataTransformationExecution("1");
        verify(databaseService).getDataTransformationExecutions("abc");
    }

    @Test
    public void validateResult() throws Exception {

        DataTransformation transformation = new DataTransformation();
        transformation.setName("name");
        transformation.setDsdLabel("dsdlabel");
        given(databaseService.getDataTransformation(Matchers.any())).willReturn(transformation);

        Execution execution = new Execution();
        execution.setDateExecuted("2010-01-01");
        execution.setOutputFilePath(Files.createTempFile("abutat-test", "tmp").toString());
        given(databaseService.getDataTransformationExecution("1")).willReturn(execution);

        ValidationResult validationResult = new ValidationResult(true, "report");
        given(validatorService.validate(any(InputStream.class))).willReturn(validationResult);

        mvc.perform(get("/transformation/data/executions/validate").param("uri", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("validation_result"))
                .andExpect(model().attribute("validation", is(notNullValue())));

        verify(databaseService).getDataTransformationExecution("1");
        verify(databaseService).getDataTransformation(any());
        verify(validatorService).validate(any(InputStream.class));
    }

}