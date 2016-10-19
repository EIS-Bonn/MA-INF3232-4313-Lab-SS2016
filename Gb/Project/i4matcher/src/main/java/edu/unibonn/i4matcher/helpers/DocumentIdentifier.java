package edu.unibonn.i4matcher.helpers;

import java.util.regex.Pattern;

/**
 * Created by phil on 03.09.16.
 */
public class DocumentIdentifier {
    public static String getFileType(String filename){
        Pattern paml = Pattern.compile(".*?\\.aml$");
        Pattern popc = Pattern.compile(".*?\\.opcua$");
        if (popc.matcher(filename).matches()) {
            return "opcua";
        }
        if (paml.matcher(filename).matches()){
            System.out.println(filename);
            return "automationML";
        }
        else return "nothing";
    }
}
