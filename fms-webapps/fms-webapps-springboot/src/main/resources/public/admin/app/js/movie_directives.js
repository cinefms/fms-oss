cinefms.directive("movieEditor", [ 'DirectiveService', 'MovieService', 'CountryService', 'UserService',
	function(directiveService, movieService,countryService,userService) {
		var out = directiveService.editor("movie", 'fms-movie-edit.html' , movieService);
		out.init = function(scope) {
			console.log("movieEditor.init(): .... ");
	        scope.countryService = countryService;
			userService.list({'movieId':scope.id}).then(
				function(users) {
					scope.users = users;
				}
			);
		};
		out.reset = function(scope) {
		};


		return out;
	}
]);


cinefms.directive("movieVersionEditor", [ 'DirectiveService', 'MovieVersionService', 'MovieService', 'MediaclipService', 'TagService', 'LanguageService', 'ControllerService', 
	function(directiveService, movieVersionService,movieService,mediaClipService,tagService,languageService,controllerService) {
		var out = directiveService.editor("movieversion", 'fms-movieversion-edit.html' , movieVersionService);

		out.scope.movieId = "=";
		out.scope.foldable = "=?";
		out.scope.expanded = "=?";

		out.init = function(scope) {

			scope.movieversion = {};

			if(angular.isUndefined(scope.expanded)) {
				scope.expanded = true;
			}
			if(angular.isUndefined(scope.foldable)) {
				scope.foldable = false;
			}
			console.log("movieVersionEditor.init(): .... ");
			scope.movieService = movieService;
			scope.mediaClipService = mediaClipService;
			scope.tagService = tagService;
			scope.languageService = languageService;

		};
		out.reset = function(scope) {
			console.log(" setting movieId .... ");
			scope.movieversion = {
				movieId : scope.movieId
			};
			console.log(scope.movieversion);
		};

		return out;
	}
]);


cinefms.directive("packageEditor", [ '$q', 'DirectiveService', 'PackageService', 'PackageFileService', 'PackageCopyService', 'PackageFileCopyService','StorageSystemService',
	function($q,directiveService, packageService, packageFileService, packageCopyService, packageFileCopyService,storageSystemService) {
		var out = directiveService.editor("package", 'fms-package-edit.html' , packageService);

		out.init = function(scope) {

			var fDef = $q.defer();
			packageFileService.list({packageId:scope.id,max:10000}).then(
				function(files) {
					fDef.resolve(files);
				}
			);
			var fcDef = $q.defer();
			packageFileCopyService.list({packageId:scope.id,max:10000}).then(
				function(filecopies) {
					fcDef.resolve(filecopies);
				}
			);
			var pcDef = $q.defer();
			packageCopyService.list({moviePackageId:scope.id,max:10000}).then(
				function(packagecopies) {
					pcDef.resolve(packagecopies);
				}
			);

			var promises = [ fDef.promise, fcDef.promise, pcDef.promise ];

			$q.all(promises).then(
				function(r) {
					scope.packageErrorMap = {};
					scope.fileMap = {};
					_.forEach(r[0],function(f) {
						scope.fileMap[f.id] = f;
					});
					scope.fileCopyMap = {};
					_.forEach(r[1],function(fc) {
						if(!fc.ok) {
							scope.packageErrorMap[fc.packageId] = ( scope.packageErrorMap[fc.packageId] || 0 ) +1;
						}						
						scope.fileCopyMap[fc.fileId] = scope.fileCopyMap[fc.fileId] || [];
						scope.fileCopyMap[fc.fileId].push(fc);
					});
					scope.packageCopyMap = {};
					_.forEach(r[2],function(pc) {
						scope.packageCopyMap[pc.id] = { packageCopy : pc };
						storageSystemService.get(pc.storageSystemId).then(
							function(storagesystem) {
								scope.packageCopyMap[pc.id].storageSystem=storagesystem;
							}
						);

					});
					scope.files = r[0];
				}

			);



		};
		out.reset = function(scope) {
		};

		return out;
	}
]);



