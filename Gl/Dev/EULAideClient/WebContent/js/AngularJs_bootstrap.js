var myApp = angular.module('myapp', ['ui.router','ngAnimate', 'ui.bootstrap']);

myApp.controller('fileCtrl', ['$scope', '$http', '$sce', function($scope, $http, $sce){
	
	$scope.eulaTitle;
	$scope.permissions = [];
	$scope.duties = [];
	$scope.prohibitions = [];	
	
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
	
				$scope.eulaTitle = data.subject;	
				$scope.permissions = summaryExtraction(data, 'permissions');
				$scope.duties = summaryExtraction(data, 'duties');
				$scope.prohibitions = summaryExtraction(data, 'prohibitions');
	
				$scope.responsed=true;
				$scope.displayChange("block", "none");
				
				// reseting all old settins
				$scope.toggleRelPermission('','','');
				
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
	
				$scope.eulaTitle = data.subject;	
				$scope.permissions = summaryExtraction(data, 'permissions');
				$scope.duties = summaryExtraction(data, 'duties');
				$scope.prohibitions = summaryExtraction(data, 'prohibitions');
				
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
		document.getElementById("loader1").style.display = param2;
		document.getElementById("spinner1").style.display = param2;
		document.getElementById("spinner2").style.display = param2;
		document.getElementById("spinner3").style.display = param2;		
	};

	var summaryExtraction = function(summary, annotType){
		
		$scope.annotations = [];
		var annotationList;

		hasData = true;
		
		if(annotType === 'permissions')
			annotationList = summary.permissions;
		else if(annotType === 'duties')
			annotationList = summary.duties;
		else if(annotType === 'prohibitions')
			annotationList = summary.prohibitions;
		
		for(var i=0; i<annotationList.length; i++)
		{			
			var annot = annotationList[i];
			var annotObj = {
					annotation: undefined,
					id : undefined,
					generalAction : undefined,
					advancedAction : undefined,
					relatedAnnotId : undefined,
					relatedAnnotType : undefined
			}
			var jsonObj = angular.fromJson(annot);			
			
			annotObj.annotation = jsonObj.annotation;
			annotObj.id =  jsonObj.id;
			if(jsonObj.hasOwnProperty("generalAction"))
				annotObj.generalAction = jsonObj.generalAction;

			if(jsonObj.hasOwnProperty("advancedAction"))
				annotObj.advancedAction = jsonObj.advancedAction;

			if(jsonObj.hasOwnProperty("relatedAnnotid"))
				annotObj.relatedAnnotId = jsonObj.relatedAnnotid;
			
			if(jsonObj.hasOwnProperty("relatedAnnottype"))
				annotObj.relatedAnnotType = jsonObj.relatedAnnottype;
			
			annotObj.isOpened = false;
			$scope.annotations.push(annotObj);
		}
		
		if($scope.annotations.length === 0 )
			hasData = false;
		return $scope.annotations;
	}
		
	$scope.openAll = function(){		
		for(var i=0; i<$scope.permissions.length; i++)
			$scope.permissions[i].isOpened = true;
		
		for(var i=0; i<$scope.duties.length; i++)
			$scope.duties[i].isOpened = true;
		
		for(var i=0; i<$scope.prohibitions.length; i++)
			$scope.prohibitions[i].isOpened = true;		
	}
	
	$scope.collapseAll = function(){
		for(var i=0; i<$scope.permissions.length; i++)
			$scope.permissions[i].isOpened = false;
		
		for(var i=0; i<$scope.duties.length; i++)
			$scope.duties[i].isOpened = false;
		
		for(var i=0; i<$scope.prohibitions.length; i++)
			$scope.prohibitions[i].isOpened = false;	
	}	
	
	$scope.toggleRelPermission = function(currentId, relatedPermissionId, userType){

		var dom;
		var i = null;		
		
		// changing border of related permission annotation
		for ( i = 0; i < $scope.permissions.length; i++ ){			
			dom = document.getElementById($scope.permissions[i].id+userType);
			if ($scope.permissions[i].id === relatedPermissionId){
				if(dom.style.borderColor === "red")
					dom.style.border = "none";				
				else
					dom.style.border = "3px solid red";
				dom.focus();
			}
			else if( typeof(dom) != 'undefined' && dom != null ){
				dom.style.border = "none";
			}
		}		

		// changing border of current duty annotation
		for ( i = 0; i < $scope.duties.length; i++ ){
			dom = document.getElementById($scope.duties[i].id+userType);
			if ($scope.duties[i].id === currentId){
				if(dom.style.borderColor === "red")
					dom.style.border = "none";
				else
					dom.style.border = "3px solid red";	
			}
			else if( typeof(dom) != 'undefined' && dom != null ){
				dom.style.border = "none";
			}
		}	

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