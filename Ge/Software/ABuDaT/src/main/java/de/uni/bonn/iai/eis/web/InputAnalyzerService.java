package de.uni.bonn.iai.eis.web;

import de.uni.bonn.iai.eis.XlsParser;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

public class InputAnalyzerService {

    private static final Logger log = LoggerFactory.getLogger(InputAnalyzerService.class);

    public Input analyzeInput(String urlString, String charset, boolean hasHeader) {
        //        if (file != null && !file.getOriginalFilename().equals("")) {
//            String originalFilename = file.getOriginalFilename();
//            log.info("file://" + originalFilename);
//            try {
//                InputStream inputStream = file.getInputStream();
//                byte[] bytes = IOUtils.toByteArray(inputStream);
//                log.info(String.format("Read %d bytes from %s", bytes.length, file.getName()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {

        URL url;

        String[] columns = null;
        String delimiter = ",";
        String type = "csv";

        try {
            url = new URL(urlString);
            InputStream inputStream = url.openStream();
            byte[] data = IOUtils.toByteArray(inputStream);

            log.info(String.format("Downloaded %d bytes from %s", data.length, urlString));

            String contentType = new Tika().detect(data);
            log.info("Content type: " + contentType);

            if (contentType.equals("text/plain")) {
                log.info("Assuming .csv file...");
                String csv = new String(data, Charset.forName(charset));
                String headerLine = csv.split(System.getProperty("line.separator"))[0];

                //FIXME delimiter hardcoded!
                columns = headerLine.split(delimiter);
                if (columns.length == 1){
                    delimiter = ";";
                    columns = headerLine.split(delimiter);
                }
            } else if (contentType.equals("application/x-tika-msoffice")) {
                log.info("Assuming .xls file...");
                XlsParser xlsParser = new XlsParser();
                try {
                    columns = xlsParser.parse(new ByteArrayInputStream(data), 0).toArray(new String[0]);
                    type = "xls";
                } catch (Exception e) {
                    log.error("Exception occured while trying to parse xls file.", e);
                }
            }

        } catch (IOException e) {
            log.error("Unable to analyze given URL: " + urlString, e);
        }

        if (!hasHeader && columns != null) {
            log.info("Creating column names...");
            String[] columnsReplacement = new String[columns.length];

            for (int i = 0; i < columns.length; i++) {
                columnsReplacement[i] = String.format("col%d", i+1);
            }

            columns = columnsReplacement;
        }

        return new Input(urlString, columns, delimiter, charset, type);
    }
}
