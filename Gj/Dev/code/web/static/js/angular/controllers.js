var app = angular.module('app', []);

app.controller('newProcess', ['$scope', '$http', function ($scope, $http) {
        $scope.range = function (min, max, step) {
            step = step || 1;
            var input = [];
            for (var i = min; i <= max; i += step) {
                input.push(i);
            }
            return input;
        };
    }]);
