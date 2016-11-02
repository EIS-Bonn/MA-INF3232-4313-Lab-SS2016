package de.uni.bonn.iai.eis;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FusekiServiceTest {

    @Test
    public void testUpload() throws Exception {
        Dataset dataset = DatasetFactory.createMem();
        DatasetAccessor datasetAccessor = DatasetAccessorFactory.create(dataset);

        FusekiService fusekiService = new FusekiService(datasetAccessor);

        URL resource = getClass().getClassLoader().getResource("test_fuseki_insert_data.ttl");
        assert resource != null;

        File file = new File(resource.getFile());

        fusekiService.upload(file);

        Model actualModel = datasetAccessor.getModel();
        Model expectedModel = FileManager.get().loadModel(resource.toString());

        assertEquals(expectedModel.size(), actualModel.size());
        assertTrue(expectedModel.containsAll(actualModel));
    }

}