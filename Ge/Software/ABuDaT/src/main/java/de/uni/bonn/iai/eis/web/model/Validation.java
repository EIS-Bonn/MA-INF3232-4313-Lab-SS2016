package de.uni.bonn.iai.eis.web.model;

import de.uni.bonn.iai.eis.ValidationResult;

public class Validation {
    private String transformationName;
    private String dsdLabel;
    private String executionDate;
    private ValidationResult validationResult;

    public String getValidationReport() {
        if (validationResult != null) {
            return validationResult.report.replaceAll("\n", "<br/>");
        }

        return null;
    }

    public String getTransformationName() {
        return transformationName;
    }

    public void setTransformationName(String transformationName) {
        this.transformationName = transformationName;
    }

    public String getDsdLabel() {
        return dsdLabel;
    }

    public void setDsdLabel(String dsdLabel) {
        this.dsdLabel = dsdLabel;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }
}
