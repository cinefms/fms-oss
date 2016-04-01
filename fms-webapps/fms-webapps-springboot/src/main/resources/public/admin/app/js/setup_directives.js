cinefms.directive("deviceEditor", [ 'DirectiveService', 'DeviceService', 'TagService', 'LocationService', 'TagService', 'DeviceProtocolService', 'CertificateService', 'MediaclipTypeService',
	function(directiveService,deviceService, tagService, locationService, tagService, deviceProtocolService, certificateService,mediaclipTypeService) {
		var out = directiveService.editor("device", 'fms-device-edit.html' , deviceService);
		out.init = function(scope) {
			scope.certificateService = certificateService;
			scope.deviceProtocolService = deviceProtocolService;
			scope.tagService = tagService;
			scope.locationService = locationService;
			scope.tagService = tagService;
			scope.mediaclipTypeService = mediaclipTypeService;
			scope.unsetCertificate = function() {
				scope.device.certificateId = undefined;
				scope.save();
			}
		};
		return out;
	}
]);

cinefms.directive("locationEditor", [ 'DirectiveService', 'LocationService', 'SiteService',  'TagService',
	function(directiveService, locationService, siteService, tagService) {
		var out = directiveService.editor("location", 'fms-location-edit.html' , locationService);
		out.init = function(scope) {
			scope.siteService = siteService;
			scope.tagService = tagService;
            scope.addContact = function() {
                if(angular.isUndefined(scope.location.data) || scope.location.data == null){
                    scope.location.data = {};
                }
                if(angular.isUndefined(scope.location.data.contacts) || scope.location.data.contacts == null){
                    scope.location.data.contacts = [];
                }
                data = {name:scope.contact.name,email:scope.contact.email,phone:scope.contact.phone};
                scope.location.data.contacts.push(data);
                scope.contact.name = "";
                scope.contact.email = "";
                scope.contact.phone = "";
                scope.save();
            };
            scope.removeContact = function (index) {
                scope.location.data.contacts.splice(index, 1);
                scope.save();
            }
		};
		return out;
	}
]);

cinefms.directive("siteEditor", [ 'DirectiveService', 'SiteService', 
	function(directiveService, siteService) {
		var out = directiveService.editor("site", 'fms-site-edit.html' , siteService);
        out.init = function(scope) {
            scope.addContact = function () {
                if(angular.isUndefined(scope.site.data) || scope.site.data == null){
                    scope.site.data = {};
                }
                if(angular.isUndefined(scope.site.data.contacts) || scope.site.data.contacts == null){
                    scope.site.data.contacts = [];
                }
                data = {name:scope.contact.name,email:scope.contact.email,phone:scope.contact.phone};
                scope.site.data.contacts.push(data);
                console.log(scope.site.data);
                scope.contact.name = "";
                scope.contact.email = "";
                scope.contact.phone = "";
                scope.save();
            };
            scope.removeContact = function (index) {
                scope.site.data.contacts.splice(index, 1);
                scope.save();
            }
        }
		return out;
	}
]);

cinefms.directive("mailEditor", [ 'DirectiveService', 'MailTemplateService', 
	function(directiveService, mailTemplateService) {
		console.log("mail template editor ....... ");
		var out = directiveService.editor("mailtemplate", 'fms-mailtemplate-edit.html' , mailTemplateService);
		out.init = function(scope) {

			scope.recipient = "me@example.com";

			scope.sendTest = function() {
				var id = scope.mailtemplate.id;
				var tokens = scope.mailtemplate.examples;
				var recipient = scope.recipient;

				mailTemplateService.sendTest(id,tokens,recipient);
			}
		}

		return out;
	}
]);

cinefms.directive("scheduledJobEditor", [ 'DirectiveService', 'ScheduledJobService', 'UserService', 'ScheduledJobTypeService', 
	function(directiveService, scheduledJobService, userService, scheduledJobTypeService) {
		console.log("scheduled job editor instantiating");
		var out = directiveService.editor("scheduledjob", 'fms-scheduledjob-edit.html' , scheduledJobService);
		out.init = function(scope) {
			scope.userService = userService;
			scope.scheduledJobTypeService = scheduledJobTypeService;
		}
		return out;
	}
]);



cinefms.directive("certificateEditor", [ 'DirectiveService', 'CertificateService', 
	function(directiveService, certificateService) {
		console.log("certificate editor instantiating");
		var out = directiveService.editor("certificate", 'fms-certificate-edit.html' , certificateService);
		out.init = function(scope) {
			scope.certificateService = certificateService;
		}
		return out;
	}
]);


