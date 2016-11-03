package de.uni.bonn.iai.eis.web.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class CodelistSlice {
    @Id
    private String id  = UUID.randomUUID().toString();

    private int startRow;
    private int endRow;
    private String keyColumn;
    private String labelColumn;
    private boolean doSlice = false;

    @ManyToOne
    private CodelistTransformation codelistTransformation;

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CodelistTransformation getCodelistTransformation() {
        return codelistTransformation;
    }

    public void setCodelistTransformation(CodelistTransformation codelistTransformation) {
        this.codelistTransformation = codelistTransformation;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public String getLabelColumn() {
        return labelColumn;
    }

    public void setLabelColumn(String labelColumn) {
        this.labelColumn = labelColumn;
    }

    public boolean getDoSlice() {
        return doSlice;
    }

    public void setDoSlice(boolean doSlice) {
        this.doSlice = doSlice;
    }
}
