package de.uni.bonn.iai.eis.web;

import de.uni.bonn.iai.eis.web.model.CodelistSlice;
import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import org.springframework.validation.Errors;

import java.util.List;

public class CodelistTransformationFormValidator extends CommonValidator {

    /**
     * This validator only validates CodelistTransformations.
     *
     * @param clazz the class to check
     * @return whether the class can be validated
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return CodelistTransformation.class.equals(clazz);
    }

    /**
     * Validates a CodelistTransformation
     *
     * @param target the transformation to validate
     * @param errors errors of the validation
     */
    @Override
    public void validate(Object target, Errors errors) {
        CodelistTransformation transformation = (CodelistTransformation) target;

        validateStringField(errors, "name");
        validateStringField(errors, "charset");
        validateIriField(errors, transformation.getUri(), "uri");

        List<CodelistSlice> slices = transformation.getSlices();
        for (int i = 0; i < slices.size(); i++) {
            validateSlice(errors, slices.get(i), String.format("slices[%d]", i));
        }
    }

    /**
     * Validate a codelist slice
     * @param errors errors to add to
     * @param slice slice to validate
     * @param fieldPrefix the indexed slice field prefix
     */
    private void validateSlice(Errors errors, CodelistSlice slice, String fieldPrefix) {
        if (slice.getDoSlice()) {
            validateStringField(errors, fieldPrefix+".startRow");
            validateStringField(errors, fieldPrefix+".endRow");
        }

        validateStringField(errors, fieldPrefix+".keyColumn");
        validateStringField(errors, fieldPrefix+".labelColumn");
    }
}
