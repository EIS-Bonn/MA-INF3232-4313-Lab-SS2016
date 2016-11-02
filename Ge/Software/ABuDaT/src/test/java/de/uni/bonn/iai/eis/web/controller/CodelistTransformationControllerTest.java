package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CodelistTransformationController.class)
public class CodelistTransformationControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testAddCode() throws Exception {
        mvc.perform(post("/transformation/codelist").param("addCode", ""))
                .andExpect(view().name("codelist_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("codelistTransformation", is(notNullValue())))
                .andExpect(model().attribute("codelistTransformation", hasProperty("slices", hasSize(1))));
    }

    @Test
    public void testSaveCodelist() throws Exception {
        given(databaseService.getCodelistTransformation(any())).willReturn(new CodelistTransformation());

        mvc.perform(post("/transformation/codelist").param("save", ""))
                .andExpect(view().name("codelist_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("codelistTransformation", is(notNullValue())));

        verify(databaseService).saveCodelistTransformation(any());
    }

    @Test
    public void getTransformation() throws Exception {
        given(databaseService.getCodelistTransformation(any())).willReturn(new CodelistTransformation());

        mvc.perform(get("/transformation/codelist").param("id", "1"))
                .andExpect(view().name("codelist_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("codelistTransformation", is(notNullValue())));
    }

    @Test
    public void copyTransformation() throws Exception {
        given(databaseService.getCodelistTransformation(any())).willReturn(new CodelistTransformation());

        mvc.perform(post("/transformation/codelist").param("copy", ""))
                .andExpect(view().name("codelist_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("codelistTransformation", is(notNullValue())));

        verify(databaseService).saveCodelistTransformation(any());
    }

    @Test
    public void executeTransformation() throws Exception {
        given(databaseService.getCodelistTransformation(any())).willReturn(new CodelistTransformation());

        mvc.perform(post("/transformation/codelist").param("execute", "")
                .param("uri", "http://example.com/foobar")
                .param("charset", "UTF-8"))
                .andExpect(view().name("codelist_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("codelistTransformation", is(notNullValue())));

        verify(databaseService).saveCodelistTransformationExecution(any());
        verify(etlService).executeTransformation(any(CodelistTransformation.class));
        verify(databaseService).getCodelistTransformation(any());
    }

    @Test
    public void executeTransformationById() throws Exception {
        given(databaseService.getCodelistTransformation(any())).willReturn(new CodelistTransformation());

        mvc.perform(get("/transformation/codelist/execute").param("id", "1"))
                .andExpect(view().name("codelist_transformation_listing"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("codelistTransformations", is(notNullValue())));

        verify(databaseService).getCodelistTransformation("1");
        verify(etlService).executeTransformation(any(CodelistTransformation.class));
        verify(databaseService).saveCodelistTransformationExecution(any());
        verify(databaseService).getCodelistTransformations();
    }

    @Test
    public void getTransformations() throws Exception {
        ArrayList<CodelistTransformation> codelistTransformations = new ArrayList<>();
        codelistTransformations.add(new CodelistTransformation());
        codelistTransformations.add(new CodelistTransformation());

        given(databaseService.getCodelistTransformations()).willReturn(codelistTransformations);

        mvc.perform(get("/transformations/codelist"))
                .andExpect(view().name("codelist_transformation_listing"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("codelistTransformations", is(notNullValue())))
                .andExpect(model().attribute("codelistTransformations", hasSize(2)));

        verify(databaseService).getCodelistTransformations();
    }

    @Test
    public void deleteTransformation() throws Exception {
        mvc.perform(get("/transformation/codelist/delete").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("codelist_transformation_listing"))
                .andExpect(model().attribute("codelistTransformations", is(notNullValue())));

        verify(databaseService).deleteCodelistTransformation("1");
        verify(databaseService).getCodelistTransformations();
    }

}