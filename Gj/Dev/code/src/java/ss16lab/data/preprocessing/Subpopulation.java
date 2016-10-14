/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss16lab.data.preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javafx.scene.input.KeyCode.K;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.eclipse.rdf4j.rio.turtle.TurtleParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javafx.scene.input.KeyCode.K;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.eclipse.rdf4j.rio.turtle.TurtleParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javafx.scene.input.KeyCode.K;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.eclipse.rdf4j.rio.turtle.TurtleParser;

/**
 * Class contains all functionality to apply subpopulation method
 *
 * @author Gj
 */
public class Subpopulation {

    // TTL file after enrichment ...........................
    public static String filename = "";
    public static String desinationFolder = "";
//    public List<String> properties = null;
    public static ArrayList<String> properties = new ArrayList<String>();
    public static ArrayList<String> property_constraint = new ArrayList<String>();

    // List of Bins 
    public static List<ArrayList<String>> list_Subgroups = new ArrayList<ArrayList<String>>();
    public static List<ArrayList<String>> List_lists_observations = new ArrayList<ArrayList<String>>();
    public static List<ArrayList<String>> New_Iteration_List_Bins = new ArrayList<ArrayList<String>>();
    public static List<ArrayList<String>> List_Bins = new ArrayList<ArrayList<String>>();
    public static List<ArrayList<String>> List_lists_Subjects_from_Bins = new ArrayList<ArrayList<String>>();
    public static List<ArrayList<String>> List_lists_Subjects_from_seed = new ArrayList<ArrayList<String>>();
    public static ArrayList<String> triples_S_P_O = new ArrayList<String>();
    public static ArrayList<String> constrain = new ArrayList<String>();
    public static ArrayList<String> Temp_constrain = new ArrayList<String>();
    public static String last_subject = "";
    public static String D_Last_Object = "";
    public static String B_last_Predicate = "";
    public static java.util.ArrayList myList = new ArrayList();
    public static ArrayList<String> Repository_triples = new ArrayList<String>();
    public static HashMap<String, String> hmap = new HashMap<String, String>();
    public static ArrayList<String> matched_path = new ArrayList<>();

    public static List<ArrayList<String>> list_Seed_bins = new ArrayList<ArrayList<String>>();
    public static ArrayList<String> list_Triples_bins = new ArrayList<String>();
    public static ArrayList<String> Subject_Observation = new ArrayList<String>();
    public static List<ArrayList<String>> list_temp_triples = new ArrayList<ArrayList<String>>();
    public static int size_constrain = constrain.size();
    public static String value = "";
    public static ArrayList<String> temp = new ArrayList<>();

    /**
     * The main method to start subpopulation process
     *
     * @author Gj
     */
    public static void subpopulation() throws IOException {
//
        System.out.println("Starting Parsing stage");
        System.out.println();
        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println();
        // parsing  TTL files 
        parsing_TTL_File(filename);
        // recognize the triples. S:P:O.........
        recognize_triples(myList);
        // call subpopulation .......

        System.out.println("Starting Subpopulation stage");
        System.out.println();
        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println();

        Seed_subpop(Repository_triples, Temp_constrain);

        System.out.println();
        System.out.println("Subpopulation stage is finished");
        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println();
    }

    /**
     * Extract all triples from a dataset
     *
     * @param filename RDF dataset file
     * @author Gj
     */
    public static void parsing_TTL_File(String filename) throws MalformedURLException, IOException {
        //read the TTI file for parsing :
        java.net.URL documentUrl = new File(filename).toURI().toURL();
        InputStream inputStream = documentUrl.openStream();
        RDFParser rdfParser = new TurtleParser();
        StatementCollector collector = new StatementCollector(myList);
        rdfParser.setRDFHandler(collector);
        try {
            rdfParser.parse(inputStream, documentUrl.toString());
        } // handle unrecoverable parse error
        // handle a problem encountered by the RDFHandler
        catch (IOException | RDFParseException | RDFHandlerException e) {
            // handle IO problems (e.g. the file could not be read)
        }
    }

