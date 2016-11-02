package de.uni.bonn.iai.eis.web.controller.example;

import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.example.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExampleController {
    @RequestMapping(value = "/transformation/data/example1", method = RequestMethod.GET)
    public String exampleDataTransformation1(Model model) {

        DataTransformation transformation = new AragonExampleDataTransformation();

        model.addAttribute("classActiveTransformation", "active");
        model.addAttribute("dataTransformation", transformation);

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data/example2", method = RequestMethod.GET)
    public String exampleDataTransformation2(Model model) {

        DataTransformation transformation = new EsifExampleDataTransformation();

        model.addAttribute("classActiveTransformation", "active");
        model.addAttribute("dataTransformation", transformation);

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/data/example3", method = RequestMethod.GET)
    public String exampleDataTransformation3(Model model) {

        DataTransformation transformation = new AragonExampleDataTransformation2();

        model.addAttribute("classActiveTransformation", "active");
        model.addAttribute("dataTransformation", transformation);

        return "data_transformation";
    }

    @RequestMapping(value = "/transformation/codelist/example1", method = RequestMethod.GET)
    public String exampleCodelistTransformation1(Model model) {

        CodelistTransformation transformation = new ExampleCodelistTransformation1();

        model.addAttribute("classActiveTransformation", "active");
        model.addAttribute("codelistTransformation", transformation);

        return "codelist_transformation";
    }

    @RequestMapping(value = "/transformation/codelist/example2", method = RequestMethod.GET)
    public String exampleCodelistTransformation2(Model model) {

        CodelistTransformation transformation = new ExampleCodelistTransformation2();

        model.addAttribute("classActiveTransformation", "active");
        model.addAttribute("codelistTransformation", transformation);

        return "codelist_transformation";
    }


}
