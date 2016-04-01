

cinefms.controller('MovieListController', ['$scope', '$location', 'ControllerService', 'MovieService', 'LocationService',
    function($scope, $location, controllerService, movieService, locationService) {
        controllerService.listController('movies',$scope,$location,movieService);
        $scope.locationService = locationService;
        $scope.start();
    }
]);


cinefms.controller("MovieController", ['$scope', '$location', '$window', '$timeout', '$routeParams', 'ControllerService', 'MovieService', 'CountryService', 'hotkeys',
    function($scope, $location, $window, $timeout, $routeParams, controllerService, movieService, countryService, hotkeys) {
        $scope.countryService = countryService;
        $scope.expanded = 'overview_expand';
        controllerService.singleController('movie',$routeParams['id'],$scope,$location,movieService);
        $scope.params.tab = $scope.params.tab || "overview";
        hotkeys.bindTo($scope)
            .add({
                combo: 'alt+o',
                description: 'Goto tab "Overview"',
                callback: function() {
                    $scope.params.tab = "overview";
                }
            })
            .add({
                combo: 'alt+k',
                description: 'Goto tab "Keys"',
                callback: function() {
                    $scope.params.tab = "keys";
                }
            })
            .add({
                combo: 'alt+v',
                description: 'Goto tab "Versions"',
                callback: function() {
                    $scope.params.tab = "versions";
                }
            })
            .add({
                combo: 'alt+m',
                description: 'Goto tab "Media Clips"',
                callback: function() {
                    $scope.params.tab = "mediaclips";
                }
            })
            .add({
                combo: 'alt+p',
                description: 'Goto tab "Packages"',
                callback: function() {
                    $scope.params.tab = "packages";
                }
            })
            .add({
                combo: 'alt+s',
                description: 'Goto tab "Screenings"',
                callback: function() {
                    $scope.params.tab = "screenings";
                }
            })
            .add({
                combo: 'alt+t',
                description: 'Goto tab "Tickets"',
                callback: function() {
                    $scope.params.tab = "tickets";
                }
            })
            .add({
                combo: 'alt+d',
                description: 'Goto tab "Digital Delivery"',
                callback: function() {
                    $scope.params.tab = "delivery";
                }
            })
        $scope.start();
    }
]);


cinefms.controller('MovieKeyListController', ['$scope', '$routeParams', 'ControllerService', 'KeyService',
    function($scope, $routeParams, controllerService, keyService) {
        $scope.movieId = $routeParams['id'];
        controllerService.listController('keys',$scope,undefined,keyService,{movieId:$scope.movieId});
        $scope.start();
    }
]);


cinefms.controller('MovieVersionListController', ['$scope', '$routeParams', 'ControllerService', 'MovieVersionService', 'MovieService', 'MediaclipService', 'TagService', 'LanguageService',
    function($scope, $routeParams, controllerService, movieVersionService, movieService, mediaclipService, tagService, languageService) {
        $scope.movieId = $routeParams['id'];
        controllerService.listController('movieversions',$scope,undefined,movieVersionService,{movieId:$scope.movieId});
        $scope.newObject.movieId = $scope.movieId;
        $scope.movieService = movieService;
        $scope.tagService = tagService;
        $scope.languageService = languageService;
        $scope.open = function() {
            $scope.newObject.movieId = $scope.movieId;
            console.log("open .... "+$scope.newObject.movieId);
        }
        $scope.$on('object_added',$scope.refresh);
        $scope.$watch('movieId',$scope.refresh);
        $scope.start();
    }
]);

