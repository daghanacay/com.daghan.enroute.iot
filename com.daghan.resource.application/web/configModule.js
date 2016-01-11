var configModule = angular.module('configModule', []);

configModule.controller('ConfigController', [ '$http', '$filter',
	function($http, $filter) {
	    var vm = this;
	    this.configs = [];
	    this.selectedConfig = null;
	    // Get the bundles
	    $http({
		method : 'GET',
		url : '/rest/configurations'
	    }).then(function successCallback(response) {
		vm.configs = response.data;
		vm.selectedConfig = vm.configs[0]; 
	    }, function errorCallback(response) {
		this.alerts.push({
		    type : 'failure',
		    msg : response
		});
	    });

	} ]);
