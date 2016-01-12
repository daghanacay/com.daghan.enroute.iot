var configModule = angular.module('configModule', []);

configModule.controller('ConfigController', [ '$http', '$filter',

function($http, $filter) {
    var vm = this;
    this.configs = [];
    this.selectedConfig = null;
    this.childFrom = null;
    this.showTheForm = true;
    this.selectConfig = function(config){
	this.selectedConfig = config;
	this.showTheForm = false;
    };
	
	// Create a child form
    this.createChildForm = function(config) {
	this.childForm = {
	    title : config.pidString,
	    description : "testDescription"
	};
	this.showTheForm = true;
	this.selectedConfig = config;
    };
   
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
