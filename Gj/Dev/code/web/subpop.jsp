<%@page import="web.DataHelper"%>
<%@page import="web.Helper"%>
<%@page import="java.io.File"%>
<%
    File[] subpopLevels = (File[]) request.getAttribute("subpopLevels");

    String findOutlier = (String) request.getAttribute("findOutlier");
    String sessionID = (String) request.getAttribute("sessionID");

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
            <h3 class="alert alert-info">
                Your session ID is: <%= sessionID%>
                <small>                
                    - Click on one of these boxes to find relevant outliers
                </small>
            </h3>
            <div class="main-content">
                <% for (int i = 0; i < subpopLevels.length; i += 1) {
                        File dir = subpopLevels[i];
                        File[] subpopFiles = dir.listFiles();
                        int bucketsNum = subpopFiles.length;
                %>
                <div class="panel-group" id="accordion">
                    <div class="panel panel-default">
                        <div class="panel-heading" style="padding: 0">
                            <h4 class="panel-title">
                                <a style="text-decoration: none; display: block; padding: 15px" class="text-uppercase" data-toggle="collapse" data-parent="#accordion" href="#collapse<%= i + 1%>">
                                    <%
                                        out.print(dir.getName() + "(<span style='color:red'>" + bucketsNum + "</span> buckets generated)");
                                    %>
                                </a>
                            </h4>
                        </div>
                        <div id="collapse<%= i + 1%>" class="panel-collapse collapse">
                            <div class="panel-body row">
                                <% for (int j = 0; j < subpopFiles.length; j += 1) {
                                        int samplesNum = Helper.fileLinesNum(subpopFiles[j]);
                                        String pathInfo = Helper.fileReadLine(subpopFiles[j], 1);
                                        String token = sessionID + dir.getName() + "/" + subpopFiles[j].getName();
                                        boolean kmeansCached = DataHelper.cachedOutlierResults.containsKey(token + "kmeans");
                                        boolean chauvenetCached = DataHelper.cachedOutlierResults.containsKey(token + "chauvenet");

                                %>
                                <div class="col-sm-4" style="margin-bottom: 30px;">
                                    <div style="border: solid 2px; border-radius: 5px">
                                        <a style="text-align: center; position: relative;display: block;
                                           <%= (kmeansCached) ? "color:red" : ""%>" href="javascript:;" 
                                           onclick="$(this).css({color: 'red'});
                                                   openWindow('outliers.jsp?sessionID=<%= sessionID%>&bucket=<%= dir.getName() + "/" + subpopFiles[j].getName()%>&find=<%= findOutlier%>&method=kmeans', 'KMeans')"
                                           data-toggle="tooltip" title="Show outliers map" >
                                            View Outliers (KMeans)
                                        </a>
                                        <a style="text-align: center; position: relative;display: block;
                                           <%= (chauvenetCached) ? "color:red" : ""%>" href="javascript:;" 
                                           onclick="$(this).css({color: 'red'});
                                                   openWindow('outliers.jsp?sessionID=<%= sessionID%>&bucket=<%= dir.getName() + "/" + subpopFiles[j].getName()%>&find=<%= findOutlier%>&method=chauvenet', 'Chauvenet')"
                                           data-toggle="tooltip" title="Show outliers map" >
                                            View Outliers (Chauvenet)
                                        </a>
                                        <div class="alert alert-success">
                                            <strong>
                                                Contains (<span style="color:red"><%= samplesNum%></span>) samples
                                            </strong><br><br>
                                            Subpopulation path: 
                                            <p style="font-size: 12px"><%= pathInfo%></p>
                                        </div>
                                    </div>
                                </div>
                                <% }%>    
                            </div>
                        </div>
                    </div>
                </div>
                <% }%>
            </div>
            <jsp:include page="footer.jsp" />
        </div>

        <script>
            function openWindow(url, title) {
                window.open(url, "", "width=700,height=400,menubar=1,resizable=0");
            }
            $(document).ready(function () {
                $('[data-toggle="tooltip"]').tooltip();
            });

            $('.collapse').on('show.bs.collapse', function () {
                $otherPanels = $(this).parents('.panel-group').siblings('.panel-group');
                $('.collapse', $otherPanels).removeClass('in');
            });
        </script>
    </body>
</html>
