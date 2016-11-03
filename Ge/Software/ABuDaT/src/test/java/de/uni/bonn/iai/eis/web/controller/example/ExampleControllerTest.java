package de.uni.bonn.iai.eis.web.controller.example;

import de.uni.bonn.iai.eis.web.controller.AbstractControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ExampleController.class)
public class ExampleControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void exampleDataTransformation1() throws Exception {
        mvc.perform(get("/transformation/data/example1"))
                .andExpect(status().isOk())
                .andExpect(view().name("data_transformation"))
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("customDimensions", hasSize(4))))
                .andExpect(model().attribute("dataTransformation", hasProperty("customMeasures", hasSize(0))))
                .andExpect(model().attribute("dataTransformation", hasProperty("mappings", hasSize(5))))
                .andReturn();
    }

    @Test
    public void exampleDataTransformation2() throws Exception {
        mvc.perform(get("/transformation/data/example2"))
                .andExpect(status().isOk())
                .andExpect(view().name("data_transformation"))
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("customDimensions", hasSize(5))))
                .andExpect(model().attribute("dataTransformation", hasProperty("customMeasures", hasSize(3))))
                .andExpect(model().attribute("dataTransformation", hasProperty("mappings", hasSize(8))))
                .andReturn();
    }

    @Test
    public void exampleCodelistTransformation() throws Exception {
        mvc.perform(get("/transformation/codelist/example1"))
                .andExpect(status().isOk())
                .andExpect(view().name("codelist_transformation"))
                .andExpect(model().attribute("codelistTransformation", is(notNullValue())))
                .andReturn();
    }

}