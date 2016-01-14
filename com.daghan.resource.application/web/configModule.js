var configModule = angular.module('configModule', []);

configModule.controller('ConfigController', [ '$http', '$filter',

function($http, $filter) {
    var vm = this;
    this.configs = [];
    this.selectedConfig = null;
    this.childFrom = null;
    this.showTheForm = false;
    this.selectConfig = function(config) {
	this.selectedConfig = config;
	this.showTheForm = false;
    };

    // Create a child form for a new configuration
    this.newChildForm = function(config) {
	$http({
	    method : 'GET',
	    url : '/system/console/configMgr/'+config.pidString,
	    params:{'post':'true'}
	}).then(function successCallback(response) {
	    vm.childForm = response.data;
	}, function errorCallback(response) {
	    this.alerts.push({
		type : 'failure',
		msg : response
	    });
	});
	this.showTheForm = true;
	this.selectedConfig = config;
    };

    // Create an update form for existing child
    this.updateChildForm = function(config) {
	// TODO
	this.showTheForm = true;
	this.selectedConfig = config;
    };

    // Get the bundles
    $http({
	method : 'GET',
	url : '/rest/configurations'
    }).then(function successCallback(response) {
	vm.configs = response.data;
	vm.selectConfig(vm.configs[0]);
    }, function errorCallback(response) {
	this.alerts.push({
	    type : 'failure',
	    msg : response
	});
    });

} ]);
