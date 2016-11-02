package de.uni.bonn.iai.eis.web.model.example;

import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.OperationCharacter;
import de.uni.bonn.iai.eis.web.model.CustomDimension;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.openrdf.model.vocabulary.SKOS;

public class AragonExampleDataTransformation2 extends DataTransformation {
    public AragonExampleDataTransformation2() {
        super.setName("ABuDaT example: Aragon expenditures 2012");
        super.setSource("https://raw.githubusercontent.com/openbudgets/datasets/master/Aragon/2012/raw/Partidas%20Gastos.csv");
        super.setDelimiterChar(";");
        super.setTableType("csv");
        super.setCharset("ISO-8859-2");
        setHasHeader(true);
        super.setDatasetUrl("http://data.openbudgets.eu/resource/datasets/aragon-expenditure-2012");
        super.setCurrency("http://data.openbudgets.eu/codelist/currency/EUR");
        super.setBudgetaryUnit("http://dbpedia.org/resource/Aragon");
        super.setFiscalPeriod("http://reference.data.gov.uk/id/year/2012");
        super.setOperationCharacter(OperationCharacter.EXPENDITURE);
        super.setDsdUrl("http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2012");
        super.setDsdLabel("Data structure definition for the expenditure part of the Aragonian budget (autonomous community in northeastern Spain).");

        CustomDimension adminClass = new CustomDimension();
        adminClass.setIri("http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2012/dimension/administrativeClassification");
        adminClass.setLabel("Organization managing the planned budget");
        adminClass.setComment("The administrative classification is organized hierarchical in four levels.");
        adminClass.setSubPropertyOf(Obeu.Classification.ADMINISTRATIVE_CLASSIFICATION.toString());
        adminClass.setRangeProperty(SKOS.CONCEPT.toString());
        adminClass.setCodeList("http://data.openbudgets.eu/resource/codelist/estructura_organica_aragon_2012");
        adminClass.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2012");
        super.addCustomDimension(adminClass);

        CustomDimension funcClass = new CustomDimension();
        funcClass.setIri("http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2012/dimension/functionalClassification");
        funcClass.setLabel("Functional Classification");
        funcClass.setComment("Classifies expenditures by general government sector and by the purpose of the expenditure. " +
                "The functional classification is organized hierarchical into groups, functions, sub-functions and programs.");
        funcClass.setSubPropertyOf(Obeu.Classification.FUNCTIONAL_CLASSIFICATION.toString());
        funcClass.setRangeProperty(SKOS.CONCEPT.toString());
        funcClass.setCodeList("http://data.openbudgets.eu/resource/codelist/estructura_funcional_aragon_2012");
        funcClass.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2012");
        super.addCustomDimension(funcClass);

        CustomDimension ecoClass = new CustomDimension();
        ecoClass.setIri("http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2012/dimension/economicClassification");
        ecoClass.setLabel("Economic Classification");
        ecoClass.setComment("Identifies the type of expenditure incurred or source of revenues. The economic classification is " +
                "organized hierarchical into chapters, articles, concepts, and sub-concepts. This dimension is used for both, expenditure and revenue.");
        ecoClass.setSubPropertyOf(Obeu.Classification.ECONOMIC_CLASSIFICATION.toString());
        ecoClass.setRangeProperty(SKOS.CONCEPT.toString());
        ecoClass.setCodeList("http://data.openbudgets.eu/resource/codelist/estructura_economica_aragon_2012");
        ecoClass.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2012");
        super.addCustomDimension(ecoClass);

        CustomDimension fundingClass = new CustomDimension();
        fundingClass.setIri("http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2012/dimension/fundingClassification");
        fundingClass.setLabel("Funding Classification");
        fundingClass.setComment("Describes the origin of the funding. The funding classification is organized hierarchical " +
                "into origin, fund, and program. This dimension is used for both, expenditure and revenue.");
        fundingClass.setSubPropertyOf(Obeu.Classification.CLASSIFICATION.toString());
        fundingClass.setRangeProperty(SKOS.CONCEPT.toString());
        fundingClass.setCodeList("http://data.openbudgets.eu/resource/codelist/estructura_financiacion_aragon_2012");
        fundingClass.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/aragon-budget-exp-2012");
        super.addCustomDimension(fundingClass);

        //no mapping for column 0

        Mapping mappingCol1 = new Mapping();
        mappingCol1.setName("CENTRO GESTOR");
        mappingCol1.setValuePrefix(adminClass.getCodeList());
        mappingCol1.setUri(adminClass.getCodeList());
        mappingCol1.setComponentProperty(adminClass.getIri());
        mappingCol1.setIsAmount(false);
        super.addMapping(mappingCol1);

        Mapping mappingCol2 = new Mapping();
        mappingCol2.setName("FUNCIONAL");
        mappingCol2.setValuePrefix(funcClass.getCodeList());
        mappingCol2.setUri(funcClass.getCodeList());
        mappingCol2.setComponentProperty(funcClass.getIri());
        mappingCol2.setIsAmount(false);
        super.addMapping(mappingCol2);

        Mapping mappingCol3 = new Mapping();
        mappingCol3.setName("ECONOMICA");
        mappingCol3.setValuePrefix(ecoClass.getCodeList());
        mappingCol3.setUri(ecoClass.getCodeList());
        mappingCol3.setComponentProperty(ecoClass.getIri());
        mappingCol3.setIsAmount(false);
        super.addMapping(mappingCol3);

        Mapping mappingCol4 = new Mapping();
        mappingCol4.setName("FINANCIACION");
        mappingCol4.setValuePrefix(fundingClass.getCodeList());
        mappingCol4.setUri(fundingClass.getCodeList());
        mappingCol4.setComponentProperty(fundingClass.getIri());
        mappingCol4.setIsAmount(false);
        super.addMapping(mappingCol4);

        //no mapping for column 5

        Mapping mappingCol6 = new Mapping();
        mappingCol6.setName("IMPORTE");
        mappingCol6.setComponentProperty(Obeu.Measure.AMOUNT.toString());
        mappingCol6.setUri(Obeu.Measure.AMOUNT.toString());
        mappingCol6.setIsAmount(true);
        super.addMapping(mappingCol6);

    }
}
