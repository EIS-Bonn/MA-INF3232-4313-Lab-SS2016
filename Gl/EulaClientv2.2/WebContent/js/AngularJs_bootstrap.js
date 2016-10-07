var myApp = angular.module('myapp', ['ui.router','ngAnimate', 'ui.bootstrap']);

myApp.controller('fileCtrl', ['$scope', '$http', function($scope, $http){
	
	$scope.eulaTitle;
	
	$scope.permissionList = [];
	$scope.pe = [];
	
	$scope.dutyList = [];
	$scope.du = [];
	
	$scope.prohibitionList = [];
	$scope.pr = [];
	
	$scope.openAll = false;		
	$scope.theFile;
	$scope.theUrl;
	
	$scope.panelStyle = 0
	$scope.errorMsg = "No file/url uploaded yet!";	
	var lastInput = "None";
	$scope.btnActive = false;
	var hasData = false;
	

	/*
	 * File format checking
	 */
	
	var validFormats = ['txt', 'pdf'];	
	$scope.setFile = function(element) {       
        	
        $scope.theFile = element.files[0];            
        
		if($scope.theFile != null){  
			var value =  element.files[0].name;					
			var ext = value.substr(value.lastIndexOf('.') + 1).toLowerCase();
			
			$scope.btnActive = true;			
			$scope.panelStyle = 1;    			
			$scope.errorMsg = value;     
			lastInput = "File";
			
			if( validFormats.indexOf(ext) == -1){
				$scope.btnActive = false;
				$scope.panelStyle = 2;
				$scope.errorMsg = "Please upload only '.txt' or '.pdf' file!";
			} 				
		} 
		
		$scope.$digest();       
    };  
	
	
	/*
	 * Url validation
	 */	
	$scope.validateUrl = function(){
		
		lastInput = "Url";
		$scope.panelStyle = 1;
		$scope.errorMsg = $scope.inputUrl;	
		$scope.theUrl = $scope.inputUrl;		
		$scope.btnActive = true;
		
		if($scope.theUrl == null || $scope.theUrl ==""){
			if($scope.theFile == null){
				lastInput = "None";
				$scope.btnActive = false;
				$scope.panelStyle = 0
				$scope.errorMsg = "No file/url uploaded yet!";
			}else {
				lastInput = "file";
				$scope.btnActive = true;
    			$scope.panelStyle = 1;    			
    			$scope.errorMsg = $scope.theFile.name;    			
			}
		}
	}	
	
	$scope.generate = function(){
		
		$scope.responsed=false;	
		$scope.displayChange("none", "block");
		
		if(lastInput === "File"){
			
			var file = $scope.theFile;			
			var fd = new FormData();
			fd.append('file', file);
			
			/*
			 * Connection with the Restful WebService
			 * */
			var uploadUrl = 'http://localhost:8081/eula/fileUploadJSON';
			$http.post(uploadUrl, fd, {			
				transformRequest: angular.identity,
				headers: {'Content-Type': undefined}
			})    
			.success(function(data, status, headers, config){
	
				summaryExtraction(data);
	
				$scope.responsed=true;
				$scope.displayChange("block", "none");
				
				if(!hasData){
					$scope.responsed=false;
					$scope.panelStyle = 2;
					$scope.errorMsg = "Content in the "+ $scope.theFile + " is not properly structured";
				}
			})    
			.error(function(data, status, headers, config){
				$scope.responsed=false;				
				$scope.displayChange("none", "none");
				
				$scope.panelStyle = 2;
				$scope.errorMsg = "Content in the "+ $scope.theFile + " is not properly structured";
			});
			
		}else if(lastInput === "Url"){
			
			/*
			 * Connection with the Restful WebService
			 * */
			var uploadUrl = 'http://localhost:8081/eula/urlUploadJSON';
			$http.post(uploadUrl, $scope.theUrl, {			 
				transformRequest : angular.identity,
				headers: {'Content-Type':  undefined}
			})
			.success(function(data, status, headers, config){
	
				summaryExtraction(data);		
				
				$scope.responsed=true;
				$scope.displayChange("block", "none");
				
				if(!hasData){
					$scope.responsed=false;
					$scope.panelStyle = 2;
					$scope.errorMsg = "Content in the "+ $scope.theUrl +" is not properly structured";
				}
			})    
			.error(function(data, status, headers, config){;	
				$scope.displayChange("none", "none");
				
				$scope.panelStyle = 2;
				$scope.errorMsg = "Content in the "+ $scope.theUrl +" is not properly structured";
				});
			
		}else{
			$scope.fBtnActive = false;
			$scope.panelStyle = 2;
			$scope.errorMsg = "Please enter a file/url";
		}
	}	

	/*
	 *  For displaying data with different action
	 * */	
	
	$scope.displayChange = function(param1, param2){
		document.getElementById("dataTable").style.display = param1;
		document.getElementById("loader").style.display = param2;	
	};

	var summaryExtraction = function(summary){

		$scope.eulaTitle = summary.subject;
		hasData = true;
		
		$scope.permissionList = summary.permissions;
		for(var i=0; i<$scope.permissionList.length; i++)
			$scope.pe[i] = false;
		
		$scope.dutyList = summary.duties;
		for(var i=0; i<$scope.dutyList.length; i++)
			$scope.du[i] = false;
		
		$scope.prohibitionList = summary.prohibitions;		
		for(var i=0; i<$scope.prohibitionList.length; i++)
			$scope.pr[i] = false;

		$scope.columnsProperties = [			
                    {"name":"Permission", 	"data":$scope.permissionList, 	"panel":"panel-success", 	"headerColor":"rgb(0, 239, 0)"},
                    {"name":"Duty", 		"data":$scope.dutyList, 		"panel":"panel-info", 		"headerColor":"rgb(72, 120, 255)"},
                    {"name":"Prohibition", 	"data":$scope.prohibitionList, 	"panel":"panel-danger", 	"headerColor":"rgb(255, 85, 70)"}
                    ];

		if($scope.permissionList.length === 0  && 	$scope.dutyList.length === 0 &&  $scope.prohibitionList.length === 0)
			hasData = false;
	}
	
	$scope.openAll = function(){		
		for(var i=0; i<$scope.pe.length; i++)
			$scope.pe[i] = true;
		
		for(var i=0; i<$scope.du.length; i++)
			$scope.du[i] = true;
		
		for(var i=0; i<$scope.pr.length; i++)
			$scope.pr[i] = true;		
	}	
	$scope.collapseAll = function(){
		for(var i=0; i<$scope.pe.length; i++)
			$scope.pe[i] = false;
		
		for(var i=0; i<$scope.du.length; i++)
			$scope.du[i] = false;
		
		for(var i=0; i<$scope.pr.length; i++)
			$scope.pr[i] = false;	
	}
	
	$scope.changePeStatus = function(i){
		$scope.pe[i] = !$scope.pe[i];
	}
	$scope.getPeStatus = function(i){
		return $scope.pe[i];
	}
	
	$scope.changeDuStatus = function(i){
		$scope.du[i] = !$scope.du[i];
	}
	$scope.getDuStatus = function(i){
		return $scope.du[i];
	}
	
	$scope.changePrStatus = function(i){
		$scope.pr[i] = !$scope.pr[i];
	}
	$scope.getPrStatus = function(i){
		return $scope.pr[i];
	}

	$scope.getActionName = function(innerJson){
		if(angular.fromJson(innerJson).hasOwnProperty("generalAction")){
			return "generalAction";
		}
		else if(angular.fromJson(innerJson).hasOwnProperty("advancedAction")){
			return "advancedAction";
		}
	}

	$scope.getAction = function(innerJson){
		var annot="";
		if(angular.fromJson(innerJson).hasOwnProperty("generalAction")){
			annot = angular.fromJson(innerJson).generalAction;
		}
		else if(angular.fromJson(innerJson).hasOwnProperty("advancedAction"))	{
			annot = angular.fromJson(innerJson).advancedAction;
		}
		return annot.toLowerCase();
	}

	$scope.getAnnot = function(innerJson){
		var annot = angular.fromJson(innerJson).annotation;
		return annot;
	}	
}]);


