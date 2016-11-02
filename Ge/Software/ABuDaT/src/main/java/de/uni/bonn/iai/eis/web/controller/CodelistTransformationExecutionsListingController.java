package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.DatabaseService;
import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import de.uni.bonn.iai.eis.web.model.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class CodelistTransformationExecutionsListingController extends ExecutionsListingController{

    private static final Logger log = LoggerFactory.getLogger(CodelistTransformationExecutionsListingController.class);

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private ABuDaTConfiguration aBuDaTConfiguration;

    @RequestMapping(value = "/transformation/codelist/executions", method = RequestMethod.GET)
    public String getExecutionsForTransformationId(Model model, @RequestParam("id") String id) {
        findExecutionsAndPrepareModel(id, model);

        return "codelist_transformation_executions_listing";
    }

    private void findExecutionsAndPrepareModel(String transformation, Model model) {
        List<Execution> executions = databaseService.getCodelistTransformationExecutions(transformation);
        findExecutionStates(model, executions);
        model.addAttribute("executions", executions);

        CodelistTransformation codelistTransformation = databaseService.getCodelistTransformation(transformation);
        model.addAttribute("transformationName", codelistTransformation.getName());
        model.addAttribute("transformationDescription", codelistTransformation.getDescription());
    }

    @RequestMapping(value = "/transformation/codelist/executions/delete", method = RequestMethod.GET)
    public String deleteExecution(@RequestParam Map<String, String> params, Model model) {

        String pipeline = params.get("id");
        String uri = params.get("execution");

        databaseService.deleteCodelistTransformationExecution(uri);

        findExecutionsAndPrepareModel(pipeline, model);

        return "codelist_transformation_executions_listing";
    }

    @RequestMapping(value = "/transformation/codelist/executions/show", method = RequestMethod.GET)
    public String openExecutionInLinkedpipes(@RequestParam Map<String, String> params) {

        return "redirect:" + buildRedirectUrl(params, aBuDaTConfiguration);
    }

    @RequestMapping(produces = "text/turtle", value = "/transformation/codelist/executions/result.ttl", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource download(@RequestParam("uri") String uri) {
        log.info("Requested download of result for execution {}", uri);
        return tryCreateDownload(databaseService.getCodelistTransformationExecution(uri));
    }

    @RequestMapping(value = "/transformation/codelist/executions/upload", method = RequestMethod.GET)
    public String upload(@RequestParam Map<String, String> params, Model model) {

        String pipeline = params.get("id");
        String uri = params.get("uri");

        log.info("Requested upload of result for execution {}", uri);

        Execution execution = databaseService.getCodelistTransformationExecution(uri);
        String path = execution.getOutputFilePath();
        log.info("File is at: {}", path);

        tryUploadToTripleStore(model, path);

        findExecutionsAndPrepareModel(pipeline, model);

        return "codelist_transformation_executions_listing";
    }

}
