
cinefms.directive('fmsPagination', [ 
	function() {
		return {
			require: 'ngModel',
			replace: true,
			transclude: true,
			templateUrl: 'app/partial/fms-pagination.html',
			scope : {
				params: "=ngModel"
			},
			link: function(scope, element, attrs, ngModel) {

				scope.params.max = scope.params.max || 25;
				scope.params.max = parseInt(scope.params.max);

				scope.previous = function() {
					scope.params.start = parseInt(scope.params.start) - parseInt(scope.params.max);
					if(scope.params.start<0) {
						scope.params.start = 0;
					}
                    scope.$emit('listParamsChanged');
                    scope.$broadcast('listParamsChanged');
				}

				scope.next = function() {
					scope.params.start = parseInt(scope.params.start) + parseInt(scope.params.max);
                    scope.$emit('listParamsChanged');
                    scope.$broadcast('listParamsChanged');
				}

			}
		}
	}
]);

cinefms.directive('fmsFilter', [ '$timeout',
	function($timeout) {
		return {
			require: 'ngModel',
			replace: true,
			transclude: true,
			templateUrl: 'app/partial/fms-filterbox.html',
			scope : {
				model: "=ngModel"
			},
			link: function(scope, element, attrs, ngModel) {

				scope.selectedFilters = [];

				var allGroups = element.find("[data-filter-group]");
				var allInputs = element.find("input");

				var updateAll = function() {
					scope.selectedFilters = [];
					allGroups.each(
						function() {
							var arr = [];
							$(this).find('input').each(
								function() {
									var me = $(this);
									if(me.prop('checked') && me.attr('data-filter-display')) {
										arr.push(me.attr('data-filter-display') || me.val());
									}
								}
							);
							if(arr.length>0) {
								scope.selectedFilters.push($(this).attr('data-filter-display')+": "+arr.join(", "));
							}
						}
					);
                    scope.$emit('listParamsChanged');
                    scope.$broadcast('listParamsChanged');
				};

				var init = function() {
					allGroups.each(
						function() {
							var ge = $(this);
							var group = ge.attr("data-filter-group");

							var clear = ge.find("[data-filter-clear]");
							var inputs = ge.find("input");

							var update = function() {
								var x = ngModel.$viewValue || {};
								var arr = x[group] || [];
								if(!Array.isArray(arr)) {
									arr = [arr];
								}
								arr.splice(0,arr.length);
								inputs.each(function() {
									var me = $(this);
									if(me.val()=="") {
									} else if (me.val()=="---none---") {
									} else if (me.val()=="on") {
									} else if (me.prop('checked')) {
										arr.push(me.val());
									}
								});
								clear.prop('checked', arr.length==0);
								x[group] = arr;
								ngModel.$setViewValue(x);
				                scope.$apply();
				                updateAll();
							};

							clear.each(function() {
								$(this).on('change',
									function(e) {
										inputs.prop('checked', false);
									}
								);
							});

							inputs.each(function() {
								var me = $(this);
								me.prop('checked', false);
								console.log(me.attr("id")+" .... added listener?");
								me.on('change',update);
							});

							var init = function(x) {
								x = x || [];
								if(!Array.isArray(x)) {
									x = [x];
								}
								inputs.each(function() {
									var me = $(this);
									if(me.val()!="" && x.indexOf(me.val())>-1) {
										me.prop('checked',true);
									}
								});
								$timeout(update);
							}

							init(ngModel.$viewValue[group]);

						}
					);

				}

				$timeout(init);
				

			}
		}
	}
]);


cinefms.directive('fmsTime', [ 'ProjectSettingsService', '$q',
	function(projectSettingsService, $q) {
		return {
			require: 'ngModel',
			template: '<span ng-transclude></span>',
			transclude: true,
			scope : {
				model: "=ngModel"
			},
			link: function(scope, element, attrs, ngModel, transclude) {

				scope.xx = "hello";

				scope.date = {
					date : "...",
					time : "...",
					since : "...",
				};

				scope.update = function(x) {
					var m =moment(x).tz(scope.dc.timezone);
					scope.date = {
						date :  m.format(scope.dc.timeFormat),
						time :  m.format(scope.dc.dateFormat),
						since : m.fromNow()
					};

					scope.date.time = m.format(scope.dc.timeFormat);
					scope.date.date = m.format(scope.dc.dateFormat);
					scope.date.since = m.fromNow();
				};


				projectSettingsService.getDateConfig().then(
					function(dateConfig) { 
						console.log("date config:"+angular.toJson(dateConfig));
						scope.dc = dateConfig;
						scope.$watch("model", scope.update);
						scope.update(scope.model);
					}
				);

				transclude(scope, function(clone, scope) {
					element.append(clone);
				});
			}
		};
	}
]);