myApp.config(['$stateProvider',
              '$urlRouterProvider',
              '$locationProvider',
              function($stateProvider, $urlRouterProvider){

	$stateProvider
	.state('home', { url:"/", templateUrl: "html/home.htm", controller: "fileCtrl" })		
	.state('aboutUs', { url:"/aboutUs", templateUrl: "html/about_us.htm", controller:"fileCtrl" })	
	.state('help', { url:"/help", templateUrl: "html/help.htm" });
	
	$urlRouterProvider.otherwise('/');
}]);

myApp.controller('TabsCtrl', function ($rootScope, $state, $scope, $window) {

	$scope.tabs = [
	               { title: "Home", route: "home", active: true },	               
	               { title: "Help", route: "help", active: false },
	               { title: "About us", route: "aboutUs", active: false },
	               ];

	$scope.go = function(route){
		$state.go(route);
	};

	$scope.active = function(route){
		return $state.is(route);
	};

	$scope.$on("$stateChangeSuccess", function() {
		$scope.tabs.forEach(function(tab) {
			tab.active = $scope.active(tab.route);
		});
	});	

	$scope.contacts = [
	                   {"name":"Md Saiful Islam Faisal",
	                	   "email":"sifzone@hotmail.com",
	                	   "gender":"male"
	                   },
	                   {"name":"Ahmed Hemaid",
	                	   "email":"ahemid@uni-bonn.de",
	                	   "gender":"male"
	                   },
	                   {"name":"Lars Möllenbrok",
	                	   "email":"lars.moellenbrok@gmail.com",
	                	   "gender":"male"
	                   },
	                   {"name":"Isunge Mwangase",
	                	   "email":"isunge@yahoo.com",
	                	   "gender":"male"
	                   },
	                   {"name":"Najmeh Mousavi Nejad",
	                	   "email":"nejad@iai.uni-bonn.de",
	                	   "gender":"female"
	                   },
	                   ];
});