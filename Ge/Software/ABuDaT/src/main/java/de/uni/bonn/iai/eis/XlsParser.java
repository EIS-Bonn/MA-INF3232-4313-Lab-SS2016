package de.uni.bonn.iai.eis;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XlsParser {
    private static final Logger LOG = LoggerFactory.getLogger(XlsParser.class);

    private boolean stripHeader = true;

    public List<String> parse(InputStream inputStream, int numberOfStartLinesToIgnore) throws IOException, InvalidFormatException {
        final Workbook wb = WorkbookFactory.create(inputStream);

        return findColumnNames(wb, 0, numberOfStartLinesToIgnore);
    }

    public List<String> findColumnNames(Workbook workbook, Integer sheetIndex, int numberOfStartLinesToIgnore) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        List<String> columnNames = new ArrayList<>();

        if (numberOfStartLinesToIgnore > sheet.getLastRowNum()) {
            return null;
        }

        int startRow = numberOfStartLinesToIgnore;

        final Row row = sheet.getRow(startRow++);
        if (row == null) {
            throw new RuntimeException("Header row is null!");
        }

        final int columnStart = row.getFirstCellNum();
        final int columnEnd = row.getLastCellNum();


        for (int columnIndex = columnStart; columnIndex < columnEnd; columnIndex++) {
            final Cell cell = row.getCell(columnIndex);
            if (cell == null) {
                columnNames.add(null);
            } else {
                String name = this.getCellValue(cell);

                if (name != null) {
                    name = name.replaceAll("\\n", " ");
                }

                columnNames.add(name);
            }
        }

        if (stripHeader) {
            int initialSize = columnNames.size();

            // Remove trailing null values.
            for (int i = columnNames.size() - 1; i > 0; --i) {
                if (columnNames.get(i) == null) {
                    columnNames.remove(i);
                } else {
                    // Non null value.
                    break;
                }
            }
            LOG.info("Removal of nulls changed header size from {} to {}", initialSize, columnNames.size());
        } else {
            LOG.debug("Header size {}", columnNames.size());
        }

        return columnNames;
    }

    private String getCellValue(Cell cell) throws IllegalArgumentException {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            default:
                return cell.getStringCellValue();
        }
    }
}
