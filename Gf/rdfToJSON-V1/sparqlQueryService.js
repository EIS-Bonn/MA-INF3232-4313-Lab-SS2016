angular.module('Enterprise2MapApp').
service('sparqlQueryService',function(){

  this.getCompanyQuery = function(){
    return '     PREFIX vivo: <http://vivoweb.org/ontology/core#>\
    PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#> \
    PREFIX eis:     <http://example.com/owl/eis/>\
    SELECT * \
    WHERE {\
      ?subject a vivo:Company;\
      rdfs:label ?companyName.\
      OPTIONAL{\
        ?subject	eis:hasCEO       	   ?companyCEO.\
      }\
      OPTIONAL{\
        ?subject	eis:headquarters  ?companyHQ.\
      }\
      OPTIONAL{\
        ?subject	eis:hasPlant         ?companyPlant.\
      }\
      OPTIONAL{\
        ?subject eis:wasBuilt   ?buildDate. \
      }\
      OPTIONAL{\
        ?subject eis:hasPolygon ?polygon. \
      }\
    }'
  }

  this.getPlantQuery = function(plantObject){
    return 'PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>\
    PREFIX eis:     <http://example.com/owl/eis/>\
    SELECT * \
    WHERE {\
      <'+plantObject+'> a eis:Plant;\
      rdfs:label	?plantName.\
      OPTIONAL{  \
        <'+plantObject+'> eis:manager   ?plantManager\
      }            \
      OPTIONAL{  \
        <'+plantObject+'> eis:workers   ?plantWorkers\
      }            \
      OPTIONAL{  \
        <'+plantObject+'> eis:wasBuilt   ?buildDate\
      }            \
      OPTIONAL{\
        <'+plantObject+'>    eis:hasFactory    ?plantFactory.\
      }\
      OPTIONAL{\
        <'+plantObject+'>    eis:hasBuilding    ?building.\
      }\
    }';
  }//getPlantQuery

  this.getFactoryQuery = function(factoryObject){
    return 'PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>\
    PREFIX eis:     <http://example.com/owl/eis/>      \
    PREFIX lgdo:  <http://linkedgeodata.org/ontology/>\
    SELECT *              \
    WHERE {               \
      <'+factoryObject+'>	a	lgdo:Factory;               \
      rdfs:label	?factoryName.      \
      OPTIONAL{\
        <'+factoryObject+'>    eis:wasBuilt    ?buildDate.               \
      }\
      OPTIONAL{ \
        <'+factoryObject+'>  eis:hasBuilding ?building  \
      }\
      OPTIONAL{\
        <'+factoryObject+'>  eis:workers ?numberOfWorkers  \
      }\
      OPTIONAL{  \
        <'+factoryObject+'> eis:manager   ?factoryManager\
      }            \
      <'+factoryObject+'>    eis:hasPolygon    ?polygon.               \
    }';
  }//getFactoryQuery

  this.getBuildingQuery = function(buildingObject){
    return 'PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>\
    PREFIX eis:     <http://example.com/owl/eis/>      \
    PREFIX lgdo:  <http://linkedgeodata.org/ontology/>\
    SELECT *              \
    WHERE {               \
      <'+buildingObject+'>	a	eis:Building;               \
      OPTIONAL{\
        <'+buildingObject+'>    eis:wasBuilt    ?buildDate.               \
      }\
      OPTIONAL{\
        <'+buildingObject+'>  eis:workers ?numberOfWorkers  \
      }\
      OPTIONAL{  \
        <'+buildingObject+'> eis:manager   ?buildingManager\
      }            \
      OPTIONAL{  \
        <'+buildingObject+'> eis:hasMachine   ?machine\
      }            \
      <'+buildingObject+'>    eis:hasPolygon    ?polygon.               \
    }';
  }//getBuildingQuery

  this.getMachineQuery = function(machineObject){
    return 'PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>\
    PREFIX eis:     <http://example.com/owl/eis/>      \
    PREFIX lgdo:  <http://linkedgeodata.org/ontology/>\
    SELECT *              \
    WHERE {               \
      <'+machineObject+'>	a	eis:Machine;               \
                          rdfs:label ?machineLabel \
      OPTIONAL{\
        <'+machineObject+'>    eis:manager    ?manager.               \
      }\
      OPTIONAL{\
        <'+machineObject+'>  eis:floor ?floor  \
      }\
      OPTIONAL{  \
        <'+machineObject+'>    eis:hasPolygon    ?polygon.               \
      }            \
    }';
  }//getMachineQuery

  this.getPolygonQuery = function(polygonObject){
    //  console.log(polygonObject);
    return 'PREFIX eis: <http://example.com/owl/eis> \
    PREFIX ngeo: <http://geovocab.org/geometry#> \
    PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>\
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \
    PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>    \
    SELECT ?polygonType ?polygonList ?lat ?long \
    WHERE { \
      <'+polygonObject+'> a ?polygonType. \
      <'+polygonObject+'> ngeo:posList/rdf:rest*/rdf:first ?polygonList. \
      ?polygonList geo:lat ?lat. \
      ?polygonList geo:long ?long.\
    } group by ?polygonType ?polygonList ?lat ?long  ';
  }



});
