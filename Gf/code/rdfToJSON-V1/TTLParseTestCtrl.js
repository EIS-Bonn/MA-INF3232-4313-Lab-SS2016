// properties

var mymap;

var markersToAdd;
var polygonsToAdd;

var markersCount = 0;
var polygonsCount = 0;

// companies data
var comps = [];

var iconHolder;
var iconNames = [];

var filterMap = ["company", "factory", "building", "machine", "No"];

var filterCountryMap = ["de", "us"];

var app = angular.module('Enterprise2MapApp');



// methods

function processObjectsArray(objArray, detailsToAppendToPopUp, filterType, filterFeature, filterCountry)
{
    var objNumber = 0;
    for (; objNumber < objArray.length; objNumber++)
    {
        var obj = objArray[objNumber];
        processObject(obj, detailsToAppendToPopUp, filterType, filterFeature, filterCountry);
    }
}

function processObject(obj, detailsToAppendToPopUp, filterType, filterFeature, filterCountry)
{
    // create its own popUp
    var popUpContent = createPopUp(obj, detailsToAppendToPopUp);

    // create its Annotation if required
    createAnnotation(obj, popUpContent, filterType, filterFeature, filterCountry);

    // check in case it has more arrays, process them too
    var objKeys = Object.keys(obj);
    if(obj.hasOwnProperty("polygons") && objKeys.length==1)
    {
        // ignore in this case
    }
    else
    {
        var keyNumber=0;
        for(; keyNumber < objKeys.length; keyNumber++)
        {
            var keyValue = objKeys[keyNumber];
            var subObj = obj[keyValue];
            if(keyValue!="polygons" && subObj instanceof Array)
            {
                processObjectsArray(subObj, popUpContent, filterType, filterFeature, filterCountry);
            }
        }
    }
}

function createPopUp(obj, detailsToAppendToPopUp)
{
    var popupContent = "";

    if(typeof(obj) == "object")
    {
        objKeys = Object.keys(obj);
        var propNumber=0;
        for(; propNumber<objKeys.length; propNumber++)
        {
            if(typeof(obj[objKeys[propNumber]]) == "object"
                && !(typeof(obj[objKeys[propNumber]]) instanceof Array)
                && objKeys[propNumber] != "polygons"
                && objKeys[propNumber] != "subject")
            {
                if(obj[objKeys[propNumber]].type == "literal") {
                    popupContent += objKeys[propNumber] + ": " + obj[objKeys[propNumber]].value + "</br>";
                }
                else if(obj[objKeys[propNumber]].type == "uri") {
                    popupContent += objKeys[propNumber] + ": " + "<a href=" + obj[objKeys[propNumber]].value + "> " + obj[objKeys[propNumber]].value + " </a></br>";
                }
            }
        }
    }

    // temporary fix to show building icon in case there is no content in the popup
    if(popupContent == "")
    {
        popupContent = "No further details about the item. </br>";
    }

    popupContent += " -------------------- </br>"
    popupContent += detailsToAppendToPopUp;

    return popupContent;
}

var getJSON = function (url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open("get", url, true);
    xhr.responseType = "json";
    xhr.onload = function () {
        var status = xhr.status;
        if (status == 200) {
            callback(null, xhr.response);
        } else {
            callback(status);
        }
    };
    xhr.send();
};


function requestAddressNominatim(latLong, countryCode, itemType, item) {
    var data;
    getJSON('http://nominatim.openstreetmap.org/reverse?format=json&lat=' + parseFloat(latLong.valueOf().lat) + '&lon=' + parseFloat(latLong.valueOf().lng) + '&format=json', function (err, data) {
        if(data.address.country_code == countryCode)
        {
            if(itemType == "m")
            {
                markersToAdd.addLayer(item);
                markersCount += 1;
            }
            else if(itemType == "p")
            {
                polygonsToAdd.addLayer(item);
                polygonsCount += 1;
            }
        }
    });
}

