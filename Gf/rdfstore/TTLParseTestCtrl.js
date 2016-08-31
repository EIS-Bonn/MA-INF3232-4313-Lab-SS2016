var app = angular.module('Enterprise2MapApp');
app.controller('TTLParseTestCtrl',function($scope,TTLParseService){
  $scope.parsedTTL = "";
  var promise = TTLParseService.parseTTL('factory_example.ttl');
  promise.then(function(resolution){
    $scope.parsedTTL = resolution;
  },function(rejection){
    console.error(rejection);
  });
});
