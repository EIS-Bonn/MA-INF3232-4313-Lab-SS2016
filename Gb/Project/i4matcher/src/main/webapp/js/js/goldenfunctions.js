var input1 = "files/gold_standard/input1.aml";  // first aml file path
var input2 = "files/gold_standard/input2.aml";  // second aml file path
var golden = "files/gold_standard/golden.json"  // path to the gold output
var file1 = "";  // content of the 1 aml
var file2 = "";  // content of the 2 aml
var goldenFile = "";  // content of the gold output
var output = "";  // produced integrated file
var matchNum = 0;  // number of matches in two JSONs
var retNum = 0;  // number of iterations in matching
var arrResult = [];  // results of matching


document.addEventListener('DOMContentLoaded', function() {
	// uploading the first aml file
	var rawFile = new XMLHttpRequest();
    rawFile.open("GET", input1, false);
    rawFile.onreadystatechange = function () {
        if(rawFile.readyState === 4) {
            if(rawFile.status === 200 || rawFile.status == 0) {
                file1 = rawFile.responseText;
				//console.log(file1);
            }
        }
    }
    rawFile.send(null);
	
	// uploading the second aml file
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", input2, false);
    rawFile.onreadystatechange = function () {
        if(rawFile.readyState === 4) {
            if(rawFile.status === 200 || rawFile.status == 0) {
                file2 = rawFile.responseText;
				//console.log(file2);
            }
        }
    }
    rawFile.send(null);
	
	// uploading the golden output
	var rawFile = new XMLHttpRequest();
    rawFile.open("GET", golden, false);
    rawFile.onreadystatechange = function () {
        if(rawFile.readyState === 4) {
            if(rawFile.status === 200 || rawFile.status == 0) {
                var goldenFileText = rawFile.responseText;
				goldenFile = JSON.parse(goldenFileText);
				console.log(goldenFile);
            }
        }
    }
    rawFile.send(null);
	
	// creating a multipart data for sending to the server
	var blob1 = new Blob([file1], {type: 'application/octet-stream'});
	var blob2 = new Blob([file2], {type: 'application/octet-stream'});
	var formData = new FormData();
	formData.append("input1", blob1, "input1.aml");
	formData.append("input2", blob2, "input2.aml");
	
	// POST sending to the server
	var xhr = new XMLHttpRequest();
	var postURL = "rest/controller/upload/strict";
	xhr.open("POST", postURL);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if(xhr.status == 200) {
				data = xhr.responseText;
				responseData = JSON.parse(data);  // response reading
                if (responseData.response) {
                    console.log('successful submit');
					var dataInt = responseData.response;
					$.get('http://localhost:8890/sparql?default-graph-uri=&query=select+*+from+%3Cjdbc%3Avirtuoso%3A%2F%2Flocalhost%3A1111%2F'+dataInt+'%3E+where+%7B+%3Fs+%3Fp+%3Fo+%7D&format=application%2Fsparql-results%2Bjson&timeout=0&debug=on', function (dataGet){
						console.log('successful request for integrated file');
						output = dataGet;
						matchDiff(output, goldenFile);  // finding what is missing in the golden output
						var matchOut = arrResult;
						arrResult = [];
						matchDiff(goldenFile, output);  // finding what is missing in the produced integrated file
						var matchGold = arrResult;
						var matchPerc = Math.round(matchNum*100 / retNum);  // calculating the percantage number
						
						// creating a matching table
						var tableStr = "<table class = \"diffTable\">";
						if (matchOut.length >= matchGold.length) {
							tableStr += "<tr><td><b>Golden Standard</b></td><td><b>Output</b></td></tr>";
							for (var i=0; i<matchOut.length; i++) {
								tableStr += "<tr><td>"+matchOut[i]+"</td><td>";
								if (i<matchGold.length) {
									tableStr += matchGold[i];
								}
								tableStr += "</td></tr>"
							}
						} else {
							tableStr += "<tr><td><b>Output</b></td><td><b>Golden Standard</b></td></tr>";
							for (var i=0; i<matchGold.length; i++) {
								tableStr += "<tr><td>"+matchGold[i]+"</td><td>";
								if (i<matchOut.length) {
									tableStr += matchOut[i];
								}
								tableStr += "</td></tr>"
							}
						}
						tableStr += "</table>";
						$("#contentArea").html('');
						$("#contentArea").append('<p>Match percentage: <span class="goldSpan">'+matchPerc+'%</span>');
						$("#contentArea").append('');
						$("#contentArea").append(tableStr);
					});
				}
			} else {
				alert("error");  // if the server doesn't respond
			}
		}
	} 
	xhr.send(formData);
}, false);

function matchDiff(output, goldenFile) {
    var result = {};
    for(key in output) {
		var flag = false;  // if matched
        if (goldenFile[key] != output[key]) {
			result[key] = goldenFile[key];
			flag = true;
		} else {
			matchNum += 1;
		}
        if (typeof goldenFile[key] == 'array' && typeof output[key] == 'array') {
            result[key] = arguments.callee(output[key], goldenFile[key]);
		} else if (typeof goldenFile[key] == 'object' && typeof output[key] == 'object') {
            result[key] = arguments.callee(output[key], goldenFile[key]);
		} else {
			if (flag) {
				arrResult.push(key+": "+output[key]);
			}
			retNum += 1;
		}
    }
    return result;
}