package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.DatabaseService;
import de.uni.bonn.iai.eis.web.CodelistTransformationFormValidator;
import de.uni.bonn.iai.eis.web.EtlService;
import de.uni.bonn.iai.eis.web.model.CodelistSlice;
import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import de.uni.bonn.iai.eis.web.model.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.util.List;
import java.util.UUID;

@Controller
public class CodelistTransformationController extends TransformationController<CodelistTransformation> {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private EtlService etlService;

    private Validator validator = new CodelistTransformationFormValidator();

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(value = "/transformation/codelist", params = {"addCode"}, method = RequestMethod.POST)
    public String addCode(@ModelAttribute CodelistTransformation codelistTransformation) {
        codelistTransformation.addSlice(new CodelistSlice());

        return "codelist_transformation";
    }

    @RequestMapping(value = "/transformation/codelist", params = {"save"}, method = RequestMethod.POST)
    public String saveTransformation(@Validated @ModelAttribute CodelistTransformation codelistTransformation,
                                     BindingResult bindingResult, Model model) {
        codelistTransformation = databaseService.saveCodelistTransformation(codelistTransformation);

        model.addAttribute("success", "The transformation was successfully saved.");

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "The form contains errors.");
        }


        return "codelist_transformation";
    }

    @RequestMapping(value = "/transformation/codelist", method = RequestMethod.GET)
    public String getTransformation(@RequestParam("id") String id, Model model) {
        CodelistTransformation codelistTransformation = databaseService.getCodelistTransformation(id);

        model.addAttribute("classActiveTransformation", "active");
        model.addAttribute("codelistTransformation", codelistTransformation);

        return "codelist_transformation";
    }

    @RequestMapping(value = "/transformation/codelist", params = {"copy"}, method = RequestMethod.POST)
    public String copyTransformation(@Validated @ModelAttribute CodelistTransformation codelistTransformation,
                                     BindingResult bindingResult, Model model) {
        codelistTransformation.setId(UUID.randomUUID().toString());
        codelistTransformation.setName("Copy of: "+codelistTransformation.getName());
        codelistTransformation = databaseService.saveCodelistTransformation(codelistTransformation);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "The form contains errors.");
        }

        model.addAttribute("success", "The transformation was successfully copied. You are now working on the copy!");
        model.addAttribute("classActiveTransformation", "active");

        return "codelist_transformation";
    }

    // TODO: 04.10.16 duplicate code
    @RequestMapping(value = "/transformation/codelist", params = {"execute"}, method = RequestMethod.POST)
    public String executeTransformation(@Validated @ModelAttribute CodelistTransformation transformation,
                                        BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "The form has errors and was not executed.");
            return "codelist_transformation";
        }

        tryExecute(transformation, model);

        transformation = databaseService.getCodelistTransformation(transformation.getId());

        model.addAttribute("classActiveTransformation", "active");

        return "codelist_transformation";
    }



    @RequestMapping(value = "/transformation/codelist/execute", method = RequestMethod.GET)
    public String executeTransformationById(@RequestParam("id") String id, Model model) {
        CodelistTransformation transformation = databaseService.getCodelistTransformation(id);

        tryExecute(transformation, model);

        List<CodelistTransformation> codelistTransformations = databaseService.getCodelistTransformations();

        // TODO: 04.10.16 check classActive stuff
        model.addAttribute("classActiveTransformations", "active");
        model.addAttribute("codelistTransformations", codelistTransformations);

        return "codelist_transformation_listing";
    }

    void execute(CodelistTransformation codelistTransformation) throws ConnectException {
        Execution execution = etlService.executeTransformation(codelistTransformation);

        databaseService.saveCodelistTransformationExecution(execution);
    }

    @RequestMapping(value = "/transformations/codelist", method = RequestMethod.GET)
    public String getTransformations(Model model) {
        List<CodelistTransformation> codelistTransformations = databaseService.getCodelistTransformations();

        model.addAttribute("classActiveCodeLists", "active");
        model.addAttribute("codelistTransformations", codelistTransformations);

        return "codelist_transformation_listing";
    }

    @RequestMapping(value = "/transformation/codelist/delete", method = RequestMethod.GET)
    public String deleteTransformation(@RequestParam("id") String id, Model model) {
        databaseService.deleteCodelistTransformation(id);

        return getTransformations(model);
    }

    @RequestMapping(value = "/transformation/codelist", params = {"removeSlice"}, method = RequestMethod.POST)
    public String removeCustomMeasure(@ModelAttribute CodelistTransformation codelistTransformation,
                                      @RequestParam("removeSlice") int index, Model model) {

        codelistTransformation.removeSlice(index);

        model.addAttribute("classActiveTransformation", "active");

        return "codelist_transformation";
    }


}
