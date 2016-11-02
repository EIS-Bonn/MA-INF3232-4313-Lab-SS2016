package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.TripleStoreService;
import de.uni.bonn.iai.eis.etl.linkedpipes.ExecutionStatus;
import de.uni.bonn.iai.eis.web.EtlService;
import de.uni.bonn.iai.eis.web.model.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ExecutionsListingController {
    private static final Logger log = LoggerFactory.getLogger(ExecutionsListingController.class);

    @Autowired
    private EtlService etlService;

    @Autowired
    private TripleStoreService tripleStoreService;

    protected void findExecutionStates(Model model, List<Execution> executions) {
        try {
            executions.forEach(execution -> {
                String executionStatusUri = etlService.getExecutionStatus(execution.getExecutionUri());
                if (executionStatusUri != null ){
                    execution.setExecutionStatus(ExecutionStatus.get(executionStatusUri));
                }
            });
        } catch (Exception e) {
            if (e.getCause() instanceof java.net.ConnectException) {
                log.error("Error connecting to linkedpipes ETL.", e);
                model.addAttribute("error", "Error connecting to linkedpipes ETL... Is it running?");
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    protected void tryUploadToTripleStore(Model model, String path) {
        File data = new File(path);
        if (!data.exists()) {
            model.addAttribute("error", "The file does not exist: "+path);
            return;
        }

        try {
            tripleStoreService.upload(data);
            model.addAttribute("success", "Successfully uploaded to the triple store.");
        } catch (Exception e) {
            log.error("Unable to upload to triple store.", e);
            model.addAttribute("error", "Error uploading to the triple store. Is it running?");
        }
    }

    protected String buildRedirectUrl(@RequestParam Map<String, String> params, ABuDaTConfiguration aBuDaTConfiguration) {
        String id = params.get("id");
        String uri = params.get("execution");

        return aBuDaTConfiguration.getLinkedpipesAddress() +
                "/#/pipelines/edit/canvas" +
                "?pipeline=" + aBuDaTConfiguration.getPipelineBaseUrl() + "/" + id +
                "&execution=" + uri;
    }


    protected FileSystemResource tryCreateDownload(Execution execution) {
        String path = execution.getOutputFilePath();

        log.info("File is at: {}", path);
        File file = new File(path);

        if (!file.exists()) {
            log.error("The requested file does not exist: "+path);
            return null;
        }

        return new FileSystemResource(file);
    }

}
