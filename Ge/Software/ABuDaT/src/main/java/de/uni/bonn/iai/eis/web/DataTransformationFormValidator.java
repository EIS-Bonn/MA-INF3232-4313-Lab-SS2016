package de.uni.bonn.iai.eis.web;

import de.uni.bonn.iai.eis.web.model.CustomDimension;
import de.uni.bonn.iai.eis.web.model.CustomMeasure;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.springframework.validation.Errors;

import java.util.List;

public class DataTransformationFormValidator extends CommonValidator {

    /**
     * This validator only validates DataTransformations.
     *
     * @param clazz the class to check
     * @return whether the class can be validated
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return DataTransformation.class.equals(clazz);
    }

    /**
     * Validates a DataTransformation
     *
     * @param target the transformation to validate
     * @param errors errors of the validation
     */
    @Override
    public void validate(Object target, Errors errors) {
        DataTransformation transformation = (DataTransformation) target;

        validateStringField(errors, "name");
        validateStringField(errors, "dsdLabel");

        validateIriField(errors, transformation.getDsdUrl(), "dsdUrl");
        validateIriField(errors, transformation.getDatasetUrl(), "datasetUrl");
        validateIriField(errors, transformation.getBudgetaryUnit(), "budgetaryUnit");
        validateIriField(errors, transformation.getFiscalPeriod(), "fiscalPeriod");
        validateIriField(errors, transformation.getCurrency(), "currency");

        validateCustomDimensions(errors, transformation);

        validateCustomMeasures(errors, transformation);

        validateMappings(errors, transformation);
    }

    /**
     * Validate the culomn mappings
     *
     * @param errors         the errors to add to
     * @param transformation the transformation to read the mappings from
     */
    private void validateMappings(Errors errors, DataTransformation transformation) {
        List<Mapping> mappings = transformation.getMappings();

        for (int i = 0; i < mappings.size(); i++) {
            validateMapping(errors, mappings.get(i), String.format("mappings[%d]", i));
        }

    }

    /**
     * Validate a single column mapping
     *
     * @param errors                   The errors to add to
     * @param mapping                  the mapping to validate
     * @param fieldNameBase            the base name of the field
     */
    private void validateMapping(Errors errors, Mapping mapping, String fieldNameBase) {
        validateStringField(errors, fieldNameBase + ".name");
        validateIriField(errors, mapping.getUri(), fieldNameBase + ".uri");

        if (!mapping.getIsAmount()) {
            validateIriField(errors, mapping.getValuePrefix(), fieldNameBase + ".valuePrefix");
        }
    }

    /**
     * Validate the custom measures
     *
     * @param errors         the errors to add to
     * @param transformation the transformation to read the custom dimensions from
     */
    private void validateCustomMeasures(Errors errors, DataTransformation transformation) {
        List<CustomMeasure> customMeasures = transformation.getCustomMeasures();
        if (!customMeasures.isEmpty()) {
            for (int i = 0; i < customMeasures.size(); i++) {
                validateCustomMeasure(errors, customMeasures.get(i), String.format("customMeasures[%d]", i));
            }
        }
    }

    /**
     * Validate the custom dimensions
     *
     * @param errors         the errors to add to
     * @param transformation the transformation to read the custom dimensions from
     */
    private void validateCustomDimensions(Errors errors, DataTransformation transformation) {
        List<CustomDimension> customDimensions = transformation.getCustomDimensions();
        if (!customDimensions.isEmpty()) {
            for (int i = 0; i < customDimensions.size(); i++) {
                validateCustomDimension(errors, customDimensions.get(i), String.format("customDimensions[%d]", i));
            }
        }
    }

    /**
     * Validates a custom measures
     *
     * @param errors        the errors to add to
     * @param customMeasure the custom measure to validate
     * @param fieldNameBase the base name of the field
     */
    private void validateCustomMeasure(Errors errors, CustomMeasure customMeasure, String fieldNameBase) {
        validateIriField(errors, customMeasure.getIri(), fieldNameBase + ".iri");
        validateStringField(errors, fieldNameBase + ".label");
        validateSubPropertyField(errors, customMeasure.getSubPropertyOf(), fieldNameBase + ".subPropertyOf");
    }

    /**
     * Validates a custom dimension
     *
     * @param errors          the errors to add to
     * @param customDimension the custom dimension to validate
     * @param fieldNameBase   the base name of the field
     */
    private void validateCustomDimension(Errors errors, CustomDimension customDimension, String fieldNameBase) {
        validateIriField(errors, customDimension.getIri(), fieldNameBase + ".iri");
        validateStringField(errors, fieldNameBase + ".label");
        validateSubPropertyField(errors, customDimension.getSubPropertyOf(), fieldNameBase + ".subPropertyOf");
        validateIriField(errors, customDimension.getRangeProperty(), fieldNameBase + ".rangeProperty");
        validateIriField(errors, customDimension.getCodeList(), fieldNameBase + ".codeList");
    }

    /**
     * Validate a subPropertyOf dropdown field
     *
     * @param errors        the errors to add to
     * @param subPropertyOf the subPropertyOf to validate
     * @param fieldName     the name of the field
     */
    private void validateSubPropertyField(Errors errors, String subPropertyOf, String fieldName) {
        if (isEmpty(subPropertyOf)) {
            errors.rejectValue(fieldName, "3", "Please choose a type.");
        }
    }

}
