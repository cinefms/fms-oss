cinefms.factory('DebounceService', ['$timeout','$q', function($timeout, $q) {
  // The service is actually this function, which we call with the func
  // that should be debounced and how long to wait in between calls
    return {

        debounce : function(func, wait, immediate) {
            var timeout;
            // Create a deferred object that will be resolved when we need to
            // actually call the func
            var deferred = $q.defer();
            return function() {
                var context = this, args = arguments;
                var later = function() {
                    timeout = null;
                    if(!immediate) {
                        deferred.resolve(func.apply(context, args));
                        deferred = $q.defer();
                    }
                };
                var callNow = immediate && !timeout;
                if ( timeout ) {
                    $timeout.cancel(timeout);
                }
                timeout = $timeout(later, wait);
                if (callNow) {
                    deferred.resolve(func.apply(context,args));
                    deferred = $q.defer();
                }
                return deferred.promise;
            }
        }
    }
}]);

cinefms.factory('ServiceService', [ 'DebounceService', 'Restangular', '$http', '$q',
    function(debounceService, restangular, $http, $q) {
        return {
            createService : function(path) {
                return {
                    getDefaultParams : function() {

                    },
                    list : 
                        function(p) {
                            var def = $q.defer();
                            var params = p || {};
                            params.start = params.start || 0;
                            params.max = params.max || 25;
                            restangular.all(path).getList(params).then(
                                function(objects) {
                                    if(params.start==0 || objects.length>0) {
                                        def.resolve(objects);
                                    } else {
                                        params.start = params.start - params.max;
                                        if(params.start<0) {
                                            params.start = 0;
                                        }
                                    }
                                },
                                function(xxx) {
                                    def.reject(xxx);
                                }
                            );
                            return def.promise;
                        },

                    get : function(id) {
                            return restangular.one(path,id).get();
                    },
                    save : debounceService.debounce(
                        function(object) {
                            var def = $q.defer();
                            if(angular.isUndefined(object.id)) {
                                restangular.one(path).customPOST(object).then(
                                    function(a) {
                                        def.resolve(a);
                                    },
                                    function(a) {
                                        def.reject(a);
                                    }
                                );
                            } else {
                                restangular.all(path).one(object.id).customPUT(object).then(
                                    function(a) {
                                        def.resolve(a);
                                    },
                                    function(a) {
                                        def.reject(a);
                                    }
                                );
                            }
                            return def.promise;
                        },200),
                    permissions : function(id) {
                        var def = $q.defer();
                        var url = '/api/v2/'+path;
                        if(angular.isDefined(id)) {
                            url = url + "/" + id;
                        }
                        var req = {
                            method: 'OPTIONS',
                            url: url,
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            data: { },
                        }
                        $http(req).
                        success(function(data, status, headers, config) {
                            var ms = headers('Allow').split(',');
                            var out = {};
                            if(angular.isUndefined(id)) {
                                out = {
                                    list : $.inArray("GET", ms)>-1,
                                    create : $.inArray("POST", ms)>-1,
                                };
                            } else {
                                out = {
                                    read : $.inArray("GET", ms)>-1,
                                    edit : $.inArray("PUT", ms)>-1,
                                    delete : $.inArray("DELETE", ms)>-1,
                                };
                            }
                            def.resolve(out);
                        }).
                        error(function(data, status, headers, config) {
                            def.reject('error retrieving options ('+status+')');
                            def.reject("error");
                        });
                        return def.promise;
                    },
                    remove : function(id) {
                        return restangular.one(path,id).remove();
                    }
                }
            }
        }
    }
]);