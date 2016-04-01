

var cinefms = angular.module('cinefms', ['ngRoute', 'restangular', 'spinner', 'multi-transclude', 'cfp.hotkeys', 'chart.js', 'ui.calendar', 'oitozero.ngSweetAlert','vjs.video', '720kb.tooltips']);

cinefms.run(['$rootScope', '$route', function($rootScope, $route) {
    console.log("cinefms.run ! ========================= ")
    $rootScope.$on('$routeChangeSuccess', function() {
        if(typeof $route.current.title != 'undefined'){
            document.title = $route.current.title;
        }else{
            document.title = 'FMS';
        }
    });
}]);

cinefms.config( 

    function($routeProvider, $httpProvider, RestangularProvider) {

        console.log ("configuring routes ... ");

        $routeProvider
            .when('/profile', {
                templateUrl: 'app/templates/profile.html'
            })
            .when('/login', {
                title: 'FMS - Login',
                templateUrl: 'app/templates/login.html'
            })
            .when('/home',{
                templateUrl: 'app/templates/home.html'
            })
            .when('/movies/movies',{
                title: 'FMS - Movies',
                templateUrl: 'app/templates/project/movies/movies.html',
                reloadOnSearch : false
            })
            .when('/movies/movies/:id',{
                title: 'FMS - Movie',
                templateUrl: 'app/templates/project/movies/movie.html',
                linkObject : "com.openfms.model.core.movie.FmsMovie",
                reloadOnSearch : false
            })
            .when('/movies/mediaclips',{
                title: 'FMS - Mediaclips',
                templateUrl: 'app/templates/project/movies/mediaclips.html',
                reloadOnSearch: false
            })
            .when('/movies/mediaclips/:id',{
                title: 'FMS - Mediaclip',
                templateUrl: 'app/templates/project/movies/mediaclip.html',
                linkObject: 'com.openfms.model.core.movie.FmsMediaClip',
                reloadOnSearch: false
            })
            .when('/movies/packages',{
                title: 'FMS - Packages',
                templateUrl: 'app/templates/project/movies/packages.html',
                reloadOnSearch: false
            })
            .when('/movies/packages/:id',{
                title: 'FMS - Package',
                templateUrl: 'app/templates/project/movies/package.html',
                linkObject: 'com.openfms.model.core.movie.FmsMoviePackage',
                reloadOnSearch: false
            })
            .when('/movies/tasks',{
                title: 'FMS - Tasks',
                templateUrl: 'app/templates/project/movies/tasks.html',
                reloadOnSearch: false
            })
            .when('/movies/tasks/:id',{
                title: 'FMS - Task',
                templateUrl: 'app/templates/project/movies/task.html',
                linkObject: 'com.openfms.model.core.movie.FmsMediaClipTask',
                reloadOnSearch: false
            })
            .when('/movies/deliveries',{
                title: 'FMS - Digital Deliveries',
                templateUrl: 'app/templates/project/movies/deliveries.html',
                reloadOnSearch: false
            })

            .when('/keys/overview',{
                title: 'FMS - Keys',
                templateUrl: 'app/templates/project/keys/overview.html',
                reloadOnSearch: false
            })
            .when('/keys/due',{
                title: 'FMS - Keys',
                templateUrl: 'app/templates/project/keys/due.html',
                reloadOnSearch: false
            })
            .when('/keys/due/:id',{
                title: 'FMS - Keys',
                templateUrl: 'app/templates/project/keys/duedetails.html',
                reloadOnSearch: false
            })
            .when('/keys/uploads',{
                title: 'FMS - Keys',
                templateUrl: 'app/templates/project/keys/uploads.html',
                reloadOnSearch: false
            })

            
            .when('/schedule/events',{
                title: 'FMS - Events',
                templateUrl: 'app/templates/project/schedule/events.html',
                reloadOnSearch: false
            })
            .when('/schedule/event/:id',{
                title: 'FMS - Event',
                templateUrl: 'app/templates/project/schedule/event.html',
                linkObject: 'com.openfms.model.core.playback.FmsEvent',
                reloadOnSearch: false
            })
            .when('/schedule/overview',{
                title: 'FMS - Schedule',
                templateUrl: 'app/templates/project/schedule/overview.html',
                reloadOnSearch: false
            })
            .when('/schedule/calendar',{
                title: 'FMS - Schedule',
                templateUrl: 'app/templates/project/schedule/calendar.html',
                reloadOnSearch: false
            })
            .when('/schedule/devicestatus',{
                templateUrl: 'app/templates/project/schedule/status_devices.html',
                reloadOnSearch: false
            })
            .when('/schedule/devicestatus/:id',{
                templateUrl: 'app/templates/project/schedule/status_device.html',
                reloadOnSearch: false
            })

            .when('/schedule/assignversions',{
                templateUrl: 'app/templates/project/schedule/assign_versions.html',
                reloadOnSearch: false
            })

            .when('/stats/media',{
                title: 'FMS - Stats',
                templateUrl: 'app/templates/project/stats/media.html',
                reloadOnSearch: false
            })


            .when('/setup/project',{
                templateUrl: 'app/templates/project/setup/project.html',
                reloadOnSearch: false
            })

            .when('/setup/sites',{
                templateUrl: 'app/templates/project/setup/sites.html',
                reloadOnSearch: false
            })
            .when('/setup/sites/:id',{
                templateUrl: 'app/templates/project/setup/site.html',
                linkObject: 'com.openfms.model.core.playback.FmsSite',
                reloadOnSearch: false
            })

            .when('/setup/locations',{
                templateUrl: 'app/templates/project/setup/locations.html',
                reloadOnSearch: false
            })
            .when('/setup/locations/:id',{
                templateUrl: 'app/templates/project/setup/location.html',
                linkObject: 'com.openfms.model.core.playback.FmsLocation',
                reloadOnSearch: false
            })

            .when('/setup/devices',{
                templateUrl: 'app/templates/project/setup/devices.html',
                reloadOnSearch: false
            })
            .when('/setup/devices/:id',{
                templateUrl: 'app/templates/project/setup/device.html',
                linkObject: 'com.openfms.model.core.playback.FmsPlaybackDevice',
                reloadOnSearch: false
            })

            
            .when('/setup/certificates',{
                templateUrl: 'app/templates/project/setup/certificates.html',
                reloadOnSearch: false
            })
            .when('/setup/certificates/:id',{
                templateUrl: 'app/templates/project/setup/certificate.html',
                linkObject: 'com.openfms.model.core.crypto.FmsCertificate',
                reloadOnSearch: false
            })


            .when('/setup/keys',{
                templateUrl: 'app/templates/project/setup/keys.html',
                reloadOnSearch: false
            })
            .when('/setup/keys/:id',{
                templateUrl: 'app/templates/project/setup/key.html',
                linkObject: 'com.openfms.model.core.crypto.FmsKey',
                reloadOnSearch: false
            })


            .when('/setup/tags',{
                templateUrl: 'app/templates/project/setup/tags.html',
                reloadOnSearch: false
            })
            .when('/setup/workflow',{
                templateUrl: 'app/templates/project/setup/workflow.html',
                reloadOnSearch: false
            })
            .when('/setup/scheduledjobs',{
                templateUrl: 'app/templates/project/setup/scheduledjobs.html',
                reloadOnSearch: false
            })
            .when('/setup/scheduledjobs/:id',{
                templateUrl: 'app/templates/project/setup/scheduledjob.html',
                linkObject: 'com.openfms.model.core.scheduledjobs.FmsScheduledJobConfig',
                reloadOnSearch: false
            })
            .when('/setup/users_groups',{
                templateUrl: 'app/templates/project/setup/user_groups.html',
                reloadOnSearch: false
            })

            .when('/global/notifications',{
                title: 'FMS - Notifications',
                templateUrl: 'app/templates/project/global/notifications.html',
                reloadOnSearch: false
            })

            .when('/admin/projects',{
                templateUrl: 'app/templates/admin/projects.html',
                reloadOnSearch: false
            })
            .when('/admin/projects/:projectId',{
                templateUrl: 'app/templates/admin/project.html',
                reloadOnSearch: false
            })
            .otherwise({
                redirectTo: '/home'
            })
        ;

        console.log ("done ... ");
        
        RestangularProvider.setBaseUrl('/');

    }
);