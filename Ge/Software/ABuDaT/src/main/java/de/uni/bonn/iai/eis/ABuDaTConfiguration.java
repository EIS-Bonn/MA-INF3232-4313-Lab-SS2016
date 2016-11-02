package de.uni.bonn.iai.eis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ABuDaTConfiguration {

    @Value("${linkedpipes.etl.host}")
    private String LINKEDPIPES_HOST;

    @Value("${linkedpipes.etl.port}")
    private Integer LINKEDPIPES_PORT;

    @Value("${abudat.output-dir}")
    private String ABUDAT_OUTPUT_DIR;

    @Value("${fuseki.data.endpoint}")
    private String FUSEKI_DATA_ENDPOINT;

    String getLinkedpipesHost() {
        return LINKEDPIPES_HOST;
    }

    int getLinkedpipesPort() {
        return LINKEDPIPES_PORT;
    }

    String getResourcesBaseUrl() {
        return getLinkedpipesAddress() + "/resources";
    }

    public String getLinkedpipesAddress() {
        return "http://" + LINKEDPIPES_HOST + ":" + LINKEDPIPES_PORT;
    }

    public String getComponentBaseUrl() {
        return getResourcesBaseUrl() + "/components";
    }

    public String getPipelineBaseUrl() {
        return getResourcesBaseUrl() + "/pipelines";
    }

    public String getExecutionsBaseUrl() {
        return getResourcesBaseUrl() + "/executions";
    }

    public String getOutputDir() {
        return ABUDAT_OUTPUT_DIR;
    }

    public String fusekiDataEndpoint() {
        return FUSEKI_DATA_ENDPOINT;
    }
}
