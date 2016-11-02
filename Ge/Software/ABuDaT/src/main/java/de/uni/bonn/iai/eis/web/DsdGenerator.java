package de.uni.bonn.iai.eis.web;

import de.uni.bonn.iai.eis.rdf.DbPediaResource;
import de.uni.bonn.iai.eis.rdf.LinkedDataCube;
import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.BudgetPhase;
import de.uni.bonn.iai.eis.rdf.obeu.OperationCharacter;
import de.uni.bonn.iai.eis.web.model.CustomDimension;
import de.uni.bonn.iai.eis.web.model.CustomMeasure;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.openrdf.model.BNode;
import org.openrdf.model.IRI;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.model.vocabulary.SKOS;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class DsdGenerator {

    private static final SimpleValueFactory valueFactory = SimpleValueFactory.getInstance();

    public String generateRDF(DataTransformation transformation, RDFFormat rdfFormat) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            RDFWriter writer = Rio.createWriter(rdfFormat, byteArrayOutputStream);
            writer.startRDF();

            handleNamespaces(writer);

            IRI dsdIRI = valueFactory.createIRI(transformation.getDsdUrl());

            writer.handleStatement(valueFactory.createStatement(dsdIRI, RDF.TYPE, LinkedDataCube.DSD));
            //TODO language not given

            String dsdLabel = transformation.getDsdLabel();
            if (dsdLabel != null && !dsdLabel.isEmpty()) {
                writer.handleStatement(valueFactory.createStatement(dsdIRI, RDFS.LABEL, valueFactory.createLiteral(dsdLabel)));
            }

            //TODO budget phase?
            //TODO check dsd's of aragon / esif for other dimensions or measures to add

            BNode bNode1 = valueFactory.createBNode();
            writer.handleStatement(valueFactory.createStatement(dsdIRI, LinkedDataCube.COMPONENT, bNode1));
            writer.handleStatement(valueFactory.createStatement(bNode1, LinkedDataCube.DIMENSION, Obeu.Dimension.BUDGETARY_UNIT));
            writer.handleStatement(valueFactory.createStatement(bNode1, LinkedDataCube.COMPONENT_ATTACHMENT, LinkedDataCube.DATASET_TYPE));

            BNode bNode2 = valueFactory.createBNode();
            writer.handleStatement(valueFactory.createStatement(dsdIRI, LinkedDataCube.COMPONENT, bNode2));
            writer.handleStatement(valueFactory.createStatement(bNode2, LinkedDataCube.ATTRIBUTE, Obeu.Attribute.CURRENCY));
            writer.handleStatement(valueFactory.createStatement(bNode2, LinkedDataCube.COMPONENT_REQUIRED, valueFactory.createLiteral(true)));
            writer.handleStatement(valueFactory.createStatement(bNode2, LinkedDataCube.COMPONENT_ATTACHMENT, LinkedDataCube.DATASET_TYPE));

            Set<String> dimensionUris = transformation.getCustomDimensions().stream().map(CustomDimension::getIri).collect(Collectors.toSet());
            Set<String> measureUris = transformation.getCustomMeasures().stream().map(CustomMeasure::getIri).collect(Collectors.toSet());

            for (Mapping mapping : transformation.getMappings()) {
                if (mapping.getIsAmount()) {
                    measureUris.add(mapping.getUri());
                } else {
                    dimensionUris.add(mapping.getUri());
                }
            }

            for (String uri : dimensionUris) {
                BNode bNode = valueFactory.createBNode();
                writer.handleStatement(valueFactory.createStatement(dsdIRI, LinkedDataCube.COMPONENT, bNode));
                writer.handleStatement(valueFactory.createStatement(
                        bNode, LinkedDataCube.DIMENSION, valueFactory.createIRI(uri)
                ));
            }

            for (String uri : measureUris) {
                BNode bNode = valueFactory.createBNode();
                writer.handleStatement(valueFactory.createStatement(dsdIRI, LinkedDataCube.COMPONENT, bNode));
                writer.handleStatement(valueFactory.createStatement(
                        bNode, LinkedDataCube.MEASURE, valueFactory.createIRI(uri)
                ));
                writer.handleStatement(valueFactory.createStatement(
                        bNode, LinkedDataCube.COMPONENT_ATTACHMENT, LinkedDataCube.DATASET_TYPE)
                );
            }

            writer.endRDF();

            return new String(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Unable to write data structure definition.");
        }
    }

    private void handleNamespaces(RDFWriter writer) {
        writer.handleNamespace(RDF.PREFIX, RDF.NAMESPACE);
        writer.handleNamespace(RDFS.PREFIX, RDF.NAMESPACE);
        writer.handleNamespace(SKOS.PREFIX, SKOS.NAMESPACE);
        writer.handleNamespace(XMLSchema.PREFIX, XMLSchema.NAMESPACE);

        writer.handleNamespace(LinkedDataCube.PREFIX, LinkedDataCube.NAMESPACE);
        writer.handleNamespace(DbPediaResource.PREFIX, DbPediaResource.NAMESPACE);

        writer.handleNamespace(Obeu.Dimension.PREFIX, Obeu.Dimension.NAMESPACE);
        writer.handleNamespace(Obeu.Attribute.PREFIX, Obeu.Attribute.NAMESPACE);
        writer.handleNamespace(Obeu.Measure.PREFIX, Obeu.Measure.NAMESPACE);
        writer.handleNamespace(Obeu.Operation.PREFIX, Obeu.Operation.NAMESPACE);
        writer.handleNamespace(Obeu.Dimension.BudgetPhase.PREFIX, Obeu.Dimension.BudgetPhase.NAMESPACE);
        writer.handleNamespace(Obeu.Attribute.Currency.PREFIX, Obeu.Attribute.Currency.NAMESPACE);
    }

    public String generateDsdData(DataTransformation transformation, RDFFormat rdfFormat) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            RDFWriter writer = Rio.createWriter(rdfFormat, byteArrayOutputStream);
            writer.startRDF();

            handleNamespaces(writer);

            IRI datasetIRI = valueFactory.createIRI(transformation.getDatasetUrl());
            IRI dataStructureDefinitionIRI = valueFactory.createIRI(transformation.getDsdUrl());

            writer.handleStatement(valueFactory.createStatement(datasetIRI, RDF.TYPE, LinkedDataCube.DATASET_TYPE));
            writer.handleStatement(valueFactory.createStatement(datasetIRI, LinkedDataCube.STRUCTURE, dataStructureDefinitionIRI));

            //budgetary unit, mandatory
            writer.handleStatement(valueFactory.createStatement(datasetIRI, Obeu.Dimension.BUDGETARY_UNIT,
                    valueFactory.createIRI(transformation.getBudgetaryUnit())));

            //fiscal period, mandatory
            writer.handleStatement(valueFactory.createStatement(datasetIRI, Obeu.Dimension.FISCAL_PERIOD,
                    valueFactory.createIRI(transformation.getFiscalPeriod())));

            //currency, mandatory
            writer.handleStatement(
                        valueFactory.createStatement(datasetIRI, Obeu.Attribute.CURRENCY, valueFactory.createIRI(transformation.getCurrency())));

            //operational character, optional
            OperationCharacter operationCharacter = transformation.getOperationCharacter();
            if (operationCharacter != null) {
                writer.handleStatement(
                        valueFactory.createStatement(datasetIRI, Obeu.Dimension.OPERATION_CHARACTER, operationCharacter.iri));
            }

            //budget phase, optional
            BudgetPhase budgetPhase = transformation.getBudgetPhase();
            if (budgetPhase != null) {
                writer.handleStatement(
                        valueFactory.createStatement(datasetIRI, Obeu.Dimension.BUDGET_PHASE, budgetPhase.iri));
            }

            //taxes included, optional
            int taxesIncluded = transformation.getTaxesIncluded();
            if (taxesIncluded != -1) {
                writer.handleStatement(
                        valueFactory.createStatement(
                                datasetIRI, Obeu.Attribute.TAXES_INCLUDED, valueFactory.createLiteral(taxesIncluded == 1)));
            }

            writer.endRDF();

            return new String(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Unable to write data structure definition data.");
        }
    }


}
