package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.SparqlUpdate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(DataTransformationController.class)
public class DataTransformationControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void addQuery() throws Exception {
        mvc.perform(post("/transformation/data").param("addQuery", ""))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("sparqlUpdates", hasSize(1))));
    }

    @Test
    public void removeQuery() throws Exception {
        mvc.perform(post("/transformation/data").param("removeQuery", "0").param("sparqlUpdates[0].query", "someQuery"))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("sparqlUpdates", hasSize(0))));
    }

    @Test
    public void addMapping() throws Exception {
        mvc.perform(post("/transformation/data").param("addMapping", ""))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("mappings", hasSize(1))));
    }

    @Test
    public void removeMapping() throws Exception {
        mvc.perform(post("/transformation/data").param("removeMapping", "0")
                    .param("mappings[0].name", "someName")
                    .param("mappings[0].uri", "someUri"))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("mappings", hasSize(0))));
    }

    @Test
    public void saveTemplate() throws Exception {

        DataTransformation transformation = new DataTransformation();
        transformation.setId("1");
        given(databaseService.saveDataTransformation(Matchers.any())).willReturn(transformation);

        mvc.perform(post("/transformation/data").param("save", ""))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())));

        verify(databaseService).saveDataTransformation(any());
    }

    @Test
    public void copyTemplate() throws Exception {
        DataTransformation transformation = new DataTransformation();
        transformation.setId("1");
        given(databaseService.saveDataTransformation(Matchers.any())).willReturn(transformation);

        mvc.perform(post("/transformation/data").param("copy", ""))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())));

        verify(databaseService).saveDataTransformation(any());
    }


    @Test
    public void testFilterEmptySqlUpdates() {
        DataTransformation transformation = new DataTransformation();
        transformation.addSparqlUpdate(new SparqlUpdate());
        transformation.addSparqlUpdate(new SparqlUpdate());

        DataTransformationController dataTransformationController = new DataTransformationController();
        List<SparqlUpdate> sparqlUpdates = dataTransformationController.filterEmptySqlUpdates(transformation);

        assertTrue(sparqlUpdates.isEmpty());
    }

    @Test
    public void testExecuteTransformation() throws Exception {
        MockHttpServletRequestBuilder servletRequestBuilder = post("/transformation/data").param("execute", "")
                .param("id", "1")
                .param("datasetUrl", "http://example.com/dataset")
                .param("dsdUrl", "http://example.com/dsd")
                .param("dsdLabel", "label")
                .param("budgetaryUnit", "http://example.com/EU")
                .param("fiscalPeriod", "http://example.com/2020")
                .param("currency", "http://example.com/currency/EUR");

        mvc.perform(servletRequestBuilder)
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())));

        verify(etlService).executeTransformation(any(DataTransformation.class));
        verify(databaseService).saveDataTransformationExecution(any());
        verify(databaseService).getDataTransformation("1");
    }

    @Test
    public void testGetTransformation() throws Exception {
        given(databaseService.getDataTransformation(any())).willReturn(new DataTransformation());

        mvc.perform(get("/transformation/data").param("id", "1"))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk());

        verify(databaseService).getDataTransformation("1");
    }

    @Test
    public void testDeleteTransformation() throws Exception {
        mvc.perform(get("/transformation/data/delete").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("data_transformation_listing"))
                .andExpect(model().attribute("dataTransformations", is(notNullValue())));

        verify(databaseService).deleteDataTransformation("1");
        verify(databaseService).getDataTransformations();
    }

    @Test
    public void testGetTransformations() throws Exception {
        List<DataTransformation> transformations = new ArrayList<>();
        transformations.add(new DataTransformation());
        transformations.add(new DataTransformation());

        given(databaseService.getDataTransformations()).willReturn(transformations);

        mvc.perform(get("/transformations/data"))
                .andExpect(status().isOk())
                .andExpect(view().name("data_transformation_listing"))
                .andExpect(model().attribute("dataTransformations", is(notNullValue())))
                .andExpect(model().attribute("dataTransformations", hasSize(2)));

        verify(databaseService).getDataTransformations();
    }

    @Test
    public void testExecuteTransformationById() throws Exception {
        DataTransformation transformation = new DataTransformation();
        transformation.setId("1");
        given(databaseService.getDataTransformation(Matchers.any())).willReturn(transformation);

        this.mvc.perform(get("/transformation/data/execute").param("id", "1"))
                .andExpect(view().name("data_transformation_listing"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformations", is(notNullValue())));

        verify(databaseService).getDataTransformation("1");
        verify(etlService).executeTransformation(any(DataTransformation.class));
        verify(databaseService).saveDataTransformationExecution(any());
        verify(databaseService).getDataTransformations();
    }

    @Test
    public void addCustomDimension() throws Exception {
        mvc.perform(post("/transformation/data").param("addCustomDimension", ""))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("customDimensions", hasSize(1))));
    }

    @Test
    public void removeCustomDimension() throws Exception {
        mvc.perform(post("/transformation/data")
                    .param("removeCustomDimension", "0")
                    .param("customDimensions[0].iri", "http://foo.bar")
                    .param("customDimensions[0].subPropertyOf", "http://example.com"))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("customDimensions", hasSize(0))));
    }

    @Test
    public void addCustomMeasure() throws Exception {
        mvc.perform(post("/transformation/data").param("addCustomMeasure", ""))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("customMeasures", hasSize(1))));
    }

    @Test
    public void removeCustomMeasure() throws Exception {
        mvc.perform(post("/transformation/data")
                .param("removeCustomMeasure", "0")
                .param("customMeasures[0].iri", "http://foo.bar")
                .param("customMeasures[0].subPropertyOf", "http://example.com"))
                .andExpect(view().name("data_transformation"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("customMeasures", hasSize(0))));
    }

}