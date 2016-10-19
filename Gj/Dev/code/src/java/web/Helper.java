/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import ss16lab.data.linking.FusekiAPI;
import ss16lab.outliers.Chauvenet;
import ss16lab.outliers.OutlierDetector;

/**
 * Class contains some miscellaneous and diverse functionality
 *
 * @author Gj
 */
public class Helper {

    public static String rootDirectory = "";

    /**
     * counts number of lines in a file
     *
     * @param f file object
     * @return Int number of lines in a file
     * @author Gj
     */
    public static int fileLinesNum(File f) {
        int lines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            while (reader.readLine() != null) {
                lines++;
            }
            reader.close();
            return lines;

        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * reads line based on the line number in a file
     *
     * @param f file object
     * @param index line number
     * @return String line
     * @author Gj
     */
    public static String fileReadLine(File f, int index) {
        String line = "";
        int counter = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            while ((line = reader.readLine()) != null) {
                if (++counter == index) {
                    break;
                }
            }
            reader.close();
            return line;

        } catch (IOException e) {
            return "";
        }
    }
}
