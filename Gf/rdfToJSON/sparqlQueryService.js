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
                     ?subject	ex:hasCEO       	   ?companyCEO.\
                   }\
                   OPTIONAL{\
                     ?subject	ex:headquarters  ?companyHQ.\
                   }\
                   OPTIONAL{\
                      ?subject	ex:hasPlant         ?companyPlant.\
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

  this.getPolygonQuery = function(polygonObject){
  //  console.log(polygonObject);
    return 'PREFIX ex: <http://example.org/> \
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

  this.getPolygonQuery1 = function(polygonObject){
    return 'PREFIX ex: <http://example.org/> \
            PREFIX ngeo: <http://geovocab.org/geometry#> \
            PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \
            PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>    \
            SELECT * \
            WHERE { \
            <'+polygonObject+'> a ?polygonType. \
  		      <'+polygonObject+'> ngeo:posList/rdf:rest*/rdf:first ?polygonList. \
  		      ?polygonList geo:lat ?lat. \
  		      ?polygonList geo:long ?long.\
            } group by ?polygonType ?lat ?long';
  }

});
