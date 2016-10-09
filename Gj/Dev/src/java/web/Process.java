package web;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.RandomStringUtils;

import ss16lab.data.linking.FusekiAPI;
import static ss16lab.data.preprocessing.Subpopulation.desinationFolder;
import ss16lab.outliers.Chauvenet;
import ss16lab.outliers.OutlierDetector;

/**
 * Servlet to handle user request to start a new process session
 *
 * @author Gj
 */
public class Process extends HttpServlet {

    FusekiAPI fuseki = new FusekiAPI();
    OutlierDetector outlierDetector;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        String token = RandomStringUtils.randomAlphanumeric(8).toLowerCase();

        String outlierPredName = "";
        String locationPredName = "";
        String root = getServletContext().getRealPath("/");
        ArrayList<String> outlierproperties = new ArrayList();

        File settingsPath = new File(root + "/uploads/" + token + "/settings.txt");
        File uploadPath = new File(root + "/uploads/" + token);
        File datasetsPath = new File(root + "/datasets/");
        File subpopulationPath = new File(root + "/subpopulation/" + token);

        uploadPath.mkdirs();

        subpopulationPath.mkdirs();

        if (isMultipart) {
            // Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                // Parse the request
                List items = upload.parseRequest(request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();

                    if (!item.isFormField()) {
                        String fileName = item.getName();
//                        File uploadedFile = new File(uploadPath + "/" + fileName);
//
//                        InputStream content = item.getInputStream();
//                        Files.copy(content, uploadedFile.toPath());

                        File uploadedFile = new File(uploadPath + "/" + fileName);
                        item.write(uploadedFile);
                    } else {
                        if (item.getFieldName().equals("outlierpredname")) {
                            outlierPredName = item.getString();
                        } else if (item.getFieldName().equals("locationpredname")) {
                            locationPredName = item.getString();
                        } else if (item.getFieldName().startsWith("outlierp")) {
                            outlierproperties.add(item.getString());
                        }
                    }

                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //// save session settings
            String settings = "outlierPredName=" + outlierPredName + "\r\n";
            settings += "locationPredName=" + locationPredName;
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(settingsPath), "utf-8"))) {
                writer.write(settings);
            }

            File folder = new File(uploadPath.getAbsolutePath());
            File[] listOfFiles = folder.listFiles();

            fuseki.dataSets.clear();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fuseki.dataSets.add(file);
                }
            }

            folder = new File(datasetsPath.getAbsolutePath());
            listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fuseki.dataSets.add(file);
                }
            }
            String dbpediaLocationAnchor = fuseki.findUniquePredicate(locationPredName);

            fuseki.enrichDataSet(dbpediaLocationAnchor);
            fuseki.mergeDatasets(uploadPath + "/RDF_FULL.ttl");// save all datasets with the newly fetched attributs into one file

            ss16lab.data.preprocessing.Subpopulation.filename = uploadPath.getAbsolutePath() + "/RDF_FULL.ttl";
            ss16lab.data.preprocessing.Subpopulation.desinationFolder = subpopulationPath.getAbsolutePath();
            ss16lab.data.preprocessing.Subpopulation.properties = outlierproperties;
            ss16lab.data.preprocessing.Subpopulation.subpopulation();
//
            folder = new File(subpopulationPath.getAbsolutePath());
            File[] subpopLevels = folder.listFiles(File::isDirectory);

            String findOutlier = fuseki.findUniquePredicate(outlierPredName);

            request.setAttribute("subpopLevels", subpopLevels);
            request.setAttribute("findOutlier", findOutlier);
            request.setAttribute("sessionID", token);
            request.getRequestDispatcher("/subpop.jsp").forward(request, response);

        }
    }

}
