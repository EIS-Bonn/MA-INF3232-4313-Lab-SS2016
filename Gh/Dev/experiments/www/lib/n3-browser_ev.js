define(function() {
    return function n3Browser(msg) {

        // set the start time for evaluation
        var start = new Date().getTime();
        require('n3-browser')

        // our data format is n-triples
        parser = N3.Parser({ format: 'N-Triples' });
        store = N3.Store();

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                parser.parse(xhttp.responseText,
                    function(error, triple, prefixes) {
                        if (triple) {
                            store.addTriple(triple);

                        } else {
                            var result = store.find(null, null, 'http://db.uwaterloo.ca/~galuc/wsdbm/Country13')[0];
                            console.log(result);

                            // set the end time, and find the total processing time
                            var end = new Date().getTime();
                            var time = end - start;
                            console.log('Execution time: ' + time + " ms");
                        }
                    });
            }
        };

        // request for data
        xhttp.open("GET", "http://localhost/test.n3", true);
        xhttp.send();

    };
});