cinefms.controller('MediaclipVersionListController', ['$scope', '$routeParams', 'ControllerService', 'MovieVersionService', 'MovieService', 'MediaclipService', 'TagService', 'LanguageService',
    function($scope, $routeParams, controllerService, movieVersionService, movieService, mediaclipService, tagService, languageService) {
        $scope.mediaClipId = $routeParams['id'];
        controllerService.listController('movieversions',$scope,undefined,movieVersionService,{mediaClipId:$scope.mediaClipId});
        $scope.newObject.movieId = $scope.movieId;
        $scope.movieService = movieService;
        $scope.tagService = tagService;
        $scope.languageService = languageService;
        $scope.open = function() {
            $scope.newObject.movieId = $scope.movieId;
            console.log("open .... "+$scope.newObject.movieId);
        }
        $scope.$on('object_added',$scope.refresh);
        $scope.$watch('mediaClipId',$scope.refresh);
        $scope.start();
    }
]);

cinefms.controller('PackageListController', ['$scope', '$routeParams', 'ControllerService', 'PackageService',
    function($scope, $routeParams, controllerService, packageService) {
        controllerService.listController('packages',$scope,undefined,packageService,{movieId:$routeParams['id']});
        $scope.start();
    }
]);




cinefms.controller('MovieVersionListEntryController', ['$scope',
    function($scope) {
        $scope.child = {
            expanded : false,
            foldable : true
        };
        $scope.$on('shortcut', function(event, mass) {
            if($scope.hovering == true && mass == 'x') {
                if ($scope.child.expanded != true) {
                    $scope.child.expanded = true;
                } else {
                    $scope.child.expanded = false;
                }
            }
        });
    }]);


cinefms.controller('MovieMediaClipListController', ['$scope', '$routeParams', 'ControllerService', 'MediaclipService',  'MovieVersionService', 'MovieService', 'MediaclipService', 'TagService', 'LanguageService',
    function($scope, $routeParams, controllerService, mediaclipService, movieVersionService, movieService, mediaclipService, tagService, languageService) {
        $scope.movieId = $routeParams['id'];
        controllerService.listController('mediaclips',$scope,undefined,mediaclipService,{movieId:$scope.movieId});
        $scope.newObject.movieId = $scope.movieId;
        $scope.movieService = movieService;
        $scope.tagService = tagService;
        $scope.languageService = languageService;
        $scope.child = {
            expanded : false,
            foldable : true
        };
        $scope.open = function() {
            $scope.newObject.movieId = $scope.movieId;
            console.log("open .... "+$scope.newObject.movieId);
        }
        $scope.$on('object_added',$scope.refresh);
        $scope.start();
    }
]);

cinefms.controller('MovieMediaClipListEntryController', ['$scope',
    function($scope) {
        $scope.child = {
            expanded : false,
            foldable : true
        };
        $scope.$on('shortcut', function(event, mass) {
            if($scope.hovering == true && mass == 'x') {
                if ($scope.child.expanded != true) {
                    $scope.child.expanded = true;
                } else {
                    $scope.child.expanded = false;
                }
            }
        });
    }]);




cinefms.controller('MovieScreeningListController', ['$scope', '$routeParams', 'ControllerService', 'EventService', 'MediaclipService',  'MovieVersionService', 'MovieService', 'MediaclipService', 'TagService', 'LanguageService',
    function($scope, $routeParams, controllerService,  eventService, mediaclipService, movieVersionService, movieService, mediaclipService, tagService, languageService) {
        $scope.movieId = $routeParams['id'];
        controllerService.listController('events',$scope,undefined,eventService,{movieId:$scope.movieId,order:"startTime"});
        $scope.newObject.movieId = $scope.movieId;
        $scope.movieService = movieService;
        $scope.open = function() {
            $scope.newObject.movieId = $scope.movieId;
            console.log("open .... "+$scope.newObject.movieId);
        }
        $scope.refreshOnUpdate = false;
        $scope.$on('object_added',$scope.refresh);
        movieService.get($scope.movieId).then(
            $scope.start
        );
    }
]);

cinefms.controller('MovieScreeningListEntryController', ['$scope','$routeParams', '$location',
    function($scope, $routeParams, $location) {
        $scope.child = {
            expanded : false,
            foldable : true
        };
        $scope.$on('shortcut', function(event, mass) {
            if($scope.hovering == true && mass == 'x') {
                if ($scope.child.expanded != true) {
                    $scope.child.expanded = true;
                } else {
                    $scope.child.expanded = false;
                }
            }
        });
    }]);



