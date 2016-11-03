package de.uni.bonn.iai.eis.web;

import java.util.LinkedList;

public class Input {
    private String url;
    private LinkedList<String> columns = new LinkedList<>();
    private String delimeter;
    private String charset;
    private String tableType;

    public Input(String url, String[] columns, String delimeter, String charset, String type) {
        this.url = url;
        this.delimeter = delimeter;
        this.charset = charset;
        this.tableType = type;

        if (columns != null && columns.length != 0) {
            for (String column : columns) {
                this.columns.addLast(column);
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public LinkedList<String> getColumns() {
        return columns;
    }

    public String getDelimeter() {
        return delimeter;
    }

    public String getCharset() {
        return charset;
    }

    public String getTableType() {
        return tableType;
    }
}