cinefms.directive("movieVersionMediaclipList", [ '$q', 'MovieVersionService', 'MovieService', 'MediaclipService', 'TagService', 'LanguageService', 'ControllerService', 
	function($q,movieVersionService,movieService,mediaClipService,tagService,languageService,controllerService) {
		var a = {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-movieversion-mediacliplist.html',
			scope : {
				mediaClips: "="
			},
			controller : function($scope) {



			},
			link : function(scope, element, attrs) {

				console.log("movieVersionMediaclipList.linking() : "+scope.mediaClips);
				console.log(scope.mediaClips);


				scope.$watch('mediaClips', function(x) {

					if(angular.isUndefined(x)) {
						scope.mediaclips = [];
						return;
					}

					var promises = [];

					_.forEach(scope.mediaClips,
						function(mcid) {
							console.log(mcid);
							var d = $q.defer();
							mediaClipService.get(mcid).then(
								function(mc) {
									console.log("got media clip: ");
									console.log(mc);
									d.resolve(mc);
								}
							);
							promises.push(d.promise);
						}
					);

					$q.all(promises).then(
						function(clips) {
							console.log("got media clips: "+clips);
							scope.mediaclips = clips;
						}
					);

				}
				);



			}
		}
		console.log(a);
		return a;

	}
]);


cinefms.directive("mediaClipEditor", [ 'DirectiveService', 'MovieVersionService', 'MovieService', 'MediaclipService', 'MediaclipTypeService', 'MediaclipFramerateService', 'MediaclipMediaAspectService', 'MediaclipScreenAspectService', 'TagService', 'LanguageService', 'AudioFormatService', 'ControllerService', 
	function(directiveService,movieVersionService,movieService,mediaClipService,mediaclipTypeService,framerateService,mediaAspectService,screenAspectService,tagService,languageService,audioFormatService,controllerService) {
		var out = directiveService.editor("mediaclip", 'fms-mediaclip-edit.html' , mediaClipService);

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

			console.log("mediaClipEditor.init(): .... ");
			scope.audioFormatService = audioFormatService;
			scope.framerateService = framerateService;
			scope.mediaAspectService = mediaAspectService;
			scope.screenAspectService = screenAspectService;
			scope.mediaclipTypeService = mediaclipTypeService;
			scope.movieService = movieService;
			scope.movieVersionService = movieVersionService;
			scope.tagService = tagService;
			scope.languageService = languageService;

		};
		out.reset = function(scope) {
			console.log(" setting movieId .... "+scope.movieId);
			scope.mediaclip = {
				movieId : scope.movieId
			};
		};
		return out;
	}
]);

cinefms.directive("mediaClipVersionCreate", [ 'DirectiveService', 'MovieVersionService', 'MovieService', 'MediaclipService', 'MediaclipTypeService', 'MediaclipFramerateService', 'MediaclipMediaAspectService', 'MediaclipScreenAspectService', 'TagService', 'LanguageService', 'CommentService', 'ControllerService',
    function(directiveService,movieVersionService,movieService,mediaClipService,mediaclipTypeService,framerateService,mediaAspectService,screenAspectService,tagService,languageService,commentService,controllerService) {
        var out = directiveService.editor("mediaclip", 'fms-mediaclip-version-create.html' , mediaClipService);
        out.scope.movieId = "=";
        out.scope.movieName = "=";
        out.scope.movieExternalId = "=";
        out.scope.foldable = "=?";
        out.scope.expanded = "=?";

        out.init = function(scope) {
            console.log("mediaClipVersionCreate.init(): .... ");
            scope.comment = {};
            scope.createVersion = {};
            scope.createVersion.create = true;
            scope.version = {};
            scope.mediaclipTypeService = mediaclipTypeService;
            scope.movieService = movieService;
            scope.movieVersionService = movieVersionService;
            scope.tagService = tagService;
            scope.languageService = languageService;
            scope.$on("object_added",function(object_added, data){
                console.log("add called in directive!!!")
                console.log(data);
                if(scope.comment.comment != undefined )
                {
                    commentData = {
                        "objectId": data.id,
                        "objectType": "com.openfms.model.core.movie.FmsMediaClip",
                        "rootName": null,
                        "rootId": data.id,
                        "rootType": "com.openfms.model.core.movie.FmsMediaClip",
                        "text": "Mediaclip created: "+scope.comment.comment,
                        "name": "Comment created while adding mediaclip"
                    }
                    commentService.save(commentData);
                    console.log("add comment!!!")
                }
                if(scope.createVersion.create == true){
                    scope.version.name = data.name;
                    scope.version.movieId = scope.movieId;
                    scope.version.externalId = data.externalId;
                    scope.version.mediaClipIds = [];
                    scope.version.mediaClipIds.push(data.id);
                    scope.version.audioLanguageIds = scope.createVersion.audioLanguageIds;
                    scope.version.subtitleLanguageIds = scope.createVersion.subtitleLanguageIds;
                    scope.createVersion.create = false;
                    console.log(scope.version);
                    movieVersionService.save(scope.version);
                }
            });
        };

        out.reset = function(scope) {
            scope.mediaclip = {
                movieId : scope.movieId,
                externalId : scope.movieExternalId+'-',
                mediaAspect: 'UNKNOWN',
                screenAspect: 'UNKNOWN',
                audioFormat: 'UNKNOWN',
                fps: 'UNKNOWN'
            };
            scope.movie = {
                name: scope.movieName
            }
            console.log(" setting default data .... ");
            console.log(scope);
        };
        return out;
    }
]);

