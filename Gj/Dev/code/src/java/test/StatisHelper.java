package test;

/**
 * **************************************************************
 */
/* Copyright 2013 Code Strategies                                */
/* This code may be freely used and distributed in any project.  */
/* However, please do not remove this credit if you publish this */
/* code in paper or electronic form, such as on a web site.      */
/**
 * **************************************************************
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import ss16lab.outliers.Chauvenet;
import ss16lab.outliers.KMeans;
import ss16lab.outliers.OutlierDetector;
import ss16lab.outliers.OutlierMethod;
import ss16lab.outliers.Statistics;

/**
 * Helper class to generate statistical reports
 *
 * @author Gj
 */
public class StatisHelper {

    public static void main(String[] args) {
        try {
            FileOutputStream fileOut = new FileOutputStream("E:\\poi-test.xls");
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("POI Worksheet");

            // index from 0,0... cell A1 is cell(0,0)
            HSSFRow row1 = worksheet.createRow((short) 0);

            HSSFCell cellA1 = row1.createCell((short) 0);
            cellA1.setCellValue("Hello");

            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Save bucket samples into Excel file
     *
     * @param hm samples
     * @param filename output filename
     * @author Gj
     */
    public static void saveToExcelFile(HashMap<String, Double> hm, String filename) {
        int rowIndex = 0;
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("POI Worksheet");

            Iterator it = hm.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                // index from 0,0... cell A1 is cell(0,0)
                HSSFRow row = worksheet.createRow((short) rowIndex++);

                HSSFCell cellA = row.createCell((short) 0);
                cellA.setCellValue(pair.getKey().toString());
                HSSFCell cellB = row.createCell((short) 1);
                cellB.setCellValue(pair.getValue().toString());
            }

            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generate statistical txt files in folder "web/statistics"
     *
     * @param hm samples
     * @param statsDir output directory
     * @author Gj
     */
    public static void saveToStatisFile(HashMap<String, Double> hm, String filename, String statsDir) throws IOException {
        String content = "";
        OutlierMethod outlierMethod = null;

        List<Double> samples = new ArrayList<>();

        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            samples.add((Double) pair.getValue());
        }

        outlierMethod = new Chauvenet(samples.toArray(new Double[0]));
        OutlierDetector outloutlierDetector = new OutlierDetector(outlierMethod);
        List<Double> l1 = outloutlierDetector.outliers;
        content += "Chauvenet found:" + l1.size() + "\r\n";

        outlierMethod = new KMeans(samples.toArray(new Double[0]));
        outloutlierDetector = new OutlierDetector(outlierMethod);
        List<Double> l2 = outloutlierDetector.outliers;
        content += "KMeans found:" + l2.size() + "\r\n";
        List<Double> intersection = new LinkedList<>();
        for (Double v : l1) {
            if (l2.contains(v)) {
                intersection.add(v);
            }
        }
        int size = Math.max(l1.size(), l2.size());
        if (size > 0) {
            content += "Agreement:" + (intersection.size() / (size)) + "\r\n";
        } else {
            content += "Agreement:" + (0.0) + "\r\n";
        }

        content += "Chauvenet subset of Kmeans:" + (Statistics.contains(l2, l1)) + "\r\n";
        content += "Kmeans subset of Chauvenet:" + (Statistics.contains(l1, l2)) + "\r\n";

        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filename), "utf-8"));
        writer.write(content);
        writer.close();

        content = "";

        File folder = new File(statsDir);
        File[] listOfFiles = folder.listFiles();

        List<Double> ChauvenetFound = new ArrayList<>();

        List<Double> KmeansFound = new ArrayList<>();
        List<Double> agreementTotal = new ArrayList<>();

        List<Double> KMeansisSubsetOfChauvenet = new ArrayList<>();
        List<Double> ChauvenetisSubsetOfKMeans = new ArrayList<>();

        int counter = 0;
        for (File file : listOfFiles) {
            if (!file.getName().contains("total")) {
                counter++;
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    ChauvenetFound.add(Double.parseDouble(br.readLine().split(":")[1]));
                    KmeansFound.add(Double.parseDouble(br.readLine().split(":")[1]));
                    agreementTotal.add(Double.parseDouble(br.readLine().split(":")[1]));
                    if ("true".equals(br.readLine().split(":")[1])) {
                        ChauvenetisSubsetOfKMeans.add(1d);
                    }
                    if ("true".equals(br.readLine().split(":")[1])) {
                        KMeansisSubsetOfChauvenet.add(1d);
                    }
                }
            }
        }
        double[] arr = ArrayUtils.toPrimitive(ChauvenetFound.toArray(new Double[ChauvenetFound.size()]));
        Statistics st = new Statistics(arr);
        content += "ChauvenetMaxFound:" + (int) st.getMax() + "\r\n";
        content += "ChauvenetMinFound:" + (int) st.getMin() + "\r\n";
        content += "ChauvenetFound:" + (int) st.getSum() + "\r\n\r\n\r\n";

        arr = ArrayUtils.toPrimitive(KmeansFound.toArray(new Double[ChauvenetFound.size()]));
        st = new Statistics(arr);
        content += "KmeansMaxFound:" + (int) st.getMax() + "\r\n";
        content += "KmeansMinFound:" + (int) st.getMin() + "\r\n";
        content += "KmeansFound:" + (int) st.getSum() + "\r\n\r\n\r\n";
        arr = ArrayUtils.toPrimitive(agreementTotal.toArray(new Double[ChauvenetFound.size()]));
        st = new Statistics(arr);
        content += "AgreementMax:" + (int) st.getMax() + "\r\n";
        content += "AgreementMin:" + (int) st.getMin() + "\r\n";
        content += "Agreement:" + st.getMean() + "%\r\n\r\n\r\n";

        content += "ChauvenetisSubsetOfKMeans:" + ChauvenetisSubsetOfKMeans.size() + "\r\n";
        content += "KMeansisSubsetOfChauvenet:" + KMeansisSubsetOfChauvenet.size() + "\r\n";

        writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(statsDir + "total.txt"), "utf-8"));
        writer.write(content);
        writer.close();
    }

}
