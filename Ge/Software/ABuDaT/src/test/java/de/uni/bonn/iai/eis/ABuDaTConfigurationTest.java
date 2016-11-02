package de.uni.bonn.iai.eis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "linkedpipes.etl.host = example.com", "linkedpipes.etl.port = 4242",
        "abudat.output-dir = /tmp/foobar", "fuseki.data.endpoint = http://localhost:3030/ds/data"})
public class ABuDaTConfigurationTest {
    @Configuration
    public static class TestConfig {
        @Bean
        public ABuDaTConfiguration aBuDaTConfiguration() {
            return new ABuDaTConfiguration();
        }
    }

    @Autowired
    private ABuDaTConfiguration aBuDaTConfiguration;

    @Test
    public void getLinkedpipesHost() throws Exception {
        assertEquals("example.com", aBuDaTConfiguration.getLinkedpipesHost());
    }

    @Test
    public void getLinkedpipesPort() throws Exception {
        assertEquals(4242, aBuDaTConfiguration.getLinkedpipesPort());
    }

    @Test
    public void getResourcesBaseUrl() throws Exception {
        assertEquals("http://example.com:4242/resources", aBuDaTConfiguration.getResourcesBaseUrl());
    }

    @Test
    public void getPipelineBaseUrl() throws Exception {
        assertEquals("http://example.com:4242/resources/pipelines", aBuDaTConfiguration.getPipelineBaseUrl());
    }

    @Test
    public void getExecutionsBaseUrl() throws Exception {
        assertEquals("http://example.com:4242/resources/executions", aBuDaTConfiguration.getExecutionsBaseUrl());
    }

    @Test
    public void getComponentBaseUrl() {
        assertEquals("http://example.com:4242/resources/components", aBuDaTConfiguration.getComponentBaseUrl());
    }

    @Test
    public void getOutputDir() throws Exception {
        assertEquals("/tmp/foobar", aBuDaTConfiguration.getOutputDir());
    }

    @Test
    public void linkedpipesAddress() throws Exception {
        assertEquals("http://example.com:4242", aBuDaTConfiguration.getLinkedpipesAddress());
    }

    @Test
    public void fusekiDataEndpoint() throws Exception {
        assertEquals("http://localhost:3030/ds/data", aBuDaTConfiguration.fusekiDataEndpoint());
    }

}