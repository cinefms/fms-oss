


cinefms.factory('ControllerService', [ '$q', '$timeout',

    function($q,$timeout) {
        return {
            singleController : function(name,id,$scope,$location,service) {
                console.log("single.start("+name+"): init ... ");
                $scope.params = {};
                if(!angular.isUndefined($location)) {
                    $scope.params = $location.search() || {};
                } else {
                    $scope.params = {};
                }

                $scope.start = function() {
                    console.log("single.start("+name+"): id is "+$scope.id+" ... ");
                    if(angular.isUndefined($scope.id)) {
                        service.permissions().then(
                            function(p) {
                                $scope.permissions = p;
                            },
                            function() {
                                console.log("single.start("+name+"): error getting permissions for "+$scope.id+" ... ");
                            }
                        );
                        console.log("single.start("+name+"): id is --- undefined ... ");
                    } else {
                        console.log("single.start("+name+"): getting permissions for '"+$scope.id+"' ... ");
                        service.permissions($scope.id).then(
                            function(p) {
                                $scope.permissions = p;

                                if(id) {
                                    $scope.id = id;
                                }
                                $scope.refresh();
                            },
                            function() {
                                console.log("single.start("+name+"): error getting permissions for "+$scope.id+" ... ");
                            }
                        );

                    }
                };

                $scope.tab = function(t) {
                    console.log("Tab: "+t);
                    if(!angular.isUndefined($location)) {
                        var s = $location.search() || {} ;
                        s.tab = t;
                        $location.search(s);
                    }
                    $scope.params.tab = t;
                }

                $scope.subtab = function(t) {
                    console.log("Subtab: "+t);
                    if(!angular.isUndefined($location)) {
                        var s = $location.search() || {} ;
                        s.subtab = t;
                        $location.search(s);
                    }
                    $scope.params.subtab = t;
                }

                $scope.edit = function() {
                    if($scope.permissions.edit) {
                        $scope.editing = $scope.editing || false;
                        $scope.editing = !$scope.editing;
                    }
                }

                $scope.delete = function() {
                    if(!$scope.permissions.delete) {
                        return;
                    };
                    service.remove($scope[name].id).then(
                        function(ob) {
                            $scope.$emit('object_removed',$scope[name]);
                            $scope[name] = undefined;
                        }
                    );
                }

                $scope.save = 
                    function() {
                        if(!$scope.permissions.edit) {
                            console.log("single.save("+name+"): saving object ... aborted (no permission)");
                            return;
                        };
                        console.log("single.save("+name+"): saving object ... ");
                        service.save($scope[name]).then(
                            function(o) {
                                console.log(" ---------------------------- saved object ... ");
                                console.log(o);
                                $scope[name] = o;
                                $scope.$emit('object_updated',$scope[name]);
                            },
                            $scope.refresh
                        );
                    };

                $scope.refresh = 
                    function() {
                        if(!$scope.permissions.read) {
                            console.log("single.refresh("+name+"): no permission ... ");
                            return;
                        };
                        if(angular.isUndefined($scope.id)) {
                            console.log("single.refresh("+name+":"+$scope.id+"): nothing to load ... ");
                            return;
                        };
                        var v = $q.defer();
                        service.get($scope.id).then(
                            function(o) {
                                $scope[name] = o;
                            }
                        );
                    };

                $scope.$watch("id",$scope.set);

                $scope.$on("selected", function(evt,id) {
                    $scope.id = id;
                    $scope.start();                    
                });

                $scope.id = id;

            },
            listController : function(name,$scope,$location,service,params) {

                $scope.refreshOnUpdate = true;

                $scope.newObject = {};
                $scope.list = {};
                if(!angular.isUndefined($location)) {
                    $scope.list.params = $location.search() || {};
                    $scope.list.params.start = $scope.list.params.start || 0;
                    $scope.list.params.max = $scope.list.params.max || 25;
                } else {
                    $scope.list.params = {};
                }

                _.keys(params||{}).forEach(
                    function(k) {
                        $scope.list.params[k] = params[k];
                    }
                );

                $scope.select = function(o) {
                    $scope.$broadcast("selected",o.id);
                }

                $scope.open = function() {
                    $scope.dialog_open = true;
                    $scope.newObject = {};
                };

                $scope.close = function() {
                    $scope.dialog_open = false;
                };

                $scope.add = function() {
                    service.save($scope.newObject).then(
                        function() {
                            $scope.$emit("close_dialog");
                            $scope.$broadcast("close_dialog");
                            $scope.newObject = {};
                            $scope.refresh();
                        },
                        function() {
                        }
                    );
                };

                $scope.remove = function(id) {
                    console.log("removing from "+name+": "+id);
                    service.remove(id).then(function() {
                        $scope.refresh();
                    });
                };

                $scope.start = function() {
                    console.log("list.start("+name+"): starting ... ");
                    service.permissions().then(
                        function(p) {
                            console.log("list.start("+name+"): got permissions ... "+p);
                            $scope.permissions = p;
                            $scope.$watch(
                                function() {return $scope.list.params;}, 
                                $scope.refresh,
                                true
                            );
                            $scope.refresh();
                        },
                        function() {
                            console.log("list.start("+name+"): error getting permissions ... ");
                        }
                    );
                };

                $scope.refresh = function() {
                    if(!$scope.permissions) {
                        console.log("list.refresh("+name+"): refresh ... no permissions available yet");
                        return;
                    }
                    if(!$scope.permissions.list) {
                        console.log("list.refresh("+name+"): refresh ... no permission");
                        return;
                    };
                    if(!angular.isUndefined($location)) {
                        $location.search($scope.list.params);
                    }
                    console.log("list.refresh("+name+"): refresh ... ");
                    service.list($scope.list.params).then(
                        function(l) {
                            console.log("list.refresh("+name+"): refresh ... finished");
                            $scope[name] = l;
                        },
                        function() {
                            console.log("list.refresh("+name+"): refresh ... an error occured!");
                        }
                    );
                };

                $scope.order = function(by) {

                    if(($scope.list.params.order+"")!=(by)) {
                        $scope.list.params.order = by;
                        $scope.list.params.asc = true;
                    } else {
                        $scope.list.params.asc = !$scope.list.params.asc;
                    }

                }

                $scope.$on('closedialog', $scope.refresh);
                $scope.$on('object_updated', function() {
                    if($scope.refreshOnUpdate) {
                        $scope.refresh()
                    }
                });
                $scope.$on('object_removed', $scope.refresh);
                $scope.$on('object_added', $scope.refresh);

                $scope.$watch($scope.list.params,$scope.refresh,true);

            }
        };
    }

])
