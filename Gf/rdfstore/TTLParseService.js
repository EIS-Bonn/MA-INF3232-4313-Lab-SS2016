angular.module('Enterprise2MapApp').
service('TTLParseService',function($q,sparqlQueryService){

  this.parseTTL = function(filePath='factory_example.ttl'){
    var mainPromise = $q(function(mainResolve,mainReject){

      console.log("i m here");

      jQuery.get(filePath, function(data) {

        rdfstore.create(function(err, store) {
          console.log("the new rdfstore is ready");

          var parsedData = {};
          console.log("parsed data: ");
          console.log(parsedData);

          //set default prefixes
          store.setPrefix("dbpedia", "http://dbpedia.org/resource/");
          store.setPrefix("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");

          //load data in store
          store.load("text/turtle",data,function(err,results){
            if(!err){//if no error
              //sparql query to get the company info from ttl data
              var companyQuery = sparqlQueryService.getCompanyQuery();
              var companyQueryPromise = $q(function(resolve,reject){
                //console.log("number of results found: "+results);
                var companies = [];
                store.execute(companyQuery, function(qErr, qResult) {
                  if(!qErr){
                    console.log("company result: ");
                    console.log(qResult);
                    for (var i = 0; i < qResult.length; i++) {
                      var currentCompany = qResult[i];
                      var companyData = {
                        companyName:  currentCompany.companyName,
                        companyCEO:   currentCompany.ceo,
                        companyHQ:    currentCompany.headquarters,
                        companyPlant: currentCompany.plant
                      };
                      companies.push(companyData);
                    }//for
                    resolve(companies);
                  }//if
                  else {
                    var rejection = {
                      error: qErr,
                      result: qResult,
                      msg: "error while executing company query: "+qErr
                    }
                    reject(rejection);
                  }//else
                });//query.execute
              });//$q

              companyQueryPromise.then(function(resolution){
                parsedData.companies = resolution;
                for (var i = 0; i < parsedData.companies.length; i++) {
                  var currentCompany = parsedData.companies[i];
                  //if plant data is defined
                  if(currentCompany.companyPlant){
                    var plantQueryPromise = getPlantData(currentCompany,store);

                    plantQueryPromise.then(function(resCompany){
                      console.log(resCompany);
                      for (var k = 0; k < resCompany.plants.length; k++) {
                        var currentPlant = resCompany.plants[k];
                        console.log(currentPlant);
                        var factoryQueryPromise = getFactoryData(currentPlant,store);

                        factoryQueryPromise.then(function(resPlant){
                          console.log(resPlant);
                          for(var j=0;j<resPlant.factories.length;j++){
                            var currentFactory = resPlant.factories[j];
                            console.log(currentFactory);
                            var polygonQueryPromise = getPolygonData(currentFactory,store);
                            polygonQueryPromise.then(function(resFactory){
                              console.log(resFactory);
                              mainResolve(parsedData);
                            },function(rejection){
                              console.error(rejection);
                            });//polygonQueryPromise.then
                          }//for
                        },function(rejection){
                          console.error(rejection);
                        });//factoryQueryPromise.then

                      }//for all plants in company
                    },function(rejection){
                      console.error(rejection);
                    });//plantQueryPromise.then
                  }//if plant given
                }//for all companies

              },function(rejection){
                console.error(rejection);
              });//companyQueryPromise.then
            }//if
            else {
              console.error("error while reading the turtle data, "+err);
            }//else
          });//store.load
        });//rdfstore.create
      });//jquery.get
    });//$q
    return mainPromise;
  }//parseTTL

  var getPolygonData = function(currentFactory,store){
    var polygonQueryPromise = $q(function(resolve,reject){
      var polygonObject = currentFactory.factoryPolygon;
      //sparql query to get factory polygon data
      // var fQuery = sparqlQueryService.getPolygonQuery(polygonObject.value);
      // var factoryPolygons = [];
      // store.execute(pQuery,function(pErr,pResult){
      //   if (pErr) {
      //     var rejection = {
      //       error: pErr,
      //       result: pResult,
      //       msg: "error while executing polygon query: "+pErr
      //     }
      //     reject(rejection);
      //   }//if pErr
      //   else {
      //     for (var k = 0; k < pResult.length; k++) {
      //       var currentPolygon = pResult[k];
      //       factoryPolygons.push(currentPolygon);
      //     }//for
      //     currentFactory.polygons = factoryPolygons;
      //     resolve(currentFactory);
      //   }//else
      // });//store.execute
      console.log(polygonObject.value);

      store.node(polygonObject.value,function(err,polygonNode){
        console.log(err);
        console.log(polygonNode);
        currentFactory.polygonNode = polygonNode
        resolve(currentFactory);
      });//store.node
    });//polygonQueryPromise $q
    return polygonQueryPromise;
  }//getPolygonData

  var getFactoryData = function(currentPlant,store){
    var factoryQueryPromise = $q(function(resolve,reject){
      var factoryObject = currentPlant.plantFactory;
      //sparql query to get company factory data
      var fQuery = sparqlQueryService.getFactoryQuery(factoryObject.value);
      var plantFactories = [];
      store.execute(fQuery,function(fErr,fResult){
        if (fErr) {
          var rejection = {
            error: fErr,
            result: fResult,
            msg: "error while executing factory query: "+fErr
          }
          reject(rejection);
        }//if fErr
        else {
          for (var k = 0; k < fResult.length; k++) {
            var currentFactory = fResult[k];
            plantFactories.push(currentFactory);
          }//for
          currentPlant.factories = plantFactories;
          resolve(currentPlant);
        }//else
      });//store.execute
    });//factoryQueryPromise $q

    return factoryQueryPromise;
  }//getFactoryData


  var getPlantData = function(currentCompany,store){
    var plantQueryPromise = $q(function(resolve,reject){
      var plantObject = currentCompany.companyPlant;
      //sparql query to get company plant data
      var pQuery = sparqlQueryService.getPlantQuery(plantObject.value);
      var companyPlants = [];
      store.execute(pQuery,function(pErr,pResult){
        if (pErr) {
          var rejection = {
            error: qErr,
            result: qResult,
            msg: "error while executing plant query: "+pErr
          }
          reject(rejection);
        }//if pErr
        else {
          for (var k = 0; k < pResult.length; k++) {
            var currentPlant = pResult[k];
            companyPlants.push(currentPlant);
          }//for
          currentCompany.plants = companyPlants;
          resolve(currentCompany);
        }//else
      });//store.execute
    });//$q
    return plantQueryPromise;
  }//getPlantData

});//service