function createAnnotation(obj, popUpContent, filterType, filterFeature, filterCountry)
{
    if(obj.hasOwnProperty("polygons"))
    {
        // setting the icon to use
        //var iconToUse = new iconHolder({iconUrl: 'data/'+"notfound"+'.png', iconSize: [12,12]});
        var iconToUse = new iconHolder({iconUrl: 'data/'+"notfound2"+'.png', iconSize: [30,30]});
        var iconSet = false;
        var tempString = popUpContent.substring(0, 9);

        var j=0;
        for(; j<iconNames.length; j++)
        {
            if(tempString.indexOf(iconNames[j]) != -1)
            {
                iconToUse = new iconHolder({iconUrl: 'data/'+iconNames[j]+'.png'});

                break;
            }
        }

        if(obj.polygons.length==1)
        {
            // create marker in this case
            var point = obj.polygons[0];
            var marker = L.marker([parseFloat(point.lat.value), parseFloat(point.long.value)], {icon: iconToUse})
                .bindPopup(popUpContent);
            marker.on('click', function (e) {
                centerLeafletMapOnMarker(this);
            });
            marker.on('mouseover', function (e) {
                this.openPopup();
            });
            marker.on('mouseout', function (e) {
                this.closePopup();
            });

            if(filterType == 0)
            {
                if(filterFeature=="" || tempString.indexOf(filterFeature) != -1)
                {
                    markersToAdd.addLayer(marker);
                    markersCount +=1;
                }
            }
            else if(filterType == 1)
            {
                requestAddressNominatim(centerOfPolygon, filterCountry, "m", marker)
            }
            else
            {
                markersToAdd.addLayer(marker);
                markersCount +=1;
            }
        }
        else if(obj.polygons.length>1)
        {
            // create polygon in this case and a marker in the middle
            var polygonPointsArray = [];
            var pointNumber=0;
            for(; pointNumber<obj.polygons.length; pointNumber++)
            {
                point = obj.polygons[pointNumber];
                polygonPointsArray.push([parseFloat(point.lat.value), parseFloat(point.long.value)])
            }

            var polygonToAdd = L.polygon(polygonPointsArray).bindPopup(popUpContent);
            var centerOfPolygon = polygonToAdd.getBounds().getCenter();
            if(filterType == 0)
            {
                if(filterFeature=="" || tempString.indexOf(filterFeature) != -1) {
                    polygonsToAdd.addLayer(polygonToAdd);
                    polygonsCount += 1;
                }
            }
            else if(filterType == 1)
            {
                requestAddressNominatim(centerOfPolygon, filterCountry, "p", polygonToAdd)
            }
            else
            {
                polygonsToAdd.addLayer(polygonToAdd);
                polygonsCount += 1;
            }

            var companyMarker = L.marker(centerOfPolygon, {icon: iconToUse})
                .bindPopup(popUpContent);
            companyMarker.on('click', function (e) {
                centerLeafletMapOnMarker(this);
            });
            companyMarker.on('mouseover', function (e) {
                this.openPopup();
            });
            companyMarker.on('mouseout', function (e) {
                this.closePopup();
            });
            if(filterType == 0)
            {
                if(filterFeature=="" || tempString.indexOf(filterFeature) != -1) {
                    markersToAdd.addLayer(companyMarker);
                    markersCount += 1;
                }
            }
            else if(filterType == 1)
            {
                requestAddressNominatim(centerOfPolygon, filterCountry, "m", companyMarker);
            }
            else
            {
                markersToAdd.addLayer(companyMarker);
                markersCount +=1;
            }
        }
    }
}

function centerLeafletMapOnMarker(marker) {
    var latLngs = [ marker.getLatLng() ];
    var markerBounds = L.latLngBounds(latLngs);
    mymap.fitBounds(markerBounds);
}

function showAllElementsOnMap() {
    mymap.addLayer(polygonsToAdd);
    mymap.addLayer(markersToAdd);
}

function removeAllElementsFromMap() {
    // show items on map

    mymap.removeLayer(markersToAdd);
    mymap.removeLayer(polygonsToAdd);
    markersCount = 0;
    polygonsCount = 0;
}

function processEverything(filterType, filterNumber)
{
    if(markersToAdd!=null && polygonsToAdd!=null)
        removeAllElementsFromMap();

    markersToAdd = new L.FeatureGroup();
    polygonsToAdd = new L.FeatureGroup();
    markersCount = 0;
    polygonsCount = 0;

    var filterFeature;
    if(filterType == 0)
    {
        filterFeature = filterMap[filterNumber];
        if(filterFeature == null)
            filterFeature = "";
    }

    var filterCountry;
    if(filterType == 1)
    {
        filterCountry = filterCountryMap[filterNumber];
        if(filterCountry == null)
            filterCountry = "";
    }

    var i=0;
    for(; i<comps.length; i++)
    {
        processObject(comps[i], "", filterType, filterFeature, filterCountry);
    }

    // show items on map
    showAllElementsOnMap();
}

