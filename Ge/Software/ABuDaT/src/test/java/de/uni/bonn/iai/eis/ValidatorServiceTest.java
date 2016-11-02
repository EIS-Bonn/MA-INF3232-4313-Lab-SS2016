package de.uni.bonn.iai.eis;

import de.uni.bonn.iai.eis.rdf.Urls;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorServiceTest {

    @Mock
    private DownloadUtils downloadUtils;

    @InjectMocks
    private ValidatorService validatorService;

    @Before
    public void setUpMocks() throws IOException {
        when(downloadUtils.download(Urls.LINKED_DATA_CUBE)).thenReturn(getTestResource("validation/cube.ttl"));
        when(downloadUtils.download(Urls.OBEU_CODE_LISTS)).thenReturn(getTestResource("validation/code_lists.ttl"));
        when(downloadUtils.download(Urls.OBEU_COMPONENTS)).thenReturn(getTestResource("validation/components.ttl"));
        when(downloadUtils.download(Urls.OBEU_CONCEPTS)).thenReturn(getTestResource("validation/concepts.ttl"));
        when(downloadUtils.download(Urls.OBEU_METADATA)).thenReturn(getTestResource("validation/metadata.ttl"));
        when(downloadUtils.download(Urls.OBEU_ONTOLOGY)).thenReturn(getTestResource("validation/ontology.ttl"));
    }

    private String getTestResource(String name) throws IOException {
        return IOUtils.toString(getClass().getClassLoader().getResourceAsStream(name), Charset.forName("UTF-8"));
    }

    @Test
    public void testFailingValidation() throws IOException {
        String data = getTestResource("validation/Aragon-Inc-DSD_incomplete.ttl");

        ValidationResult validationResult = validatorService.validate(data);
        assertFalse(validationResult.passed);
        assertFalse(validationResult.report.isEmpty());
    }

    @Test
    public void testSuccessfulValidation() throws IOException {
        String data = getTestResource("validation/Aragon-Inc-DSD.ttl");

        ValidationResult validationResult = validatorService.validate(data);
        assertTrue(validationResult.passed);
        assertFalse(validationResult.report.isEmpty());
    }

}