    /**
     * Check if the string represent a number
     *
     * @param str1 string
     * @return boolean TRUE or FALSE
     * @author Gj
     */
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    /**
     * Extract URI value from a triple's component (subject, object, predicate)
     *
     * @param list list of RDF triples
     * @author Gj
     */
    public static void recognize_triples(java.util.ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            // for triple spliting 
            String triple = list.get(i).toString();

            String[] parts = triple.split(",", 3);

            //----------------------for subject 
            //for subject spliting 
            String part1 = parts[0];
            String check_Observation = parts[0];
            ArrayList<String> list_s = new ArrayList<String>();

            if (check_Observation.indexOf("observation") != -1) {
                last_subject = part1.substring(part1.lastIndexOf("/") + 1);
                if (Subject_Observation.size() > 0) {
                    if (!Subject_Observation.get(Subject_Observation.size() - 1).equals(last_subject)) {
                        list_s.add(last_subject);
                        List_lists_Subjects_from_seed.add(list_s);
                    }
                } else if (Subject_Observation.size() == 0) {
                    list_s.add(last_subject);
                    List_lists_Subjects_from_seed.add(list_s);
                }
                //----------------------------For Predicate / Property 
                String part2 = parts[1];
                String A_last_Predicate;
                B_last_Predicate = part2.substring(part2.lastIndexOf("/") + 1);
                if (B_last_Predicate.indexOf("#") != -1) {
                    A_last_Predicate = B_last_Predicate.substring(B_last_Predicate.lastIndexOf("#") + 1);
                    if (constrain.size() > 0) {
                        if (!constrain.get(constrain.size() - 1).equals(A_last_Predicate)) {
                        }
                    } else if (constrain.size() == 0) {
                    }
                    //-----------------------------
                } else {
                    A_last_Predicate = B_last_Predicate;
                }
                //----------------------------For Object  
                String part3 = parts[2];
                if (part3.indexOf("^^") != -1) {
                    String string = parts[2];
                    Pattern p = Pattern.compile("\"([^\"]*)\"");
                    Matcher m = p.matcher(string);
                    while (m.find()) {
                        String x = m.group(1);
                        Repository_triples.add(last_subject + ":" + B_last_Predicate + ":" + x);

                        String key = last_subject;
                        String Value = B_last_Predicate + ":" + x;
                        boolean keyFlag1 = hmap.containsKey(key);

                        if (keyFlag1 == true) {
                            String Valu_value = hmap.get(key);
                            String last_value = Valu_value + "-" + Value;
                            hmap.put(key, last_value);
                        } else {

                            hmap.put(key, Value);
                        }

                        if (!isNumeric(x)) {
                            if (constrain.contains(B_last_Predicate + "=" + x)) {
                                // do no thing .......
                            } else {
                                constrain.add(B_last_Predicate + "=" + x);
                                property_constraint.add(B_last_Predicate);
                            }
                        }

                    }
                } else {
                    String A_last_Object = part3.substring(part3.lastIndexOf("/") + 1);
                    String B_last_Object = A_last_Object.substring(A_last_Object.lastIndexOf("#") + 1);
                    String[] C_last_Object = B_last_Object.split(">", 2);
                    D_Last_Object = C_last_Object[0];
                    if (D_Last_Object.charAt(D_Last_Object.length() - 1) == ')') {
                        D_Last_Object = D_Last_Object.replace(D_Last_Object.substring(D_Last_Object.length() - 1), "");
                    }
                    Repository_triples.add(last_subject + ":" + A_last_Predicate + ":" + D_Last_Object);
                    // Hash Table 
                    String key2 = last_subject;
                    String Value2 = A_last_Predicate + ":" + D_Last_Object;

                    boolean keyFlag2 = hmap.containsKey(key2);

                    if (keyFlag2 == true) {
                        String Valu_value2 = hmap.get(key2);
                        String last_value2 = Valu_value2 + "-" + Value2;
                        hmap.put(key2, last_value2);
                    } else {

                        hmap.put(key2, Value2);
                    }

                    if (!isNumeric(D_Last_Object)) {
                        if (constrain.contains(A_last_Predicate + "=" + D_Last_Object)) {
                            // do no thing .......
                        } else {
                            constrain.add(A_last_Predicate + "=" + D_Last_Object);
                            property_constraint.add(A_last_Predicate);
                        }
                    }
                }
            }

        }

