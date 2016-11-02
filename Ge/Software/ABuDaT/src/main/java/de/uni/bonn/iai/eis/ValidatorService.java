package de.uni.bonn.iai.eis;

import cn.yyz.nospa.validator.nonsparql.NospaValidator;
import de.uni.bonn.iai.eis.rdf.Urls;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.Charset;

public class ValidatorService {

    @Autowired
    private DownloadUtils downloadUtils;

    public ValidationResult validate(String cubeData) throws IOException {
        return validate(IOUtils.toInputStream(cubeData, Charset.forName("UTF-8")));
    }

    public ValidationResult validate(InputStream cubeData) throws IOException {
        InputStream dataToValidate = new SequenceInputStream(cubeData,
                IOUtils.toInputStream(downloadDataCubeAndObeuRelevantStuff(), Charset.forName("UTF-8")));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);

        NospaValidator nospaValidator = new NospaValidator(dataToValidate, "TTL", outputStreamWriter);
        nospaValidator.normalize();
        boolean outcome = nospaValidator.validateAllAndGiveResult();
        String result = IOUtils.toString(byteArrayOutputStream.toByteArray(), "UTF-8");

        return new ValidationResult(outcome, result);
    }

    private String downloadDataCubeAndObeuRelevantStuff() throws IOException {
        String dataCubeVocabulary = downloadUtils.download(Urls.LINKED_DATA_CUBE);
        String codeLists = downloadUtils.download(Urls.OBEU_CODE_LISTS);
        String components = downloadUtils.download(Urls.OBEU_COMPONENTS);
        String concepts = downloadUtils.download(Urls.OBEU_CONCEPTS);
        String metaData = downloadUtils.download(Urls.OBEU_METADATA);
        String ontology = downloadUtils.download(Urls.OBEU_ONTOLOGY);

        return dataCubeVocabulary + codeLists + components + concepts + metaData + ontology;
    }

}
