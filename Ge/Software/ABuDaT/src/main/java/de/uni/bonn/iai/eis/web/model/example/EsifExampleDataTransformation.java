package de.uni.bonn.iai.eis.web.model.example;

import de.uni.bonn.iai.eis.rdf.Obeu;
import de.uni.bonn.iai.eis.rdf.obeu.BudgetPhase;
import de.uni.bonn.iai.eis.rdf.obeu.OperationCharacter;
import de.uni.bonn.iai.eis.web.model.CustomDimension;
import de.uni.bonn.iai.eis.web.model.CustomMeasure;
import de.uni.bonn.iai.eis.web.model.DataTransformation;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;

public class EsifExampleDataTransformation extends DataTransformation {

    public EsifExampleDataTransformation() {
        setName("ABuDaT example: ESIF 2014-2020");
        setSource("https://raw.githubusercontent.com/openbudgets/datasets/master/ESIF/2014/raw/ESIF_FINANCE_DETAILS.csv");
        setDelimiterChar(",");
        setTableType("csv");
        setCharset("UTF-8");
        setDatasetUrl("http://data.openbudgets.eu/resource/dataset/ESIF-2014-2020");
        setCurrency("http://data.openbudgets.eu/codelist/currency/EUR");
        setBudgetaryUnit("http://dbpedia.org/resource/European_Union");
        setFiscalPeriod("http://data.openbudgets.eu/resource/dataset/2014-2020");
        setOperationCharacter(OperationCharacter.EXPENDITURE);
        setBudgetPhase(BudgetPhase.APPROVED);
        setHasHeader(true);
        setDsdUrl("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        setDsdLabel("Data structure definition for the European Structural and Investment Funds of the years 2014-2020.");

        CustomDimension fundClass = new CustomDimension();
        fundClass.setIri("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/dimension/fund");
        fundClass.setLabel("European Structural and Investment Fund");
        fundClass.setComment("Different funds of the European Union for structural development and investment.");
        fundClass.setSubPropertyOf(Obeu.Classification.CLASSIFICATION.toString());
        fundClass.setCodeList("http://data.openbudgets.eu/resource/ESIF-2014-2020/codelist/eu-funds/");
        fundClass.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        addCustomDimension(fundClass);

        CustomDimension functionalClass = new CustomDimension();
        functionalClass.setIri("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/dimension/functionalClassification");
        functionalClass.setLabel("Intention of expenditure");
        functionalClass.setComment("Classifies expenditures by the purpose of the funded money.");
        functionalClass.setSubPropertyOf(Obeu.Classification.FUNCTIONAL_CLASSIFICATION.toString());
        functionalClass.setCodeList("http://data.openbudgets.eu/resource/ESIF-2014-2020/codelist/function/");
        functionalClass.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        addCustomDimension(functionalClass);

        CustomDimension memberStatesDimension = new CustomDimension();
        memberStatesDimension.setIri("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/dimension/memberStates");
        memberStatesDimension.setLabel("Describes the European Union member state.");
        memberStatesDimension.setSubPropertyOf(Obeu.Classification.ADMINISTRATIVE_CLASSIFICATION.toString());
        memberStatesDimension.setCodeList("http://data.openbudgets.eu/resource/ESIF-2014-2020/codelist/memberStates");
        memberStatesDimension.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        addCustomDimension(memberStatesDimension);

        CustomDimension priorityDimension = new CustomDimension();
        priorityDimension.setIri("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/dimension/priority");
        priorityDimension.setLabel("Priority");
        priorityDimension.setSubPropertyOf(Obeu.Classification.CLASSIFICATION.toString());
        priorityDimension.setRangeProperty("http://data.openbudgets.eu/ontology/european-funds/Priority");
        priorityDimension.setCodeList("http://data.openbudgets.eu/resource/ESIF-2014-2020/codelist/priority");
        priorityDimension.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        addCustomDimension(priorityDimension);

        CustomDimension programDimension = new CustomDimension();
        programDimension.setIri("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/dimension/program");
        programDimension.setLabel("Program");
        programDimension.setComment("EU ESIF Program.");
        programDimension.setSubPropertyOf(Obeu.Classification.CLASSIFICATION.toString());
        programDimension.setRangeProperty("http://data.openbudgets.eu/ontology/european-funds/Program");
        programDimension.setCodeList("http://data.openbudgets.eu/resource/ESIF-2014-2020/codelist/program");
        programDimension.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        addCustomDimension(programDimension);

        CustomMeasure euAmountMeasure = new CustomMeasure();
        euAmountMeasure.setIri("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/measure/amountEU");
        euAmountMeasure.setLabel("Amount EU");
        euAmountMeasure.setComment("Amount funded by the resp. EU fund.");
        euAmountMeasure.setSubPropertyOf(Obeu.Measure.AMOUNT.toString());
        euAmountMeasure.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        addCustomMeasure(euAmountMeasure);

        CustomMeasure nationalAmountMeasure = new CustomMeasure();
        nationalAmountMeasure.setIri("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/measure/amountNational");
        nationalAmountMeasure.setLabel("Amount National");
        nationalAmountMeasure.setComment("National share for the resp. EU fund and project.");
        nationalAmountMeasure.setSubPropertyOf(Obeu.Measure.AMOUNT.toString());
        nationalAmountMeasure.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        addCustomMeasure(nationalAmountMeasure);

        CustomMeasure amountTotalMeasure = new CustomMeasure();
        amountTotalMeasure.setIri("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/measure/amountTotal");
        amountTotalMeasure.setLabel("Amount Total");
        amountTotalMeasure.setComment("Sum of the amount funded by the resp. EU fund and the national part.");
        amountTotalMeasure.setSubPropertyOf(Obeu.Measure.AMOUNT.toString());
        amountTotalMeasure.setDefinedBy("http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020");
        addCustomMeasure(amountTotalMeasure);

        Mapping memberStateMapping = new Mapping();
        memberStateMapping.setName("MS");
        memberStateMapping.setComponentProperty(memberStatesDimension.getIri());
        memberStateMapping.setUri(memberStatesDimension.getCodeList());
        memberStateMapping.setValuePrefix(memberStatesDimension.getCodeList());
        addMapping(memberStateMapping);

        Mapping programMapping = new Mapping();
        programMapping.setName("CCI");
        programMapping.setComponentProperty(programDimension.getIri());
        programMapping.setUri(programDimension.getCodeList());
        programMapping.setValuePrefix(programDimension.getCodeList());
        addMapping(programMapping);

        Mapping fundMapping = new Mapping();
        fundMapping.setName("Fund");
        fundMapping.setComponentProperty(fundClass.getIri());
        fundMapping.setUri(fundClass.getCodeList());
        fundMapping.setValuePrefix(fundClass.getCodeList());
        addMapping(fundMapping);

        Mapping toMapping = new Mapping();
        toMapping.setName("TO");
        toMapping.setComponentProperty(functionalClass.getIri());
        toMapping.setUri(functionalClass.getCodeList());
        toMapping.setValuePrefix(functionalClass.getCodeList());
        addMapping(toMapping);

        Mapping priorityMapping = new Mapping();
        priorityMapping.setName("Priority");
        priorityMapping.setComponentProperty(priorityDimension.getIri());
        priorityMapping.setUri(priorityDimension.getCodeList());
        priorityMapping.setValuePrefix(priorityDimension.getCodeList());
        addMapping(priorityMapping);

        Mapping euAmountMapping = new Mapping();
        euAmountMapping.setName("EU Amount");
        euAmountMapping.setComponentProperty(euAmountMeasure.getIri());
        euAmountMapping.setUri(euAmountMeasure.getIri());
        euAmountMapping.setIsAmount(true);
        addMapping(euAmountMapping);

        Mapping nationalAmountMapping = new Mapping();
        nationalAmountMapping.setName("National Amount");
        nationalAmountMapping.setComponentProperty(nationalAmountMeasure.getIri());
        nationalAmountMapping.setUri(nationalAmountMeasure.getIri());
        nationalAmountMapping.setIsAmount(true);
        addMapping(nationalAmountMapping);

        Mapping totalAmountMapping = new Mapping();
        totalAmountMapping.setName("Total Amount");
        totalAmountMapping.setComponentProperty(amountTotalMeasure.getIri());
        totalAmountMapping.setUri(amountTotalMeasure.getIri());
        totalAmountMapping.setIsAmount(true);
        addMapping(totalAmountMapping);

    }


}
