var numberOfFiles = 0;  // checks the number of uploaded files (global variable)
var dataType;

function submitFormFunc() {  // on submiting 2 files
    var start = new Date().getTime();
    var form = document.forms.submitForm;
    var formData = new FormData(form);  // special type for multiple file upload
    /*var xhr = new XMLHttpRequest;
    xhr.open('POST', '/', true);
    xhr.send(formData);*/
    var selection = $("#matchingChoice option:selected").val();  // saving the way of matching
    $("#submitButton").html('<img src="img/upload.gif" class="uploadGif"/>');  // showing load image
    var xhr = new XMLHttpRequest();
    var postURL = '';
    switch(selection) {  // depending on the way of matching
        case 'str': 
            postURL = "rest/controller/upload/strict";
            break;
        case 'sft':
            postURL = "rest/controller/upload/soft";
            break;
        case 'non':
            postURL = "rest/controller/upload/nonstrict";
            break;
        default:
            console.log("unknown matching choice");
            break;
    }
    xhr.open("POST", postURL);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 3) {
            if(xhr.status == 200) {
                data = xhr.responseText;
                responseData = JSON.parse(data);  // reading the response of the server
                if (responseData.response) {  // if response exists
                    console.log('successful submit');
                    var dataInt = responseData.response;
                    $.get('http://localhost:8890/sparql?default-graph-uri=&query=select+*+from+%3Cjdbc%3Avirtuoso%3A%2F%2Flocalhost%3A1111%2F'+dataInt+'%3E+where+%7B+%3Fs+%3Fp+%3Fo+%7D&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on', function (dataGet){
                        var key = Date.now();  // creating a key for saving in cash (current date)
                        localStorage[key] = "data:;base64,77u/"+btoa(JSON.stringify(dataGet));  // saving json file to cash (in base64 format)
                        console.log('successful request for integrated file');
                        // inserting into the page all the information about the integrated file
                        $("#downloadArea").append('Integrated file has been created. <p>Choose the file format for downloading: <select name="type" id="choice"><option selected value="json">JSON</option><option value="ttl">Turtle</option><option value="xml">XML</option><option value="html">HTML</option></select> <input type="button" onclick="downloadInt('+dataInt+')" value="Download"/>');
                        $("#submitButton").html('');
                        $("#submitButton").append('<div class="queryTrialZone"><label>You can see the visualization of the integrated file <a href="tree.html?type=json&key='+key+'" target="_blank" >here.</a> <p>In order to retrieve any specific information, input your SPARQL query below:</label><br /><textarea class="queryField" placeholder=\'select * from $table$ where { ?s ?p ?o }\'></textarea><br><input type="button" onclick="getQuery('+dataInt+')" value="Run Query"/><span class="comment">*use $table$ as a table name</span></div><div class="moreInfo"></div>');
                    var elapsed = new Date().getTime() - start;
                    console.log("Function for submitting file, execution time "+elapsed);
                    });
                } else {  // in case server doesn't respond correctly
                    $("#submitButton").html('Something went wrong! Try to reload the page!');
                    var elapsed = new Date().getTime() - start;
                    console.log("Function for submitting file, execution time "+elapsed);
                }
            }
        } else if (xhr.readyState == 4) {
            if(xhr.status == 0) {
                $("#submitButton").html("Sorry, server doesn't respond!");
                    var elapsed = new Date().getTime() - start;
                    console.log("Function for submitting file, execution time "+elapsed);
            }
        }
    }
    xhr.send(formData);
}

function downloadInt(dataInt) {  // for downloading integrated file
    var start = new Date().getTime();
    var selection = $("#choice option:selected").val();  // saving the type of the file to save in
    // creating a get request
    var query = encodeURI('select * from <jdbc:virtuoso://localhost:1111/'+dataInt+'> where { ?s ?p ?o} ');
    var format = '';
    var dataType = '';
    switch(selection) {  // depending on selected type
        case 'json': 
            format = 'application/sparql-results%2Bjson';
            dataType = 'json';
            break;
        case 'ttl': 
            format = encodeURI('text/turtle');
            dataType = 'ttl';
            break;
        case 'xml': 
            format = 'application/sparql-results%2Bxml';
            dataType = 'xml';
            break;
        case 'html': 
            format = encodeURI('text/html');
            dataType = 'html';
            break;
        default:
            console.log("unknown format");
            break;
    }
    var timeout = encodeURI('0');
    var debug = encodeURI('on');
    var tmpURL = 'http://localhost:8890/sparql?default-graph-uri=&query='+query+'&format='+format+'&timeout='+timeout+'&debug='+debug;
    $.get('http://localhost:8890/sparql?default-graph-uri=&query='+query+'&format='+format+'&timeout='+timeout+'&debug='+debug, function (data){
        console.log(data);
        $("#downloadArea").html=('');  // appending the information about produced content
        $("#downloadArea").append('<a href="data:;base64,77u/'+btoa(data)+'" download="integrated_file.'+dataType+'" id="downloadInt'+dataType+'" class="hiddenLink"></a>');
        document.getElementById("downloadInt"+dataType).click();
    var elapsed = new Date().getTime() - start;
    console.log("Function downloadInt time "+elapsed);
    }, "text");
}

