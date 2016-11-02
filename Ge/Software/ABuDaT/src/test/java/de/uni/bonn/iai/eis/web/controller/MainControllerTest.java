package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.web.Input;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void about() throws Exception {
        mvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

    @Test
    public void information() throws Exception {
        mvc.perform(get("/information"))
                .andExpect(status().isOk())
                .andExpect(view().name("information"));
    }

    @Test
    public void startDataTransformation() throws Exception {
        mvc.perform(get("/transformation/data/start").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("data_transformation_start"))
                .andExpect(model().attribute("chooseInput", is(notNullValue())))
                .andReturn();
    }

    @Test
    public void startDataTransformationPost() throws Exception {
        Input input = new Input("http://example.com/input", new String[]{"col1", "col2"}, ",", "UTF-8", "csv");
        given(inputAnalyzerService.analyzeInput(any(), any(), anyBoolean())).willReturn(input);

        mvc.perform(post("/transformation/data/start").param("url", "http://example.com/foobar"))
                .andExpect(status().isOk())
                .andExpect(view().name("data_transformation"))
                .andExpect(model().attribute("dataTransformation", is(notNullValue())))
                .andExpect(model().attribute("dataTransformation", hasProperty("mappings", hasSize(2))))
                .andReturn();
    }

    @Test
    public void startCodelistTransformation() throws Exception {
        mvc.perform(get("/transformation/codelist/start").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("codelist_transformation_start"))
                .andExpect(model().attribute("chooseInput", is(notNullValue())))
                .andReturn();
    }

    @Test
    public void startCodelistTransformationPost() throws Exception {
        Input input = new Input("http://example.com/input", new String[]{}, ",", "UTF-8", "csv");
        given(inputAnalyzerService.analyzeInput(any(), any(), anyBoolean())).willReturn(input);

        mvc.perform(post("/transformation/codelist/start").param("url", "http://example.com/foobar"))
                .andExpect(status().isOk())
                .andExpect(view().name("codelist_transformation"))
                .andExpect(model().attribute("codelistTransformation", is(notNullValue())))
                .andReturn();
    }


    @Test
    public void index() throws Exception {
        mvc.perform(get("/").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andReturn();
    }

    // FIXME: when html is stable, add test comparing content
//        String expected;
//        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("expected_transformation_after_start.html")) {
//            expected = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
//        }

//        String actual = IOUtils.toString(mvcResult.getResponse().getContentAsByteArray(), "UTF-8");
//        assertEquals(expected, actual);

}