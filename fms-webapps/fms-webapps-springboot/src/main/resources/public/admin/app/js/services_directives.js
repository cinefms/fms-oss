


cinefms.factory("DirectiveService", [ '$q', '$timeout', 'ControllerService', 
	function($q,$timeout,controllerService) {
		var a = {

			editor : function (name, template, service) {

				var me = {

					replace: false,
					transclude: true,
					templateUrl: 'app/partial/' + template,
					require: '^?fmsDialog',
					scope : {
						id: "=?"
					},
					reset : function(scope) {
					},
					refreshed : function(scope) {
					},
					init : function(scope) {
					},
					controller : function($scope) {

		                console.log("editor.controller("+name+"): instantiating controller ... ");

		                $scope.edit = function() {
		                	if($scope.permissions.edit == true) {
		                		$scope.editing = !$scope.editing;
		                	}
		                }

		                $scope.start = function() {
		                    console.log("editor.controller.start("+name+"): id is "+$scope.id+" ... ");
		                    if(angular.isUndefined($scope.id)) {
		                        service.permissions().then(
		                            function(p) {
		                                $scope.permissions = p;
		                                $scope.refresh();
		                                console.log("editor.controller.start("+name+"): got permissions for list ... ");
		                                me.reset($scope);
		                            },
		                            function() {
		                                console.log("editor.controller.start("+name+"): error getting permissions for "+$scope.id+" ... ");
		                            }
		                        );
		                    } else {
		                        service.permissions($scope.id).then(
		                            function(p) {
		                                $scope.permissions = p;
	                                	$scope.refresh();
		                                console.log("editor.controller.start("+name+"): got permissions for "+$scope.id+" ... ");
		                            },
		                            function() {
		                                console.log("editor.controller.start("+name+"): error getting permissions for "+$scope.id+" ... ");
		                            }
		                        );

		                    }
		                };
		
		                $scope.close = function() {
                            $scope[name] = {};
                            me.reset($scope);
			                if($scope.dialog && $scope.dialog.close) {
				                $scope.dialog.close();
			                }
		                }

		                $scope.remove = function() {
		                	service.remove($scope[name].id).then(
		                		function() {
				                	$scope.$emit("object_removed",$scope[name].id);
				                	$scope.$broadcast("object_removed",$scope[name].id);
				                	$scope[name] = undefined;
		                		}
		                	);
		                }

		                $scope.add = 
		                    function() {
                                console.log("editor.controller.add("+name+")!");
		                        if(!$scope.permissions.create) {
	                                console.log("editor.controller.add("+name+") no CREATE permissions ... !");
		                            return;
		                        };
		                        var v = $q.defer();
		                        service.save($scope[name]).then(
		                            function(o) {
		                            	console.log("add success!");
		                                v.resolve(o);
		                                $scope.$emit("close_dialog");
		                                $scope.$emit("object_added",o);
		                                $scope.$broadcast("close_dialog");
		                                $scope.$broadcast("object_added",o);
		                            },
		                            function() { 
		                            	console.log("add error!");
			                            $scope.refresh();
		                            	v.reject();
		                            }
		                        );
		                        return v.promise;
		                    };

		                $scope.save = 
		                    function() {
                                console.log("editor.controller.save("+name+")!");
		                        if(!$scope.permissions || !$scope.permissions.edit) {
		                            return;
		                        };
		                        var v = $q.defer();
		                        service.save($scope[name]).then(
		                            function(o) {
		                                $scope[name] = o;
		                                v.resolve(o);
		                            },
		                            function(){ 
			                            $scope.refresh();
		                            	v.reject();
		                            }
		                        );
		                        return v.promise;
		                    };

		                $scope.refreshed = function($scope) {

		                }

		                $scope.refresh = 
		                    function() {
                                console.log("editor.controller.refresh("+name+")!");
		                        if(angular.isUndefined($scope.id)) {
		                            return;
		                        };
		                        if(!$scope.permissions) {
		                            return;
		                        };
		                        if(!$scope.permissions.read) {
		                            return;
		                        };
		                        var v = $q.defer();
		                        service.get($scope.id).then(
		                            function(o) {
		                                $scope[name] = o;
		                                v.resolve(o);
		                            },
		                            function() {
		                            	v.reject();
		                            }
		                        );
		                        v.promise.then(
		                        	function() {
		                        		me.refreshed($scope);
		                        		$scope.refreshed($scope);
		                        	});
		                        return v.promise;
		                    };

		                $scope.open = function() {
		                	me.open($scope);
		                }

		                $scope.$watch("id", $scope.refresh);

		                $scope.start();

					},
					link : function(scope, element, attrs, dialog) {

		                if(!angular.isUndefined(dialog)) {
			                scope.dialog = dialog;
		                }
						me.init(scope);
						scope.element = element;
						if(angular.isUndefined(scope.id)) {
							scope.editing = true;
							scope.canEdit = false;
						} else {
							scope.editing = false;
							scope.canEdit = true;
						}
					}
				}
				return me;
			}
		}

		return a;

	}
]);