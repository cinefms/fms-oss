

cinefms.controller('LoginController', [ '$scope', 'AuthService' ,
	function($scope,auth) {
		$scope.login = function() {
			$scope.success = auth.login($scope.user.username,$scope.user.password);
		}
	}
]); 

cinefms.controller('LoginStatusController', [ '$scope', 'AuthService',
	function($scope,auth) {
		auth.getProject().then(
			function(p) {
				$scope.project = p;
				$scope.isProject = true;
			},
			function(p) {
				$scope.project = undefined;
				$scope.isProject = false;
			}			
		);
		$scope.$on('auth.updated', function(event,authenticated) {
			$scope.authenticated = authenticated;
			$scope.user = auth.getUser();
		});
		$scope.logout = function() {
			auth.logout();
		}
	}
]); 



cinefms.controller('MOTDController', ['$scope', '$location', 'ControllerService', 'MOTDService',
    function($scope, $location, controllerService, motdService) {
        controllerService.listController('messages',$scope,$location,motdService);
        $scope.start();
    }
]);

cinefms.controller('CommentController', ['$scope', '$location', 'ControllerService', 'CommentService','SweetAlert',
    function($scope, $location, controllerService, commentService, SweetAlert) {
		$params={"asc":false}
        controllerService.listController('comments',$scope,$location,commentService,$params);
		$scope.start();
        $scope.delete = function(comment) {
            console.log(comment);
            commentService.remove(comment.id);
            $scope.refresh();
        }
    }
]);

cinefms.controller('SearchController', [ '$q', '$location','$timeout', '$window', '$scope', 'MovieService', 'MediaclipService', 'EventService','hotkeys',

    function($q,$location,$timeout,$window,$scope,movieService,mediaclipService,eventService,hotkeys) {

        $scope.on = false;

        $scope.results = [];
        $scope.sresult = -1;
        $scope.params = {
        	searchTerm : ""
        };

        $scope.openTo = function(a) {
        	if($scope.tab!=a) {
		        $scope.results = [];
        	}
        	$scope.sresult = -1;
        	$scope.tab = a;
        	$scope.params.searchTerm = "";
        	$scope.$apply();
        	$scope.open();
        }

        $scope.on = false;

    	$scope.open = function() {
    		if($scope.on) {
    			return;
    		}
	    	$timeout(function() {
	    		$("#search-input").focus();
	    	},10);
  			$("#search-dialog").show();
	    	$scope.on = true;
    	}
    	$scope.close = function() {
        	$scope.tab = "a";
        	$scope.params.searchTerm = "";
        	$scope.$apply();
  			$("#search-dialog").hide();
	    	$scope.on = false;
    	}

    	$scope.update = _.debounce(
    		function() {
    			console.log(" update ..... ");
    			if($scope.tab=='movies') {
    				movieService.list($scope.params).then(
    					function(results) { $scope.results = results }
    				);
    			} else if($scope.tab=='clips') {
    				    mediaclipService.list($scope.params).then(
    					function(results) { $scope.results = results }
    				);
    			} else if($scope.tab=='events') {
    				eventService.list($scope.params).then(
    					function(results) { $scope.results = results }
    				);
    			}
    		}, 
    		100
    	);

        hotkeys.add('o', 'Overview', function (event) {
            event.preventDefault();
            $location.path('/schedule/overview')
        });

        hotkeys.add('c', 'Search for Mediaclip', function (event) {
            event.preventDefault();
            $scope.openTo("clips");
        });

        hotkeys.add('m', 'Search for Movie', function (event) {
            event.preventDefault();
            $scope.openTo("movies");
        });

        hotkeys.add('s', 'Search for Screenings / Events', function (event) {
            event.preventDefault();
            $scope.openTo("events");
        });

            $(document).keydown(
                function(e) {
                    if (e.altKey) {
                        return;
                    } else if (e.ctrlKey) {
                        return;
                    } else if (e.metaKey) {
                        return;
                    }
                    if($scope.on) {
                        if(e.keyCode==9) {
                            if($scope.tab == 'movies') {
                                $scope.openTo('clips');
                            } else if($scope.tab == 'clips') {
                                $scope.openTo('events');
                            } else if($scope.tab == 'events') {
                                $scope.openTo('movies');
                            }
                            e.preventDefault();
                        }

                        if (e.keyCode==38 || e.keyCode==40) {
                            $scope.sresult = $scope.sresult+(e.keyCode-39);
                            if($scope.results.length==0) {
                                $scope.sresult = -1;
                            } else if($scope.sresult<0) {
                                $scope.sresult = 0;
                            }
                            if($scope.sresult>=$scope.results.length) {
                                $scope.sresult = $scope.results.length-1;
                            }
                            $scope.$apply();
                        }
                        if($scope.sresult>-1 && e.keyCode==13) {
                            $location.path($("#result-"+$scope.sresult).find("a").attr("href").substring(1));
                            $location.search({});
                            $scope.close();
                        }
                    }
                    if (e.keyCode==27) {
                        $( document.activeElement ).blur();
                        $scope.close();
                    }
                    if(e.target.nodeName == "INPUT") {
                        console.log ("in input field, ignoring");
                    } else if(e.target.nodeName == "TEXTAREA") {
                        console.log ("in input field, ignoring");
                    } else if (e.keyCode==90 && e.ctrlKey) {
                        console.log("ctrl-z");
                    } else if (e.keyCode==77) {
                        // m
                        $scope.openTo("movies");
                    } else if (e.keyCode==83) {
                        // e
                        $scope.openTo("events");
                    } else if (e.keyCode==67) {
                        // c
                        $scope.openTo("clips");
                    }  else {
                        console.log(e.keyCode);
                    }
                }
            );
	}

]);


