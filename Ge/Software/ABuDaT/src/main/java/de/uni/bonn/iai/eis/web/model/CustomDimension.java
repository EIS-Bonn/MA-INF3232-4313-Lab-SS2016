package de.uni.bonn.iai.eis.web.model;

import de.uni.bonn.iai.eis.rdf.LinkedDataCube;
import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.ComponentProperty;
import org.openrdf.model.IRI;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.model.vocabulary.SKOS;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import javax.persistence.Embeddable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Embeddable
public class CustomDimension implements ComponentProperty {

    private String iri;
    private String definedBy;
    private String label;
    private String comment;
    private String codeList;
    private String rangeProperty = SKOS.CONCEPT.toString();

    private String subPropertyOf;

    public String getDefinedBy() {
        return definedBy;
    }

    public void setDefinedBy(String definedBy) {
        this.definedBy = definedBy;
    }

    public String getSubPropertyOf() {
        return subPropertyOf;
    }

    public void setSubPropertyOf(String subPropertyOf) {
        this.subPropertyOf = subPropertyOf;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCodeList() {
        return codeList;
    }

    public void setCodeList(String codeList) {
        this.codeList = codeList;
    }

    @Override
    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public String getRangeProperty() {
        return rangeProperty;
    }

    public void setRangeProperty(String rangeProperty) {
        this.rangeProperty = rangeProperty;
    }

    public String generateRdf(RDFFormat rdfFormat) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            RDFWriter writer = Rio.createWriter(rdfFormat, byteArrayOutputStream);
            writer.startRDF();

            SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();

            IRI dimensionIri = valueFactory.createIRI(this.iri);

            writer.handleNamespace(RDF.PREFIX, RDF.NAMESPACE);
            writer.handleNamespace(RDFS.PREFIX, RDFS.NAMESPACE);
            writer.handleNamespace(LinkedDataCube.PREFIX, LinkedDataCube.NAMESPACE);
            writer.handleNamespace(Obeu.Dimension.PREFIX, Obeu.Dimension.NAMESPACE);


            writer.handleStatement(valueFactory.createStatement(dimensionIri, RDF.TYPE, RDF.PROPERTY));
            writer.handleStatement(valueFactory.createStatement(dimensionIri, RDF.TYPE, LinkedDataCube.CODED_PROPERTY));
            writer.handleStatement(valueFactory.createStatement(dimensionIri, RDF.TYPE, LinkedDataCube.DIMENSION));
            writer.handleStatement(valueFactory.createStatement(dimensionIri, RDFS.LABEL, valueFactory.createLiteral(label)));
            writer.handleStatement(valueFactory.createStatement(dimensionIri, RDFS.COMMENT, valueFactory.createLiteral(comment)));
            writer.handleStatement(valueFactory.createStatement(dimensionIri, RDFS.SUBPROPERTYOF, valueFactory.createIRI(subPropertyOf)));

            if (rangeProperty != null && !rangeProperty.isEmpty()) {
                writer.handleStatement(valueFactory.createStatement(dimensionIri, RDFS.RANGE, valueFactory.createIRI(rangeProperty)));
            }
            if (codeList != null && !codeList.isEmpty()) {
                writer.handleStatement(valueFactory.createStatement(dimensionIri, LinkedDataCube.CODELIST, valueFactory.createIRI(codeList)));
            }
            writer.handleStatement(valueFactory.createStatement(dimensionIri, RDFS.ISDEFINEDBY, valueFactory.createIRI(definedBy)));

            writer.endRDF();

            return new String(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
