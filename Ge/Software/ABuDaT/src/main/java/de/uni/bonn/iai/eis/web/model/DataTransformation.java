package de.uni.bonn.iai.eis.web.model;

import com.linkedpipes.plugin.extractor.httpget.HttpGetConfiguration;
import com.linkedpipes.plugin.loader.local.LoaderLocalConfiguration;
import com.linkedpipes.plugin.transformer.rdftofile.RdfToFileConfiguration;
import com.linkedpipes.plugin.transformer.sparql.update.SparqlUpdateConfiguration;
import com.linkedpipes.plugin.transformer.tabularuv.TabularConfig_V2;
import com.linkedpipes.plugin.transformer.tabularuv.parser.ParserType;
import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.etl.Pipeline;
import de.uni.bonn.iai.eis.etl.PipelineBuilder;
import de.uni.bonn.iai.eis.etl.component.*;
import de.uni.bonn.iai.eis.rdf.obeu.BudgetPhase;
import de.uni.bonn.iai.eis.rdf.obeu.OperationCharacter;
import de.uni.bonn.iai.eis.web.DsdGenerator;
import de.uni.bonn.iai.eis.web.model.mapping.Mapping;
import org.openrdf.rio.RDFFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
public class DataTransformation extends AbstractTransformation {
    @Id
    private String id = UUID.randomUUID().toString();

    private String name = "Data transformation " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private String source;

    //// TODO: 04.10.16 validate
    private String charset;

    @NotNull
    private String datasetUrl;

    @Enumerated(EnumType.STRING)
    private BudgetPhase budgetPhase;

    @Enumerated(EnumType.STRING)
    private OperationCharacter operationCharacter;

    @NotNull
    private String dsdUrl;

    private String dsdLabel;

    @NotNull
    private String budgetaryUnit;

    private String fiscalPeriod;

    private String currency;

    private int taxesIncluded = -1;

    @ElementCollection
    private List<Mapping> mappings = new LinkedList<>();

    @ElementCollection
    private List<SparqlUpdate> sparqlUpdates = new LinkedList<>();

    @ElementCollection
    private List<CustomDimension> customDimensions = new LinkedList<>();

    @ElementCollection
    private List<CustomMeasure> customMeasures = new LinkedList<>();

    private String delimiterChar;
    private String tableType;

    private boolean hasHeader;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Mapping> getMappings() {
        //Set the column mappings valuePrefix to be the link to the codelist of the given custom dimension.
        //If the component property is a custom dimension...
        if (!customDimensions.isEmpty()) {
            //build a map of dimension iri's to code list iri's
            Map<String, String> dimensionsMap = new HashMap<>();
            for (CustomDimension customDimension : customDimensions) {
                if (customDimension.getIri() == null || customDimension.getIri().isEmpty()) continue;
                if (customDimension.getCodeList() == null || customDimension.getCodeList().isEmpty()) continue;

                dimensionsMap.put(customDimension.getIri(), customDimension.getCodeList());
            }

            for (Mapping mapping : mappings) {
                //if the mapping has no value prefix yet
                if (mapping.getValuePrefix() != null && !mapping.getValuePrefix().isEmpty()) {
                    continue;
                }

                String componentProperty = mapping.getComponentProperty();
                if (dimensionsMap.containsKey(componentProperty)) {
                    String codeList = dimensionsMap.get(componentProperty);
                    if (!codeList.endsWith("/")) {
                        codeList += "/";
                    }
                    mapping.setValuePrefix(codeList);
                }
            }
        }

        return mappings;
    }

    public void setMappings(List<Mapping> mappings) {
        this.mappings = mappings;
    }

    public void removeMapping(int index) {
        mappings.remove(index);
    }

    public void addSparqlUpdate(SparqlUpdate sparqlUpdate) {
        sparqlUpdates.add(sparqlUpdate);
    }

    public void removeSparqlUpdate(int index) {
        sparqlUpdates.remove(index);
    }

    public void setSparqlUpdates(List<SparqlUpdate> sparqlUpdates) {
        this.sparqlUpdates = sparqlUpdates;
    }

