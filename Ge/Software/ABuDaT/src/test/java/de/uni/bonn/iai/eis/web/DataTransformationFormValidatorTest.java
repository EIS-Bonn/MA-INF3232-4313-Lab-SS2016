package de.uni.bonn.iai.eis.web;

import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import de.uni.bonn.iai.eis.web.model.CustomDimension;
import de.uni.bonn.iai.eis.web.model.CustomMeasure;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.example.AragonExampleDataTransformation;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.junit.Test;
import org.springframework.validation.DirectFieldBindingResult;

import static org.junit.Assert.*;

public class DataTransformationFormValidatorTest {

    private DataTransformationFormValidator validator = new DataTransformationFormValidator();

    @Test
    public void testSupports() throws Exception {
        assertTrue(validator.supports(DataTransformation.class));
        assertFalse(validator.supports(CodelistTransformation.class));
    }

    @Test
    public void testValidateSuccess() throws Exception {
        AragonExampleDataTransformation transformation = new AragonExampleDataTransformation();

        DirectFieldBindingResult errors = new DirectFieldBindingResult(transformation, "transformation");
        validator.validate(transformation, errors);

        assertTrue(errors.getAllErrors().isEmpty());
    }

    @Test
    public void testValidateFailure() throws Exception {
        DataTransformation transformation = new DataTransformation();

        //add a custom measure
        transformation.addCustomMeasure(new CustomMeasure());

        //add a custom dimension
        transformation.addCustomDimension(new CustomDimension());

        //add a mapping
        transformation.addMapping(new Mapping());

        DirectFieldBindingResult errors = new DirectFieldBindingResult(transformation, "transformation");
        validator.validate(transformation, errors);

        assertFalse(errors.getAllErrors().isEmpty());

        assertNotNull(errors.getFieldError("dsdLabel"));
        assertNotNull(errors.getFieldError("dsdUrl"));
        assertNotNull(errors.getFieldError("datasetUrl"));
        assertNotNull(errors.getFieldError("budgetaryUnit"));
        assertNotNull(errors.getFieldError("fiscalPeriod"));
        assertNotNull(errors.getFieldError("currency"));

        assertNotNull(errors.getFieldError("customMeasures[0].iri"));
        assertNotNull(errors.getFieldError("customMeasures[0].label"));
        assertNotNull(errors.getFieldError("customMeasures[0].subPropertyOf"));

        assertNotNull(errors.getFieldError("customDimensions[0].iri"));
        assertNotNull(errors.getFieldError("customDimensions[0].label"));
        assertNotNull(errors.getFieldError("customDimensions[0].subPropertyOf"));
        assertNotNull(errors.getFieldError("customDimensions[0].codeList"));

        assertNotNull(errors.getFieldError("mappings[0].name"));
        assertNotNull(errors.getFieldError("mappings[0].uri"));
    }

}