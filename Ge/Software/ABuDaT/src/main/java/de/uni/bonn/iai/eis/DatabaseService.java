package de.uni.bonn.iai.eis;

import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseService.class);

    @Autowired
    private DataTransformationRepository dataTransformationRepository;

    @Autowired
    private CodelistTransformationRepository codelistTransformationRepository;

    @Autowired
    private DataTransformationExecutionRepository dataTransformationExecutionRepository;

    @Autowired
    private CodelistTransformationExecutionRepository codelistTransformationExecutionRepository;

    public DataTransformation saveDataTransformation(DataTransformation transformation) {
        return dataTransformationRepository.save(transformation);
    }

    public List<DataTransformation> getDataTransformations(){
        return dataTransformationRepository.findAll();
    }

    public DataTransformation getDataTransformation(String id) {
        return dataTransformationRepository.findOne(id);
    }

    public void deleteDataTransformation(String id) {
        dataTransformationRepository.delete(id);
    }

    public Execution saveDataTransformationExecution(Execution execution) {
        return dataTransformationExecutionRepository.save(execution);
    }

    public Execution getDataTransformationExecution(String uri) {
        return dataTransformationExecutionRepository.getOne(uri);
    }

    public List<Execution> getDataTransformationExecutions(String pipelineId) {
        List<Execution> executions = dataTransformationExecutionRepository.findAll();

        return executions.stream()
                .filter(execution -> execution.getPipelineId().equals(pipelineId))
                .collect(Collectors.toList());
    }

    public void deleteDataTransformationExecution(String uri) {
        dataTransformationExecutionRepository.delete(uri);
    }

    public List<CodelistTransformation> getCodelistTransformations() {
        return codelistTransformationRepository.findAll();
    }

    public CodelistTransformation saveCodelistTransformation(CodelistTransformation codelistTransformation) {
        return codelistTransformationRepository.save(codelistTransformation);
    }

    public void deleteCodelistTransformation(String id) {
        codelistTransformationRepository.delete(id);
    }


    public CodelistTransformation getCodelistTransformation(String id) {
        return codelistTransformationRepository.findOne(id);
    }

    public List<Execution> getCodelistTransformationExecutions(String pipelineId) {
        List<Execution> executions = codelistTransformationExecutionRepository.findAll();

        return executions.stream()
                .filter(execution -> execution.getPipelineId().equals(pipelineId))
                .collect(Collectors.toList());
    }

    public Execution getCodelistTransformationExecution(String uri) {
        return codelistTransformationExecutionRepository.findOne(uri);
    }

    public void deleteCodelistTransformationExecution(String uri) {
        codelistTransformationExecutionRepository.delete(uri);
    }

    public Execution saveCodelistTransformationExecution(Execution execution) {
        return codelistTransformationExecutionRepository.save(execution);
    }
}
