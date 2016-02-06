var bundleModule = angular.module('bundleModule', []);

bundleModule.directive("fileread", [function () {
    return {
        scope: {
            fileread: "="
        },
        link: function (scope, element, attributes) {
            element.bind("change", function (changeEvent) {
                scope.$apply(function () {
                    scope.fileread = changeEvent.target.files[0];
                    // or all selected files:
                    // scope.fileread = changeEvent.target.files;
                });
            });
        }
    }
}]);

bundleModule
	.controller(
		'BundleController',
		[
			'$http',
			'$filter',
			function($http, $filter) {
			    var vm = this;
			    this.resources = [];
			    this.selected = [];
			    this.selectedState = [];
				getBundles();

			    this.isSelected = function(bundle) {
				return bundle === this.selected;
			    };

			    this.updateState = function() {
				if (this.selected.stateRaw == 1) {
				    this.selected.state = "Uninstalled";
				    // css class
				    this.selectedState = "warning";
				}
				if (this.selected.stateRaw == 2) {
				    this.selected.state = "Installed";
				    // css class
				    this.selectedState = "warning";
				}
				if (this.selected.stateRaw == 4) {
				    this.selected.state = "Resolved";
				    // css class
				    this.selectedState = "info";
				}
				if (this.selected.stateRaw == 32) {
				    this.selected.state = "Active";
				    // css class
				    this.selectedState = "success";
				}
			    };

			    this.select = function(bundle) {
				this.selected = bundle;
				this.updateState();
			    };

			    // Check the state of the
			    // !isFragment && (installed | resolved)
			    this.hasStart = function() {
				return (!this.selected.fragment)
					&& (this.selected.stateRaw == 2 || this.selected.stateRaw == 4)
			    };
			    // !isFragment && active
			    this.hasStop = function hasStop() {
				return (!this.selected.fragment)
					&& (this.selected.stateRaw == 32)
			    };
			    // installed | resolved | active
			    this.hasUninstall = function hasUninstall() {
				return this.selected.stateRaw == 2
					|| this.selected.stateRaw == 4
					|| this.selected.stateRaw == 32
			    };

			    // Get the bundles
			    function getBundles(){$http({
				method : 'GET',
				url : '/system/console/bundles.json'
			    }).then(
				    function successCallback(response) {
					vm.resources = $filter('filter')(
						response.data.data, {
						    category : "IoT"
						}, true);
					vm.select(vm.resources[0]);
				    }, function errorCallback(response) {
					this.alerts.push({
					    type : 'failure',
					    msg : response
					});
				    });
				};
			    // update the bundle state
			    this.updateBundleState = function(action) {
				$http(
					{
					    method : 'POST',
					    url : '/system/console/bundles/'
						    + vm.selected.id,
					    headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					    },
					    transformRequest : function(obj) {
						var str = [];
						for ( var p in obj)
						    str
							    .push(encodeURIComponent(p)
								    + "="
								    + encodeURIComponent(obj[p]));
						return str.join("&");
					    },
					    data : {
						action : action
					    }

					})
					.then(
						function successCallback(
							response) {
						    if (1 == response.data.stateRaw) { // uninstalled
							index = vm.resources
								.indexOf(vm.selected);
							vm.resources.splice(
								index, 1);
							vm.selected = [];
						    } else {
							vm.selected.stateRaw = response.data.stateRaw;
						    }
						    vm.updateState();
						},
						function errorCallback(response) {
						    this.alerts.push({
							type : 'failure',
							msg : response
						    });
						});
			    };

				// upload bundle
			    this.uploadBundle = function() {
					var fd = new FormData();
    				//Take the first selected file
    				fd.append("bundlefile", vm.uploadme);
    				fd.append("action", "install");
    				fd.append("bundlestart", "start");

				    $http.post("/system/console/bundles", fd, {
				        withCredentials: true,
				        headers: {'Content-Type': undefined },
				        transformRequest: angular.identity
				    }).success(function(){getBundles();});
				 } 
			} ]);