function getQuery(dataInt) {  // for sending SPARQL query
    var start = new Date().getTime();
    var query = encodeURI($(".queryField").val()).replace('$table$','<jdbc:virtuoso://localhost:1111/'+dataInt+'>');
    console.log(query);
    $.get('http://localhost:8890/sparql?default-graph-uri=&query='+query+'&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on', function (data){
        console.log('successful request for query');
        var key = Date.now();  // creating a key for saving into cash
        localStorage[key] = "data:;base64,77u/"+btoa(data.replace('\n',''));  // saving into cash (in base64 format)
        $('.moreInfo').html('');
        setTimeout(function(){$('.moreInfo').html('<a href="tree.html?type=json&key='+key+'" target="_blank" >Visualization</a></br><a href="data:;base64,77u/'+btoa(data)+'" target="_blank" download="query_result.json">Download the result in JSON</a>');}, 500);
    }, "text");
    var elapsed = new Date().getTime() - start;
    console.log("Function getQuery time "+elapsed);
}

function handleFileSelect(evt) {
    var start = new Date().getTime();
    var file = evt.target.files; // FileList object
    var f = file[0];
    var name = evt.target.name;
// Only process aml and opcua files.
    var dotInd = f.name.lastIndexOf('.');
    var type = f.name.substring(dotInd+1, f.name.length+1);
    var reader = new FileReader();
    if (type == 'aml') {
// Closure to capture the file information.
        reader.onload = (function(theFile) {
            return function(e) {
      // Add links to the file and its visualization.
                if (name == "file1") {
                    var key = Date.now();
                    localStorage[key] = e.target.result;
                    var fileInput = document.getElementById('file1'),
                    p1 = document.createElement('P1');
                    p1.innerHTML = ['<a href="tree.html?type=aml&key='+key+'" target="_blank" title="Visualization of '+escape(theFile.name)+ 
                    '">Visualization</a><br><a href="'+e.target.result+'" download="'+escape(theFile.name)+'" title="'+escape(theFile.name)+'">Download the file</a>'];
                    fileInput.parentNode.insertBefore(p1, fileInput.nextSibling);
                    $("#file1").css("display","none");
                    $("#descr1").css("display","none");
                } else if (name == "file2") {
                    var key = Date.now();
                    localStorage[key] = e.target.result;
                    var fileInput = document.getElementById('file2'),
                    p2 = document.createElement('P2');
                    p2.innerHTML = ['<a href="tree.html?type=aml&key='+key+'" target="_blank" title="Visualization of '+ escape(theFile.name)+ 
                    '">Visualization</a><br><a href="'+ e.target.result+'" download="'+ escape(theFile.name)+'" title="'+ escape(theFile.name)+'">Download the file</a>'];
                    fileInput.parentNode.insertBefore(p2, fileInput.nextSibling);
                    $("#file2").css("display","none");
                    $("#descr2").css("display","none");
                }
                else 
                {
                    console.log("Unknown element id");
                }
            };
        })(f);

// Read in the file as a data URL.
        reader.readAsDataURL(f);
        console.log('Success AML');
        numberOfFiles += 1;
        console.log(numberOfFiles + " file uploaded");
    } else if (type == 'opcua') {
// Closure to capture the file information.
        reader.onload = (function(theFile) {
            return function(e) {
                if (name == "file1") {
                    var key = Date.now();
                    localStorage[key] = e.target.result;
                    var fileInput = document.getElementById('file1'),
                    p1 = document.createElement('P1');
                    p1.innerHTML = ['<a href="tree.html?type=opcua&key='+key+'" target="_blank" title="Visualization of '+ escape(theFile.name)+ 
                    '">Visualization</a><br><a href="'+ e.target.result+'" download="'+ escape(theFile.name)+'" title="'+ escape(theFile.name)+'">Download the file</a>'];
                    fileInput.parentNode.insertBefore(p1, fileInput.nextSibling);
                    $("#file1").css("display","none");
                    $("#descr1").css("display","none");
                } else if (name == "file2") {
                    var key = Date.now();
                    localStorage[key] = e.target.result;
                    var fileInput = document.getElementById('file2'),
                    p2 = document.createElement('P2');
                    p2.innerHTML = ['<a href="tree.html?type=opcua&key='+key+'" target="_blank" title="Visualization of '+ escape(theFile.name)+ 
                    '">Visualization</a><br><a href="'+ e.target.result+'" download="'+ escape(theFile.name)+'" title="'+ escape(theFile.name)+'">Download the file</a>'];
                    fileInput.parentNode.insertBefore(p2, fileInput.nextSibling);
                    $("#file2").css("display","none");
                    $("#descr2").css("display","none");
                } else {
                    console.log("Unknown element id");
                }
            };
        })(f);

// Read in the file as a data URL.
        reader.readAsDataURL(f);
        console.log('Success OPCUA');
        numberOfFiles += 1;
        console.log(numberOfFiles + " file uploaded");
    } else {
        alert ('Error: you should upload either AML or OPCUA file.')
    }

    if (numberOfFiles == 2) {
        $("#submitButton").append('<button id="submit" type="submit">Submit files to the server</button> with <select name="matchingSelect" id="matchingChoice"><option selected value="str">strict</option><option value="sft">soft</option><option value="non">non-strict</option></select> matching.');
    }
    var elapsed = new Date().getTime() - start;
    console.log("Function handleFileSelect time "+elapsed);
}
document.getElementById('file1').addEventListener('change', handleFileSelect, false);
document.getElementById('file2').addEventListener('change', handleFileSelect, false);
//localStorage.clear();