app.controller('TTLParseTestCtrl', function ($scope, TTLParseService) {
    $scope.parsedTTL = "";
    var promise = TTLParseService.parseTTL('Factory');
    promise.then(function (resolution) {
        $scope.parsedTTL = resolution;

        //$scope.plotDataOnMap();
        console.log($scope.parsedTTL);
        var normalPopUpMap = {};
        var detailedPopUpMap = {};

        $scope.generateMapAnnotations(normalPopUpMap, detailedPopUpMap);
        


    }, function (rejection) {
        console.error(rejection);
    });

    $scope.generateMapAnnotations = function (normalPopUpMap, detailedPopUpMap)
    {
        mymap = L.map('map', {}).setView([-0.515278, 47.501111], 2);

        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiczZhc2FsdGEiLCJhIjoiY2lveXUwb3dqMDBlaXZ2bHdoZjQ5dHlrbiJ9.xKMDfR_36OSyxiBT_jftig', {
            maxZoom: 18,
            attribution: 'EIS LAB 2016',
            id: 'mapbox.streets'
        }).addTo(mymap);

        // volkswagenIcon
          iconHolder = L.Icon.extend({
            options: {
                iconSize: [30, 30],
                iconAnchor: [15, 15],
                popupAnchor: [0, -10]
            }
        });

        //var vw_icon = new iconHolder({iconUrl: 'data/vw-logo.png'});

        iconNames.push("company");
        iconNames.push("factory");
        iconNames.push("industry");
        iconNames.push("machine");
        iconNames.push("plant");
        iconNames.push("building");
        iconNames.push("vw-logo");

        // todo: loop all companies and pass them to createPopUp function
        // and save popups in respective maps against their key values that will be used later on

        comps = $scope.parsedTTL.companies;

        // process all companies
        var filterFeature = ""
        processEverything(filterFeature);
    }

    $scope.plotDataOnMap = function () {
        console.log($scope.parsedTTL);

        var companies = $scope.parsedTTL.companies;
        var markers = [];

        // all the data that is shown later on
        var polygonsPoints = [];

        // loop on the object, get the values and create objects to show on map
        var comp = 0;
        for (; comp < companies.length; comp++) {
            var company = companies[comp];
            var polygonPointsArray = [];
            polygonsPoints.push(polygonPointsArray);
            var plan = 0;
            for (; plan < company.plants.length; plan++) {
                var fact = 0;
                for (; fact < company.plants[plan].factories.length; fact++) {
                    var poly = 0;
                    for (; poly < company.plants[plan].factories[fact].polygons.length; poly++) {
                        var polygon = company.plants[plan].factories[fact].polygons[poly];
                        var marker = L.marker([parseFloat(polygon.lat.value), parseFloat(polygon.long.value)], {icon: vw_icon});
                        polygonPointsArray.push([parseFloat(polygon.lat.value), parseFloat(polygon.long.value)])
                        marker.bindPopup(company.companyName.value);
                        markers.push(marker);
                    }
                }
            }
        }

        var popUpMap = {};
        var objectIdHolder;
        var mymap = L.map('map', {}).setView([-0.515278, 47.501111], 2);

        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiczZhc2FsdGEiLCJhIjoiY2lveXUwb3dqMDBlaXZ2bHdoZjQ5dHlrbiJ9.xKMDfR_36OSyxiBT_jftig', {
            maxZoom: 18,
            attribution: 'EIS LAB 2016',
            id: 'mapbox.streets'
        }).addTo(mymap);

        // add the markersToAdd to the mapview
        for (i = 0; i < markers.length; i++) {
            //markersToAdd[i].addTo(mymap);
            //markersToAdd[i].openPopup();
        }

        // add polygonsToAdd to the mapview
        for (i = 0; i < polygonsPoints.length; i++) {
            var company = companies[i];

            var polygonPointsArray = polygonsPoints[i];
            var polygonToAdd = L.polygon(polygonPointsArray);
            polygonToAdd.addTo(mymap).bindPopup(company.companyName.value).openPopup();

            var centerOfPolygon = polygonToAdd.getBounds().getCenter();

            var companyMarker = L.marker(centerOfPolygon, {icon: vw_icon}).bindPopup(company.companyName.value);
            companyMarker.addTo(mymap);
        }

        function onEachFeature(feature, layer) {
            var popupContent = "";

            if (feature.properties) {
                if (feature.properties.OBJECTID) {
                    objectIdHolder = feature.properties.OBJECTID
                    popupContent += "Factory ID: " + feature.properties.OBJECTID + "</br></br>";
                }
                if (feature.properties.POPUP_CONTENT) {
                    popupContent += feature.properties.POPUP_CONTENT;
                }
                if (feature.properties.LINK) {
                    popupContent += "</br></br><a href=" + feature.properties.LINK + "> " + feature.properties.LINK + " </a> ";
                }
            }

            if (objectIdHolder in popUpMap) {
                layer.bindPopup(popUpMap[objectIdHolder]);
            }

            else {
                popUpMap[objectIdHolder] = popupContent;
                layer.bindPopup(popupContent);
            }
        }

        L.geoJson(all_features, {

            style: function (feature) {
                return feature.properties && feature.properties.style;
            },

            onEachFeature: onEachFeature,

            pointToLayer: function (feature, latlng) {
                return L.marker(latlng, {
                    radius: 8,
                    fillColor: "#ff7800",
                    color: "#000",
                    weight: 1,
                    opacity: 1,
                    fillOpacity: 0.8,
                    icon: vw_icon
                });
            }
        });
        //}).addTo(mymap);
    }

});