cinefms.directive('fmsTimeEdit', [ 'ProjectSettingsService', '$q',
	function(projectSettingsService, $q) {
		return {
			templateUrl: 'app/partial/fms-timepicker.html',
			transclude : true,
			scope : {
				model: "=ngModel",
				ngDisabled: "=ngDisabled",
				change : "&change"
			},
			link: function(scope, element, attrs) {

				var optionsDate =  {
					pickDate: true,
    				pickTime: false,
				    format: 'YYYY-MM-DD'
				};

				var optionsTime =  {
					pickDate: false,
    				pickTime: true,
				    useMinutes: true,
				    useSeconds: false,
				    format: 'HH:mm',
				    pick12HourFormat: false,
				    minuteStepping:5
				};

				scope.disabled = false;

				scope.time = {};
				scope.date = {};

				projectSettingsService.getDateConfig().then(
					function(dc) {
						scope.dc = dc;
					
						$(element).find(".date").datetimepicker(optionsDate);
						$(element).find(".time").datetimepicker(optionsTime);

						scope.updateView();

						scope.$watch("time", function(x,y) {
							console.log("time changed: "+y+" --- "+x);
							scope.updateModel();
						});
						scope.$watch("date", function(x,y) {
							console.log("date changed: "+y+" --- "+x);
							scope.updateModel();
						});
						scope.$watch("model", function(x,y) {
							console.log("model changed: "+y+" --- "+x);
							scope.updateView(x,y);
						});
						scope.$watch("ngDisabled", function(x,y) {
							scope.disabled = x;
						});

						scope.disabled = scope.ngDisabled;
					}

				);


				scope.updateView = function(x,y) {

					scope.time = moment.tz(scope.model,scope.dc.timezone).format("HH:mm");
					$(element).find(".time").data("DateTimePicker").setDate(scope.time);
					scope.date = moment.tz(scope.model,scope.dc.timezone).format("YYYY-MM-DD");
					$(element).find(".date").data("DateTimePicker").setDate(scope.date);

				};

				scope.updateModel = function() {
					var format = optionsDate.format+" "+optionsTime.format;
					var combined = scope.date+" "+scope.time;
					var out = moment.tz(combined,format,scope.dc.timezone);
					out = Number(out+"");
					if(scope.model != out) {
						scope.model = out;
						//scope.$apply();
						scope.change();
					}
				};

			}
		};
	}
]);

cinefms.directive("navActive", [ '$location', 
	function($location) {
		function link(scope, element, attrs) {
			scope.$on('$locationChangeSuccess', function(event) {
				if($location.path().indexOf(attrs.navActive)==0) {
					element.addClass('active');
				} else {
					element.removeClass('active');
				}
			});
		}
		return {
      		link: link
    	};		
	} 
]);

cinefms.directive("fmsHeadline", [  function() {
	return {
		transclude : true,
		templateUrl : 'app/partial/fms-headline.html'
	}
}]);

cinefms.directive("fmsStatus", [  function() {
	return {
		transclude : true,
		templateUrl : 'app/partial/fms-status.html',
		scope : {
			status : "="
		},
  		link: function(scope, el, attrs) {
  		}
	}
}]);


cinefms.directive("fmsLink", [ '$location', '$route', 
	function($location,$route) {
		return {
			restrict : 'E',
			replace : false,
			transclude : true,
			template : '<a href="#{{href}}" ng-transclude></a>',
			scope : {
				object : "=?",
				type : "=?",
				id : "=?"
			},
    		link: function(scope, el, attrs) {
				if(!angular.isUndefined(scope.object)) {
					scope.id = scope.id || scope.object.id;
					scope.type = scope.type || scope.object.objectTypeName;
				}
				scope.update = function() {
					_.forOwn($route.routes, function(route) {
						//console.log(route.linkObject+" // "+scope.id+" // "+scope.type);
						if(route.linkObject && route.linkObject == scope.type) {
							scope.href = route.originalPath.replace(/:id/,scope.id);
						}
					});
				}
				scope.$watch("id",scope.update);
				scope.$watch("object",scope.update);
			}
		} 
	} 
]);

cinefms.directive('videoUrl', function () {
    return {
        restrict: 'A',
        link: function postLink(scope, element, attr) {
            element.attr('src', attr.videoUrlSrc);
        }
    };
});

