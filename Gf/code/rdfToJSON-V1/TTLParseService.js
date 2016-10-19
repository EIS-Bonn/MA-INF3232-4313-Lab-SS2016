angular.module('Enterprise2MapApp').
service('TTLParseService',function($q,sparqlQueryService){

  var baseURL = "http://localhost:3030/";
  var endPointURL="";

  this.parseTTL = function(endPointName='Factory'){
    var mainPromise = $q(function(mainResolve,mainReject){

      var parsedData = {};
      console.log("parsed data: ");
      console.log(parsedData);
      endPointURL = baseURL+endPointName;

      var promises = [];
      //sparql query to get the company info
      var companyQuery = sparqlQueryService.getCompanyQuery();
      //console.log(companyQuery);
      jQuery.get(endPointURL,{query:companyQuery},function(results){
        var companies = [];
        //console.log(results);
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentCompany = results.results.bindings[i];
          //console.log(results);
          var plantObject = currentCompany.companyPlant;
          if(plantObject){
            var plantQueryPromise  = getPlantData(currentCompany,plantObject);
            promises.push(plantQueryPromise);
            delete currentCompany.companyPlant;
          }
          delete currentCompany.subject;
          companies.push(currentCompany);
        }

        var promiseFulfillCount = 0;
    //    console.log(promises);
        for(var i =0 ;i<promises.length;i++){
          var currentPromise = promises[i];
          currentPromise.then(function(res){
            promiseFulfillCount++;
            //console.log(promiseFulfillCount);
            //console.log(promiseFulfillCount+"...."+promises.length);
            if(promiseFulfillCount==promises.length){
              var companiesToRemove = [];
              for(var i=0; i<companies.length;i++){
                var currentCompany = companies[i];
                for(var j=i+1;j<companies.length;j++){
                  var otherCompany = companies[j];
                  //console.log("i: "+i+"    j: "+j);
                  //console.log(currentCompany.companyName.value);
                  //console.log(otherCompany.companyName.value);
                  if(!companiesToRemove.includes(j)){
                    if(currentCompany.companyName.value==otherCompany.companyName.value){
                      //console.log("match");
                      for(var k=0;k<otherCompany.plants.length;k++){
                        currentCompany.plants.push(otherCompany.plants[k]);
                      }//for
                      companiesToRemove.push(j);
                      //console.log(companiesToRemove);
                    }//if
                  }//if
                }//for
              }//for

              //console.log("total comps: "+companies.length);
              //console.log("to remove: "+companiesToRemove.length);
              for(var i =0;i<companiesToRemove.length;i++){
                var index = companiesToRemove[i];
                companies.splice(index,1);
                for(var j =i+1; j<companiesToRemove.length;j++){
                  var nextIndex = companiesToRemove[j];
                  if(nextIndex>index){
                    companiesToRemove[j] = --nextIndex;
                  }
                }
              }
              parsedData.companies = companies;
              //console.log(parsedData);
              mainResolve(parsedData);
            }
          });
        }//for

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
      //console.log(fQuery);
      var promises = [];
      jQuery.get(endPointURL,{query:fQuery},function(results){
        var plantFactories = [];
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentFactory = results.results.bindings[i];
          //console.log(currentFactory);

          var buildingObject = currentFactory.building;
          var polygonObject = currentFactory.polygon;

          if(buildingObject){
            var promise = getBuildingData(currentFactory,buildingObject);
            promises.push(promise);
            delete currentFactory.building;
          }
          if(polygonObject){
            var polygonQueryPromise = getPolygonData(currentFactory,polygonObject);
            promises.push(polygonQueryPromise);
            delete currentFactory.polygon;
          }
          plantFactories.push(currentFactory);
        }

        var promiseFulfillCount = 0;
        for(var i =0 ;i<promises.length;i++){
          var currentPromise = promises[i];
          currentPromise.then(function(res){
            promiseFulfillCount++;

            if(promiseFulfillCount==promises.length){
              currentPlant.factories = plantFactories;
              resolve(currentPlant);
            }

          });

        }//for
      });//jQuery.get
    });//factoryQueryPromise $q
    return factoryQueryPromise;
  }//getFactoryData

  var getBuildingData = function(parentObject,buildingObject){
    var buildingQueryPromise = $q(function(resolve,reject){
      var bQuery = sparqlQueryService.getBuildingQuery(buildingObject.value);
      jQuery.get(endPointURL,{query:bQuery},function(results){
        var buildings = [];
        var promises = [];
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentBuilding = results.results.bindings[i];

          var machineObject = currentBuilding.machine;
          var polygonObject = currentBuilding.polygon;
          //console.log(currentBuilding);
          if(machineObject){
            var promise = getMachineData(currentBuilding,machineObject);
            promises.push(promise);
            delete currentBuilding.machine;
          }

          if(polygonObject){
            var queryPromise = getPolygonData(currentBuilding,polygonObject);
            promises.push(queryPromise);
            delete currentBuilding.polygon;
          }

          buildings.push(currentBuilding);
        }

        var promiseFulfillCount = 0;
    //    console.log(promises);
        for(var i =0 ;i<promises.length;i++){
          var currentPromise = promises[i];
          currentPromise.then(function(res){
            promiseFulfillCount++;
            //console.log(promiseFulfillCount);

            if(promiseFulfillCount==promises.length){
              //console.log(promises);
              parentObject.buildings = buildings;
              resolve(parentObject);

            }
          });
        }//for

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
        var promises = [];
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentMachine = results.results.bindings[i];

          var polygonObject = currentMachine.polygon;

          if(polygonObject){
            var machinePolygonPromise = getPolygonData(currentMachine,polygonObject);
            promises.push(machinePolygonPromise);
            delete currentMachine.polygon;
          }

          machines.push(currentMachine);

        }

        var promiseFulfillCount = 0;
    //    console.log(promises);
        for(var i =0 ;i<promises.length;i++){
          var currentPromise = promises[i];
          currentPromise.then(function(res){
            promiseFulfillCount++;
            if(promiseFulfillCount==promises.length){
              parentObject.machines = machines;
              resolve(parentObject);

            }
          });
        }//for
      });//jQuery.get
    });//$q
    return machineQueryPromise;
  }


  var getPlantData = function(currentCompany,plantObject){
    var plantQueryPromise = $q(function(resolve,reject){
      //sparql query to get company plant data
      var pQuery = sparqlQueryService.getPlantQuery(plantObject.value);
      //console.log(pQuery);
      var promises = [];
      jQuery.get(endPointURL,{query:pQuery},function(results){
        var companyPlants = [];
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentPlant = results.results.bindings[i];
        // console.log(results);
          var factoryObject = currentPlant.plantFactory;
          if(factoryObject){
            var factoryQueryPromise = getFactoryData(currentPlant,factoryObject);
            promises.push(factoryQueryPromise);
            delete currentPlant.plantFactory;
          }

          companyPlants.push(currentPlant);
        }
        //console.log("next");
        var promiseFulfillCount = 0;
        for(var i =0 ;i<promises.length;i++){
          var currentPromise = promises[i];
          currentPromise.then(function(res){
            promiseFulfillCount++;
            if(promiseFulfillCount==promises.length){
              var plantsToRemove = [];
              for(var i=0; i<companyPlants.length;i++){
                var currentPlant = companyPlants[i];
                for(var j=i+1;j<companyPlants.length;j++){
                  var otherPlant = companyPlants[j];
                  if(!plantsToRemove.includes(otherPlant)){
                    //console.log(currentPlant);
                    if(currentPlant.plantName.value==otherPlant.plantName.value){
                      for(var k=0;k<otherPlant.factories.length;k++){
                        currentPlant.factories.push(otherPlant.factories[k]);
                      }//for
                      plantsToRemove.push(j);
                    }//if
                  }//if
                }//for
              }//for
              for(var i =0;i<plantsToRemove.length;i++){
                companyPlants.splice(plantsToRemove[i],1);
              }
              currentCompany.plants = companyPlants;
              resolve(currentCompany);
            }//if
          });//then
        }//for

    });//jQuery.get
  });//$q
  return plantQueryPromise;
}//getPlantData

});//service
