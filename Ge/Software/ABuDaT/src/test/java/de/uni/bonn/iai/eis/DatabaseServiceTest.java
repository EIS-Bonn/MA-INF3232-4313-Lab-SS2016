package de.uni.bonn.iai.eis;

import de.uni.bonn.iai.eis.web.model.CodelistTransformation;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.Execution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class DatabaseServiceTest {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private DataTransformationRepository dataTransformationRepository;

    @Autowired
    private DataTransformationExecutionRepository dataTransformationExecutionRepository;

    @Autowired
    private CodelistTransformationRepository codelistTransformationRepository;

    @Autowired
    private CodelistTransformationExecutionRepository codelistTransformationExecutionRepository;

    @Configuration
    public static class TestContextConfiguration {

        @Bean
        public DatabaseService databaseService() {
            return new DatabaseService();
        }

    }

    private DataTransformation createDataTransformation() {
        DataTransformation originalTransformation1 = new DataTransformation();
        originalTransformation1.setBudgetaryUnit("http://some/foo");
        originalTransformation1.setDsdUrl("http://other/foo");
        originalTransformation1.setDatasetUrl("http://another/foo");
        return originalTransformation1;
    }

    private Execution createExecution() {
        Execution execution = new Execution();
        execution.setPipelineId("0");
        execution.setExecutionUri("http://example.com/execution/" + UUID.randomUUID().toString());
        return execution;
    }

    private CodelistTransformation createCodelistTransformation() {
        return new CodelistTransformation();
    }

    @Test
    public void saveDataTransformation() throws Exception {
        DataTransformation dataTransformation = createDataTransformation();
        databaseService.saveDataTransformation(dataTransformation);
        assertTrue(dataTransformationRepository.exists(dataTransformation.getId()));
    }

    @Test
    public void saveAlreadyExistingDataTransformation() {
        DataTransformation dataTransformation = createDataTransformation();
        DataTransformation savedDataTransformation = databaseService.saveDataTransformation(dataTransformation);
        databaseService.saveDataTransformation(savedDataTransformation);
        assertTrue(dataTransformationRepository.exists(savedDataTransformation.getId()));
    }


    @Test
    public void getDataTransformations() throws Exception {
        DataTransformation dataTransformation1 = createDataTransformation();
        DataTransformation dataTransformation2 = createDataTransformation();
        DataTransformation savedDataTransformation1 = dataTransformationRepository.save(dataTransformation1);
        DataTransformation savedDataTransformation2 = dataTransformationRepository.save(dataTransformation2);

        List<DataTransformation> transformations = databaseService.getDataTransformations();
        assertNotNull(transformations);

        List<String> transformationsIds = transformations.stream()
                .map(DataTransformation::getId)
                .collect(Collectors.toList());

        assertTrue(transformationsIds.contains(savedDataTransformation1.getId()));
        assertTrue(transformationsIds.contains(savedDataTransformation2.getId()));
    }

    @Test
    public void getDataTransformation() throws Exception {
        DataTransformation dataTransformation = createDataTransformation();
        DataTransformation savedDataTransformation = dataTransformationRepository.save(dataTransformation);
        DataTransformation readTransformation = databaseService.getDataTransformation(savedDataTransformation.getId());

        assertNotNull(readTransformation);
        assertEquals(savedDataTransformation.getId(), readTransformation.getId());
    }

    @Test
    public void deleteDataTransformation() throws Exception {
        DataTransformation dataTransformation1 = createDataTransformation();
        DataTransformation dataTransformation2 = createDataTransformation();
        DataTransformation savedDataTransformation1 = dataTransformationRepository.save(dataTransformation1);
        DataTransformation savedDataTransformation2 = dataTransformationRepository.save(dataTransformation2);

        databaseService.deleteDataTransformation(savedDataTransformation1.getId());

        List<DataTransformation> transformations = databaseService.getDataTransformations();
        assertNotNull(transformations);

        List<String> transformationsIds = transformations.stream().map(DataTransformation::getId).collect(Collectors.toList());
        assertFalse(transformationsIds.contains(savedDataTransformation1.getId()));
        assertTrue(transformationsIds.contains(savedDataTransformation2.getId()));
    }

    @Test
    public void saveDataTransformationExecution() throws Exception {
        Execution execution = createExecution();
        databaseService.saveDataTransformationExecution(execution);
        assertTrue(dataTransformationExecutionRepository.exists(execution.getExecutionUri()));
        assertTrue(dataTransformationExecutionRepository.exists(execution.getExecutionUri()));
    }

    @Test
    public void getDataTransformationExecution() throws Exception {
        Execution execution = createExecution();
        dataTransformationExecutionRepository.save(execution);
        Execution readExecution = databaseService.getDataTransformationExecution(execution.getExecutionUri());

        assertNotNull(readExecution);
        assertEquals(execution.getExecutionUri(), readExecution.getExecutionUri());
    }

    @Test
    public void getDataTransformationExecutions() throws Exception {
        Execution execution1 = createExecution();
        execution1.setPipelineId("1");
        Execution execution2 = createExecution();
        execution2.setPipelineId("1");
        Execution execution3 = createExecution();
        execution3.setPipelineId("2");
        dataTransformationExecutionRepository.save(execution1);
        dataTransformationExecutionRepository.save(execution2);
        dataTransformationExecutionRepository.save(execution3);

        List<Execution> dataTransformationExecutions = databaseService.getDataTransformationExecutions("1");
        List<String> uris = dataTransformationExecutions.stream().map(Execution::getExecutionUri).collect(Collectors.toList());

        assertTrue(uris.contains(execution1.getExecutionUri()));
        assertTrue(uris.contains(execution2.getExecutionUri()));
        assertFalse(uris.contains(execution3.getExecutionUri()));
    }

    @Test
    public void deleteDataTransformationExecution() throws Exception {
        Execution execution = createExecution();
        String executionUri = execution.getExecutionUri();

        databaseService.saveDataTransformationExecution(execution);
        assertNotNull(dataTransformationExecutionRepository.findOne(executionUri));

        databaseService.deleteDataTransformationExecution(executionUri);
        assertNull(dataTransformationExecutionRepository.findOne(executionUri));
    }

    @Test
    public void getCodelistTransformations() throws Exception {
        CodelistTransformation codelistTransformation1 = createCodelistTransformation();
        CodelistTransformation codelistTransformation2 = createCodelistTransformation();
        codelistTransformationRepository.save(codelistTransformation1);
        codelistTransformationRepository.save(codelistTransformation2);

        List<CodelistTransformation> codelistTransformations = databaseService.getCodelistTransformations();
        List<String> ids = codelistTransformations.stream().map(CodelistTransformation::getId).collect(Collectors.toList());

        assertTrue(ids.contains(codelistTransformation1.getId()));
        assertTrue(ids.contains(codelistTransformation2.getId()));
    }

    @Test
    public void saveCodelistTransformation() throws Exception {
        CodelistTransformation codelistTransformation = createCodelistTransformation();
        databaseService.saveCodelistTransformation(codelistTransformation);

        assertNotNull(codelistTransformationRepository.findOne(codelistTransformation.getId()));
    }

    @Test
    public void deleteCodelistTransformation() throws Exception {
        CodelistTransformation codelistTransformation1 = createCodelistTransformation();
        CodelistTransformation codelistTransformation2 = createCodelistTransformation();
        codelistTransformationRepository.save(codelistTransformation1);
        codelistTransformationRepository.save(codelistTransformation2);

        databaseService.deleteCodelistTransformation(codelistTransformation1.getId());

        List<CodelistTransformation> codelistTransformations = codelistTransformationRepository.findAll();

        List<String> ids = codelistTransformations.stream().map(CodelistTransformation::getId).collect(Collectors.toList());

        assertFalse(ids.contains(codelistTransformation1.getId()));
        assertTrue(ids.contains(codelistTransformation2.getId()));
    }

    @Test
    public void getCodelistTransformation() throws Exception {
        CodelistTransformation codelistTransformation = createCodelistTransformation();
        codelistTransformationRepository.save(codelistTransformation);

        assertNotNull(databaseService.getCodelistTransformation(codelistTransformation.getId()));
    }

    @Test
    public void getCodelistTransformationExecutions() throws Exception {
        Execution execution1 = createExecution();
        execution1.setPipelineId("1");
        Execution execution2 = createExecution();
        execution2.setPipelineId("1");
        Execution execution3 = createExecution();
        execution3.setPipelineId("2");

        codelistTransformationExecutionRepository.save(execution1);
        codelistTransformationExecutionRepository.save(execution2);
        codelistTransformationExecutionRepository.save(execution3);

        List<Execution> codelistTransformationExecutions = databaseService.getCodelistTransformationExecutions("1");
        List<String> uris = codelistTransformationExecutions.stream().map(Execution::getExecutionUri).collect(Collectors.toList());

        assertTrue(uris.contains(execution1.getExecutionUri()));
        assertTrue(uris.contains(execution2.getExecutionUri()));
        assertFalse(uris.contains(execution3.getExecutionUri()));
    }

    @Test
    public void getCodelistTransformationExecution() throws Exception {
        Execution execution = createExecution();
        codelistTransformationExecutionRepository.save(execution);

        assertNotNull(databaseService.getCodelistTransformationExecution(execution.getExecutionUri()));
    }

    @Test
    public void deleteCodelistTransformationExecution() throws Exception {
        Execution execution = createExecution();
        String executionUri = execution.getExecutionUri();

        codelistTransformationExecutionRepository.save(execution);
        assertNotNull(codelistTransformationExecutionRepository.findOne(executionUri));

        databaseService.deleteCodelistTransformationExecution(executionUri);
        assertNull(codelistTransformationExecutionRepository.findOne(executionUri));
    }

    @Test
    public void saveCodelistTransformationExecution() throws Exception {
        Execution execution = createExecution();
        databaseService.saveCodelistTransformationExecution(execution);

        assertNotNull(codelistTransformationExecutionRepository.findOne(execution.getExecutionUri()));
    }

}