cinefms.directive("fmsDialog", [
	function() {
		return {
			restrict : 'E',
			scope: true,
			controller : function($scope) {

				$scope.showDialog = false;

				this.close = function() {
					console.log("closing dialog ... "+$scope.showDialog);
					$scope.showDialog=false;
				};

				this.open = function() {
					console.log("opening dialog ... "+$scope.showDialog);
					$scope.showDialog=true;
				};

				$scope.open = this.open;
				$scope.close = this.close;

			},
			link : function(scope, element, attrs) {

				//$(element).html("bound to this");

				scope.params = {
					showDialog : false
				};

				$(element).find(".open-dialog").on("click",
					function() {
						scope.showDialog = true;
						scope.$apply();
						console.log("open triggered ... "+scope.showDialog);
						scope.open();
					}
				);
				scope.$on("close_dialog",
					function() {
						scope.showDialog = false;
					}
				);
			}
		}
	}
]);

cinefms.directive('jsonEditor', function($parse) {
	return {
		restrict : 'A',
		require: 'ngModel',
		replace : false,
		transclude : false,
		link: function(scope, el, attrs, ngModel) {

			console.log("json editor .... ");



			ngModel.$formatters.push(function(modelValue) {
				if(angular.isUndefined(modelValue)) {
					return {};
				}
				return modelValue;
			})

			ngModel.$render = function() {
			    editor.set(ngModel.$viewValue);
			};


			var options = { 
				mode : "code", 
				modes: ['code', 'form', 'text', 'tree', 'view'], 
				search: true
			};

			var editor = new JSONEditor(el[0], options, {});

			var processChange = function () {
				var json = editor.get();
				//ngModel.$setViewValue(json);
              	scope.$apply(function (scope) {
                	ngModel.$setViewValue(json);
               	});

				console.log("----- new view value: ");
				console.log(ngModel.$viewValue);
				ngModel.$render();
            };

            el.on('focusout',processChange);

		}
	};
});


cinefms.directive('singleSelect', ['$q', '$timeout', 
    function($q,$timeout) {
        return {
            require: 'ngModel',
			scope : {
				service : "=",
				change : "&onChange",
				model: "=ngModel",
				params: "@?",
				mparams: "=?"
			},
			link: function(scope, element, attrs, ngModel) {

				var options = 
            	{
                    width: '100%',
                    initSelection : function(elem,callback) {
                    	//noop
                    },
                    query: function(q) {
                    	def = $q.defer();
                    	var params;

                    	if(!angular.isUndefined(scope.mparams)) {
                    		params = angular.fromJson(angular.toJson(scope.mparams));
                    	}

						if(angular.isUndefined(scope.params)) {
							params = {};
						} else {
							params = angular.fromJson(scope.params);
						}
                    	params['searchTerm'] = q.term;
						console.log(" ----- "+angular.toJson(params));
                    	scope.service.list(params).then(
                    		function(rs) {
                        		var out = [];
                    			_.each(rs, function(r) {
                    				out.push({ id: r.id, text : r.displayName ? r.displayName : r.name , disabled: r.disabled ? r.disabled: false });
                    			});
                    			def.resolve(out);
                    		},
                    		function() {
                    			def.reject("an error occured");
                    		}
                    	);
                    	def.promise.then(
                    		function(d) {
		                    	q.callback({ results : d });
	                		}
                    	);
                    }
                };

               	$(element).select2(options);
               	$(element).on(
               		'change',
               		function() {
               			console.log("SELECT2 (single): CHANGE ---- ");
               			console.log(scope.change);
	               		scope.change();
               		}
               	);

               	scope.$watch("model",function(x,y) {
           			console.log("value changed! "+y+" to "+x);
           			if(angular.isUndefined(x) || x == "") {
		            	$(element).select2("data",{id:"0",text:" --- please select ... --- "});
		            } else {
	                	scope.service.get(x).then(
	                		function(v) { 
			               		$(element).select2("data",{id:v.id,text : v.displayName ? v.displayName : v.name , disabled: v.disabled ? v.disabled: false });
	                		},
	                		function() { 
	                		}
	                	);
		            }
               	});
            }
        };
    }
]);