cinefms.directive("mediaClipTaskList", [ 'DirectiveService', 'MediaclipTaskService', 
	function(directiveService,taskService) {
		var a = {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-mediaclip-tasklist.html',
			scope : {
				mediaClipId: "=",
				order: "@?",
				asc: "@?"
			},
			controller : function($scope) {

				$scope.showClosed = false;

				$scope.toggleShowClosed = function() {
					$scope.showClosed = !$scope.showClosed;					
					$scope.refresh();
				}

				$scope.refresh = function() {
					if(angular.isUndefined($scope.mediaClipId)) {
						$scope.tasks = [];
					} else {
						taskService.list({mediaClipId:$scope.mediaClipId,closed:$scope.showClosed,order:$scope.order,asc:$scope.asc}).then(
							function(tasks) {
								$scope.tasks = tasks;
							}
						);

					}
				}
				$scope.$on("object_added",$scope.refresh);
			},
			link : function(scope, element, attrs) {
				console.log("mediaClipTaskList.link: "+scope.mediaClipId);
				scope.$watch('mediaClipId', scope.refresh);
			}
		}
		return a;
	}
]);

cinefms.directive("mediaClipTaskListSmall", [ 'DirectiveService', 'MediaclipTaskService', 
	function(directiveService,taskService) {
		var a = {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-mediaclip-tasklist-small.html',
			scope : {
				mediaClipId: "="
			},
			controller : function($scope) {

				$scope.refresh = function() {
					if(angular.isUndefined($scope.mediaClipId)) {
						$scope.tasks = [];
					} else {
						taskService.list({mediaClipId:$scope.mediaClipId,closed:$scope.showClosed}).then(
							function(tasks) {
								$scope.tasks = tasks;
							}
						);

					}
				}
			},
			link : function(scope, element, attrs) {
				console.log("mediaClipTaskListSmall linking ... ");
				console.log("mediaClipTaskList.link: "+scope.mediaClipId);
				scope.$watch('mediaClipId', scope.refresh);
			}
		}
		return a;
	}
]);



cinefms.directive("mediaClipScreeningList", [ 'DirectiveService', 'EventService', 
	function(directiveService,eventService) {
		var a = {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-mediaclip-tasklist.html',
			scope : {
				mediaClipId: "="
			},
			controller : function($scope) {
				$scope.refresh = function() {
					if(angular.isUndefined($scope.mediaClipId)) {
						$scope.tasks = [];
					} else {
						taskService.list({mediaClipId:$scope.mediaClipId}).then(
							function(tasks) {
								$scope.tasks = tasks;
							}
						);

					}
				}
			},
			link : function(scope, element, attrs) {
				console.log("mediaClipTaskList.link: "+scope.mediaClipId);
				scope.$watch('mediaClipId', scope.refresh);
			}
		}
		return a;
	}
]);

