package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import de.uni.bonn.iai.eis.web.model.Execution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CodelistTransformationExecutionsListingController.class)
public class CodelistTransformationExecutionsListingControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void getExecutionsForTransformationId() throws Exception {
        ArrayList<Execution> executions = new ArrayList<>();
        executions.add(new Execution());

        given(databaseService.getCodelistTransformationExecutions("abc")).willReturn(executions);

        CodelistTransformation codelistTransformation = new CodelistTransformation();
        codelistTransformation.setName("name");
        codelistTransformation.setDescription("description");
        given(databaseService.getCodelistTransformation("abc")).willReturn(codelistTransformation);

        mvc.perform(get("/transformation/codelist/executions").param("id", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("codelist_transformation_executions_listing"))
                .andExpect(model().attribute("executions", is(notNullValue())))
                .andExpect(model().attribute("executions", hasSize(1)));

        verify(databaseService).getCodelistTransformationExecutions("abc");
    }

    @Test
    public void deleteExecution() throws Exception {
        ArrayList<Execution> executions = new ArrayList<>();
        executions.add(new Execution());

        given(databaseService.getCodelistTransformationExecutions("abc")).willReturn(executions);

        CodelistTransformation codelistTransformation = new CodelistTransformation();
        codelistTransformation.setName("name");
        codelistTransformation.setDescription("description");
        given(databaseService.getCodelistTransformation("abc")).willReturn(codelistTransformation);

        mvc.perform(get("/transformation/codelist/executions/delete").param("id", "abc").param("execution", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("codelist_transformation_executions_listing"))
                .andExpect(model().attribute("executions", is(notNullValue())))
                .andExpect(model().attribute("executions", hasSize(1)));

        verify(databaseService).deleteCodelistTransformationExecution("1");
        verify(databaseService).getCodelistTransformationExecutions("abc");
    }

    @Test
    public void openExecutionInLinkedpipes() throws Exception {
        String expectedRedirectUrl = "http://localhost:8080/#/pipelines/edit/canvas?pipeline=http://localhost:8080/resources/pipelines/abc&execution=1";

        mvc.perform(get("/transformation/codelist/executions/show").param("id", "abc").param("execution", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(expectedRedirectUrl));
    }

    @Test
    public void download() throws Exception {
        String path = getClass().getClassLoader().getResource("test_execution_result").getPath();

        Execution execution = new Execution();
        execution.setOutputFilePath(path);

        given(databaseService.getCodelistTransformationExecution("1")).willReturn(execution);

        mvc.perform(get("/transformation/codelist/executions/result.ttl").param("uri", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle"))
                .andExpect(content().string("test file content"));

        verify(databaseService).getCodelistTransformationExecution("1");
    }

    @Test
    public void upload() throws Exception {
        String path = getClass().getClassLoader().getResource("test_execution_result").getPath();

        Execution execution = new Execution();
        execution.setOutputFilePath(path);

        given(databaseService.getCodelistTransformationExecution("1")).willReturn(execution);

        ArrayList<Execution> executions = new ArrayList<>();
        executions.add(new Execution());
        given(databaseService.getCodelistTransformationExecutions("abc")).willReturn(executions);

        CodelistTransformation codelistTransformation = new CodelistTransformation();
        codelistTransformation.setName("name");
        codelistTransformation.setDescription("description");
        given(databaseService.getCodelistTransformation("abc")).willReturn(codelistTransformation);

        mvc.perform(get("/transformation/codelist/executions/upload").param("uri", "1").param("id", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("codelist_transformation_executions_listing"))
                .andExpect(model().attribute("executions", is(notNullValue())))
                .andExpect(model().attribute("executions", hasSize(1)));

        verify(tripleStoreService).upload(any());
        verify(databaseService).getCodelistTransformationExecution("1");
        verify(databaseService).getCodelistTransformationExecutions("abc");
    }
}