cinefms.controller('ProjectController', [ 'ProjectService', '$scope', '$location', 
	function(ProjectService,$scope,$location) {

		$scope.params = $location.search();

		$scope.newProject = { name : "", shortName : "" };

		$scope.refresh = _.debounce(function() {
			ProjectService.list($scope.params).then(
				function(projects) {
					$scope.projects = projects;
				}
			);
		},200);

		$scope.delete = function(project) {
			ProjectService.delete(project.id);
		}

		$scope.add = function() {
			ProjectService.add($scope.newProject).then(
				function() {
					$scope.newProject = { name : "", shortName : "" };
					$scope.refresh();
				}
			)
		}

		$scope.prev = function(){
			$scope.start = $scope.start-$scope.max;
			if($scope.start<0) {
				$scope.start = 0;
			}
			$scope.refresh();
		}

		$scope.next = function() {
			$scope.start = $scope.start+$scope.max;
			$scope.refresh();
		}

		$scope.$watch('params.searchTerm', $scope.refresh);


		$scope.refresh();
	}
]); 


cinefms.controller('ProjectDetailController', [ 'ProjectService', '$scope', '$routeParams', 
	function(projectService,$scope,$routeParams) {

		$scope.newHostname = "";

		$scope.projectId = $routeParams['projectId'];

		$scope.refresh = function() {
			projectService.get($scope.projectId).then(
				function(project) {
					$scope.project = project;
				}
			)
		}

		$scope.save = function() {
			projectService.save($scope.project).then(
				function(project) {
					$scope.project = project;
				}
			)
		}

		$scope.addHostname = function() {
			if($.inArray($scope.newHostname, $scope.project.hostnames)==-1) {
				$scope.project.hostnames.push($scope.newHostname);
			}
			$scope.newHostname = "";
		}

		$scope.removeHostname = function(hostname) {
			index = $.inArray(hostname, $scope.project.hostnames);
			if(index > -1) {
				$scope.project.hostnames.splice(index,1);
			}
		}

		$scope.refresh();
	}
]); 



cinefms.controller('ProfileController', [ 'UserService', '$scope', 'SweetAlert',
	function(userService,$scope,SweetAlert) {
		$scope.enable = false;
		$scope.reset = {

			"oldPassword" : "",
			"newPasswordA" : "",
			"newPasswordB" : "",

		}

		userService.getCurrentUser().then(
			function(user) {
				$scope.user = user;
			}
		);

		$scope.doReset = function() {
			userService.reset($scope.user.username,$scope.reset.oldPassword,$scope.reset.newPasswordA);
            SweetAlert.swal("Password saved!", "Please log out now.", "success");
        }

		$scope.checkPW = function() {
			var ok = true;
			console.log($scope.reset);
			if($scope.reset.newPasswordA != $scope.reset.newPasswordB) {
				console.log(" pw / pw repeat dont match");
				ok = false;
			} else if($scope.reset.newPasswordA.length < 6) {
				console.log(" pw too short");
				ok = false;
			} else if ($scope.reset.oldPassword.length < 6) {
				console.log(" old pw too short");
				ok = false;
			}
			$scope.enable = ok;
		}

		$scope.$watch("reset", $scope.checkPW, true);

	}
]);
	


cinefms.controller('ErrorController', [ 'ErrorService', '$scope', 
	function(errorService,$scope) {
		$scope.show = false;
		$scope.report = function(status,error) {
			$scope.error = error;
			$scope.show = true;
		}
		$scope.dismiss = function() {
			$scope.show = false;
		}
		errorService.addCallback($scope.report);
	}
]);

cinefms.controller('ShortcutController', ['$scope', '$location' ,'$window', '$timeout', 'hotkeys',
    function($scope, $location, $window, $timeout, hotkeys) {
        hotkeys.bindTo($scope)
            .add({
                combo: 'x',
                description: 'Expand details (toogle)',
                callback: function() {
                    $scope.$broadcast('shortcut', 'x');
                }
            })
            .add({
                combo: 'e',
                description: 'Edit (toogle)',
                callback: function() {
                    $scope.$broadcast('shortcut', 'e');
                }
            })
    }
]);

cinefms.controller('NotificationListController', ['$scope', '$location', '$routeParams', 'ControllerService', 'NotificationService','UserService','SweetAlert',
    function($scope, $location, $routeParams, controllerService, notificationService, userService, SweetAlert) {
		$scope.showClosed = false;
        $scope.userService = userService;

        function messageList() {
			notificationService.list({includeRead:$scope.showClosed}).then(
				function(notifications) {
					notifications.forEach(function (element, index, array) {
						array[index].message=element.message.replace(/[^:]*:/,"");
					});
					$scope.notifications = notifications;
				}
			);
        }
		messageList();
        $scope.toggleShowClosed = function() {
            $scope.showClosed = !$scope.showClosed;
			messageList();
        }
        $scope.add = function() {
            console.log($scope);
            if($scope.notification.to != undefined)
            {
                userService.getCurrentUser().then(
                    function(user) {
                        $scope.notification.to.forEach(function (entry) {
                            console.log("notification for "+entry)
                            notificationData = {
                                "name": "Message",
                                "from": user.name,
                                "to": entry,
                                "message": $scope.notification.text,
                                "objectTypeName": "com.openfms.model.core.global.FmsNotification",
                                "objectTypeDisplayName": "FmsNotification",
                                "searchable": "Message"
                            }
                            notificationService.save(notificationData);
                        });
                    }
                )
                console.log("add comment!!!")
                SweetAlert.swal("Notification sent!", "Your notification has been sent.", "success");
            }
        }
    }
]);
