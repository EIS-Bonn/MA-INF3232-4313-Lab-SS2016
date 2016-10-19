package web;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
 * Servlet to retrieve an old process session
 *
 * @author Gj
 */
public class Retrieve extends HttpServlet {

    FusekiAPI fuseki = new FusekiAPI();
    OutlierDetector outlierDetector;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("sessionID");

        String outlierPredName = "";
        String locationPredName = "";
        String root = getServletContext().getRealPath("/");
        File uploadPath = new File(root + "/uploads/" + token);
        File datasetsPath = new File(root + "/datasets/");
        File subpopulationPath = new File(root + "/subpopulation/" + token);
        File settingsPath = new File(root + "/uploads/" + token + "/settings.txt");

        BufferedReader br = new BufferedReader(new FileReader(settingsPath));

        outlierPredName = br.readLine().split("=")[1];
        locationPredName = br.readLine().split("=")[1];

        br.close();

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

        fuseki.retreiveModels();
        folder = new File(subpopulationPath.getAbsolutePath());
        File[] subpopLevels = folder.listFiles(File::isDirectory);

        String findOutlier = fuseki.findUniquePredicate(outlierPredName);

        request.setAttribute("subpopLevels", subpopLevels);
        request.setAttribute("findOutlier", findOutlier);
        request.setAttribute("sessionID", token);
        request.getRequestDispatcher("/subpop.jsp").forward(request, response);

    }

}
