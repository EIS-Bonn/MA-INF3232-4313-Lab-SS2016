package de.uni.bonn.iai.eis;

import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FusekiService extends TripleStoreService {

    private DatasetAccessor datasetAccessor;

    @Autowired
    public FusekiService(DatasetAccessor datasetAccessor) {
        this.datasetAccessor = datasetAccessor;
    }

    @Override
    public void upload(File data) {
        Model model = ModelFactory.createDefaultModel();

        try (FileInputStream in = new FileInputStream(data)) {
            model.read(in, null, "TURTLE");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        datasetAccessor.add(model);
    }

}
