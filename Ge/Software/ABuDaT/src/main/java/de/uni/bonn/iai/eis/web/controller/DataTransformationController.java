package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.DatabaseService;
import de.uni.bonn.iai.eis.web.DataTransformationFormValidator;
import de.uni.bonn.iai.eis.web.EtlService;
import de.uni.bonn.iai.eis.web.model.*;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class DataTransformationController extends TransformationController<DataTransformation> {

    @Autowired
    private EtlService etlService;

    @Autowired
    private DatabaseService databaseService;

    private Validator validator = new DataTransformationFormValidator();

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(value = "/transformation/data", params = {"addQuery"}, method = RequestMethod.POST)
    public String addQuery(@ModelAttribute DataTransformation dataTransformation, Model model) {
        List<SparqlUpdate> sparqlUpdates = filterEmptySqlUpdates(dataTransformation);

        dataTransformation.setSparqlUpdates(sparqlUpdates);
        dataTransformation.addSparqlUpdate(new SparqlUpdate());

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", params = {"removeQuery"}, method = RequestMethod.POST)
    public String removeQuery(@ModelAttribute DataTransformation dataTransformation,
                              @RequestParam("removeQuery") int index, Model model) {

        dataTransformation.removeSparqlUpdate(index);
        List<SparqlUpdate> sparqlUpdates = filterEmptySqlUpdates(dataTransformation);
        dataTransformation.setSparqlUpdates(sparqlUpdates);

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", params = {"addMapping"}, method = RequestMethod.POST)
    public String addMapping(@ModelAttribute DataTransformation dataTransformation, Model model) {
        List<Mapping> mappings = filterEmptyMappings(dataTransformation);

        dataTransformation.setMappings(mappings);
        dataTransformation.addMapping(new Mapping());

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }


    @RequestMapping(value = "/transformation/data", params = {"removeMapping"}, method = RequestMethod.POST)
    public String removeMapping(@ModelAttribute DataTransformation dataTransformation,
                                @RequestParam("removeMapping") int index, Model model, BindingResult bindingResult) {

        dataTransformation.removeMapping(index);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "The form contains errors.");
        }

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", params = {"addCustomDimension"}, method = RequestMethod.POST)
    public String addCustomDimension(@ModelAttribute DataTransformation dataTransformation, Model model) {

        //TODO filter empty
        dataTransformation.addCustomDimension(new CustomDimension());

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", params = {"removeCustomDimension"}, method = RequestMethod.POST)
    public String removeCustomDimension(@ModelAttribute DataTransformation dataTransformation,
                                        @RequestParam("removeCustomDimension") int index, Model model) {

        dataTransformation.removeCustomDimension(index);

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", params = {"addCustomMeasure"}, method = RequestMethod.POST)
    public String addCustomMeasure(@ModelAttribute DataTransformation dataTransformation, Model model) {

        //TODO filter empty
        dataTransformation.addCustomMeasure(new CustomMeasure());

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", params = {"removeCustomMeasure"}, method = RequestMethod.POST)
    public String removeCustomMeasure(@ModelAttribute DataTransformation dataTransformation,
                                      @RequestParam("removeCustomMeasure") int index, Model model) {

        dataTransformation.removeCustomMeasure(index);

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", params = {"save"}, method = RequestMethod.POST)
    public String saveTransformation(@Validated @ModelAttribute DataTransformation dataTransformation,
                                     BindingResult bindingResult, Model model) {
        dataTransformation = databaseService.saveDataTransformation(dataTransformation);

        model.addAttribute("success", "The transformation was successfully saved.");

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "The form contains errors.");
        }

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", params = {"copy"}, method = RequestMethod.POST)
    public String copyTransformation(@Validated @ModelAttribute DataTransformation dataTransformation,
                                     BindingResult bindingResult, Model model) {
        dataTransformation.setId(UUID.randomUUID().toString());
        dataTransformation.setName("Copy of: "+dataTransformation.getName());
        dataTransformation = databaseService.saveDataTransformation(dataTransformation);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "The form contains errors.");
        }

        model.addAttribute("success", "The transformation was successfully copied. You are now working on the copy!");
        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", method = RequestMethod.GET)
    public String getTransformation(@RequestParam("id") String id, Model model) {
        DataTransformation dataTransformation = databaseService.getDataTransformation(id);

        model.addAttribute("classActiveTransformation", "active");
        model.addAttribute("dataTransformation", dataTransformation);

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data", params = {"execute"}, method = RequestMethod.POST)
    public String executeTransformation(@Validated @ModelAttribute DataTransformation transformation,
                                        BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "The form has errors and was not executed.");
            return "data_transformation";
        }

        tryExecute(transformation, model);

        transformation = databaseService.getDataTransformation(transformation.getId());

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data/delete", method = RequestMethod.GET)
    public String deleteTransformation(@RequestParam("id") String id, Model model) {
        databaseService.deleteDataTransformation(id);

        return getTransformations(model);
    }

    @RequestMapping(value = "/transformations/data", method = RequestMethod.GET)
    public String getTransformations(Model model) {
        List<DataTransformation> dataTransformations = databaseService.getDataTransformations();

        model.addAttribute("classActiveTransformations", "active");
        model.addAttribute("dataTransformations", dataTransformations);

        return "data_transformation_listing";
    }

    @RequestMapping(value = "/transformation/data/execute", method = RequestMethod.GET)
    public String executeTransformationById(@RequestParam("id") String id, Model model) {
        DataTransformation transformation = databaseService.getDataTransformation(id);

        tryExecute(transformation, model);

        List<DataTransformation> dataTransformations = databaseService.getDataTransformations();

        model.addAttribute("classActiveTransformations", "active");
        model.addAttribute("dataTransformations", dataTransformations);

        return "data_transformation_listing";
    }

    @RequestMapping(value = "/transformation/data", params = {"update"}, method = RequestMethod.POST)
    public String justForUpdates(@Validated @ModelAttribute DataTransformation dataTransformation,
                                     BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "The form contains errors.");
        }

        model.addAttribute("classActiveTransformation", "active");

        return "data_transformation";
    }



    void execute(DataTransformation transformation) throws ConnectException {
        //TODO thats a hack, but probably the best way to do it ?!
        for (CustomDimension customDimension : transformation.getCustomDimensions()) {
            customDimension.setDefinedBy(transformation.getDsdUrl());
        }

        //TODO thats a hack, but probably the best way to do it ?!
        for (CustomMeasure customMeasure : transformation.getCustomMeasures()) {
            customMeasure.setDefinedBy(transformation.getDsdUrl());
        }

        Execution execution = etlService.executeTransformation(transformation);

        databaseService.saveDataTransformationExecution(execution);
    }

    List<SparqlUpdate> filterEmptySqlUpdates(DataTransformation transformation) {
        return transformation.getSparqlUpdates().stream()
                .filter(sqlUpdate -> !StringUtils.isEmpty(sqlUpdate.getQuery()))
                .collect(Collectors.toList());
    }

    private List<Mapping> filterEmptyMappings(DataTransformation transformation) {
        return transformation.getMappings().stream()
                .filter(mapping -> !StringUtils.isEmpty(mapping.getName()))
                .collect(Collectors.toList());
    }
}
