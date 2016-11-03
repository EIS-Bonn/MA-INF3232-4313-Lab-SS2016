package de.uni.bonn.iai.eis.web;

import de.uni.bonn.iai.eis.web.model.CodelistSlice;
import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.example.ExampleCodelistTransformation1;
import org.junit.Test;
import org.springframework.validation.DirectFieldBindingResult;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CodelistTransformationFormValidatorTest {

    private CodelistTransformationFormValidator validator = new CodelistTransformationFormValidator();

    @Test
    public void testSupports() throws Exception {
        assertTrue(validator.supports(CodelistTransformation.class));
        assertFalse(validator.supports(DataTransformation.class));
    }

    @Test
    public void testValidateSuccess() throws Exception {
        ExampleCodelistTransformation1 transformation = new ExampleCodelistTransformation1();

        DirectFieldBindingResult errors = new DirectFieldBindingResult(transformation, "transformation");
        validator.validate(transformation, errors);

        assertTrue(errors.getAllErrors().isEmpty());
    }

    @Test
    public void testValidateFailure() {
        CodelistTransformation transformation = new CodelistTransformation();

        transformation.addSlice(new CodelistSlice());

        DirectFieldBindingResult errors = new DirectFieldBindingResult(transformation, "transformation");
        validator.validate(transformation, errors);

        assertFalse(errors.getAllErrors().isEmpty());

        assertNotNull(errors.getFieldError("charset"));
        assertNotNull(errors.getFieldError("uri"));
        assertNotNull(errors.getFieldError("slices[0].keyColumn"));
        assertNotNull(errors.getFieldError("slices[0].labelColumn"));
    }

}