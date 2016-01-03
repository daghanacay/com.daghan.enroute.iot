var iotManagementPortalApp = angular.module('iotManagementPortalApp', [
	'ngRoute', 'bundleModule', 'configModule' ]);

iotManagementPortalApp.controller('MainController', [ '$location',
	function($location) {
	    var vm = this;

	    this.page = function() {
		return $location.path();
	    }
	} ]);

iotManagementPortalApp.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/bundles', {
	templateUrl : '/com.daghan.resource/main/htm/BundleView.htm',
	controller : 'BundleController'
    }).when('/configurations', {
	templateUrl : '/com.daghan.resource/main/htm/ConfigView.htm',
	controller : 'ConfigController'
    }).otherwise({
	redirectTo : '/bundles'
    });
} ]);