cinefms.controller('MediaclipsController', ['$scope', '$location', '$window', '$timeout', 'ControllerService', 'MediaclipService', 'MediaclipTypeService', 'MovieService',
         'MediaclipFramerateService', 'MediaclipMediaAspectService', 'MediaclipScreenAspectService', 'TagService',  'LanguageService',
    function($scope, $location, $window, $timeout, controllerService, mediaclipService, mediaclipTypeService, movieService, framerateService, mediaAspectService, screenAspectService, tagService, languageService) {
        controllerService.listController('mediaclips',$scope,$location,mediaclipService);
        $scope.movieService = movieService;
        $scope.mediaclipTypeService = mediaclipTypeService;
        $scope.framerateService = framerateService;
        $scope.mediaAspectService = mediaAspectService;
        $scope.screenAspectService = screenAspectService;
        $scope.tagService = tagService;
        $scope.languageService = languageService;
        mediaclipTypeService.list().then(
            function(types) {
                $scope.mediaClipTypes = types;
            }
        )
        tagService.list().then(
            function(tags) {
                $scope.tags = tags;
            }
        )
        $scope.list.params.tab = $scope.list.params.tab || "overview";
        $scope.start();
    }
]);

cinefms.controller('MediaclipController', ['$scope', '$http', '$location', '$window', '$timeout', '$routeParams', 'ControllerService', 'MediaclipService', 'MediaclipTypeService', 'MovieService',
            'MediaclipFramerateService', 'MediaclipMediaAspectService', 'MediaclipScreenAspectService', 'TagService', 'PackageService',  'LanguageService', 'EventService', 'hotkeys',
    function($scope, $http, $location, $window, $timeout, $routeParams, controllerService, mediaclipService, mediaclipTypeService, movieService, framerateService, mediaAspectService, screenAspectService, tagService, packageService, languageService, eventService, hotkeys) {
        controllerService.singleController('mediaclip',$routeParams['id'],$scope,$location,mediaclipService);
        $scope.movieService = movieService;
        $scope.mediaclipTypeService = mediaclipTypeService;
        $scope.framerateService = framerateService;
        $scope.mediaAspectService = mediaAspectService;
        $scope.screenAspectService = screenAspectService;
        $scope.tagService = tagService;
        $scope.packageService = packageService;
        $scope.languageService = languageService;
        $scope.params.tab = $scope.params.tab || "overview";

        $scope.$watch("mediaclip.id", function() {
            if(angular.isUndefined($scope.mediaclip)) {
                $scope.events = [];
            } else {
                eventService.list({mediaClipId:$scope.mediaclip.id}).then(
                    function(events) {
                        $scope.events = events;
                    }
                );
            }

        })
        $scope.logjson = function() {
          console.log($scope);
        };


        $scope.$on('vjsVideoReady', function (e, data) {
            //data contains `id` and `vid`
            console.log(data);
            data.vid.playbackRates =  [0.5, 1, 1.5, 2];
            data.vid.hotkeys({
                volumeStep: 0.1,
                seekStep: 5
            });
            $scope.player = data.vid;
//            $element.focus();
        });

        $scope.checkTimecode = function () {
            $scope.mediaclip.data.tape.inPoint = ("00000000" + $scope.mediaclip.data.tape.inPoint.replace(/[^\d]/g,'')).slice(-8);
            $scope.mediaclip.data.tape.inPoint = $scope.mediaclip.data.tape.inPoint.replace(/(\d)(?=(\d{2})+(?!\d))/g, "$1:");
            $scope.mediaclip.data.tape.outPoint = ("00000000" + $scope.mediaclip.data.tape.outPoint.replace(/[^\d]/g,'')).slice(-8);
            $scope.mediaclip.data.tape.outPoint = $scope.mediaclip.data.tape.outPoint.replace(/(\d)(?=(\d{2})+(?!\d))/g, "$1:");
        }

        hotkeys.bindTo($scope)
            .add({
                combo: 'alt+o',
                description: 'Goto tab "Overview"',
                callback: function() {
                    $scope.params.tab = "overview";
                }
            })
            .add({
                combo: 'alt+t',
                description: 'Goto tab "Tasks"',
                callback: function() {
                    $scope.params.tab = "tasks";
                }
            })
            .add({
                combo: 'alt+k',
                description: 'Goto tab "Keys"',
                callback: function() {
                    $scope.params.tab = "keys";
                }
            })
            .add({
                combo: 'alt+r',
                description: 'Goto tab "Key Requests"',
                callback: function() {
                    $scope.params.tab = "keyrequests";
                }
            })
            .add({
                combo: 'alt+p',
                description: 'Goto tab "Packages"',
                callback: function() {
                    $scope.params.tab = "packages";
                }
            })
            .add({
                combo: 'alt+s',
                description: 'Goto tab "Screenings"',
                callback: function() {
                    $scope.params.tab = "screenings";
                }
            })
            .add({
                combo: 'alt+d',
                description: 'Goto tab "Data"',
                callback: function() {
                    $scope.params.tab = "data";
                }
            })
            .add({
                combo: 'alt+a',
                description: 'Goto tab "Attachments"',
                callback: function() {
                    $scope.params.tab = "attachments";
                }
            })
            .add({
                combo: 'alt+v',
                description: 'Goto tab "Devices"',
                callback: function() {
                    $scope.params.tab = "devices";
                }
            })
            .add({
                combo: 'alt+i',
                description: 'Goto tab "Tickets"',
                callback: function() {
                    $scope.params.tab = "tickets";
                }
            })
            .add({
                combo: 'alt+m',
                description: 'Goto tab "Movie"',
                callback: function() {
                    $scope.params.tab = "movie";
                }
            })
        $scope.start();
    }
]);