cinefms.directive("taskEditor", [ 'DirectiveService', 'MediaclipTaskService', 'MediaclipTaskTypeService', 'MediaclipService', 'UserService', 'CommentService','DeviceService',
	function(directiveService,taskService,mediaclipTaskTypeService,mediaclipService,userService, commentService, deviceService) {
		var out = directiveService.editor("task", 'fms-task-edit.html' , taskService);
		out.scope.mediaClipId = "=";
		out.init = function(scope) {


			console.log("taskEditor.init(): .... ");
			scope.mediaclipTaskTypeService = mediaclipTaskTypeService;
			scope.mediaclipService = mediaclipService;
			scope.userService = userService;
            scope.deviceService = deviceService;

            console.log(scope);

            scope.unsetAssignTo = function() {
				console.log("resetting assigned user .... "+scope.mediaclip.assignTo+" -> ");
				scope.progress.assignTo = undefined;
			}

            scope.unsetDeviceId = function() {
                console.log("resetting assigned device .... "+scope.progress.deviceId+" -> ");
                scope.progress.deviceId = undefined;
            }

			scope.assignToMe = function() {
				userService.getCurrentUser().then(
					function(user) {
						var progress = {
							"assignTo" : user.id,
                            "deviceId": scope.progress.deviceId,
                            "dueDate": scope.progress.dueDate
                        };
						taskService.saveProgress(scope.task.id,progress).then(
							function() {
								scope.refresh();
							}
						);
					}

				)
			}

			scope.expand = function() {
				scope.expanded = !scope.expanded;
			}

			scope.$watch("task.mediaClipId", function(x) {
				if(!angular.isUndefined(x)) {
					mediaclipService.get(x).then(function(mc){scope.mediaclip=mc});
                    console.log("loading taskSiblings");
                    taskService.list({max: 100, closed: false, mediaClipId: x}).then(
                        function (tasks) {
                            scope.taskSiblings = tasks;
                            console.log(tasks);
                        }
                    );
                }
			});
			scope.$watch("task.type", function(x) {
				if(!angular.isUndefined(x)) {
					mediaclipTaskTypeService.get(x).then(function(tt){
						scope.tasktype = tt;
						scope.resetProgress();
					});
				}
			});
			scope.refreshed = function() {
				mediaclipService.get(scope.task.mediaClipId).then(function(mc){scope.mediaclip=mc});
				taskService.getProgress(scope.task.id).then(function(tp){
					scope.progresses=tp;
					scope.resetProgress();
				});
			}

			scope.resetProgress = function() {
				scope.progress = {
					followUps : [],
					assignTo : scope.task.userId,
                    deviceId: scope.task.deviceId
				}
				_.forEach(scope.tasktype.next,function(tt) {
					var n = {
						type: tt.nextTaskType,
						create: false,
						priority: 0
					};
					scope.progress.followUps.push(n);
				});
			}

			scope.$watch("task.id", scope.refreshProgress);

			scope.closeAndSendComment = function() {
				scope.progress.close = true;
				scope.sendComment();
			}

			scope.reopenAndSendComment = function() {
				scope.progress.close = false;
				scope.sendComment();
			}

			scope.sendComment = function() {
				if(!angular.isUndefined(scope.progress.status) && scope.progress.status=="") {
					scope.progress.status = undefined;
				}
				if(!angular.isUndefined(scope.progress.disable) && scope.progress.disable=="") {
					scope.progress.disable = undefined;
				}
				taskService.saveProgress(scope.task.id,scope.progress).then(
					function(a){
						scope.refresh();
						// scope.refreshProgress();
						scope.resetProgress();
				});
                console.log (scope);

                angular.forEach(scope.taskSiblings, function (taskSibling) {
                   if(taskSibling.update == true){
                       console.log("add message");
                       delete taskSibling.update;
                       console.log(taskSibling);
                       var siblingProgress = {
                           "assignTo" : taskSibling.userId,
                           "deviceId": taskSibling.deviceId,
                           "dueDate": taskSibling.dueDate,
                           "comment": taskSibling.comment,
                           "priority": taskSibling.priority
                       };
                       taskService.saveProgress(taskSibling.id,siblingProgress).then(
                           function(a){
                               scope.refresh();
                               scope.resetProgress();
                           });
                   }

                });

                if(scope.progress.addcomment == true)
                {
                    commentData = {
                        "objectId": scope.mediaclip.id,
                        "objectType": "com.openfms.model.core.movie.FmsMediaClip",
                        "rootName": null,
                        "rootId": scope.mediaclip.id,
                        "rootType": "com.openfms.model.core.movie.FmsMediaClip",
                        "text": "Task comment: "+scope.progress.comment,
                        "name": "CPL Comment from Task"
                    }
                    commentService.save(commentData);
                    console.log("add comment!!!")
                    scope.progress.addcomment = undefined;
                }
			}
	

		};
		return out;
	}
]);

