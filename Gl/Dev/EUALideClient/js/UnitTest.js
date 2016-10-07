/**
 * 
 */

describe('EulaClient', function(){
	
	var $controller,
		$rootScope,
		$scope,
		$httpBackend,
		controller;
	
	var baseURL = 'https://localhost:8081/eula';
	
	beforeEach(angular.mock.module('myapp'));
									
	beforeEach(angular.mock.inject(function($injector){
		$controller = $injector.get('$controller');	
		$rootScope = $injector.get('$rootScope');
		$scope = $rootScope.$new();
		$httpBackend = $injector.get('$httpBackend');
		
	}));

	describe('File validation', function(){		

		var input = angular.element("<input type=\"file\" accept=\".txt, .pdf\">");
		
		beforeEach( function(){
			controller = $controller('fileCtrl', {$scope : $scope});
		});
		
		it('.txt file should be accepted', function() {			
			var file = {
					name : "test.txt",
					size : 10000,
					type : "text/plain"
			};			
		    var fileList = { 
		    		0: file,
		    		length: 1,
		    		item: function (index) { return file; }
		    };		    
		    input.files = fileList;
		    $scope.setFile(input);	    
		    
		    expect($scope.btnActive).toBe(true);
		    }
		  );
		
		it('.jpg file should not be accepted', function() {			
			var file = {
					name : "image.jpg",
					size : 10000,
					type : "image/png"
			};			
		    var fileList = { 
		    		0: file,
		    		length: 1,
		    		item: function (index) { return file; }
		    };		    
		    input.files = fileList;
		    $scope.setFile(input);	    
		    
		    expect($scope.btnActive).toBe(false);
		 });
	});
	
	
	describe('URL validation', function(){
		var input = angular.element("<input type=\"text\" data-ng-model=\"input.url\">");
		
		beforeEach( function(){
			controller = $controller('fileCtrl', {$scope : $scope});
		});
		
		it('non-empty url should be accepted', function(){
			var Url = "https://www.example.com";
			
			input.value = Url;			
			
			$scope.inputUrl = Url;
			$scope.validateUrl();
			
			expect($scope.btnActive).toBe(true);
			expect($scope.errorMsg).toBe(Url);
		});
		
		it('empty url should not be accepted', function(){
			$scope.inputUrl = "";
			$scope.validateUrl();
			
			expect($scope.btnActive).toBe(false);
			expect($scope.errorMsg).toBe("No file/url uploaded yet!");
		});
	});		

});


