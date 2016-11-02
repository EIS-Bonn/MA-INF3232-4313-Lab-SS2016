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
import de.uni.bonn.iai.eis.etl.component.SparqlUpdate;
import org.openrdf.model.vocabulary.SKOS;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

//TODO pull up fields common with DataTransformation to AbstractTransformation

@Entity
public class CodelistTransformation extends AbstractTransformation {
    @Id
    private String id = UUID.randomUUID().toString();

    private String name = "Codelist transformation " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private String source;

    // TODO: 04.10.16 validate
    private String charset;

    private String delimiterChar;

    @ElementCollection
    private List<String> columns;

    private String uri;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "codelist_transformation_id")
    private List<CodelistSlice> slices = new LinkedList<>();
    private String tableType;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getDelimiterChar() {
        return delimiterChar;
    }

    public void setDelimiterChar(String delimiterChar) {
        this.delimiterChar = delimiterChar;
    }

    public List<CodelistSlice> getSlices() {
        return slices;
    }

    public void setSlices(List<CodelistSlice> slices) {
        this.slices = slices;
    }

    public void addSlice(CodelistSlice slice) {
        slices.add(slice);
    }

    public void removeSlice(int index) {
        slices.remove(index);
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
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

    @Override
    public Pipeline createPipeline(String outputFileName, String outputFilePath, ABuDaTConfiguration aBuDaTConfiguration) {
        PipelineBuilder pipelineBuilder = new PipelineBuilder(aBuDaTConfiguration);

        HttpGetConfiguration httpGetConfiguration = new HttpGetConfiguration();
        httpGetConfiguration.setFileName("data.csv");
        httpGetConfiguration.setUri(source);

        HttpGet httpGet = new HttpGet(httpGetConfiguration);
        httpGet.setPrefLabel("HttpGet");
        httpGet.setDescription("download the data to transform");
        httpGet.setX(300);
        httpGet.setY(300);

        pipelineBuilder.withComponent(httpGet);

        //-------------

        GraphUnion graphUnion = new GraphUnion();
        graphUnion.setPrefLabel("Union");
        graphUnion.setX(300);
        graphUnion.setY(700);

        pipelineBuilder.withComponent(graphUnion);

        //---------------

        int x = 100;
        for (CodelistSlice codelistSlice : slices) {
            addSliceToPipeline(codelistSlice, httpGet, graphUnion, pipelineBuilder,
                    delimiterChar, uri, charset, x);
            x += 250;
        }

        //------------

        SparqlUpdateConfiguration addPrefixesConfig = new SparqlUpdateConfiguration();

        String query = "INSERT DATA {\n" +
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
                "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
                "}";


        addPrefixesConfig.setQuery(query);
        SparqlUpdate sparqlUpdateAddPrefixes = new SparqlUpdate(addPrefixesConfig);
        sparqlUpdateAddPrefixes.setPrefLabel("SparqlUpdate");
        sparqlUpdateAddPrefixes.setDescription("add prefixes");
        sparqlUpdateAddPrefixes.setX(300);
        sparqlUpdateAddPrefixes.setY(800);
        pipelineBuilder.withComponent(sparqlUpdateAddPrefixes).withConnection(new ExtendedConnection(graphUnion, sparqlUpdateAddPrefixes));


        //------------

        RdfToFileConfiguration rdfToFileConfiguration = new RdfToFileConfiguration();
        rdfToFileConfiguration.setFileName(outputFileName);
        rdfToFileConfiguration.setFileType("text/turtle");

        RdfToFile rdfToFile = new RdfToFile(rdfToFileConfiguration);
        rdfToFile.setPrefLabel("RdfToFile");
        rdfToFile.setX(300);
        rdfToFile.setY(900);

        pipelineBuilder.withComponent(rdfToFile).withConnection(new ExtendedConnection(sparqlUpdateAddPrefixes, rdfToFile));

        //------------

        LoaderLocalConfiguration loaderLocalConfiguration = new LoaderLocalConfiguration();
        loaderLocalConfiguration.setPath(outputFilePath);

        FilesToLocal filesToLocal = new FilesToLocal(loaderLocalConfiguration);
        filesToLocal.setPrefLabel("FilesToLocal");
        filesToLocal.setDescription("copy result to local file system");
        filesToLocal.setX(300);
        filesToLocal.setY(1000);

        pipelineBuilder.withComponent(filesToLocal).withConnection(new ExtendedConnection(rdfToFile, filesToLocal));

        //------------

        String pipelineId = id;

        return pipelineBuilder.withLabel(name).build(pipelineId);
    }

    private void addSliceToPipeline(CodelistSlice codelistSlice, SimpleComponent before, SimpleComponent after,
                                    PipelineBuilder pipelineBuilder, String delimiterChar, String uri, String charset, int x) {

        TabularConfig_V2 tabularConfig = new TabularConfig_V2();

        if (!uri.endsWith("/")) {
            uri = uri + "/";
        }

        tabularConfig.setBaseURI(uri);
        tabularConfig.setEncoding(charset);
        tabularConfig.setIgnoreMissingColumn(true);
        tabularConfig.setIgnoreBlankCells(true);
        tabularConfig.setGenerateRowTriple(false);
        tabularConfig.setGenerateNew(false);
        tabularConfig.setRowsClass("http://www.w3.org/2004/02/skos/core#Concept");
        tabularConfig.setDelimiterChar(delimiterChar);
        tabularConfig.setHasHeader(false);

        ParserType parserType = ParserType.CSV;
        if (tableType.equals("xls")){
            parserType = ParserType.XLS;
        }
        tabularConfig.setTableType(parserType);


        int keyColumnIndex = columnIndexByName(codelistSlice.getKeyColumn());
        tabularConfig.setKeyColumn("col" + keyColumnIndex);

        if (codelistSlice.getDoSlice()) {
            tabularConfig.setLinesToIgnore(codelistSlice.getStartRow() - 1);
            tabularConfig.setRowsLimit(codelistSlice.getEndRow() - (codelistSlice.getStartRow() - 1));
        } else {
            //FIXME only if the data has a header line to ignore
            tabularConfig.setLinesToIgnore(1);
        }

        List<TabularConfig_V2.ColumnInfo_V1> colums = new ArrayList<>();

        TabularConfig_V2.ColumnInfo_V1 keyColumn = createColumnInfo("col" + keyColumnIndex, TabularConfig_V2.ColumnType.String, SKOS.NOTATION.toString());
        colums.add(keyColumn);

        int labelColumnIndex = columnIndexByName(codelistSlice.getLabelColumn());
        TabularConfig_V2.ColumnInfo_V1 labelColumn = createColumnInfo("col" + labelColumnIndex, TabularConfig_V2.ColumnType.String, SKOS.PREF_LABEL.toString());
        colums.add(labelColumn);

        tabularConfig.setColumnsInfo(colums);

        TabularUv tabularUv = new TabularUv(tabularConfig);
        tabularUv.setPrefLabel("Mapping");
        tabularUv.setDescription("mapping to obeu data model");
        tabularUv.setX(x);
        tabularUv.setY(450);

        //-----------------

        GraphMerger graphMerger = new GraphMerger();
        graphMerger.setPrefLabel("GraphMerger");
        graphMerger.setX(x);
        graphMerger.setY(600);

        pipelineBuilder
                .withComponent(tabularUv)
                .withComponent(graphMerger)
                .withConnection(new ExtendedConnection(before, tabularUv))
                .withConnection(new ExtendedConnection(tabularUv, graphMerger))
                .withConnection(new ExtendedConnection(graphMerger, after));
    }

    private int columnIndexByName(String name) {
        int index = -1;

        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).equals(name)) {
                index = i + 1;
                break;
            }
        }

        return index;
    }


}
