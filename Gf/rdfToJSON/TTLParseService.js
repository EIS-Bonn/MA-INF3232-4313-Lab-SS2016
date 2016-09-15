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

      //sparql query to get the company info
      var companyQuery = sparqlQueryService.getCompanyQuery();
      jQuery.get(endPointURL,{query:companyQuery},function(results){
        var companies = [];
        var plantQueryPromise;
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentCompany = results.results.bindings[i];
          companies.push(currentCompany);
          if(currentCompany.companyPlant){
            plantQueryPromise  = getPlantData(currentCompany);
          }
        }

        plantQueryPromise.then(function(resCompany){
          parsedData.companies = companies;
          mainResolve(parsedData);
        });
      });


        //
        //
        //             factoryQueryPromise.then(function(resPlant){
        //               console.log(resPlant);
        //               for(var j=0;j<resPlant.factories.length;j++){
        //                 var currentFactory = resPlant.factories[j];
        //                 console.log(currentFactory);
        //                 var polygonQueryPromise = getPolygonData(currentFactory,store);
        //                 polygonQueryPromise.then(function(resFactory){
        //                   console.log(resFactory);
        //                   mainResolve(parsedData);
        //                 },function(rejection){
        //                   console.error(rejection);
        //                 });//polygonQueryPromise.then
        //               }//for
        //             },function(rejection){
        //               console.error(rejection);
        //             });//factoryQueryPromise.then
        //
        //           }//for all plants in company
        //         },function(rejection){
        //           console.error(rejection);
        //         });//plantQueryPromise.then
        //       }//if plant given
        //     }//for all companies
        //
        //   },function(rejection){
        //     console.error(rejection);
        //   });//companyQueryPromise.then
        // }//if
        // else {
        //   console.error("error while reading the turtle data, "+err);
        // }//else
    });//$q
    return mainPromise;
  }//parseTTL

  var getPolygonData = function(currentFactory){
    var polygonQueryPromise = $q(function(resolve,reject){
      var polygonObject = currentFactory.factoryPolygon;
      //sparql query to get factory polygon data
      var pQuery = sparqlQueryService.getPolygonQuery(polygonObject.value);
      //console.log(pQuery);
      var factoryPolygons = [];
      jQuery.get(endPointURL,{query:pQuery},function(results){
        var factoryPolygons = [];
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentPolygon = results.results.bindings[i];
          currentPolygon = {
            lat:currentPolygon.lat,
            long:currentPolygon.long,
            type:currentPolygon.polygonType
          }
          factoryPolygons.push(currentPolygon);
        }
        currentFactory.polygons = factoryPolygons;
        resolve(currentFactory);
      });//jQuery.get
    });//polygonQueryPromise $q
    return polygonQueryPromise;
  }//getPolygonData

  var getFactoryData = function(currentPlant){
    var factoryQueryPromise = $q(function(resolve,reject){
      var factoryObject = currentPlant.plantFactory;
      //sparql query to get company factory data
      var fQuery = sparqlQueryService.getFactoryQuery(factoryObject.value);
      jQuery.get(endPointURL,{query:fQuery},function(results){
        var plantFactories = [];
        var polygonQueryPromise;
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentFactory = results.results.bindings[i];
          plantFactories.push(currentFactory);
          //console.log(currentFactory);
          if(currentFactory.factoryPolygon){
            polygonQueryPromise = getPolygonData(currentFactory);
          }
        }
        polygonQueryPromise.then(function(resolution){
          currentPlant.factories = plantFactories;
          resolve(currentPlant);
        });
      });//jQuery.get
    });//factoryQueryPromise $q
    return factoryQueryPromise;
  }//getFactoryData


  var getPlantData = function(currentCompany){
    var plantQueryPromise = $q(function(resolve,reject){
      var plantObject = currentCompany.companyPlant;
      //sparql query to get company plant data
      var pQuery = sparqlQueryService.getPlantQuery(plantObject.value);
      jQuery.get(endPointURL,{query:pQuery},function(results){
        var companyPlants = [];
        var factoryQueryPromise;
        for(var i = 0;i<results.results.bindings.length;i++){
          var currentPlant = results.results.bindings[i];
          companyPlants.push(currentPlant);
          if(currentPlant.plantFactory){
            factoryQueryPromise = getFactoryData(currentPlant);
          }
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
