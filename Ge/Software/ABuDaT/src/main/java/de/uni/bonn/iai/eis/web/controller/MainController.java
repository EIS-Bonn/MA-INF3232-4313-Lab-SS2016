package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.web.Input;
import de.uni.bonn.iai.eis.web.InputAnalyzerService;
import de.uni.bonn.iai.eis.web.model.ChooseInput;
import de.uni.bonn.iai.eis.web.model.CodelistSlice;
import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedList;

@Controller
public class MainController {

//    @Autowired
//    private MultipartResolver multipartResolver;

    @Autowired
    private InputAnalyzerService inputAnalyzerService;

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

//    @ExceptionHandler(Exception.class)
//    public String handleError(HttpServletRequest request, Exception exception, Model model) {
//        log.error("Request: " + request.getRequestURL() + " raised " + exception);
//
//        model.addAttribute("url", request.getRequestURL());
//        model.addAttribute("exception", exception);
//
//        return "error";
//    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("classActiveHome", "active");
        return "index";
    }

    @RequestMapping("/about")
    public String about (Model model) {
        model.addAttribute("classActiveAbout", "active");
        return "about";
    }

    @RequestMapping("/information")
    public String information (Model model) {
        model.addAttribute("classActiveInfo", "active");
        return "information";
    }

    @RequestMapping(value = "/transformation/data/start", method = RequestMethod.GET)
    public String startDataTransformation(Model model) {

        model.addAttribute("chooseInput", new ChooseInput());
        return "data_transformation_start";
    }

    // FIXME: 28.08.16 handle errors correctly
    @RequestMapping(value = "/transformation/data/start", method = RequestMethod.POST)
    public String startDataTransformationPost(@ModelAttribute ChooseInput chooseInput, Model model) {
        Input input = inputAnalyzerService.analyzeInput(chooseInput.getSource(), chooseInput.getCharset(), chooseInput.getHasHeader());

        LinkedList<Mapping> mappings = new LinkedList<>();

        for (String column : input.getColumns()) {
            Mapping mapping = new Mapping();
            mapping.setName(column);
            mappings.add(mapping);
        }

        DataTransformation transformation = new DataTransformation();
        transformation.setSource(input.getUrl());
        transformation.setDelimiterChar(input.getDelimeter());
        transformation.setCharset(input.getCharset());
        transformation.setMappings(mappings);
        transformation.setTableType(input.getTableType());
        transformation.setHasHeader(chooseInput.getHasHeader());

        model.addAttribute("classActiveTransformation", "active");
        model.addAttribute("dataTransformation", transformation);

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/codelist/start", method = RequestMethod.GET)
    public String startCodelistTransformation(Model model) {

        model.addAttribute("chooseInput", new ChooseInput());
        return "codelist_transformation_start";
    }

    // FIXME: 28.08.16 handle errors correctly
    @RequestMapping(value = "/transformation/codelist/start", method = RequestMethod.POST)
    public String startCodelistTransformationPost(@ModelAttribute ChooseInput chooseInput, Model model) {
        Input input = inputAnalyzerService.analyzeInput(chooseInput.getSource(), chooseInput.getCharset(), chooseInput.getHasHeader());

        CodelistTransformation transformation = new CodelistTransformation();
        transformation.setSource(input.getUrl());
        transformation.setDelimiterChar(input.getDelimeter());
        transformation.setColumns(input.getColumns());
        transformation.setCharset(input.getCharset());
        transformation.setTableType(input.getTableType());

        CodelistSlice slice = new CodelistSlice();
        transformation.addSlice(slice);

        model.addAttribute("classActiveCodelist", "active");
        model.addAttribute("codelistTransformation", transformation);

        return "codelist_transformation";
    }

//    @RequestMapping(value = "/start", method = RequestMethod.POST)
//    public String start(@ModelAttribute("chooseInput") ChooseInput chooseInput,
//                        @RequestParam("thefile") MultipartFile file, Model model) {
//
//    }

}
