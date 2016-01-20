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
	    url : '/system/console/configMgr/' + config.pidString,
	    params : {
		'post' : 'true'
	    }
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
	this.childSelected = false;
    };

    // Create an update form for existing child
    this.updateChildForm = function(childConfigStr) {
	$http({
	    method : 'GET',
	    url : '/system/console/configMgr/' + childConfigStr,
	    params : {
		'post' : 'true'
	    }
	}).then(function successCallback(response) {
	    vm.childForm = response.data;
	}, function errorCallback(response) {
	    this.alerts.push({
		type : 'failure',
		msg : response
	    });
	});
	this.showTheForm = true;
	this.selectedChildConfig = childConfigStr;
	this.childSelected = true;
    };
    
    this.addConfig = function(){
	//TODO
    };
    
    this.updateConfig = function(){
	//TODO
    };

    this.removeConfig = function(){
	//TODO
    };
    
    // Get the configurations
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
