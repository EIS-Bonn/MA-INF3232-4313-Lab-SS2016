angular.module('Enterprise2MapApp').
service('sparqlQueryService',function(){

  this.getCompanyQuery = function(){
    return 'PREFIX vivo: <http://vivoweb.org/ontology/core#>\
                 PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#> \
                 PREFIX ex:     <http://example.org/>\
                 SELECT * \
                 WHERE {\
                   ?subject a vivo:Company;\
                   rdfs:label ?companyName.\
                   OPTIONAL{\
                     ?subject	ex:hasCEO       	   ?ceo.\
                   }\
                   OPTIONAL{\
                     ?subject	ex:headquarters  ?headquarters.\
                   }\
                   OPTIONAL{\
                      ?subject	ex:hasPlant         ?plant.\
                    }\
                  }'
  }

  this.getPlantQuery = function(plantObject){
    return 'PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>\
             PREFIX ex:     <http://example.org/>\
             SELECT * \
             WHERE {\
               <'+plantObject+'> a ex:Plant;\
               rdfs:label	?plantName.\
               OPTIONAL{\
                 <'+plantObject+'>    ex:hasFactory    ?plantFactory.\
               }\
             }';
  }//getPlantQuery

  this.getFactoryQuery = function(factoryObject){
    return 'PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>\
            PREFIX ex:     <http://example.org/>      \
            PREFIX lgdo:  <http://linkedgeodata.org/ontology/>\
            SELECT *              \
            WHERE {               \
	             <'+factoryObject+'>	a	lgdo:Factory;               \
				                rdfs:label	?factoryName.      \
	          OPTIONAL{\
	             <'+factoryObject+'>    ex:wasBuilt    ?buildDate.               \
             }\
	            <'+factoryObject+'>    ex:hasPolygon    ?factoryPolygon.               \
            }';
  }//getFactoryQuery

});