        for (int o = 0; o < Repository_triples.size(); o++) {
//            System.out.println(Repository_triples.get(o) + "---" + o);

        }

        Object[] st = constrain.toArray();
        for (Object s : st) {
            if (constrain.indexOf(s) != constrain.lastIndexOf(s)) {
                constrain.remove(constrain.lastIndexOf(s));
            }
        }

        Object[] st2 = property_constraint.toArray();
        for (Object s2 : st2) {
            if (property_constraint.indexOf(s2) != property_constraint.lastIndexOf(s2)) {
                property_constraint.remove(property_constraint.lastIndexOf(s2));
            }
        }

//        System.out.println("The constraints are ....................: ");
//        System.out.println(".......................................: ");
        for (int q = 0; q < constrain.size(); q++) {
//            System.out.println(constrain.get(q) + "--" + q);

        }
        //----------------

        for (int d1 = 0; d1 < properties.size(); d1++) {

            String property = properties.get(d1);

            if (!property_constraint.contains(property)) {
                properties.remove(property);
            }
        }

        for (int d = 0; d < properties.size(); d++) {

            for (int b = 0; b < constrain.size(); b++) {

                String p_o = constrain.get(b);
                String[] p = p_o.split("=", 2);
                String predicate_value = p[0];

                String object_value = p[1];

                if (properties.get(d).equals(predicate_value)) {
                    Temp_constrain.add(p_o);
                }

            }

        }

    }

    /**
     * Start subpopulation process for first level
     *
     * @param Repository_T root node contains full repository
     * @param constrain list of applied constrains
     * @author Gj
     */
    public static void Seed_subpop(ArrayList<String> Repository_T, ArrayList<String> constrain) throws FileNotFoundException, IOException {

        int numberlevel = 0;
        ArrayList<String> Guid = new ArrayList<String>();
        String c = "level_" + numberlevel;
        Guid.add(c);
        List<ArrayList<String>> seed_Level = new ArrayList<ArrayList<String>>();
        seed_Level.add(Guid);

        for (int y = 0; y < constrain.size(); y++) {
            String myconstrain = constrain.get(y);
            ArrayList<String> bin = new ArrayList<String>();
            String Guide_value = myconstrain;
            bin.add(Guide_value);

            for (int z = 0; z < Repository_T.size(); z++) {
                String p_o_S = Repository_T.get(z).toString();
                String[] p = p_o_S.split(":", 3);
                String S_value = p[0];
                String predicate_value = p[1];
                String object_value = p[2];
                if (myconstrain.equals(predicate_value + "=" + object_value)) {
                    bin.add(p_o_S);
                }
            }
            if (!(bin.isEmpty())) {

                if (bin.size() > 1) {
                    seed_Level.add(bin);

                }

            }
        }
        int countter = 1;
        int guid = numberlevel + 1;
        String dirname = "Level" + guid;
        File dir = new File(desinationFolder + "/" + dirname);
        // you can create directory using mkdirs method of File
        //object.
        dir.mkdirs();
        for (int a = 1; a < seed_Level.size(); a++) {

            File file2 = new File(desinationFolder + "/" + dirname + "/" + "Level" + (numberlevel + 1) + "_Bin_" + countter + ".txt");
            if (!file2.exists()) {
                file2.createNewFile();
            }
            countter++;
            FileWriter fw2 = new FileWriter(file2, true);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            PrintWriter pw2 = new PrintWriter(bw2);

            if (seed_Level.get(a).size() > 1) {
                for (int h = 0; h < seed_Level.get(a).size(); h++) {
//                System.out.println(seed_Level.get(a).get(h));
                    pw2.println(seed_Level.get(a).get(h));
                }
            }
            pw2.close();
//            System.out.println("From seed .....The Next Bin ");
        }
        numberlevel++;

        Bins_subpop(Repository_triples, seed_Level, constrain, numberlevel);
    }

    /**
     * Forward subpopulation process for next levels
     *
     * @param Repository_T a node contains a repository
     * @param list_Bins_in_Pre_Level list of bins' names of the previous level
     * @param constrain list of applied constrains
     * @param numberlevel number of current level
     * @author Gj
     */
    public static void Bins_subpop(ArrayList<String> Repository_T, List<ArrayList<String>> list_Bins_in_Pre_Level, ArrayList<String> constrain, int numberlevel) throws FileNotFoundException, IOException {

//        System.out.println(".........The level is :" + numberlevel);
        ArrayList<String> Guid = new ArrayList<String>();
        String c = "level_" + numberlevel;
        Guid.add(c);
        List<ArrayList<String>> list_Bins_in_after_Level = new ArrayList<ArrayList<String>>();
        list_Bins_in_after_Level.add(Guid);

        if (numberlevel == 1) {
            for (int i = 0; i < constrain.size(); i++) {// constrain 
                String new_myconstrain = constrain.get(i);
                String[] w = new_myconstrain.split("=", 2);
                String PP = w[0];
                String oo = w[1];

                for (int a = 1; a < list_Bins_in_Pre_Level.size(); a++) {// seed level ..... 

                    ArrayList<String> new_bin = new ArrayList<String>();

                    String old_constrain = list_Bins_in_Pre_Level.get(a).get(0);

                    if (!old_constrain.equals(new_myconstrain)) {
                        String new_Guide_value = old_constrain + "->" + new_myconstrain;

                        new_bin.add(new_Guide_value);

                        String check_delet_status1 = list_Bins_in_Pre_Level.get(a).get(0);

                        if (!check_delet_status1.contains("_D")) {
                            for (int h = 1; h < list_Bins_in_Pre_Level.get(a).size(); h++) {// each Bin in Seed level.....

                                String p_o_S1 = list_Bins_in_Pre_Level.get(a).get(h);
                                String[] p1 = p_o_S1.split(":", 3);
                                String S_value1 = p1[0];
                                String tr = S_value1 + ":" + PP + ":" + oo;

                                String key = S_value1;
                                String Long_string_fromhash = hmap.get(key);
                                String word = PP + ":" + oo;
                                Boolean found;
                                found = Long_string_fromhash.contains(word);
                                if (found == true) {
                                    new_bin.add(tr);
                                }
                            }
                            if (!(new_bin.isEmpty()) && new_bin.size() > 1) {

                                list_Bins_in_after_Level.add(new_bin);

                            }
                        }
                    }
                }
            }

//             connect the nodes to have lattice .....
            connect_lattice(list_Bins_in_after_Level);
            // prune for not reduce status 
            prune_not_reducing_state(list_Bins_in_after_Level, list_Bins_in_Pre_Level);
            // Prune by KLV ....
            prune_by_KLV(list_Bins_in_after_Level, list_Bins_in_Pre_Level);
            //===========Fill Bins==========================================
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!! ");

            int guide1 = numberlevel + 1;
            String dirname1 = "Level" + guide1;
            File dir1 = new File(desinationFolder + "/" + dirname1);
            int countter1 = 1;
            dir1.mkdirs();

            for (ArrayList stb : list_Bins_in_after_Level) {

                if (stb.size() > 0) {
                    String cheking1 = stb.get(0).toString();

                    if (!cheking1.contains("_D")) {

                        if (stb.size() > 1) {

                            File file1 = new File(desinationFolder + "/" + dirname1 + "/" + "Level" + (numberlevel + 1) + "_Bin_" + (countter1) + ".txt");
                            file1.createNewFile();
                            countter1++;
                            FileWriter fw1 = new FileWriter(file1, true);
                            BufferedWriter bw1 = new BufferedWriter(fw1);
                            PrintWriter pw1 = new PrintWriter(bw1);
                            for (int x = 0; x < stb.size(); x++) {

//                                System.out.println(stb.get(x).toString());
                                pw1.println(stb.get(x).toString());

                            }
                            pw1.close();
                        }
                    }
                }

            }
        } //==========================
        else if (numberlevel > 1) {

            for (int i2 = 0; i2 < constrain.size(); i2++) {// constrain 

                String new_myconstrain2 = constrain.get(i2);
                String[] w2 = new_myconstrain2.split("=", 2);
                String PP2 = w2[0];
                String oo2 = w2[1];

                for (int a2 = 1; a2 < list_Bins_in_Pre_Level.size(); a2++) {// seed level ..... 

                    ArrayList<String> new_bin = new ArrayList<String>();

                    String path = list_Bins_in_Pre_Level.get(a2).get(0);
                    String partofconstrain = path.substring(path.lastIndexOf("->"));
                    String Last_partofpath = partofconstrain.replace("->", "");

                    if (!Last_partofpath.equals(new_myconstrain2)) {
                        String new_Guide_value = path + "->" + new_myconstrain2;

                        new_bin.add(new_Guide_value);

                        String check_delet_status2 = list_Bins_in_Pre_Level.get(a2).get(0);

                        if (!check_delet_status2.contains("_D")) {
                            for (int h = 1; h < list_Bins_in_Pre_Level.get(a2).size(); h++) {// each Bin in Seed level.....

                                String p_o_S1 = list_Bins_in_Pre_Level.get(a2).get(h);
                                String[] p1 = p_o_S1.split(":", 3);
                                String S_value1 = p1[0];
                                String tr = S_value1 + ":" + PP2 + ":" + oo2;

                                String key = S_value1;
                                String Long_string_fromhash = hmap.get(key);
                                String word = PP2 + ":" + oo2;
                                Boolean found;
                                found = Long_string_fromhash.contains(word);
                                if (found == true) {
                                    new_bin.add(tr);
                                }
                            }
                            if (!(new_bin.isEmpty())) {

                                if (new_bin.size() > 1) {

                                    list_Bins_in_after_Level.add(new_bin);
                                }
                            }
                        }
                    }

                }

            }

            // connect the nodes to have lattice .....
            connect_lattice(list_Bins_in_after_Level);
            // prune for not reduce status 
            prune_not_reducing_state(list_Bins_in_after_Level, list_Bins_in_Pre_Level);
            // Prune by KLV ....
            prune_by_KLV(list_Bins_in_after_Level, list_Bins_in_Pre_Level);
            //===========Fill Bins==========================================
//            System.out.println("******************");
            int guide2 = numberlevel + 1;

            String dirname2 = "Level" + guide2;
            File dir2 = new File(desinationFolder + "/" + dirname2);
            int countter2 = 1;
            dir2.mkdirs();

            for (ArrayList stb2 : list_Bins_in_after_Level) {

                String cheking2 = stb2.get(0).toString();

                if (!cheking2.contains("_D")) {
                    if (stb2.size() > 1) {
                        File file2 = new File(desinationFolder + "/" + dirname2 + "/" + "Level" + (numberlevel + 1) + "_Bin_" + (countter2) + ".txt");
                        file2.createNewFile();
                        countter2++;
                        FileWriter fw2 = new FileWriter(file2, true);
                        BufferedWriter bw2 = new BufferedWriter(fw2);
                        PrintWriter pw2 = new PrintWriter(bw2);

                        for (int x2 = 0; x2 < stb2.size(); x2++) {

//                            System.out.println(stb2.get(x2).toString());
                            pw2.println(stb2.get(x2).toString());

                        }
                        pw2.close();
                    }
                }

            }
        }
//--------------------------------------------------------------------------
        numberlevel = numberlevel + 1;

        int flage = properties.size() - 1;
        if (numberlevel <= flage) {
            Bins_subpop(Repository_T, list_Bins_in_after_Level, constrain, numberlevel);
        }

    }

    /**
     * Group bins the come from similar paths
     *
     * @param list_Bins_in_after_Level list of bins' names of the previous level
     * @author Gj
     */
    public static void connect_lattice(List<ArrayList<String>> list_Bins_in_after_Level) {
        for (int i = 1; i < list_Bins_in_after_Level.size(); i++) {
            for (int k = i + 1; k < list_Bins_in_after_Level.size(); k++) {

                String x1 = list_Bins_in_after_Level.get(i).get(0);
                String x2 = list_Bins_in_after_Level.get(k).get(0);

                boolean flage = checkSameChar(x1, x2);

                if (flage == true) {
                    String new_path = x2 + "_D";

                    for (int y = 1; y < list_Bins_in_after_Level.get(k).size(); y++) {

                        String tr = list_Bins_in_after_Level.get(k).get(y);
                        String p_o_S1 = list_Bins_in_after_Level.get(k).get(y);
                        String[] p1 = p_o_S1.split(":", 3);
                        String S_value1 = p1[0];
                        String p_value1 = p1[1];
                        String O_value1 = p1[2];

                        for (int f = 1; f < list_Bins_in_after_Level.get(i).size(); f++) {

                            String p_o_S2 = list_Bins_in_after_Level.get(i).get(f);
                            String[] p2 = p_o_S2.split(":", 3);
                            String S_value2 = p1[0];
                            String p_value2 = p1[1];
                            String O_value2 = p1[2];

                            if (!S_value1.equals(S_value2)) {
                                list_Bins_in_after_Level.get(i).add(tr);

                            }
                        }

                    }

                    list_Bins_in_after_Level.get(k).set(0, new_path);
                }

            }
        }
    }

    /**
     * Check if two paths have same elements but different sequence
     *
     * @param str1 path one
     * @param str2 path two
     * @return boolean TRUE or FALSE (similar or not similar)
     * @author Gj
     */
    public static boolean checkSameChar(String str1, String str2) {

        boolean flage = false;
        for (char c : str1.toCharArray()) {
            if (str2.indexOf(c) < 0) {
                return false;
            }

        }
        for (char c : str2.toCharArray()) {
            if (str1.indexOf(c) < 0) {
                return false;
            }

        }

        return true;
    }

    /**
     * Prune according to number of generated child nodes
     *
     * @param list_Bins_in_after_Level list of bins in previous level
     * @param list_Bins_in_before_Level list of bins in next level
     * @author Gj
     */
    public static void prune_not_reducing_state(List<ArrayList<String>> list_Bins_in_after_Level, List<ArrayList<String>> list_Bins_in_before_Level) {

        for (int i = 1; i < list_Bins_in_after_Level.size(); i++) {

            String checking = list_Bins_in_after_Level.get(i).get(0);

            if (!checking.contains("_D")) {
                if (list_Bins_in_after_Level.get(i).size() > 1) {
                    String path = list_Bins_in_after_Level.get(i).get(0);
                    String partofconstrain = path.substring(path.lastIndexOf("->"));
                    String Last_partofconstrain = path.replace(partofconstrain, "");

                    for (int j = 1; j < list_Bins_in_before_Level.size(); j++) {

                        String checking2 = list_Bins_in_before_Level.get(j).get(0);
                        if (!checking2.contains("_D")) {

                            String father_path = list_Bins_in_before_Level.get(j).get(0);
                            if (Last_partofconstrain.equals(father_path)) {
                                if (list_Bins_in_after_Level.get(i).size() == list_Bins_in_before_Level.get(j).size()) {

                                    String new_change = checking + "_D";
                                    list_Bins_in_after_Level.get(i).set(0, new_change);
                                }
                            }
                        }

                    }
                }
            }

        }

    }

    /**
     * Prune child node according to KLV
     *
     * @param list_Bins_in_after_Level list of bins in previous level
     * @param list_Bins_in_before_Level list of bins in next level
     * @author Gj
     */
    public static void prune_by_KLV(List<ArrayList<String>> list_Bins_in_after_Level, List<ArrayList<String>> list_Bins_in_before_Level) {

        for (int i = 1; i < list_Bins_in_after_Level.size(); i++) {

            ArrayList<String> list_child = new ArrayList<String>();

            String checking = list_Bins_in_after_Level.get(i).get(0);

            if (!checking.contains("_D")) {
                if (list_Bins_in_after_Level.get(i).size() > 1) {
                    String path = list_Bins_in_after_Level.get(i).get(0);
                    String partofconstrain = path.substring(path.lastIndexOf("->"));
                    String Last_partofconstrain = path.replace(partofconstrain, "");

                    for (int u = 1; u < list_Bins_in_after_Level.get(i).size(); u++) {
                        String triples_childe = list_Bins_in_after_Level.get(i).get(u);
                        list_child.add(triples_childe);
                    }

                    for (int j = 1; j < list_Bins_in_before_Level.size(); j++) {

                        String checking2 = list_Bins_in_before_Level.get(j).get(0);
                        if (!checking2.contains("_D")) {

                            String father_path = list_Bins_in_before_Level.get(j).get(0);
                            if (Last_partofconstrain.equals(father_path)) {
                                ArrayList<String> list_parent = new ArrayList<String>();

                                for (int p = 0; p < list_Bins_in_before_Level.get(j).size(); p++) {
                                    String triples_parent = list_Bins_in_before_Level.get(j).get(p);
                                    list_parent.add(triples_parent);
                                }
                                // KLV formoula 
                                double reuslt = KLD(list_parent, list_child);
                                if (reuslt < 0.01) {
                                    String new_change = checking + "_D";
                                    list_Bins_in_after_Level.get(i).set(0, new_change);

                                }
                            }
                        }

                    }

                }
            }

        }
    }

    /**
     * Find the deviation between parent and child node
     *
     * @param values1 list of items in parent node
     * @param value2 list of items in child node
     * @return Double KLD value of the deviation
     * @author Gj
     */
    public static Double KLD(ArrayList<String> values1, ArrayList<String> value2) {

        Map<String, Integer> map1 = new HashMap<String, Integer>();
        Map<String, Integer> map2 = new HashMap<String, Integer>();
        for (String sequence : values1) {
            if (!map1.containsKey(sequence)) {
                map1.put(sequence, 0);
            }
            map1.put(sequence, map1.get(sequence) + 1);
        }

        for (String sequence : value2) {
            if (!map2.containsKey(sequence)) {
                map2.put(sequence, 0);
            }
            map2.put(sequence, map2.get(sequence) + 1);
        }

        Double result = 0.0;
        Double frequency2 = 0.0;
        for (String sequence : map1.keySet()) {

            Double frequency1 = (double) map1.get(sequence) / values1.size();
//            System.out.println("Freuency1 " + frequency1.toString());
            if (map2.containsKey(sequence)) {

                frequency2 = (double) map2.get(sequence) / value2.size();
            }
            result += frequency1 * (Math.log(frequency1 / frequency2) / Math.log(2));
        }

        double r = result / 2.4;

        return r;
    }
    int x = 1;
}
