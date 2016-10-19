/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss16lab.data.linking;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.net.*;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Class contains all functionality to interact with Fuseki server
 *
 * @author Gj
 */
public class FusekiAPI {

    public LinkedList<File> dataSets = new LinkedList();

    /**
     * Fetches observations information based on inputed IDs
     *
     * @param observations array of observations IDs
     * @param outliePropertyName outlier property name in RDF dataset
     * @return Json format of observations information
     * @author Gj
     */
    public String fetchObservationsInfo(String[] observations, String outliePropertyName)
            throws FileNotFoundException, IOException {
        String ret = "[";

        String serviceURI = "http://localhost:3030/test/data";
        DatasetAccessorFactory factory = null;
        DatasetAccessor accessor;
        accessor = factory.createHTTP(serviceURI);
        Model m = ModelFactory.createDefaultModel();
        for (int i = 0; i < observations.length; i++) {
            String observation = observations[i];
            String q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                    + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                    + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                    + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                    + "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n"
                    + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                    + "PREFIX datacube: <http://example.org/datacube/>\n"
                    + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                    + "\n"
                    + "SELECT (str(?observation) as ?ID) (str(?prefLabel) AS ?functionLabel)  (str(?definition) AS ?functionDefinition) \n"
                    + "(str(?lat) AS ?latitude) (str(?long) AS ?longitude) (str(?value) AS ?amount)\n"
                    + "WHERE {\n"
                    + " ?observation a qb:Observation.\n"
                    + "?observation   datacube:info_lat ?lat .\n"
                    + "?observation   datacube:info_long ?long .\n"
                    + "?observation " + outliePropertyName + " ?value .\n"
                    + "OPTIONAL {"
                    + " ?observation <http://data.openbudgets.eu/ontology/dsd/ESIF-2014-2020/dimension/functionalClassification> ?function.\n"
                    + " ?function skos:prefLabel ?prefLabel.\n"
                    + " ?function skos:definition ?definition.\n"
                    + "}"
                    + "FILTER (contains(str(?observation),'" + observation + "'))\n"
                    + "}", "UTF-8");
            InputStream queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(queryRes, "UTF-8"));
            JsonArray jsonArr = jsonObject.getAsJsonObject("results").getAsJsonArray("bindings");
            String functionLabel, functionDefinition, ID = "";
            if (jsonArr.size() > 0) {
                functionLabel = "";
                functionDefinition = "";
                ID = "";
                JsonObject obj = jsonArr.get(0).getAsJsonObject();

                ID = obj.get("ID").getAsJsonObject().get("value").getAsString();
                if (obj.get("functionLabel") != null) {
                    functionLabel = obj.get("functionLabel").getAsJsonObject().get("value").getAsString();
                }
                if (obj.get("functionDefinition") != null) {
                    functionDefinition = obj.get("functionDefinition").getAsJsonObject().get("value").getAsString();
                }
                float latitude = obj.get("latitude").getAsJsonObject().get("value").getAsFloat();
                float longitude = obj.get("longitude").getAsJsonObject().get("value").getAsFloat();
                double amountValue = obj.get("amount").getAsJsonObject().get("value").getAsDouble();
                Gson gson = new Gson();
                JsonObject newobj = new JsonObject(); //Json Object

                newobj.add("lat", gson.toJsonTree(latitude));
                newobj.add("lng", gson.toJsonTree(longitude));
                newobj.add("functionLabel", gson.toJsonTree(functionLabel));
                newobj.add("functionDefinition", gson.toJsonTree(functionDefinition));
                newobj.add("amount", gson.toJsonTree(amountValue));
                newobj.add("ID", gson.toJsonTree(observation));

                ret += newobj.toString();
                ret += ",";
            }
        }
        ret += "]";
        ret = ret.replace(",]", "]");
        return ret;
    }

    /**
     * Fetches candidate outlier values based on inputed observations IDs
     *
     * @param observations array of observations IDs
     * @param outliePropertyName outlier property name in RDF dataset
     * @return HashMap contains observation ID liked to candidate outlier value
     * @author Gj
     */
    public HashMap<String, Double> fetchCandidateOutliers(String[] observations, String outliePropertyName)
            throws FileNotFoundException, IOException {
        HashMap<String, Double> ret = new HashMap<>();

        String serviceURI = "http://localhost:3030/test/data";
        DatasetAccessorFactory factory = null;
        DatasetAccessor accessor;
        accessor = factory.createHTTP(serviceURI);
        Model m = ModelFactory.createDefaultModel();
        for (int i = 0; i < observations.length; i++) {
            String observation = observations[i];
            String q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                    + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                    + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                    + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                    + "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n"
                    + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                    + "PREFIX datacube: <http://example.org/datacube/>\n"
                    + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                    + "\n"
                    + "SELECT (str(?value) AS ?val)  \n"
                    + "WHERE {\n"
                    + "?observation " + outliePropertyName + " ?value .\n"
                    + "FILTER (contains(str(?observation),'" + observation + "'))\n"
                    + "}", "UTF-8");
            InputStream queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(queryRes, "UTF-8"));
            JsonArray jsonArr = jsonObject.getAsJsonObject("results").getAsJsonArray("bindings");

            if (jsonArr.size() > 0) {
                JsonObject obj = jsonArr.get(0).getAsJsonObject();

                double val = obj.get("val").getAsJsonObject().get("value").getAsDouble();
                ret.put(observation, val);
            }
        }
        return ret;
    }

    /**
     * runs enrichment stage by linking between local dataset and DBPedia
     *
     * @param locationPredName location property name in RDF dataset
     * @author Gj
     */
    public void enrichDataSet(String locationPredName) throws FileNotFoundException, IOException {
        System.out.println("Starting enrichment stage");
        System.out.println();
        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println();

        String serviceURI = "http://localhost:3030/test/data";
        DatasetAccessorFactory factory = null;
        DatasetAccessor accessor;
        accessor = factory.createHTTP(serviceURI);

        Model m = ModelFactory.createDefaultModel();
        m.removeAll();
        putBaseModels(m);

        accessor.putModel(m);

        String q = URLEncoder.encode(
                "PREFIX datacube: <http://example.org/datacube/>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + " ?location datacube:isLocation ?location.\n"
                + "\n"
                + "}  \n"
                + " where {\n"
                + " ?subject " + locationPredName + " ?location .\n"
                + " \n"
                + "}", "UTF-8");
        InputStream queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res", "Turtle");
        accessor.putModel(m);

        System.out.println();
        System.out.println("Fetching RDF data from DBPedia");
        System.out.println();
        System.out.println("------------------------------------------");

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + " ?location dbo:populationTotal ?pop.\n"
                + " ?location geo:lat ?lat.\n"
                + " ?location geo:long ?long.\n"
                + " ?location dbo:areaTotal ?area. \n"
                + " ?location dbo:country ?country ."
                + " ?country dbp:gdpPppPerCapita ?countryGDP ."
                + " ?country dbo:populationTotal ?countryPopulation ."
                + " ?country dbo:areaTotal ?countryArea ."
                + "\n"
                + "}  \n"
                + " where {\n"
                + " ?subject datacube:isLocation ?location .\n"
                + " \n"
                + " SERVICE <http://dbpedia.org/sparql> { \n"
                + " ?location dbo:populationTotal ?pop.\n"
                + " ?location geo:lat ?lat.\n"
                + " ?location geo:long ?long.\n"
                + " OPTIONAL {"
                + " ?location dbo:areaTotal ?area ."
                + " ?location dbo:country ?country ."
                + " ?country dbp:gdpPppPerCapita ?countryGDP ."
                + " ?country dbo:populationTotal ?countryPopulation ."
                + " ?country dbo:areaTotal ?countryArea ."
                + "}\n"
                + "  } \n"
                + "}", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + " ?observation datacube:info_lat ?lat.\n"
                + " ?observation datacube:info_long ?long.\n"
                + " ?observation datacube:info_country ?country.\n"
                + "\n"
                + "}  \n"
                + " where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation " + locationPredName + " ?location.\n"
                + " ?location geo:lat ?lat.\n"
                + " ?location geo:long ?long.\n"
                + " OPTIONAL {"
                + " ?location dbo:country ?country."
                + "} \n"
                + " \n"
                + "}", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        attachAreaInfo(accessor, m, locationPredName);
        attachPopulationInfo(accessor, m, locationPredName);
        attachGDPInfo(accessor, m);

        System.out.println("Enrichment stage is finished");
        System.out.println();
        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println();
    }

    /**
     * saves model files in the Fuseki server
     *
     * @param m model file object
     * @author Gj
     */
    public void putBaseModels(Model m) {
        try {
            InputStream in;

            for (int i = 0; i < this.dataSets.size(); i++) {
                File file = this.dataSets.get(i);
                in = new FileInputStream(file);
                m.read(in, file.getName(), "Turtle");
            }

        } catch (Exception e) {
        }

    }

    /**
     * searches for a unique URI based on predicate name
     *
     * @param name predicate name to search for
     * @return String as unique URI
     * @author Gj
     */
    public String findUniquePredicate(String name) throws IOException {
        String ret = "";

        String serviceURI = "http://localhost:3030/test/data";
        DatasetAccessorFactory factory = null;
        DatasetAccessor accessor;
        accessor = factory.createHTTP(serviceURI);
        Model m = ModelFactory.createDefaultModel();

        String q = URLEncoder.encode("SELECT DISTINCT ?pred WHERE{\n"
                + "?sub ?pred ?obj.\n"
                + "  filter(regex(str(?pred),'" + name + "'))\n"
                + "} limit 1", "UTF-8");

        InputStream queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(new InputStreamReader(queryRes, "UTF-8"));
        JsonArray jsonArr = jsonObject.getAsJsonObject("results").getAsJsonArray("bindings");

        if (jsonArr.size() > 0) {
            JsonObject obj = jsonArr.get(0).getAsJsonObject();

            String pred = obj.get("pred").getAsJsonObject().get("value").getAsString();
            ret = "<" + pred + ">";
        }

        return ret;
    }

    /**
     * merges local dataset with the comes after enrichment stage
     *
     * @param fpath path of directory containing datasets to merge
     * @author Gj
     */
    public void mergeDatasets(String fpath) throws IOException {
        InputStream input = new URL("http://localhost:3030/test/data").openStream();
        File f = new File(fpath);
        Files.copy(input, f.toPath());

        /// remove '{' and '}' from generated file
        File tempFile = File.createTempFile("buffer", ".tmp");
        FileWriter fw = new FileWriter(tempFile);

        Reader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        while (br.ready()) {
            fw.write(br.readLine().replace("{", "").replace("}", "") + "\n");
        }

        fw.close();
        br.close();
        fr.close();

        // Finally replace the original file.
        Files.delete(f.toPath());
        tempFile.renameTo(new File(fpath));
    }

    /**
     * attaches location and country population information to the corresponding
     * observation
     *
     * @param accessor Fuseki server access object
     * @param m model file object
     * @param locationPredName location property name in RDF dataset
     * @author Gj
     */
    private void attachPopulationInfo(DatasetAccessor accessor, Model m, String locationPredName)
            throws IOException {
        System.out.println();
        System.out.println("Attaching location and country POPULATION information to observations");
        System.out.println();
        System.out.println("------------------------------------------");
        ////////// AREA LOCATION //////////
        String q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_population \"low\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation " + locationPredName + " ?location .\n"
                + " ?location dbo:populationTotal ?pop .\n"
                + "  filter(?pop > " + Ranges.locationPopLow + " &&  ?pop < " + Ranges.locationPopMid + ").\n"
                + "}\n"
                + "", "UTF-8");
        InputStream queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_population \"mid\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation " + locationPredName + " ?location .\n"
                + " ?location dbo:populationTotal ?pop .\n"
                + "  filter(?pop > " + Ranges.locationPopMid + " &&  ?pop < " + Ranges.locationPopHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_population \"high\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation " + locationPredName + " ?location .\n"
                + " ?location dbo:populationTotal ?pop .\n"
                + "  filter(?pop > " + Ranges.locationPopHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        ////////// POPULATION COUNTRY //////////
        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_countrypopulation \"low\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation datacube:info_country ?country .\n"
                + " ?country dbo:populationTotal ?pop .\n"
                + "  filter(?pop > " + Ranges.countryPopLow + " &&  ?pop < " + Ranges.countryPopMid + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_countrypopulation \"mid\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation datacube:info_country ?country .\n"
                + " ?country dbo:populationTotal ?pop .\n"
                + "  filter(?pop > " + Ranges.countryPopMid + " &&  ?pop < " + Ranges.countryPopHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_countrypopulation \"high\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation datacube:info_country ?country .\n"
                + " ?country dbo:populationTotal ?pop .\n"
                + "  filter(?pop > " + Ranges.countryPopHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);
    }

    /**
     * attaches location and country area information to the corresponding
     * observation
     *
     * @param accessor Fuseki server access object
     * @param m model file object
     * @param locationPredName location property name in RDF dataset
     * @author Gj
     */
    private void attachAreaInfo(DatasetAccessor accessor, Model m, String locationPredName)
            throws IOException {
        System.out.println();
        System.out.println("Attaching location and country AREA information to observations");
        System.out.println();
        System.out.println("------------------------------------------");
        ////////// AREA LOCATION //////////
        String q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_area \"low\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation " + locationPredName + " ?location .\n"
                + " ?location dbo:areaTotal ?area .\n"
                + "  filter(?area > " + Ranges.locationAreadLow + " &&  ?area < " + Ranges.locationAreadMid + ").\n"
                + "}\n"
                + "", "UTF-8");
        InputStream queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_area \"mid\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation " + locationPredName + " ?location .\n"
                + " ?location dbo:areaTotal ?area .\n"
                + "  filter(?area > " + Ranges.locationAreadMid + " &&  ?area < " + Ranges.locationAreadHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_area \"high\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation " + locationPredName + " ?location .\n"
                + " ?location dbo:areaTotal ?area .\n"
                + "  filter(?area >= " + Ranges.locationAreadHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        ////////// AREA COUNTRY //////////
        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_countryarea \"low\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation datacube:info_country ?country .\n"
                + " ?country dbo:areaTotal ?area .\n"
                + "  filter(?area > " + Ranges.countryAreadLow + " &&  ?area < " + Ranges.countryAreadMid + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_countryarea \"mid\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation datacube:info_country ?country .\n"
                + " ?country dbo:areaTotal ?area .\n"
                + "  filter(?area > " + Ranges.countryAreadMid + " &&  ?area < " + Ranges.countryAreadHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_countryarea \"high\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation datacube:info_country ?country .\n"
                + " ?country dbo:areaTotal ?area .\n"
                + "  filter(?area > " + Ranges.countryAreadHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);
    }

    /**
     * attaches GDP per capita for country to the corresponding observation
     * observation
     *
     * @param accessor Fuseki server access object
     * @param m model file object
     * @author Gj
     */
    private void attachGDPInfo(DatasetAccessor accessor, Model m)
            throws IOException {
        System.out.println();
        System.out.println("Attaching GDP information to observations");
        System.out.println();
        System.out.println("------------------------------------------");

        ////////// GDP COUNTRY //////////
        String q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_countryGDP \"low\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation datacube:info_country ?country .\n"
                + " ?country dbp:gdpPppPerCapita ?gdp .\n"
                + "  filter(xsd:float(?gdp) > " + Ranges.countryGDPLow + " &&  xsd:float(?gdp)  < " + Ranges.countryGDPMid + ").\n"
                + "}\n"
                + "", "UTF-8");
        InputStream queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_countryGDP \"mid\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation datacube:info_country ?country .\n"
                + " ?country dbp:gdpPppPerCapita ?gdp .\n"
                + "  filter(xsd:float(?gdp)  > " + Ranges.countryGDPMid + " &&  xsd:float(?gdp)  < " + Ranges.countryGDPHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);

        q = URLEncoder.encode("PREFIX dbpedia: <http://dbpedia.org/resource/>\n"
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX dbp: <http://dbpedia.org/property/>\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "PREFIX datacube: <http://example.org/datacube/>\n"
                + "PREFIX qb: <http://purl.org/linked-data/cube#>\n"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                + "\n"
                + "CONSTRUCT \n"
                + "{\n"
                + "   ?observation datacube:info_countryGDP \"high\".\n"
                + "}  \n"
                + "where {\n"
                + " ?observation a qb:Observation .\n"
                + " ?observation datacube:info_country ?country .\n"
                + " ?country dbp:gdpPppPerCapita ?gdp .\n"
                + "  filter(xsd:float(?gdp)  > " + Ranges.countryGDPHigh + ").\n"
                + "}\n"
                + "", "UTF-8");
        queryRes = new URL("http://localhost:3030/test/query?query=" + q).openStream();
        m.read(queryRes, "res2", "Turtle");
        accessor.putModel(m);
    }

    public void retreiveModels() {
        String serviceURI = "http://localhost:3030/test/data";
        DatasetAccessorFactory factory = null;
        DatasetAccessor accessor;
        accessor = factory.createHTTP(serviceURI);

        Model m = ModelFactory.createDefaultModel();
        m.removeAll();
        putBaseModels(m);
        accessor.putModel(m);
    }
}

class Ranges {

    public static double locationAreadLow = 1000000d;
    public static double locationAreadMid = 1962500000d;
    public static double locationAreadHigh = 3925000000d;

    public static double locationPopLow = 1000d;
    public static double locationPopMid = 1000000d;
    public static double locationPopHigh = 2000000d;

    public static double countryAreadLow = 1000000d;
    public static double countryAreadMid = 178584000000d;
    public static double countryAreadHigh = 357168000000d;

    public static double countryPopLow = 10000d;
    public static double countryPopMid = 20000000d;
    public static double countryPopHigh = 60000000d;

    public static double countryGDPLow = 1000d;
    public static double countryGDPMid = 32000d;
    public static double countryGDPHigh = 45000d;

}