cinefms.directive("taskTypeEditor", [ 'DirectiveService', 'MediaclipTaskTypeService', 'MediaclipTypeService', 
	function(directiveService, taskTypeService, mediaclipTypeService) {
		var out = directiveService.editor("tasktype", 'fms-tasktype-edit.html' , taskTypeService);

		out.init = function(scope) {
			scope.refreshOnUpdate = false;
			scope.mediaclipTypeService = mediaclipTypeService;

			scope.hasTaskType = function(a) {
				var out = false;
				if(angular.isUndefined(scope.tasktype)) {
					return;
				}
				_.forEach(scope.tasktype.next,function(x) {
					if(x.nextTaskType==a) {
						out = true;
					}
				});
				return out;
			}
 
			taskTypeService.list().then(
				function(types) {
					scope.types = [];
					_.forEach(types,function(a) {
						if(!scope.hasTaskType(a)) {
							scope.types.push(a);
						}
					})
				}
			);
			scope.ntt = {
				tt : undefined
			};
			scope.removeFollowUp = function(a) {
				_.forEach(scope.tasktype.next,function(n) {
					if(n.nextTaskType == a) {
						scope.tasktype.next.splice(scope.tasktype.next.indexOf(n),1);
						scope.save();
						return;
					}
				});
			}
			scope.addFollowUp = function() {
				if(scope.hasTaskType(scope.ntt.tt)) {
					return;
				}
				var x = {
					nextTaskType : scope.ntt.tt
				};

				scope.tasktype.next.push(x);
				scope.save();	
			}
		};
		out.reset = function(scope) {
		};

		return out;
	}
]);



cinefms.directive("userEditor", [ 'DirectiveService', 'UserService', 'GroupService', 'MovieService','SweetAlert',
	function(directiveService,userService,groupService,movieService,SweetAlert) {
		var out = directiveService.editor("user", 'fms-user-edit.html' , userService);
		out.init = function(scope) {

			console.log("userEditor.init(): .... ");

 	       	scope.set = {newPasswordA:"",newPasswordB:"",newPasswordOK:false};
			scope.groupService = groupService;
			scope.movieService = movieService;

	        scope.reset = function(username,oldPW,newPW) {
	            console.log("reset password ... "+username+" / "+oldPW+" / "+newPW);
	            userService.reset(username,oldPW,newPW);
                SweetAlert.swal("New password sent.", "", "success");
            }

	        scope.checkPassword = function() {
	        	console.log(" --- check password: "+scope.set.newPasswordA);
	            scope.set.newPasswordOK = scope.set.newPasswordA.length > 5 && scope.set.newPasswordA == scope.set.newPasswordB;
	        }

	        scope.$watch("set.newPasswordA", scope.checkPassword);
	        scope.$watch("set.newPasswordB", scope.checkPassword);

		};
		return out;
	}
]);


cinefms.directive("groupEditor", [ '$q', 'DirectiveService', 'GroupService', 'AccessTypeService', 'EntityGroupService', 
	function($q,directiveService, groupService, accessTypeService, entityGroupService) {
		var me = {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-group-edit.html',
			scope : {
				id: "=?"
			},
			reset : function(scope) {
			},
			refreshed : function(scope) {
			},
			init : function(scope) {
			},
			controller : function($scope) {

	            $scope.edit = function() {
	           		$scope.editing = !$scope.editing;
	            }

	            $scope.remove = function() {
	            	groupService.remove($scope.group.id).then(
	            		function() {
		                	$scope.$emit("object_removed",$scope.group.id);
		                	$scope.$broadcast("object_removed",$scope.group.id);
		                	$scope.group = undefined;
	            		}
	            	);
	            }

	            $scope.save = 
	                function() {
	                    var v = $q.defer();
	                    groupService.save($scope.group).then(
	                        function(o) {
	                            $scope.group = o;
	                            v.resolve(o);
	                            $scope.$emit('object_updated',o.id);
	                            $scope.$broadcast('object_updated',o.id);
	                        },
	                        function(){ 
	                            $scope.refresh();
	                        	v.reject();
	                        }
	                    );
	                    return v.promise;
	                };

	            $scope.update = 
	            	function() {
	            		var egs = _.keys($scope.acls);
	            		var acls = [];
						_.forEach(egs,function(eg) {
							var acl = { group: eg, access : [] };
		            		var ats = _.keys($scope.acls[eg]);
							_.forEach(ats,function(at) {
								if($scope.acls[eg][at]) {
									acl.access.push(at);
								}
							});
							acls.push(acl);
						});
						if(!angular.isUndefined($scope.group)) {
							$scope.group.acls = acls;
							$scope.save();
						};
	            	};

	            $scope.refresh = 
	                function() {
	                	groupService.get($scope.id).then(
	                		function(group) {
								entityGroupService.list().then(
									function(entityGroups) {
										console.log("entityGroups: "+angular.toJson(entityGroups));
										$scope.entityGroups = entityGroups;
										accessTypeService.list().then(
											function(accessTypes) {
												console.log("accessTypes: "+angular.toJson(accessTypes));
												$scope.accessTypes = accessTypes;
												$scope.acls = {};
												_.forEach(entityGroups,function(eg) {
													$scope.acls[eg] = {};
													_.forEach(accessTypes,function(at) {
														$scope.acls[eg][at] = false;
													});
												});
												_.forEach(group.acls,function(acl) {
													_.forEach(acl.access,function(a) {
														$scope.acls[acl.group][a] = true;
													});
												});
												$scope.group = group;
											}
										);
									}
								);
	                		}
	                	);
	                };

	            $scope.open = function() {
	            	me.open($scope);
	            }
				$scope.$watch("id",$scope.refresh);
				$scope.$watch("acls",$scope.update,true);
			},
			link : function(scope, element, attrs) {

			}
		}
		return me;

	}
]);

