angular.module('Enterprise2MapApp').
service('TTLParseService',function($q,sparqlQueryService){

  var baseURL = "http://localhost:3030/";
  var endPointURL="";

  this.parseTTL = function(endPointName='Factory'){
    var mainPromise = $q(function(mainResolve,mainReject){

      var parsedData = {};
      //console.log("parsed data: ");
      //console.log(parsedData);
      endPointURL = baseURL+endPointName;

      //sparql query to get the company info
      var companyQuery = sparqlQueryService.getCompanyQuery();
    //  console.log(companyQuery);
      jQuery.get(endPointURL,{query:companyQuery},function(results){
        var companies = [];
        var plantQueryPromise;
    //    console.log(results);
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentCompany = results.results.bindings[i];
          var plantObject = currentCompany.companyPlant;
          if(plantObject){
            plantQueryPromise  = getPlantData(currentCompany,plantObject);
            delete currentCompany.companyPlant;
          }
          delete currentCompany.subject;
          companies.push(currentCompany);
        }

        plantQueryPromise.then(function(resCompany){
          parsedData.companies = companies;
          mainResolve(parsedData);
        });
      });
    });//$q
    return mainPromise;
  }//parseTTL

  var getPolygonData = function(parentObject,polygonObject){
    var polygonQueryPromise = $q(function(resolve,reject){
      //sparql query to get factory polygon data
      var pQuery = sparqlQueryService.getPolygonQuery(polygonObject.value);
      //console.log(pQuery);
      var polygons = [];
      jQuery.get(endPointURL,{query:pQuery},function(results){
        var polygons = [];
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentPolygon = results.results.bindings[i];
          currentPolygon = {
            lat:currentPolygon.lat,
            long:currentPolygon.long,
            type:currentPolygon.polygonType
          }
          polygons.push(currentPolygon);
        }
        parentObject.polygons = polygons;
        resolve(parentObject);
      });//jQuery.get
    });//polygonQueryPromise $q
    return polygonQueryPromise;
  }//getPolygonData

  var getFactoryData = function(currentPlant,factoryObject){
    var factoryQueryPromise = $q(function(resolve,reject){
      //sparql query to get company factory data
      var fQuery = sparqlQueryService.getFactoryQuery(factoryObject.value);
      jQuery.get(endPointURL,{query:fQuery},function(results){
        var plantFactories = [];
        var polygonQueryPromise;
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentFactory = results.results.bindings[i];
          //console.log(currentFactory);

          var buildingObject = currentFactory.building;
          var polygonObject = currentFactory.polygon;

          if(buildingObject){
            getBuildingData(currentFactory,buildingObject);
            delete currentFactory.building;
          }
          if(polygonObject){
            polygonQueryPromise = getPolygonData(currentFactory,polygonObject);
            delete currentFactory.polygon;
          }
          plantFactories.push(currentFactory);
        }
        polygonQueryPromise.then(function(resolution){
          currentPlant.factories = plantFactories;
          resolve(currentPlant);
        });
      });//jQuery.get
    });//factoryQueryPromise $q
    return factoryQueryPromise;
  }//getFactoryData

  var getBuildingData = function(parentObject,buildingObject){
    var buildingQueryPromise = $q(function(resolve,reject){
      var bQuery = sparqlQueryService.getBuildingQuery(buildingObject.value);
      jQuery.get(endPointURL,{query:bQuery},function(results){
        var buildings = [];
        var machineQueryPromise;
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentBuilding = results.results.bindings[i];

          var machineObject = currentBuilding.machine;
          var polygonObject = currentBuilding.polygon;

          if(machineObject){
            machineQueryPromise = getMachineData(currentBuilding,machineObject);
            delete currentBuilding.machine;
          }

          if(polygonObject){
            getPolygonData(currentBuilding,polygonObject);
            delete currentBuilding.polygon;
          }

          buildings.push(currentBuilding);
        }

        machineQueryPromise.then(function(resolution){
          parentObject.buildings = buildings;
          resolve(parentObject);
        });

      });//jQuery.get
    });//$q
    return buildingQueryPromise;
  }

  var getMachineData = function(parentObject){
    var machineQueryPromise = $q(function(resolve,reject){
      var machineObject = parentObject.machine;
      var mQuery = sparqlQueryService.getMachineQuery(machineObject.value);
      jQuery.get(endPointURL,{query:mQuery},function(results){
        var machines = [];
        var machinePolygonPromise;
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentMachine = results.results.bindings[i];

          var polygonObject = currentMachine.polygon;

          if(polygonObject){
            machinePolygonPromise = getPolygonData(currentMachine,polygonObject);
            delete currentMachine.polygon;
          }

          machines.push(currentMachine);

        }
        machinePolygonPromise.then(function(resolution){
          parentObject.machines = machines;
          resolve(parentObject);
        });

      });//jQuery.get
    });//$q
    return machineQueryPromise;
  }


  var getPlantData = function(currentCompany,plantObject){
    var plantQueryPromise = $q(function(resolve,reject){
      //sparql query to get company plant data
      var pQuery = sparqlQueryService.getPlantQuery(plantObject.value);

      jQuery.get(endPointURL,{query:pQuery},function(results){
        var companyPlants = [];
        var factoryQueryPromise;
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentPlant = results.results.bindings[i];

          var factoryObject = currentPlant.plantFactory;
          if(factoryObject){
            factoryQueryPromise = getFactoryData(currentPlant,factoryObject);
          }

          delete currentPlant.plantFactory;
          companyPlants.push(currentPlant);
        }
        factoryQueryPromise.then(function(resolution){
          currentCompany.plants = companyPlants;
          resolve(currentCompany);
        });
      });//jQuery.get
    });//$q
    return plantQueryPromise;
  }//getPlantData

});//service
