package de.uni.bonn.iai.eis.web;

import org.openrdf.model.impl.SimpleValueFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

abstract class CommonValidator implements Validator {

    private SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();

    /**
     * Try to create an iri from the falue of the given field. If this fails an error is added.
     *
     * @param errors    the errors to add to
     * @param field     the field to validate
     * @param fieldName the field name to add the error for
     */
    void validateIriField(Errors errors, String field, String fieldName) {
        validateStringField(errors, fieldName);
        if (!isEmpty(field)) {
            try {
                valueFactory.createIRI(field);
            } catch (IllegalArgumentException e) {
                errors.rejectValue(fieldName, "1", "Error, not a valid IRI.\n");
            }
        }
    }

    /**
     * Validate that a field is not null or empty
     *
     * @param errors    the errors to add to
     * @param fieldName the name of the field to validate.
     */
    void validateStringField(Errors errors, String fieldName) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldName, "0", "Error, this field must not be empty.\n");
    }

    /**
     * Check if string is empty. That means, if it is null or equals the empty string ""
     *
     * @param string the String to check.
     * @return whether the string is emtpy.
     */
    protected boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
