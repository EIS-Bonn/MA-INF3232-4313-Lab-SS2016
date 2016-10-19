<%--
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.

 Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 Other names may be trademarks of their respective owners.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://www.netbeans.org/cddl-gplv2.html
 or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License file at
 nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 particular file as subject to the "Classpath" exception as provided
 by Oracle in the GPL Version 2 section of the License file that
 accompanied this code. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 Contributor(s):
 
 The Original Software is NetBeans. The Initial Developer of the Original
 Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 Microsystems, Inc. All Rights Reserved.
 
 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 2, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 2] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 2 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 2 code and therefore, elected the GPL
 Version 2 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.
--%>
<%@page import="web.DataHelper"%>
<%
    if (DataHelper.rootDirectory.equals("")) {
        DataHelper.rootDirectory = application.getRealPath("/").replace('\\', '/');
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Outlier Detection on Financial RDF Data</title>
        <link rel="stylesheet" type="text/css" href="static/css/style.css" />

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <script type="text/javascript" src="static/js/angular/angular.min.js"></script>
        <script type="text/javascript" src="static/js/angular/controllers.js"></script>
    </head>
    <body>
        <div class="container" style="width: 800px">
            <jsp:include page="header.jsp" />
            <div class="main-content row" ng-app="app" ng-controller="newProcess as ct">
                <div class="col-sm-6">
                    <h3>Create and new session</h3>

                    <form role="form" action="process" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <label>Outlier predicate name:</label>
                            <input type="text" name="outlierpredname" class="form-control" required/>
                        </div>
                        <div class="form-group">
                            <label>Location predicate name:</label>
                            <input type="text" name="locationpredname" class="form-control" required/>
                        </div>
                        <div class="form-group form-inline">
                            <label>How many outlier properties?:</label>
                            <input type="number" class="form-control input-sm"ng-model="ct.outlierPropertiesNumber" min="1" max="10" ng-init="ct.outlierPropertiesNumber = 1" />
                        </div>
                        <div class="form-group">
                            <label onmouseover="$('#info-div-1').show()" onmouseout="$('#info-div-1').hide()">Propery name:<span class="glyphicon glyphicon-info-sign"></span>
                                <div id="info-div-1" class="alert alert-info" style="display: none">
                                    <strong style="font-size: 15px">
                                        Properties with numeric object values will not be considered
                                    </strong>
                                    <br/>
                                    <strong style="font-size: 15px">These properties will be automatically added, so you can also use them:</strong>
                                    <br/><br/>
                                    <p>- info_country</p>
                                    <p>- info_population</p>
                                    <p>- info_countrypopulation</p>
                                    <p>- info_area</p>
                                    <p>- info_countryarea</p>
                                    <p>- info_countryGDP</p>
                                </div>
                            </label>
                            <input type="text" name="outlierp-{{n}}" class="form-control" ng-repeat="n in range(1, ct.outlierPropertiesNumber)" required/>
                        </div>
                        <div class="form-group form-inline">
                            <label>How many datasets?:</label>
                            <input type="number" class="form-control input-sm"ng-model="ct.datasetNumber" min="1" max="5" ng-init="ct.datasetNumber = 1" />
                        </div>
                        <div class="form-group">
                            <label>Upload your file(s):</label>
                            <input type="file" name="data-{{n}}" class="form-control" ng-repeat="n in range(1, ct.datasetNumber)" required/>
                        </div>
                        <div class="form-group">

                            <input type="submit" class="btn btn-success" />
                        </div>
                    </form>
                </div>

                <div class="col-sm-6">
                    <h3>Results from a previous session</h3>
                    <form action="retrieve" method="post">
                        <div class="form-group">
                            <label>Enter a session ID:</label><input type="text" name="sessionID" class="form-control" required />
                        </div>
                        <div class="form-group">

                            <input type="submit" class="btn btn-success" />
                        </div>
                    </form>
                </div>
            </div>

            <jsp:include page="footer.jsp" />
        </div>

        <script>
                    var outliers = '[{"lat": 50.0598057,"lng": 14.3255392,"info": "<h1>sdfsdf</h1>"},{"lat": 51.528308,"lng": -0.3817765,"info": "bbbbb"}]';
                    // This example adds a marker to indicate the position of Bondi Beach in Sydney,
                            // Australia.
                                    function initMap() {
                                    var map = new google.maps.Map(document.getElementById("map"), {
                                    zoom: 4,
                                            center: {lat: 50.0598057, lng: 14.3255392},
                                            zoomControl: false,
                                            scaleControl: false,
                                            mapTypeControl: false,
                                    });
                                            var image = "./static/imgs/icons/outlier.jpg";
                                            $(jQuery.parseJSON(outliers)).each(function (key, value) {
                                    var marker = new google.maps.Marker({
                                    position: {lat: value.lat, lng: value.lng},
                                            map: map,
                                            icon: image,
                                            animation: google.maps.Animation.BOUNCE,
                                            label: '23'
                                    });
                                            var infowindow = new google.maps.InfoWindow({
                                            content: value.info
                                            });
                                            marker.addListener("click", function () {
                                            infowindow.open(map, marker);
                                            });
                                    });
                                    }
        </script>
        <script async defer
                src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAvve5XJD-HaIftFxkwVygYeSAguNOi0Gg&callback=initMap">
        </script>
    </body>
</html>