cinefms.controller('MediaclipTasklistController', ['$scope', '$location', '$routeParams', 'MediaclipTaskService',
    function($scope, $location, $routeParams, mediaclipTaskService) {
        console.log(" ------------------- getting tasks for media clip ");
        mediaclipTaskService.list({max:100,closed:true,mediaClipId : $routeParams['id']}).then(
            function(tasks) {
                $scope.tasks = tasks;
            }
        );
    }
]);

cinefms.controller('MediaclipSingleController', ['$scope', '$location', '$routeParams', 'MediaclipService',
    function($scope, $location, $routeParams, mediaclipService) {
        $scope.init = function(id) {
            mediaclipService.get(id).then(function(mediaclip){
                $scope.mediaclip = mediaclip;
            });
            console.log(" ------------------- getting data for media clip " + id);
        }
    }
]);

cinefms.controller('MediaclipSearchController', ['$scope', '$location', '$routeParams', 'MediaclipService',
    function($scope, $location, $routeParams, mediaclipService) {
        $scope.init = function(id) {
            mediaclipService.list({externalId: id}).then(function(mediaclips){
                $scope.mediaclips = mediaclips;
            });
        }
    }
]);

cinefms.controller('PackagesController', ['$scope', '$location', 'ControllerService', 'PackageService', 'PackageTypeService', 'MovieService',
    function($scope, $location, controllerService, packageService, packageTypeService, movieService) {
        controllerService.listController('packages',$scope,$location,packageService);
        $scope.movieService = movieService;
        $scope.packageTypeService = packageTypeService;
        packageTypeService.list().then(
            function(types) {
                $scope.packagetypes = types;
            }
        );

        $scope.start();
    }
]);

