    define(function() {
        return function rdflibEv(msg) {
            console.log("ddddd");

            require('levelgraph')

            // set the start time for evaluation
            var start = new Date().getTime();

            var rdflib = require('rdflib');

            // request for data
            $rdf.sym('http://localhost/test.n3')

            var kb = new $rdf.IndexedFormula();
            var fetcher = new $rdf.Fetcher(kb, 5000)
            var graph = $rdf.graph()

            fetcher.nowOrWhenFetched("http://localhost/test.n3", function(ok, body, xhr) {
                if (!ok) {
                    console.log("Oops, something happened and couldn't fetch data");
                } else {
                    var uri = 'https://example.org';
                    var body = xhr.response;
                    var mimeType = 'text/n3';

                    try {
                        $rdf.parse(body, graph, uri, mimeType)

                        // query1 is simple
                        var query1 = "PREFIX uw: <http://db.uwaterloo.ca/~galuc/wsdbm/> \n" +
                            "SELECT * \n" +
                            "WHERE {\n" +
                            "  ?s ?p uw:Country13 . \n" +
                            "}";

                        // query2 is a bit more complex
                        var query2 = "PREFIX uw: <http://db.uwaterloo.ca/~galuc/wsdbm/> \n" +
                            "PREFIX rev: <http://purl.org/stuff/rev#> \n" +
                            "PREFIX sorg: <http://schema.org/> \n" +
                            "SELECT * \n" +
                            "WHERE { \n" +
                            "?v0 sorg:caption    ?v1 . \n" +
                            "?v0 sorg:text   ?v2 . \n" +
                            "?v0 sorg:contentRating  ?v3 . \n" +
                            "?v0 rev:hasReview   ?v4 . \n" +
                            "?v4 rev:title   ?v5 . \n" +
                            "?v4 rev:reviewer    ?v6 . \n" +
                            "?v7 sorg:actor  ?v6 . \n" +
                            "?v7 sorg:language   ?v8 . \n" +
                            "}";

                        // query3 is a bit more complex
                        var query3 = "PREFIX og: <http://ogp.me/ns#> \n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                            "PREFIX sorg: <http://schema.org/> \n" +
                            "PREFIX wsdbm: <http://db.uwaterloo.ca/~galuc/wsdbm/> \n" +
                            "SELECT * \n" +
                            "WHERE { \n" +
                            "?v0 og:tag  ?v1 . \n" +
                            "?v0 rdf:type    ?v2 . \n" +
                            "?v3 sorg:trailer    ?v4 . \n" +
                            "?v3 sorg:keywords   ?v5 . \n" +
                            "?v3 wsdbm:hasGenre  ?v0 . \n" +
                            "?v3 rdf:type    wsdbm:ProductCategory2 ." +
                            "}";

                        $rdf.fetcher = null;

                        // change query types here
                        var eq = $rdf.SPARQLToQuery(query3, true, kb);

                        kb.fetcher = null;
                        // kb.query(eq, onResult, undefined, onDone)
                        kb.query(eq, function(result) {
                            // print(result)
                        }, undefined, function(result) {

                            // set the end time, and find the total processing time
                            var end = new Date().getTime();
                            var time = end - start;
                            console.log('Execution time: ' + time + " ms");
                            // console.log(result)
                        });
                    } catch (err) {
                        console.log(err);
                    }
                }
            })
        };
    });
