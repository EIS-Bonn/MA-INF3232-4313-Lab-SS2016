var start = new Date().getTime();

    var rdfstore = require('rdfstore');
    rdfstore.create(function(err, store) {
        console.log("loading...")
        store.execute('LOAD <http://localhost/test.n3> INTO GRAPH <http://example.org>', function() {
            console.log("done...")

            query1 = "PREFIX uw: <http://db.uwaterloo.ca/~galuc/wsdbm/>\
                    SELECT * FROM <http://example.org> { ?s ?p uw:Country13 }";

            query2 = "PREFIX uw: <http://db.uwaterloo.ca/~galuc/wsdbm/>\
                PREFIX rev: <http://purl.org/stuff/rev#> \
                PREFIX sorg: <http://schema.org/> \
                SELECT * \
                WHERE { \
                    ?v0 sorg:caption    ?v1 . \
                    ?v0 sorg:text   ?v2 . \
                    ?v0 sorg:contentRating  ?v3 . \
                    ?v0 rev:hasReview   ?v4 . \
                    ?v4 rev:title   ?v5 . \
                    ?v4 rev:reviewer    ?v6 . \
                    ?v7 sorg:actor  ?v6 . \
                    ?v7 sorg:language   ?v8 . \
                }";

            query3 = "PREFIX og: <http://ogp.me/ns#> \
                PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \
                PREFIX sorg: <http://schema.org/> \
                PREFIX wsdbm: <http://db.uwaterloo.ca/~galuc/wsdbm/> \
                SELECT * \
                WHERE { \
                    ?v0 og:tag  ?v1 . \
                    ?v0 rdf:type    ?v2 . \
                    ?v3 sorg:trailer    ?v4 . \
                    ?v3 sorg:keywords   ?v5 . \
                    ?v3 wsdbm:hasGenre  ?v0 . \
                    ?v3 rdf:type    wsdbm:ProductCategory2 .\
                }"
                
            store.execute(query3, function(err, results) {
                if (!err) {
                    // process results
                    if (results && results.length > 0 && results[0].s.token === 'uri') {
                        // console.log("Results: ", results.length, results);
                    }
                    var end = new Date().getTime();
                    var time = end - start;
                    console.log('Execution time: ' + time + " ms");
                }
            });
        });
    });   
