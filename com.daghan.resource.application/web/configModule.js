var configModule = angular.module('configModule', []);

configModule.controller('ConfigController', [ '$http', '$filter',
	function($http, $filter) {
	    var vm = this;
	    this.variable = "variable";
	    this.configs = [];
	    // Get the bundles
	    $http({
		method : 'GET',
		url : '/rest/configurations'
	    }).then(function successCallback(response) {
		vm.configs = response.data;
	    }, function errorCallback(response) {
		this.alerts.push({
		    type : 'failure',
		    msg : response
		});
	    });

	} ]);
