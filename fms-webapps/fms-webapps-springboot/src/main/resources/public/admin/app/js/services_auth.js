
cinefms.factory('AuthService', [ 'Restangular', '$q', '$rootScope', '$interval', '$location',

    function(restangular, $q, $rootScope, $interval, $location) {

        var user = undefined;
        var authenticated = true;
        var rootScope = $rootScope;

        start = function() {
        	check();
			if (!angular.isDefined(self.update)) {
    	    	console.log("auth service starting ... ");
				self.update = $interval(check, 60000);
      		}
        };

        check = function() {
        	console.log("auth service checking ... ");
			restangular.all('').one('admin/authentication/login').get().then(
				function(u) {
					user = u;
					authenticated = true;
					console.log("logged in ... ");
					$rootScope.$broadcast('auth.updated',true);
					return true;
				},
				function(u) {
					user = undefined;
					authenticated = false;
					console.log("NOT logged in ... ");
					$rootScope.$broadcast('auth.updated',false);
					return false;
				}
			);
        };

        isAuthenticated = function() {
            console.log("isAuthenticated: "+authenticated);
            return authenticated;
        };

        logout = function() {
			auth = restangular.one('admin/authentication/login');
			auth.remove().then( 
				function() {
					check();
				}
			)
		};

        login = function(username,password) {

            credentials = restangular.all('admin/authentication').one('login');
            credentials.username = username;
            credentials.password = password;

            credentials.put().then(
                function() {
                    $location.path('/home');
                    return check();
                },
                function() {
                    credentials = restangular.all('admin/authentication').one('login');
                    credentials.emailAddress = username;
                    credentials.password = password;
                    credentials.put().then(
                        function() {
                            $location.path('/home');
                            return check();
                        },
                        function() {
                            return check();
                        }
                    );
                }
            );
		};

        getUser = function() {
            return user;
        };

        getProject = function() {
            var deferred = $q.defer();
            restangular.all('').one('admin/currentproject').get().then(
                function(p) {
                    deferred.resolve(p);
                },
                function(p) {
                    deferred.reject();
                }
            );
            return deferred.promise;
        };

		start();

		//console.log(angular.toJson(user,true));

        return {
        	isAuthenticated : isAuthenticated,
            login : login,
        	logout : logout,
            getUser : getUser,
            getProject : getProject
        }
    }
]);

