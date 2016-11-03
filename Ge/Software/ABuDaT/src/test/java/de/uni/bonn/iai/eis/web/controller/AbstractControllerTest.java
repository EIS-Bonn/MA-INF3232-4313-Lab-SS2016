package de.uni.bonn.iai.eis.web.controller;

import de.uni.bonn.iai.eis.*;
import de.uni.bonn.iai.eis.web.EtlService;
import de.uni.bonn.iai.eis.web.InputAnalyzerService;
import org.springframework.boot.test.mock.mockito.MockBean;

public class AbstractControllerTest {

    @MockBean
    protected DataTransformationRepository dataTransformationRepository;

    @MockBean
    protected DataTransformationExecutionRepository dataTransformationExecutionRepository;

    @MockBean
    protected CodelistTransformationRepository codelistTransformationRepository;

    @MockBean
    protected CodelistTransformationExecutionRepository codelistTransformationExecutionRepository;

    @MockBean
    protected DatabaseService databaseService;

    @MockBean
    protected EtlService etlService;

    @MockBean
    protected InputAnalyzerService inputAnalyzerService;

    @MockBean
    protected TripleStoreService tripleStoreService;

    @MockBean
    protected ValidatorService validatorService;
    
}
