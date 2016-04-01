cinefms.controller('EventsController', ['$scope', '$location', 'ControllerService', 'EventService', 'TagService', 'MediaClipTypeService',
    function($scope, $location, controllerService, eventService, tagService, mediaClipTypeService) {
        controllerService.listController('events',$scope,$location,eventService);
        tagService.list().then(
            function(tags) {
                $scope.tags = tags;
            }
        )
        mediaClipTypeService.list().then(
            function(mediacliptype) {
                $scope.mediaClipTypes = mediacliptype;
            }
        )
        $scope.start();
    }
]);

cinefms.controller('AssignVersionsController', ['$scope', '$location', 'ControllerService', 'EventService', 'TagService', 'MediaClipTypeService','MovieService', 'MovieVersionService','MediaclipService',
    function($scope, $location, controllerService, eventService, tagService, mediaClipTypeService, movieService, movieVersionService, mediaclipService) {

        mediaClipTypeService.list().then(
            function(mediacliptype) {
                $scope.mediaClipTypes = mediacliptype;
            }
        )
        $scope.refreshOnUpdate = true;
        $scope.currentEventId = '';
        $scope.currentEventItemExternalId = '';

        $scope.newObject = {};
        $scope.list = {};
        if(!angular.isUndefined($location)) {
            $scope.list.params = $location.search() || {};
            $scope.list.params.start = $scope.list.params.start || 0;
            $scope.list.params.max = 200;
        } else {
            $scope.list.params = {};
        }
        $scope.list.params.within = "0h|";
        $scope.list.params.order = "startTime";
        $scope.list.params.versionStatus = [0,1,2];
        $scope.list.params.asc = "true";
        $scope.list.params.max || 75;

        $scope.select = function(o) {
            $scope.$broadcast("selected",o.id);
        }

        $scope.start = function() {
            console.log("list.start("+"events"+"): starting ... ");
            eventService.permissions().then(
                function(p) {
                    console.log("list.start("+"events"+"): got permissions ... "+p);
                    $scope.permissions = p;
                    $scope.$watch(
                        function() {return $scope.list.params;},
                        $scope.refresh,
                        true
                    );
                    $scope.refresh();
                },
                function() {
                    console.log("list.start("+"events"+"): error getting permissions ... ");
                }
            );
        };

        $scope.refresh = function() {
            if(!$scope.permissions) {
                console.log("list.refresh("+"events"+"): refresh ... no permissions available yet");
                return;
            }
            if(!$scope.permissions.list) {
                console.log("list.refresh("+"events"+"): refresh ... no permission");
                return;
            };
            if(!angular.isUndefined($location)) {
                $location.search($scope.list.params);
            }
            console.log("list.refresh("+"events"+"): refresh ... ");
            eventService.list($scope.list.params).then(
                function(l) {
                    $scope.eventItems = [];
                    // Filter Events by MediaClipTypes
                    console.log("list.refresh("+"events"+"): refresh ... finished");

                    angular.forEach(l, function (event) {
                        console.log("Processing Event " + event.name)
                            // Load MediaClips for EventItem
                                    angular.forEach(event.eventItems, function (eventItem) {
                                        var addEventItem = false;
                                        var matchingMediaclipCount = 0;
                                        console.log("Processing EventItem" + eventItem.name)
                                        mediaclipService.list({movieId:eventItem.movieId}).then(
                                                function(mediaclips) {
                                                    console.log("MCs found: "+mediaclips.length);
                                                    angular.forEach(mediaclips, function (mediaclip) {
                                                        if($scope.list.params.type == mediaclip.type && mediaclip.disabled == false){
                                                            addEventItem = true;
                                                            console.log(mediaclip.type+"   "+mediaclip.name);
                                                            console.log("Found Matching MC of type " +$scope.list.params.type);
                                                            matchingMediaclipCount ++;
                                                        }
                                                    });
                                                    eventItem.event = event;
                                                    eventItem.mediaclips = mediaclips;
                                                    eventItem.mediaClipCount = mediaclips.length;
                                                    eventItem.matchingMediaClipCount = matchingMediaclipCount;
                                                        if((eventItem.versionStatus == 1 || eventItem.versionStatus == 2) && addEventItem == true){
                                                            console.log("Adding...")
                                                            $scope.eventItems.push(eventItem);
                                                        }else{
                                                            console.log("Skipping...")
                                                        }
                                                }
                                            );
                                    });
                    });

                },
                function() {
                    console.log("list.refresh("+"events"+"): refresh ... an error occured!");
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

        $scope.$watch($scope.list.params,$scope.refresh,true);


        $scope.start();
        console.log($scope);
    }
]);

cinefms.controller('EventController', ['$scope', '$location', '$routeParams', 'ControllerService', 'EventService', 'LocationService', 'TagService', 'LanguageService', 'MovieService', 'MovieVersionService',
    function($scope, $location, $routeParams, controllerService, eventService, locationService, tagService, languageService, movieService, movieVersionService) {
        controllerService.singleController('event',$routeParams['id'],$scope,$location,eventService);
        $scope.locationService = locationService;
        $scope.tagService = tagService;
        $scope.languageService = languageService;
        $scope.movieService = movieService;
        $scope.movieVersionService = movieVersionService;
        $scope.start();
    }
]);


cinefms.controller('EventsCalendarController', ['$scope', '$http', '$location', '$routeParams', 'ControllerService', 'EventService', 'LocationService',
    function ($scope, $http, $location, $routeParams, controllerService, eventService, locationService) {
    $scope.eventSources = [];
    $scope.changeView = function(view,calendar) {
        console.log ('changeView');
    };

    $scope.uiConfig = {
        calendar:{
            defaultView: 'timelineDay',
            schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives',
            lazyFetching: false,
            height: 'auto',
            editable: false,
            slotLabelInterval: "02:00",
            displayEventTime: false,
            timeFormat: 'H(:mm)',
            header:{
                center: 'title',
                left: 'prev,next',
                right: 'timelineDay, timelineWeek, prev,next'
            },
            changeView: console.log ('changeView'),
            resourceColumns: [
                {
                    labelText: 'Location',
                    field: 'location'
                }
            ],
            resources: [],
            events: [],
            eventResourceField: 'location'
        }
    };


    locationService.list({start:0,max:100000,active:true}).then(
        function(locations) {
            console.log (locations);
            angular.forEach(locations, function (location) {
                $scope.uiConfig.calendar.resources.push({id: location.id, location: location.name, site: location.siteName});
            });
        }
    )

    eventService.list({start:0,max:100000}).then(
        function(events) {
            angular.forEach(events, function (event) {
                title = event.name + "\n " + event.length + " Min\n";
                $scope.uiConfig.calendar.events.push({id: event.id, location: event.locationId, title: title , start: event.startTime, end: event.endTime, url: "/admin/#/schedule/event/"+event.id});
                console.log (event.name);
            });
        }
    )

    }

]);

