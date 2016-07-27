var app = angular.module("heatMapApp", [ 'ui-leaflet', 'ngRoute' ]);
app
	.controller(
		"MainHeatmapController",
		[
			"$scope",
			"$http",
			"$interval",
			function($scope, $http, $interval) {

			    $interval(function() {
				updateData();
				$scope.layers.overlays.heat.doRefresh = true;
			    }, 10000);

			    updateData = function() {
				$http.get("../rest/sensorData").success( 
					function(data) {
					    $scope.sensorData = data
						    .map(function(dat) {
							return [ dat.latitude, dat.longitude, dat.sensorValue ]
						    });
					    $scope.layers.overlays = {
						heat : {
						    name : 'Heat Map',
						    type : 'heat',
						    data : $scope.sensorData,
						    layerOptions : {
							radius : 20,
							blur : 10
						    },
						    visible : true,
						    doRefresh : true
						}
					    };
					});
			    }

			    angular
				    .extend(
					    $scope,
					    {
						center : {
						    lat : 37.774546,
						    lng : -122.433523,
						    zoom : 16
						},
						markers : {
						    melbourne : {
							lat : -37.8136,
							lng : 144.9631,
						    }
						},

						layers : {
						    baselayers : {
							osm : {
							    name : 'OpenStreetMap',
							    url : 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
							    type : 'xyz'
							}
						    }
						}
					    });
			} ]);

//Configuating navigation
app.config([ "$routeProvider", function($routeProvider) {
    $routeProvider.when("/sensorDataView", {
	templateUrl : "/heatmap/views/SensorDataView.html",
	controller : ""
    }).when("/warningsView", {
	templateUrl : "/heatmap/views/WarningsView.html",
	controller : ""
    });
} ]);
