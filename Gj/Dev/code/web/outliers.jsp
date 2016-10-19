<%@page import="test.StatisHelper"%>
<%@page import="ss16lab.outliers.KMeans"%>
<%@page import="ss16lab.outliers.OutlierMethod"%>
<%@page import="web.DataHelper"%>
<%@page import="ss16lab.outliers.Chauvenet"%>
<%@page import="ss16lab.outliers.OutlierDetector"%>
<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@page import="org.apache.poi.util.ArrayUtil"%>
<%@page import="ss16lab.data.linking.FusekiAPI"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonParser"%>
<%@page import="com.google.gson.JsonObject"%>
<%
    String outliePropertyName = request.getParameter("find");
    String sessionID = request.getParameter("sessionID");
    String bucket = request.getParameter("bucket");
    String method = request.getParameter("method");
    String fileName = sessionID + "/" + bucket;

    String outliersInfoJson = "";
    if (DataHelper.cachedOutlierResults.containsKey(sessionID + bucket + method)) {// cached results
        outliersInfoJson = DataHelper.cachedOutlierResults.get(sessionID + bucket + method);
    } else {
        FusekiAPI fu = new FusekiAPI();

        List<String> IDS = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(application.getRealPath("/").replace('\\', '/') + "subpopulation/" + fileName));
        for (String line; (line = br.readLine()) != null;) {
            String[] tokens = line.split(":");
            if (tokens.length > 1) {
                IDS.add(tokens[0]);
            }
        }

        HashMap<String, Double> hm = fu.fetchCandidateOutliers(IDS.toArray(new String[0]), outliePropertyName);
        StatisHelper.saveToExcelFile(hm, DataHelper.rootDirectory + "/excels/" + bucket.replace("/", "-") + ".xls");
        StatisHelper.saveToStatisFile(hm, 
                DataHelper.rootDirectory + "/statistics/" + bucket.replace("/", "-"),
                DataHelper.rootDirectory + "/statistics/");

        List<Double> samples = new ArrayList();

        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            samples.add((Double) pair.getValue());
        }
        OutlierMethod outlierMethod = null;
        if (method.equals("chauvenet")) {
            outlierMethod = new Chauvenet(samples.toArray(new Double[0]));
        } else {
            outlierMethod = new KMeans(samples.toArray(new Double[0]));
        }

        OutlierDetector outloutlierDetector = new OutlierDetector(outlierMethod);

        it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (!outloutlierDetector.outliers.contains(pair.getValue())) {
                IDS.remove(pair.getKey());
            }
        }

        outliersInfoJson = fu.fetchObservationsInfo(IDS.toArray(new String[0]), outliePropertyName);

        HashMap<String, String> locationsHash = new HashMap();
        JsonArray jsonArr = new JsonParser().parse(outliersInfoJson).getAsJsonArray();
        int x = 0;
        for (int i = 0; i < jsonArr.size(); i++) {
            JsonObject obj = jsonArr.get(i).getAsJsonObject();
            String latitude = obj.get("lat").getAsString();
            String longitude = obj.get("lng").getAsString();
            if (!locationsHash.containsKey(latitude.toString() + ":" + longitude.toString())) {
                locationsHash.put(latitude.toString() + ":" + longitude.toString(), "<table>");
            }

        }
        String functionLabel, functionDefinition;

        for (int i = 0; i < jsonArr.size(); i++) {
            JsonObject obj = jsonArr.get(i).getAsJsonObject();
            String latitude = obj.get("lat").getAsString();
            String longitude = obj.get("lng").getAsString();
            String ID = obj.get("ID").getAsString();

            functionLabel = "";
            functionDefinition = "";
            if (obj.get("functionLabel").isJsonObject()) {
                functionLabel = obj.get("functionLabel").getAsJsonObject().get("value").getAsString();
            }
            if (obj.get("functionDefinition").isJsonObject()) {
                functionDefinition = obj.get("functionDefinition").getAsJsonObject().get("value").getAsString();
            }

            String amountNational = obj.get("amount").getAsString();

            String tmp = locationsHash.get(latitude + ":" + longitude);
            String tmp2 = "<tr>"
                    + "<td>"
                    + "<div>"
                    + "Observation(" + ID + ")"
                    + "</div>"
                    + "</td>"
                    + "<td>Amount: " + amountNational + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td colspan=2>"
                    + "<div>"
                    + functionDefinition
                    + "</div>"
                    + "</td>"
                    + "</tr>"
                    + "<tr><td colspan=2></td></tr><tr><td colspan=2></td></tr><tr><td colspan=2></td></tr>";

            locationsHash.replace(latitude + ":" + longitude, tmp + tmp2);
        }
        it = locationsHash.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            pair.setValue(pair.getValue() + "</table>");
        }

        outliersInfoJson = "[";
        System.out.print(outliersInfoJson);

        it = locationsHash.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Gson gson = new Gson();
            JsonObject newobj = new JsonObject(); //Json Object
            String key = (String) pair.getKey();
            String value = (String) pair.getValue();

            newobj.add("lat", gson.toJsonTree(Float.parseFloat(key.split(":")[0])));
            newobj.add("lng", gson.toJsonTree(Float.parseFloat(key.split(":")[1])));
            newobj.add("info", gson.toJsonTree(value));
            outliersInfoJson += newobj.toString();
            outliersInfoJson += ",";
        }

        outliersInfoJson += "]";
        outliersInfoJson = outliersInfoJson.replace(",]", "]");
        DataHelper.cachedOutlierResults.put(sessionID + bucket + method, outliersInfoJson);
    }

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Outlier Detection Using <%= method%></title>
        <link rel="stylesheet" type="text/css" href="static/css/style.css" />

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <script type="text/javascript" src="static/js/angular/angular.min.js"></script>
        <script type="text/javascript" src="static/js/angular/controllers.js"></script>
        <style>
            table td {
                padding: 5px;
            }
        </style>
    </head>
    <body>
        <div id="map"></div>
        <script>
            var outliers = '<%= outliersInfoJson%>';
            // This example adds a marker to indicate the position of Bondi Beach in Sydney,
            // Australia.
            function initMap() {
                var map = new google.maps.Map(document.getElementById("map"), {
                    zoom: 4,
                    center: {lat: 50.0598057, lng: 14.3255392},
                    zoomControl: false,
                    scaleControl: false,
                    mapTypeControl: false,
                });
                var image = "./static/imgs/icons/outlier.png";
                $(jQuery.parseJSON(outliers)).each(function (key, value) {
                    var marker = new google.maps.Marker({
                        position: {lat: value.lat, lng: value.lng},
                        map: map,
                        icon: image,
                        animation: google.maps.Animation.BOUNCE,
                    });
                    var infowindow = new google.maps.InfoWindow({
                        content: value.info
                    });
                    marker.addListener("click", function () {
                        infowindow.open(map, marker);
                    });
                });
            }
        </script>
        <script async defer
                src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAvve5XJD-HaIftFxkwVygYeSAguNOi0Gg&callback=initMap">
        </script>
    </body>
</html>
