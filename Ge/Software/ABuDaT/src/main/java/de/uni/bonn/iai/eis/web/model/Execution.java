package de.uni.bonn.iai.eis.web.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Entity
public class Execution {
    @Id
    private String executionUri;

    private String pipelineId;

    private String outputFilePath;

    private String dateExecuted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @Transient
    private String executionStatus;

    public String getPipelineId() {
        return pipelineId;
    }

    public String getExecutionUri() {
        return executionUri;
    }

    public String getExecutionStatus() {
        return Optional.ofNullable(executionStatus).orElse("unknown");
    }

    public void setExecutionUri(String executionUri) {
        this.executionUri = executionUri;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public void setExecutionStatus(String executionStatus) {
        this.executionStatus = executionStatus;
    }


    public String getDateExecuted() {
        return dateExecuted;
    }

    public void setDateExecuted(String dateExecuted) {
        this.dateExecuted = dateExecuted;
    }
}
