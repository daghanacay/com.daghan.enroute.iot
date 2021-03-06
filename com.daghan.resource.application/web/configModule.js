var configModule = angular.module('configModule', []);

configModule
	.controller(
		'ConfigController',
		[
			'$http',
			'$filter',
			function($http, $filter) {
			    // Init
			    var vm = this;
			    this.configs = [];
			    this.selectedConfig = null;
			    this.childFrom = null;
			    this.showTheForm = false;
			    getAllConfigs();
			    

			    // functions
			    this.selectConfig = function(config) {
					this.selectedConfig = config;
					this.showTheForm = false;
			    };

			    // Create a child form for a new configuration
			    this.newChildForm = function(config) {
					$http(
					{
					    method : 'GET',
					    url : '/system/console/configMgr/'
						    + config.pidString,
					    params : {
						'post' : 'true'
					    }
					}).then(
					function successCallback(response) {
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
					$http(
					{
					    method : 'GET',
					    url : '/system/console/configMgr/'
						    + childConfigStr,
					    params : {
						'post' : 'true'
					    }
					}).then(
					function successCallback(response) {
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

			    this.addConfig = function() {
					$http(
					{
					    method : 'POST',
					    url : '/system/console/configMgr/[Temporary PID replaced by real PID upon save]',
					    headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					    },
					    transformRequest : function(childForm) {
						var str = [];
						var propList = [];
						str.push("apply=true");
						str.push("factoryPid="+encodeURIComponent(childForm.pid));
						str.push("action=ajaxConfigManager");
						for ( var p in childForm.properties){
						    str
							    .push(encodeURIComponent(p)
								    + "="
								    + encodeURIComponent(childForm.properties[p].value));
							propList.push(encodeURIComponent(p));
						}
						str.push("propertylist="+propList.join(","));
						return str.join("&");
					    },
					    data : vm.childForm
					})
					.then(
						function successCallback(
							response) {
						   getAllConfigs();
						},
						function errorCallback(response) {
						    this.alerts.push({
							type : 'failure',
							msg : response
						    });
						});
			    };

			    this.updateConfig = function() {
$http(
					{
					    method : 'POST',
					    url : '/system/console/configMgr/'+vm.childForm.pid,
					    headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					    },
					    transformRequest : function(childForm) {
						var str = [];
						var propList = [];
						str.push("apply=true");
						str.push("factoryPid="+encodeURIComponent(childForm.pid));
						str.push("action=ajaxConfigManager");
						for ( var p in childForm.properties){
						    str
							    .push(encodeURIComponent(p)
								    + "="
								    + encodeURIComponent(childForm.properties[p].value));
							propList.push(encodeURIComponent(p));
						}
						str.push("propertylist="+propList.join(","));
						return str.join("&");
					    },
					    data : vm.childForm
					})
					.then(
						function successCallback(
							response) {
						   getAllConfigs();
						},
						function errorCallback(response) {
						    this.alerts.push({
							type : 'failure',
							msg : response
						    });
						});
			    };

			    this.removeConfig = function() {
				$http({
						method : 'POST',
						url : '/system/console/configMgr/' + vm.childForm.pid,
						params : {
							"apply":1,
							"delete":1
							}
			    	}).then(function successCallback(response) {
						getAllConfigs();
			    	}, function errorCallback(response) {
						this.alerts.push({
				    		type : 'failure',
				    		msg : response
						});
			    	});
			    };

			    // Get the configurations
			    function getAllConfigs(){
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
			    };

			} ]);
