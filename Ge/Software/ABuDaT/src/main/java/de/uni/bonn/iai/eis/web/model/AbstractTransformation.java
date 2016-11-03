package de.uni.bonn.iai.eis.web.model;

import com.linkedpipes.plugin.transformer.tabularuv.TabularConfig_V2;
import de.uni.bonn.iai.eis.ABuDaTConfiguration;
import de.uni.bonn.iai.eis.etl.Pipeline;

public abstract class AbstractTransformation {
    public abstract String getId();

    public abstract Pipeline createPipeline(String outputFileName, String outputFilePath, ABuDaTConfiguration aBuDaTConfiguration);

    //TODO move this somewhere else
    protected TabularConfig_V2.ColumnInfo_V1 createColumnInfo(String name, TabularConfig_V2.ColumnType columnType, String uri) {
        TabularConfig_V2.ColumnInfo_V1 ms_name = new TabularConfig_V2.ColumnInfo_V1();
        ms_name.setName(name);
        ms_name.setType(columnType);
        ms_name.setURI(uri);
        return ms_name;
    }
}
