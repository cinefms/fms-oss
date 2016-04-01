cinefms.directive("eventEditor", [ 'DirectiveService', 'EventService', 'LocationService', 'DeviceService', 'MovieVersionService', 'MovieService', 'MediaclipService', 'TagService', 'LanguageService',

	function(directiveService, eventService, locationService, deviceService, movieVersionService,movieService,mediaClipService,tagService,languageService) {
		var out = directiveService.editor("event", 'fms-event-edit.html' , eventService);

		out.scope.movieId = "=";
		out.scope.foldable = "=?";
		out.scope.expanded = "=?";

		out.init = function(scope) {
			if(angular.isUndefined(scope.expanded)) {
				scope.expanded = true;
			}
			if(angular.isUndefined(scope.foldable)) {
				scope.foldable = false;
			}
			console.log("eventEditor.init(): .... ");
			scope.removeItem = function(a) {
				console.log("eventEditor.removeItem("+a+"): .... ");
				scope.event.eventItems.splice(a,1);
				scope.save();
			}
			scope.addItem = function() {
				scope.event.eventItems = scope.event.eventItems || [];
				scope.event.eventItems.push({});
			}
			scope.resetVersion = function(a) {
				console.log("resetVersion ------- :"+a)
				scope.event.eventItems[a].movieVersionId = undefined;
				scope.save();
			}

            scope.$on('shortcut', function(event, mass) {
                if(scope.hovering == true && mass == 'e') {
                    scope.edit();
                }
            });

			scope.$watch('event.locationId', function(x) {
				console.log("location ID: "+x);
				if(angular.isUndefined(x)) {
					scope.devices = [];
				} else {
					deviceService.list({locationId:x}).then(
						function(devices) {
							scope.devices = devices;
						}
					);
				}
			});

			scope.locationService = locationService;
			scope.movieService = movieService;
			scope.movieVersionService = movieVersionService;
			scope.mediaClipService = mediaClipService;
			scope.tagService = tagService;
			scope.languageService = languageService;
			
		};

		return out;
	}
]);

cinefms.directive("eventItemMediaclipList", [ '$q', 'MovieVersionService', 'MovieService', 'MediaclipService', 'TagService', 'LanguageService', 'ControllerService', 
	function($q,movieVersionService,movieService,mediaClipService,tagService,languageService,controllerService) {
		var a = {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-movieversion-mediacliplist.html',
			scope : {
				movieVersionId: "="
			},
			controller : function($scope) {

			},
			link : function(scope, element, attrs) {
				console.log("eventItemMediaclipList linking ...  ");

				scope.$watch('movieVersionId', function(x) {

					console.log("eventItemMediaclipList watch triggered: "+x);

					if(angular.isUndefined(x)) {
						scope.mediaclips = [];
						return;
					}

					console.log("getting movie version .... "+x);
					movieVersionService.get(x).then(
						function(mv) { 
							console.log("got movie version .... "+x);
						}
					);
				});

			}
		}
		console.log(a);
		return a;
	}
]);

cinefms.directive("eventItemAssigner", [ '$q', 'MovieVersionService', 'MovieService', 'MediaclipService', 'TagService', 'LanguageService', 'ControllerService','EventService',
    function($q,movieVersionService,movieService,mediaClipService,tagService,languageService,controllerService,eventService) {
        var a = {
            replace: false,
            transclude: true,
            templateUrl: 'app/partial/fms-eventitem-assigner.html',
            scope : {
                currentEventId: "=",
                currentEventItemExternalId: "="
            },
            controller : function($scope) {

            },
            link : function(scope, element, attrs) {
                console.log("eventItemAssigner ...  ");
                scope.$watch('currentEventItemExternalId', function(x) {

                    console.log("eventItemAssigner watch triggered: "+x);
                    eventService.get(scope.currentEventId).then(
                        function(event) {
                            scope.event = event;
                        }
                    );
                    console.log(scope.currentEventId);
                    console.log(scope.currentEventItemExternalId);
                });

            }
        }
        console.log(a);
        return a;
    }
]);

