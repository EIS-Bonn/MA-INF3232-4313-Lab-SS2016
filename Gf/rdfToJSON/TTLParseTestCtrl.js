var app = angular.module('Enterprise2MapApp');
app.controller('TTLParseTestCtrl',function($scope,TTLParseService){
  $scope.parsedTTL = "";
  var promise = TTLParseService.parseTTL('Factory');
  promise.then(function(resolution){
    $scope.parsedTTL = JSON.stringify(resolution);
  },function(rejection){
    console.error(rejection);
  });
});
