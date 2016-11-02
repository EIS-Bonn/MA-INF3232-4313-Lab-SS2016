package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.DatabaseService;
import de.uni.bonn.iai.eis.ValidationResult;
import de.uni.bonn.iai.eis.ValidatorService;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.Execution;
import de.uni.bonn.iai.eis.web.model.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class DataTransformationExecutionsListingController extends ExecutionsListingController {

    private static final Logger log = LoggerFactory.getLogger(DataTransformationExecutionsListingController.class);

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private ABuDaTConfiguration aBuDaTConfiguration;

    @Autowired
    private ValidatorService validatorService;

    @RequestMapping(value = "/transformation/data/executions", method = RequestMethod.GET)
    public String getExecutionsForTransformationId(Model model, @RequestParam("id") String id) {
        findExecutionsAndPrepareModel(id, model);

        return "data_transformation_executions_listing";
    }

    @RequestMapping(value = "/transformation/data/executions/delete", method = RequestMethod.GET)
    public String deleteExecution(@RequestParam Map<String,String> params, Model model) {

        String pipeline = params.get("id");
        String uri = params.get("execution");

        databaseService.deleteDataTransformationExecution(uri);

        findExecutionsAndPrepareModel(pipeline, model);

        return "data_transformation_executions_listing";
    }

    private void findExecutionsAndPrepareModel(String transformation, Model model) {
        List<Execution> executions = databaseService.getDataTransformationExecutions(transformation);
        findExecutionStates(model, executions);
        model.addAttribute("executions", executions);

        DataTransformation dataTransformation = databaseService.getDataTransformation(transformation);
        model.addAttribute("transformationName", dataTransformation.getName());
        model.addAttribute("transformationDescription", dataTransformation.getDsdLabel());
    }

    @RequestMapping(value = "/transformation/data/executions/validate", method = RequestMethod.GET)
    public String validateResult(@RequestParam String uri, Model model) {

        Execution execution = databaseService.getDataTransformationExecution(uri);

        //TODO rename pipelineId to transformationId
        DataTransformation dataTransformation = databaseService.getDataTransformation(execution.getPipelineId());

        Validation validation = new Validation();
        validation.setTransformationName(dataTransformation.getName());
        validation.setExecutionDate(execution.getDateExecuted());
        validation.setDsdLabel(dataTransformation.getDsdLabel());

        try {
            File file = new File(execution.getOutputFilePath());
            FileInputStream fileInputStream = new FileInputStream(file);

            ValidationResult validationResult = validatorService.validate(fileInputStream);
            validation.setValidationResult(validationResult);

            if (validationResult.passed) {
                model.addAttribute("success", "Data cube validation passed!");
            } else {
                model.addAttribute("error", "Data cube validation failed!");
            }
        } catch (IOException e) {
            model.addAttribute("error", "Error!");
            throw new RuntimeException(e);
        }

        log.info(validation.getTransformationName());

        model.addAttribute("validation", validation);

        return "validation_result";
    }

    @RequestMapping(value = "/transformation/data/executions/show", method = RequestMethod.GET)
    public String openExecutionInLinkedpipes(@RequestParam Map<String,String> params) {
        return "redirect:" + buildRedirectUrl(params, aBuDaTConfiguration);
    }

    @RequestMapping(produces = "text/turtle", value = "/transformation/data/executions/result.ttl", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource download(@RequestParam("uri") String uri) {
        log.info("Requested download of result for execution {}", uri);
        return tryCreateDownload(databaseService.getDataTransformationExecution(uri));
    }

    @RequestMapping(value = "/transformation/data/executions/upload", method = RequestMethod.GET)
    public String upload(@RequestParam Map<String, String> params, Model model) {

        String id = params.get("id");
        String uri = params.get("uri");

        log.info("Requested upload of result for execution {}", uri);

        Execution execution = databaseService.getDataTransformationExecution(uri);
        String path = execution.getOutputFilePath();
        log.info("File is at: {}", path);

        tryUploadToTripleStore(model, path);

        findExecutionsAndPrepareModel(id, model);

        return "data_transformation_executions_listing";
    }
}