cinefms.directive("scheduleOverview", [ '$q', '$timeout', '$route', 'EventService', 'LocationService', 'ProjectSettingsService', 'hotkeys',
	function($q,$timeout,$route,eventService,locationService,projectSettingsService, hotkeys) {
		return {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-schedule-overview.html',
			scope : {
				currentDate : "=?",
				currentGroup : "=?"
			},
			controller : function($scope) {

			},
			link : function(scope, element, attrs, ngModel, transclude) {
				transclude(scope, function(clone, scope) {
  					element.append(clone);
  				});
  				scope.date = Date.now();
				scope.size = -1;
				scope.start = 0;
				scope.current = {
					date : undefined,
					group : undefined,
					count : -1,
					width : 140,
					detail_expanded : false
				}

				scope.display = {};

                hotkeys.add({
                    combo: 'esc',
                    description: 'Close Details',
                    callback: function(event, hotkey) {
                        $(element).find("#details").hide();
                        event.preventDefault();
                    }
                });

                hotkeys.add({
                    combo: 'right',
                    description: 'Next Page',
                    callback: function(event, hotkey) {
                        if(_.findIndex(scope.locationGroups, scope.current.group) < _.size(scope.locationGroups) -1){
                            scope.current.group = scope.locationGroups[_.findIndex(scope.locationGroups, scope.current.group)+1];
                            scope.updateDisplayed();
                        }
                        event.preventDefault();
                    }
                });

                hotkeys.add({
                    combo: 'left',
                    description: 'Previous Page',
                    callback: function(event, hotkey) {
                        if(_.findIndex(scope.locationGroups, scope.current.group) > 0){
                            scope.current.group = scope.locationGroups[_.findIndex(scope.locationGroups, scope.current.group)-1];
                            scope.updateDisplayed();
                        }
                        event.preventDefault();
                    }
                });

                hotkeys.add({
                    combo: 'down',
                    description: 'Next Day',
                    callback: function(event, hotkey) {
                        currentIndex = null;
                        scope.days.forEach(function (element, index) {
                           if(element.day == scope.current.date){
                               currentIndex = index;
                           }
                        });
                        if(currentIndex < _.size(scope.days) -1){
                            scope.current.date = scope.days[currentIndex+1].day;
                            scope.updateDisplayed();
                        }
                        event.preventDefault();
                    }
                });

                hotkeys.add({
                    combo: 'up',
                    description: 'Previous Day',
                    callback: function(event, hotkey) {
                        currentIndex = null;
                        scope.days.forEach(function (element, index) {
                            if(element.day == scope.current.date){
                                currentIndex = index;
                            }
                        });
                        if(currentIndex > 0){
                            scope.current.date = scope.days[currentIndex-1].day;
                            scope.updateDisplayed();
                        }
                        event.preventDefault();
                    }
                });
                hotkeys.add({
                    combo: 't',
                    description: 'Go to Today',
                    callback: function(event, hotkey) {
                        scope.current.date = moment().format("YYYY-MM-DD");
                        scope.updateDisplayed();
                        event.preventDefault();
                    }
                });
                hotkeys.add({
                    combo: 'z',
                    description: 'Go to Tomorrow',
                    callback: function(event, hotkey) {
                        scope.current.date = "2016-02-11";
                        scope.updateDisplayed();
                        event.preventDefault();
                    }
                });
                hotkeys.add({
                    combo: 'u',
                    description: 'Go to Day after Tomorrow',
                    callback: function(event, hotkey) {
                        scope.current.date = "2016-02-12";
                        scope.updateDisplayed();
                        event.preventDefault();
                    }
                });
				/**
				scope.$on('$destroy', function() {
					console.log("destroy");
					cleanup();
				});
		        $(document).keydown(
        			function(e) {
        			}
        		);
        		**/

                scope.getToday = moment().format("YYYY-MM-DD");


				scope.updateDisplayed = function() {

					scope.display.locations = [];
					scope.display.events = {};

					if(!scope.current.group || !scope.current.date) {
						console.log(" --- data incomplete .. ");
					} else {
                        console.log(scope.current.date);
						if(scope.current.group) {
							_.forEach(scope.days,function(day){
								var a = 0;
								_.forEach(scope.current.group.locations,function(g) {
									if(
										scope.eventMap[day.day] && 
										scope.eventMap[day.day].locations &&
										scope.eventMap[day.day].locations[g.id] &&
										scope.eventMap[day.day].locations[g.id].events
									) {
										a = a + scope.eventMap[day.day].locations[g.id].events.length;
									}
								});
								day.disabled = a == 0;
								day.size = a;
							});

						}

						scope.display = {
							locations : scope.current.group.locations,
							events : {}
						}

						_.forEach(scope.display.locations,function(l) {
							console.log(l);
							if(scope.eventMap[scope.current.date].locations[l.id]) {
								console.log(scope.eventMap[scope.current.date].locations[l.id]);
								scope.display.events[l.id] = scope.eventMap[scope.current.date].locations[l.id].events;
							}
						})

					}
				}

				scope.updateDisplay = function() {

					if(!scope.current.count || scope.current.count<0) {
						scope.count = scope.fit;
					} else {
						scope.count = scope.current.count;
					}

					if(scope.count > scope.locations.length) {
						scope.count = scope.locations.length;
					}

					scope.locationGroups = [];
					scope.days = [];
					while(true) {

						var x = scope.locations.splice(0,scope.count);
						if(x.length==0) {
							break;
						}

						var l = {
							name : x[0].name+" - "+x[x.length-1].name,
							locations : x
						}

						scope.locationGroups.push(l);
					}

					scope.days = [];

					_.forEach(_.keys(scope.eventMap),function(day) {

						scope.days.push(
							{
								day : day,
								disable : false
							}

						);
					});
					scope.current.date = scope.days[0];
					console.log(scope.eventMap);

					scope.updateDisplayed();

				}


	
				scope.updateData = function() {

					console.log("updating movies ... ");
					var m = $q.defer();
					eventService.list({start:0,max:100000,order:"startTime"}).then(
						function(events) { m.resolve(events); }
					);
					var l = $q.defer();
					locationService.list({start:0,max:100000,active:true}).then(
						function(locations) { l.resolve(locations); }
					);
					var d = $q.defer();
					projectSettingsService.getDateConfig().then(
						function(dateConfig) { 
							console.log(dateConfig);
							d.resolve(dateConfig); 
						}
					);

					var promises = [];
					promises.push(m.promise);
					promises.push(l.promise);
					promises.push(d.promise);
					$q.all(promises).then(
						function(r) {
							scope.events = r[0];
							scope.locations = r[1];
							scope.dateConfig = r[2];

							scope.eventMap = {};
							scope.locationMap = {};
							_.forEach(scope.locations, function(location) {
								scope.locationMap[location.id] = location;
							});
							_.forEach(scope.events, function(event) {
								var date = moment.tz(event.startTime,scope.dateConfig.timezone).format(scope.dateConfig.dateFormat);
								scope.eventMap[date] = scope.eventMap[date] || { locations : [], day : date };
								scope.eventMap[date].locations[event.locationId] = scope.eventMap[date].locations[event.locationId] || {location : scope.locationMap[event.locationId], events : [] };
								scope.eventMap[date].locations[event.locationId].events.push(event);
							});
							console.log(scope.eventMap);
							scope.updateDisplay();

						}
					);
				}

				scope.updateSize = function() {
					scope.current.width = Number(scope.current.width);
					console.log("scope.current.width: "+scope.current.width);
					scope.height = $(element).height(); 
					scope.width = $(element).width();
					scope.locations = [];
					scope.fit = Math.floor(scope.width/scope.current.width);
					scope.rest = scope.width - (scope.fit*scope.current.width);
					scope.extra = Math.floor(scope.rest / scope.fit);
					scope.each = scope.current.width+scope.extra;
					scope.count = scope.fit;
					scope.updateData();
				}

				scope.showDetails = function(elem,evt) {
					console.log("showDetails ... ");
					console.log(elem);
					console.log(evt);
					$(element).find("#details").show();
					scope.current.detail = evt;


				}
				scope.hideDetails = function() {
					$(element).find("#details").hide();
				}

				// $(window).resize(function(){scope.updateSize()});

				scope.updateSize();

			}

		}
	}
]);