    public List<SparqlUpdate> getSparqlUpdates() {
        return sparqlUpdates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addMapping(Mapping mapping) {
        mappings.add(mapping);
    }

    public String getDatasetUrl() {
        return datasetUrl;
    }

    public void setDatasetUrl(String datasetUrl) {
        this.datasetUrl = datasetUrl;
    }

    public OperationCharacter getOperationCharacter() {
        return operationCharacter;
    }

    public void setOperationCharacter(OperationCharacter operationCharacter) {
        this.operationCharacter = operationCharacter;
    }

    public BudgetPhase getBudgetPhase() {
        return budgetPhase;
    }

    public void setBudgetPhase(BudgetPhase budgetPhase) {
        this.budgetPhase = budgetPhase;
    }

    public String getDsdUrl() {
        return dsdUrl;
    }

    public void setDsdUrl(String dsdUrl) {
        this.dsdUrl = dsdUrl;
    }

    public String getDsdLabel() {
        return dsdLabel;
    }

    public void setDsdLabel(String dsdLabel) {
        this.dsdLabel = dsdLabel;
    }

    public String getBudgetaryUnit() {
        return budgetaryUnit;
    }

    public void setBudgetaryUnit(String budgetaryUnit) {
        this.budgetaryUnit = budgetaryUnit;
    }

    public String getFiscalPeriod() {
        return fiscalPeriod;
    }

    public void setFiscalPeriod(String fiscalPeriod) {
        this.fiscalPeriod = fiscalPeriod;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<CustomDimension> getCustomDimensions() {
        return customDimensions;
    }

    public void setCustomDimensions(List<CustomDimension> customDimensions) {
        this.customDimensions = customDimensions;
    }

    public void addCustomDimension(CustomDimension customDimension) {
        this.customDimensions.add(customDimension);
    }

    public List<CustomMeasure> getCustomMeasures() {
        return customMeasures;
    }

    public void setCustomMeasures(List<CustomMeasure> customMeasures) {
        this.customMeasures = customMeasures;
    }

    public void addCustomMeasure(CustomMeasure customMeasure) {
        customMeasures.add(customMeasure);
    }

    public String getDelimiterChar() {
        return delimiterChar;
    }

    public void setDelimiterChar(String delimiterChar) {
        this.delimiterChar = delimiterChar;
    }

    public void removeCustomDimension(int index) {
        customDimensions.remove(index);
    }

    public void removeCustomMeasure(int index) {
        customMeasures.remove(index);
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getTableType() {
        return tableType;
    }

    public int getTaxesIncluded() {
        return taxesIncluded;
    }

    public void setTaxesIncluded(int taxesIncluded) {
        this.taxesIncluded = taxesIncluded;
    }

    public boolean getHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    @Override
    public Pipeline createPipeline(String outputFileName, String outputFilePath, ABuDaTConfiguration aBuDaTConfiguration) {
        PipelineBuilder pipelineBuilder = new PipelineBuilder(aBuDaTConfiguration).withAutoConnect();

        HttpGetConfiguration httpGetConfiguration = new HttpGetConfiguration();
        httpGetConfiguration.setFileName("data.csv");
        httpGetConfiguration.setUri(source);

        HttpGet httpGet = new HttpGet(httpGetConfiguration);
        httpGet.setPrefLabel("HttpGet");
        httpGet.setDescription("download the data to transform");
        httpGet.setX(300);
        httpGet.setY(300);

        pipelineBuilder.withComponent(httpGet);

        //----------

        TabularConfig_V2 tabularConfig_v2 = new TabularConfig_V2();
        String baseURI = dsdUrl + "/observation/";
        tabularConfig_v2.setBaseURI(baseURI);
        tabularConfig_v2.setEncoding(charset);
        tabularConfig_v2.setIgnoreMissingColumn(true);
        tabularConfig_v2.setIgnoreBlankCells(true);
        tabularConfig_v2.setGenerateRowTriple(false);
        tabularConfig_v2.setGenerateNew(false);
        tabularConfig_v2.setRowsClass("http://purl.org/linked-data/cube#Observation");
        tabularConfig_v2.setDelimiterChar(delimiterChar);
        tabularConfig_v2.setHasHeader(hasHeader);

        ParserType parserType = ParserType.CSV;
        if (tableType.equals("xls")) {
            parserType = ParserType.XLS;
        }
        tabularConfig_v2.setTableType(parserType);

        List<TabularConfig_V2.ColumnInfo_V1> colums = new ArrayList<>();

        for (Mapping mapping : mappings) {
            String name = mapping.getName();

            TabularConfig_V2.ColumnType columnType = TabularConfig_V2.ColumnType.String;
            if (mapping.getIsAmount()) {
                columnType = TabularConfig_V2.ColumnType.Decimal;
            }
//            TabularConfig_V2.ColumnType columnType = TabularConfig_V2.ColumnType.valueOf(mapping.getType());

            String uri = mapping.getUri();

            TabularConfig_V2.ColumnInfo_V1 columnInfo = createColumnInfo(name, columnType, uri);
            colums.add(columnInfo);
        }

        tabularConfig_v2.setColumnsInfo(colums);

        TabularUv tabularUv = new TabularUv(tabularConfig_v2);
        tabularUv.setPrefLabel("Mapping");
        tabularUv.setDescription("mapping to obeu data model");
        tabularUv.setX(600);
        tabularUv.setY(300);

        pipelineBuilder.withComponent(tabularUv);

        //--------------

        GraphMerger graphMerger1 = new GraphMerger();
        graphMerger1.setPrefLabel("GraphMerger");
        graphMerger1.setX(900);
        graphMerger1.setY(300);

        pipelineBuilder.withComponent(graphMerger1);


        //--------------

        SparqlUpdateConfiguration decimalMarkUpdateConfig = new SparqlUpdateConfiguration();
        String query =
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                        "\n" +
                        "DELETE { ?s ?p ?amount . }\n" +
                        "INSERT { ?s ?p ?amountFixed . }\n" +
                        "WHERE {\n" +
                        "  ?s ?p ?amount .\n" +
                        "  FILTER(datatype(?amount) = xsd:decimal)\n" +
                        "  FILTER(REGEX(str(?amount), \"^[0-9.]+,[0-9]{1,2}$\")) .\n" +
                        "  BIND(REPLACE(str(?amount), \"\\\\.\", \"\") AS ?tmp1)\n" +
                        "  BIND(REPLACE(str(?tmp1), \",\", \".\") AS ?tmp)\n" +
                        "  BIND(STRDT(?tmp, xsd:decimal) AS ?amountFixed)\n" +
                        "}";

        decimalMarkUpdateConfig.setQuery(query);

        de.uni.bonn.iai.eis.etl.component.SparqlUpdate decimalMarkUpdate = new de.uni.bonn.iai.eis.etl.component.SparqlUpdate(decimalMarkUpdateConfig);
        decimalMarkUpdate.setPrefLabel("SparqlUpdate");
        decimalMarkUpdate.setDescription("fix amounts decimal mark");
        decimalMarkUpdate.setX(350);
        decimalMarkUpdate.setY(500);

        pipelineBuilder.withComponent(decimalMarkUpdate);


        //--------------


        if (!customDimensions.isEmpty()) {

            String updateQuery = "PREFIX qb: <http://purl.org/linked-data/cube#>\n\n";

            for (CustomDimension customDimension : customDimensions) {
                String iri = customDimension.getIri();

                if (iri.endsWith("/")) {
                    iri = iri.substring(0, iri.length() - 1);
                }

                updateQuery += "INSERT { ?subject <" + iri + "> \"n.a.\" }\n" +
                        "WHERE {\n" +
                        "  ?subject a qb:Observation .\n" +
                        "  FILTER NOT EXISTS { ?subject <" + iri + "> _:a }\n" +
                        "};\n";
            }

            SparqlUpdateConfiguration missingDimensionUpdateConfig = new SparqlUpdateConfiguration();
            missingDimensionUpdateConfig.setQuery(updateQuery);

            de.uni.bonn.iai.eis.etl.component.SparqlUpdate missingDimenisonUpdate =
                    new de.uni.bonn.iai.eis.etl.component.SparqlUpdate(missingDimensionUpdateConfig);

            missingDimenisonUpdate.setPrefLabel("SparqlUpdate");
            missingDimenisonUpdate.setDescription("update missing dimensions");
            missingDimenisonUpdate.setX(350);
            missingDimenisonUpdate.setY(600);

            pipelineBuilder.withComponent(missingDimenisonUpdate);
        }


        //--------------

        DsdGenerator dsdGenerator = new DsdGenerator();

        //dsd generated by wizard
        String dsd = dsdGenerator.generateRDF(this, RDFFormat.TURTLE);
        SparqlUpdateConfiguration dsdSparqlUpdateConfiguration = new SparqlUpdateConfiguration();
        dsdSparqlUpdateConfiguration.setQuery("INSERT DATA {\n" + dsd + "}");

        de.uni.bonn.iai.eis.etl.component.SparqlUpdate sparqlUpdateForDsd =
                new de.uni.bonn.iai.eis.etl.component.SparqlUpdate(dsdSparqlUpdateConfiguration);
        sparqlUpdateForDsd.setPrefLabel("SparqlUpdate");
        sparqlUpdateForDsd.setDescription("insert dsd");
        sparqlUpdateForDsd.setX(350);
        sparqlUpdateForDsd.setY(700);

        pipelineBuilder.withComponent(sparqlUpdateForDsd);

        //--------------

        String data = dsdGenerator.generateDsdData(this, RDFFormat.TURTLE);

        SparqlUpdateConfiguration dataSetDatasparqlUpdateConfiguration = new SparqlUpdateConfiguration();
        dataSetDatasparqlUpdateConfiguration.setQuery("INSERT DATA {\n" + data + "}");

        de.uni.bonn.iai.eis.etl.component.SparqlUpdate sparqlUpdateForDsdData =
                new de.uni.bonn.iai.eis.etl.component.SparqlUpdate(dataSetDatasparqlUpdateConfiguration);
        sparqlUpdateForDsdData.setPrefLabel("SparqlUpdate");
        sparqlUpdateForDsdData.setDescription("insert dsd data");
        sparqlUpdateForDsdData.setX(350);
        sparqlUpdateForDsdData.setY(800);

        pipelineBuilder.withComponent(sparqlUpdateForDsdData);

        //--------------

        SparqlUpdateConfiguration observationDataSetUpdateConfig = new SparqlUpdateConfiguration();
        observationDataSetUpdateConfig.setQuery(
                "PREFIX qb: <http://purl.org/linked-data/cube#>\n" +
                        "\n" +
                        "INSERT { ?obs qb:dataSet ?ds }\n" +
                        "WHERE {\n" +
                        "  ?obs a qb:Observation .\n" +
                        "  ?ds a qb:DataSet .\n" +
                        "}");
        de.uni.bonn.iai.eis.etl.component.SparqlUpdate obsDSUpdate =
                new de.uni.bonn.iai.eis.etl.component.SparqlUpdate(observationDataSetUpdateConfig);

        obsDSUpdate.setPrefLabel("SpaqrlUpdate");
        obsDSUpdate.setDescription("insert qb:Dimension triples");
        obsDSUpdate.setX(800);
        obsDSUpdate.setY(500);

        pipelineBuilder.withComponent(obsDSUpdate);


        //------------------ Custom dimensions ------------------

        if (!customDimensions.isEmpty()) {
            StringBuilder customDimensionUpdateStringBuilder = new StringBuilder();
            for (CustomDimension customDimension : customDimensions) {
                customDimensionUpdateStringBuilder.append(customDimension.generateRdf(RDFFormat.TURTLE)).append("\n");
            }

            SparqlUpdateConfiguration config = new SparqlUpdateConfiguration();
            config.setQuery("INSERT DATA {\n" + customDimensionUpdateStringBuilder.toString() + "}");

            de.uni.bonn.iai.eis.etl.component.SparqlUpdate customDimensionInsert =
                    new de.uni.bonn.iai.eis.etl.component.SparqlUpdate(config);


            customDimensionInsert.setPrefLabel("SparqlUpdate");
            customDimensionInsert.setDescription("insert custom dimensions");
            customDimensionInsert.setX(800);
            customDimensionInsert.setY(600);

            pipelineBuilder.withComponent(customDimensionInsert);
        }

        //---------------- Custom measures ---------------------

        if (!customMeasures.isEmpty()) {
            StringBuilder customMeasuresRdfStringBuilder = new StringBuilder();

            for (CustomMeasure customMeasure : customMeasures) {
                customMeasuresRdfStringBuilder.append(customMeasure.generateRdf(RDFFormat.TURTLE)).append("\n");
            }

            SparqlUpdateConfiguration config = new SparqlUpdateConfiguration();
            config.setQuery("INSERT DATA {\n" + customMeasuresRdfStringBuilder.toString() + "}");

            de.uni.bonn.iai.eis.etl.component.SparqlUpdate customMeasureInsert =
                    new de.uni.bonn.iai.eis.etl.component.SparqlUpdate(config);

            customMeasureInsert.setPrefLabel("SparqlUpdate");
            customMeasureInsert.setDescription("insert custom measures");
            customMeasureInsert.setX(800);
            customMeasureInsert.setY(700);

            pipelineBuilder.withComponent(customMeasureInsert);
        }

        //----------------


        //create a sparql update to prepend the given value prefix
        if (!mappings.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();

            for (Mapping mapping : mappings) {
                if (mapping.getIsAmount()) {
                    continue;
                }
                String uri = mapping.getUri();
                String valueToPrefix = mapping.getValuePrefix();

                if (uri != null && !uri.isEmpty()) {
                    if (valueToPrefix != null && !valueToPrefix.endsWith("/")) {
                        valueToPrefix += "/";
                    }

                    stringBuilder.append("DELETE { ?name <" + uri + "> ?value }\n");
                    stringBuilder.append("INSERT { ?name <" + uri + "> ?valueWithPrefix }\n");
                    stringBuilder.append(
                            "WHERE {\n" +
                                    "        ?name <" + uri + "> ?value .\n" +
                                    "        BIND(REPLACE(?value, \" \", \"_\") AS ?tmpValue)\n" +
                                    "        BIND(URI(CONCAT(\"" + valueToPrefix + "\",?tmpValue)) AS ?valueWithPrefix)\n" +
                                    "};\n");
                }
            }

            SparqlUpdateConfiguration sparqlUpdateForValuePrefixConfig = new SparqlUpdateConfiguration();
            sparqlUpdateForValuePrefixConfig.setQuery(stringBuilder.toString());
            de.uni.bonn.iai.eis.etl.component.SparqlUpdate sparqlUpdateForValuePrefix =
                    new de.uni.bonn.iai.eis.etl.component.SparqlUpdate(sparqlUpdateForValuePrefixConfig);
            sparqlUpdateForValuePrefix.setPrefLabel("SparqlUpdate");
            sparqlUpdateForValuePrefix.setDescription("add uri prefixes to values");
            sparqlUpdateForValuePrefix.setX(800);
            sparqlUpdateForValuePrefix.setY(800);

            pipelineBuilder.withComponent(sparqlUpdateForValuePrefix);
        }


        int x = 800;
        int y = 900;
        for (de.uni.bonn.iai.eis.web.model.SparqlUpdate sparqlUpdate : sparqlUpdates) {
            SparqlUpdateConfiguration sparqlUpdateConfiguration2 = new SparqlUpdateConfiguration();
            sparqlUpdateConfiguration2.setQuery(sparqlUpdate.getQuery());

            de.uni.bonn.iai.eis.etl.component.SparqlUpdate sparqlUpdateComponent =
                    new de.uni.bonn.iai.eis.etl.component.SparqlUpdate(sparqlUpdateConfiguration2);
            sparqlUpdateComponent.setPrefLabel("SparqlUpdate");
            sparqlUpdateComponent.setDescription("user defined sparql update");
            sparqlUpdateComponent.setX(x);
            sparqlUpdateComponent.setY(y);

            pipelineBuilder.withComponent(sparqlUpdateComponent);

            y = y + 100;
        }

        //---------------

//        GraphUnion graphUnion = new GraphUnion();
//        graphUnion.setPrefLabel("Union");
//        graphUnion.setX(1200);
//        graphUnion.setY(500);
//
//        pipelineBuilder.withComponent(graphUnion);

        //------------

        RdfToFileConfiguration rdfToFileConfiguration = new RdfToFileConfiguration();
        rdfToFileConfiguration.setFileName(outputFileName);
        rdfToFileConfiguration.setFileType("text/turtle");

        RdfToFile rdfToFile = new RdfToFile(rdfToFileConfiguration);
        rdfToFile.setPrefLabel("RdfToFile");
        rdfToFile.setX(1200);
        rdfToFile.setY(500);

        pipelineBuilder.withComponent(rdfToFile);

        //------------

        LoaderLocalConfiguration loaderLocalConfiguration = new LoaderLocalConfiguration();
        loaderLocalConfiguration.setPath(outputFilePath);

        FilesToLocal filesToLocal = new FilesToLocal(loaderLocalConfiguration);
        filesToLocal.setPrefLabel("FilesToLocal");
        filesToLocal.setDescription("copy result to local file system");
        filesToLocal.setX(1200);
        filesToLocal.setY(600);

        pipelineBuilder.withComponent(filesToLocal);

        //------------

        String pipelineId = id;

        return pipelineBuilder.withLabel(name).build(pipelineId);
    }
}