cinefms.controller('PackageController', ['$scope', '$location', '$routeParams', 'ControllerService', 'PackageService', 'PackageTypeService',
    function($scope, $location, $routeParams, controllerService, packageService, packageTypeService) {
        controllerService.singleController('package',$routeParams['id'],$scope,$location,packageService);
        $scope.start();
    }
]);


cinefms.controller('TaskListController', ['$scope', '$location', 'ControllerService', 'MediaclipTaskService', 'MediaclipTaskTypeService',
    function($scope, $location, controllerService, taskService, taskTypeService) {
        controllerService.listController('tasks',$scope,$location,taskService);
        taskTypeService.list().then(function(tasktypes){
            $scope.tasktypes = tasktypes;
        });
        $scope.start();
    }
]);


cinefms.controller("TaskController", ['$scope', '$location', '$routeParams', 'ControllerService', 'MediaclipTaskService',
    function($scope, $location, $routeParams, controllerService, taskService) {
        controllerService.singleController('task',$routeParams['id'],$scope,$location,taskService);
        $scope.start()
    }
]);

cinefms.controller('MediaclipDevicelistController', ['$scope', '$location', 'ControllerService', 'DeviceService','DeviceStatusService',
    function($scope, $location, $routeParams, deviceService, deviceStatusService) {
        $scope.devices = [];
        $scope.init = function(UUID) {
            console.log(" ------------------- getting devices for media clip ");
            deviceService.list({max: 1000}).then(
                function (devices) {
                    console.log($scope);
                    angular.forEach(devices, function (device) {
                        deviceStatusService.list(device.id).then(
                            function (statuses) {
                                deviceStatusService.get(device.id, statuses[0].id).then(
                                    function (status) {
                                        angular.forEach(status.cplStatus, function (cplStatus) {
                                            if(cplStatus.cplUuid == UUID){
                                                device.cplStatus = cplStatus;
                                                $scope.devices.push(device);
                                            }
                                        });
                                    }
                                );
                            });
                    });
                });
            console.log($scope.devices);
        }
    }
]);

cinefms.controller('MediaclipScreeningDevicelistController', ['$scope', '$routeParams', 'ControllerService', 'EventService',
    function($scope, $routeParams, controllerService,  eventService) {
            if(angular.isUndefined($scope.mediaclip)) {
                $scope = [];
            } else {
                $scope.devices = [];
                eventService.list({mediaClipId:$scope.mediaclip.id}).then(
                    function(events) {
                        angular.forEach(events, function(value) {
                            add = true;
                                angular.forEach(value.devices, function(value) {
                                    angular.forEach($scope.devices, function(existingDevices) {
                                        if(existingDevices.deviceModel == value.deviceModel && existingDevices.deviceVendor == value.deviceVendor){
                                            add = false;
                                        }
                                    });
                                    if(add == true && value.mediaClipTypes.indexOf($scope.mediaclip.type) >= 0 ){
                                        $scope.devices.push(value);
                                    }
                                });
                        });
                    }
                );
            }
        console.log($scope);
    }
]);

cinefms.controller('MediaclipEventController', ['$scope', '$routeParams', 'ControllerService', 'EventService',
    function($scope, $routeParams, controllerService,  eventService) {
        if(angular.isUndefined($scope.mediaclip)) {
            $scope = [];
        } else {
            $scope.events = [];
            eventService.list({mediaClipId:$scope.mediaclip.id}).then(
                function(events) {
                    angular.forEach(events, function(event) {
                        event.audioLanguageIds = [];
                        event.subtitleLanguageIds = [];
                            angular.forEach(event.eventItems, function(eventItem) {
                                    angular.forEach(eventItem.mediaClips, function(mediaClip) {
                                        if(mediaClip.mediaClipId == $scope.mediaclip.id){
                                            event.audioLanguageIds.push(eventItem.audioLanguageIds);
                                            event.subtitleLanguageIds.push(eventItem.subtitleLanguageIds);
                                        }
                                    });
                            });
                        $scope.events.push(event);
                    });
                }
            );
        }
        console.log($scope);
    }
]);