cinefms.directive('multiSelect', ['$q', '$timeout',
    function($q,$timeout) {
        return {
			scope : {
				service : "=",
				change : "&change",
				model: "=",
				params: "@?"
			},
			link: function(scope, element, attrs) {

              	console.log("select2 starting!");
              	console.log(scope);


				scope.params = scope.params || {};

				scope.current = "";

				if(angular.isUndefined(scope.model)) {
					scope.model = [];
				}
				if(!Array.isArray(scope.model)) {
					scope.model = [];
				}

                scope.query = function(q) {
                	def = $q.defer();
                	var params;
					if(angular.isUndefined(scope.params)) {
						params = {};
					} else {
						params = angular.fromJson(scope.params);
					}
                	params['searchTerm'] = q.term;
                	scope.service.list(params).then(
                		function(rs) {
                    		var out = [];
                			_.each(rs, function(r) {
                				if(angular.isUndefined(r.id)) {
                    				out.push({ id: r, text: r});
                				} else {
                    				out.push({ id: r.id, text : r.displayName ? r.displayName : r.name, disabled: r.disabled ? r.disabled : false});
                				}
                			});
                			def.resolve(out);
                		},
                		function() {
                			def.reject("an error occured");
                		}
                	);
                	def.promise.then(
                		function(d) {
	                    	q.callback({ results : d });
                		}
                	);

                }

                scope.lookup = function(value,callback) {

                	console.log("select2 lookup: "+value);

                	if((value+"")=="null") {
	                	console.log("select2 setting value to empty ... ");
                		value = "";
                	}

                	if(angular.isUndefined(value) || value == "") {
	                	console.log("select2 lookup: returning empty array");
                		callback([]);
                	} else {
	                	console.log("select2 lookup: splitting ... "+value);
						var promises = []; 
						var v= [];
						if(Array.isArray(value)) {
		                	console.log("select2 lookup: input is an array, pushing to: "+v+" / "+value);
							v = value;
						} else {
		                	console.log("select2 lookup: input is an NOT array, splitting, then pushing to: "+v+" / "+value);
							_.foreach((value+"").split(","), v.push);
						}
						_.forEach((v), function(id) {
							if(!angular.isUndefined(id) && id!="") {
								var def = $q.defer();
		                    	scope.service.get(id).then(
		                    		function(v) { 
		                    			var o = {};
		                    			if(angular.isUndefined(v.id)) {
		                    				o = {id:id,text:id};
		                    			} else {
		                    				o = {id:v.id,text:v.displayName?v.displayName:v.name,disabled:v.disables?v.disabled:false,valid:true};	                    				
		                    			}
		                    			def.resolve(o);
		                    		},
		                    		function(v) { 
		                    			var o = {id:id,text:"[deleted:"+id+"]",disabled:v.disables?v.disabled:false,valid:false};
		                    			def.resolve(o);
		                    		}
		                    	)
								promises.push(def.promise);
							}

						});
						$q.all(promises).then(
							function(objects) {
								var ok = true;
								var out = [];
								_.forEach(objects,function(o) {
									if(o.valid) {
										out.push(o);
									} else {
										ok = false;
									}
								});
								callback(out);
							}
						);

					}
                }

				var options = 
            	{
                    width: '100%',
                    multiple: true,
                    maximumSelectionSize: 30,
                    initSelection : scope.lookup,
                    query: scope.query
                };

                var s2 = $(element).select2(options);

                $(s2).on("change",function() {
                	console.log("select2 changed! ");
                	var n = $(s2).select2("val");
                	console.log(n);
                	scope.model = n;
                	scope.$apply();
                	if(scope.change) {
                		scope.change();
                	}
                });

                scope.$watch("model", function(x,y) {
                	if((x+"")==scope.current) {
	                	console.log("select2 value NOT changed: "+y+" --- "+x);
                		return;
                	}
                	scope.current = (x+"");
                	console.log("select2 value changed: "+y+" --- "+x);
                	scope.lookup(
                		x,
                		function(objects) { 
		                	console.log("select2 setting objects .... ");
		                	console.log(objects	);
                			$(s2).select2("data",objects);
                		}
                	);
               	});


            }
        };
    }
]);



cinefms.directive('notificationDisplaySmall', ['$q', '$timeout', 'NotificationService',
    function($q,$timeout,notificationService) {
        return {
			replace: true,
			transclude: false,
			templateUrl: 'app/partial/fms-notifications-small.html',
			scope : true,
			controller: function($scope){
				$scope.toggleDetails = function() {
					$scope.showDetails = !$scope.showDetails;
				}
				$scope.markRead = function(id) {
					console.log("mark as read: "+id);
					notificationService.get(id).then(
						$scope.refresh
					);
				}
			},
			link: function(scope, element, attrs) {
				scope.showDetails = false;
				scope.refresh = function() {
					notificationService.list().then(
						function(notifications) {
							notifications.forEach(function (element, index, array) {
								array[index].message=element.message.replace(/[^:]*:/g,"");
							});
							scope.notifications = notifications;
						}, 
						function() {
							// failure
						}
					);
					$timeout(scope.refresh,20000);
				}

				scope.refresh();



            }
        };
    }
]);



cinefms.directive('jobLog', [ 
	function() {
		return {
			require: 'ngModel',
			replace: true,
			transclude: true,
			templateUrl: 'app/partial/fms-job-log.html',
			scope : {
				log: "=ngModel"
			},
			controller: function($scope){
			},
			link: function(scope, element, attrs, ngModel, transclude) {
				transclude(scope, function(clone, scope) {
  					element.append(clone);
  				});
  			}
		}
	}
]);