cinefms.directive("taskCreate", [ 'DirectiveService', 'MediaclipTaskService', 'MediaclipTaskTypeService', 'MediaclipService',
	function(directiveService,taskService,mediaclipTaskTypeService,mediaclipService) {
		var out = directiveService.editor("task", 'fms-task-create.html' , taskService);
		out.scope.mediaClipId = "=";
		out.init = function(scope) {
			scope.task = {
				mediaClipId : scope.mediaClipId
			}
			scope.mediaclipTaskTypeService = mediaclipTaskTypeService;
			scope.mediaclipService = mediaclipService;
		};
		out.reset = function(scope) {
			console.log(" setting mediaClipId .... ");
			scope.task = {
				mediaClipId : scope.mediaClipId
			};
			console.log(scope.movieversion);
		};
		return out;
	}
]);


cinefms.directive("keyList", [ '$q', 'KeyService',
	function($q,keyService) {
		var a = {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-keylist-small.html',
			scope : {
				movieId: "=?",
				mediaClipExternalId: "=?"
			},
			controller : function($scope) {
			},
			link : function(scope, element, attrs) {
				console.log("KEYLIST ----------------------------------------------------------------------------");
				console.log(scope.params);
				console.log(_.keys(scope.params));
				keyService.list(
					{ 
						movieId : scope.movieId,
						mediaClipExternalId : scope.mediaClipExternalId,
						max : 1000
					}
					).then(
					function(keys) {
						console.log("KEYLIST: ");
						console.log(keys);
						scope.keys = keys;
					}
				);
			}
		}
		return a;
	}
]);

cinefms.directive("keyRequestList", [ '$q', 'KeyRequestService', 'MovieService', 'MediaclipService', 'LocationService', 'DeviceService', 
	function($q,keyRequestService,movieService,mediaclipService,locationService,deviceService) {
		var a = {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-keyrequest-list.html',
			scope : {
				movieId: "=?",
				mediaClipId: "=?"
			},
			controller : function($scope) {
			},
			link : function(scope, element, attrs) {

				scope.movieService = movieService;
				scope.mediaclipService = mediaclipService;
				scope.locationService = locationService;
				scope.deviceService = deviceService;

				scope.resetRequest = function() {
					scope.newRequest = {
					};
					if(!angular.isUndefined(scope.movieId)) {
						scope.newRequest.movieId = scope.movieId;					
					}
					if(!angular.isUndefined(scope.mediaClipId)) {
						scope.newRequest.mediaClipId = scope.mediaClipId;					
					}
				}

				scope.resetRequest();

				scope.mediaclipParams = { movieId : scope.newRequest.movieId };

				scope.showAddDialog = false;

				scope.add = function() {
					scope.showAddDialog = !scope.showAddDialog;
				}


				scope.updateMC = function() {
					scope.newRequest.mediaClipId = undefined;
				}

				scope.send = function() {
					keyRequestService.save(scope.newRequest).then(
						function() {
							scope.refresh();
							scope.resetRequest();
						}
					);
				}


				console.log("KEYREQUESTLIST ----------------------------------------------------------------------------");
				console.log(scope.params);
				console.log(_.keys(scope.params));

				scope.refresh = function() {
					keyRequestService.list(
						{ 
							movieId : scope.movieId,
							mediaClipId : scope.mediaClipId

						}
						).then(
						function(keyrequests) {
							console.log("KEYLIST: ");
							console.log(keyrequests);
							scope.keyrequests = keyrequests;
						}
					);
				}
				scope.refresh();
			}
		}
		return a;
	}
]);
