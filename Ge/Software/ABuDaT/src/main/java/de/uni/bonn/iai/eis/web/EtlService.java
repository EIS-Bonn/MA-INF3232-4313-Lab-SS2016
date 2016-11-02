package de.uni.bonn.iai.eis.web;

import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.etl.linkedpipes.LinkedPipesETL;
import de.uni.bonn.iai.eis.web.model.AbstractTransformation;
import de.uni.bonn.iai.eis.web.model.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.ConnectException;
import java.util.UUID;

@Service
public class EtlService {
    private final ABuDaTConfiguration aBuDaTConfiguration;

    private final LinkedPipesETL linkedPipesETL;

    private static final Logger log = LoggerFactory.getLogger(EtlService.class);

    @Autowired
    public EtlService(ABuDaTConfiguration aBuDaTConfiguration, LinkedPipesETL linkedPipesETL) {
        this.aBuDaTConfiguration = aBuDaTConfiguration;
        this.linkedPipesETL = linkedPipesETL;

        File abudatOutputDir = new File(this.aBuDaTConfiguration.getOutputDir());

        if (!abudatOutputDir.exists()) {
            boolean mkdirs = abudatOutputDir.mkdirs();

            if (!mkdirs && !abudatOutputDir.exists()) {
                throw new RuntimeException(String.format("Unable to create output directory %s", abudatOutputDir.getAbsolutePath()));
            }
        } else {
            if (!abudatOutputDir.isDirectory()) {
                throw new RuntimeException(String.format("The specified output directory is not a directory: %s", abudatOutputDir.getAbsolutePath()));
            }
        }
    }

    public Execution executeTransformation(AbstractTransformation transformation) throws ConnectException {

        String pipelineId = transformation.getId();

        if (!linkedPipesETL.doesPipelineExist(pipelineId)) {
            log.info("Pipeline does not exist in linkedpipes etl. Creating it...");
        } else {
            log.info("Pipeline exists, calling update prior to executing it.");
        }

        String outputFileName = UUID.randomUUID().toString() + "_data.ttl";

        Pipeline pipelineFromTransformation = transformation.createPipeline(outputFileName, aBuDaTConfiguration.getOutputDir(), aBuDaTConfiguration);

        linkedPipesETL.createPipeline(pipelineId);

        log.info("Calling update for pipeline id {}", pipelineId);
        linkedPipesETL.updatePipeline(pipelineFromTransformation);

        log.info("Reading pipeline before execution");
        Pipeline pipeline = linkedPipesETL.readPipeline(pipelineId);

        log.info("executing pipeline");
        String executionUrl = linkedPipesETL.executePipeline(pipeline);

        log.info("Execution has URL {}", executionUrl);

        Execution execution = new Execution();
        execution.setPipelineId(pipelineId);
        execution.setExecutionUri(executionUrl);
        execution.setOutputFilePath(aBuDaTConfiguration.getOutputDir() + "/" + outputFileName);

        return execution;
    }

    public String getExecutionStatus(String uri) {
        return linkedPipesETL.getExecutionStatus(uri);
    }
}
