var angular = angular;

var spinner = angular.module('spinner', []);
spinner.factory('spinnerInterceptor', [ 'ErrorService', '$q', '$location' , 
    function (errorService,$q, $location) {

        var numLoadings = 0;

        return {
            request : function(config) {
                numLoadings++;
                $('.loader-not-spinning').addClass('loader-spinning');
                $('.loader-inactive').addClass('loader-active');
                //config.url = URI(config.url).addSearch({'apiKey':'SKJLLS-BASE-542fae473a46e97bed59f903'}).toString();
                return config;
            },
            response : function (response) {
                if (!(--numLoadings)) {
                    $('.loader-not-spinning').removeClass('loader-spinning');
                    $('.loader-inactive').removeClass('loader-active');
                }
                return response || $q.when(response);
            },
            responseError : function (response) {
                console.log("error ... ");
                if (!(--numLoadings)) {
                    $('.loader-not-spinning').removeClass('loader-spinning');
                    $('.loader-inactive').removeClass('loader-active');
                }
                if(response.status == 401) {
                    $location.path('/login');
                    console.log($location.path());
                } else if(response.status > 399) {
                    errorService.reportError(response.status,response);
                }
                return $q.reject(response);
            }
        }
    }
]);

spinner.config(
    function($httpProvider) {
        $httpProvider.defaults.withCredentials = true;
        $httpProvider.defaults.useXDomain = true;
        $httpProvider.interceptors.push('spinnerInterceptor');
    }
);

