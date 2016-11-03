package de.uni.bonn.iai.eis;

public class ValidationResult {
    public boolean passed;
    public String report;

    public ValidationResult(boolean passed, String report) {
        this.passed = passed;
        this.report = report;
    }
}