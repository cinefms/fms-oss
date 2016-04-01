
cinefms.controller('DeviceListController', ['$scope', '$location', 'ControllerService', 'DeviceService', 

    function($scope, $location, controllerService, deviceService) {
        controllerService.listController('devices',$scope,$location,deviceService);
        $scope.start();
    }
]);

cinefms.controller("DeviceController", ['$scope', '$location', '$routeParams', 'ControllerService', 'DeviceService', 'DeviceStatusService', 
    function($scope, $location, $routeParams, controllerService, deviceService, deviceStatusService) {
        controllerService.singleController('device',$routeParams['id'],$scope,$location,deviceService);

        $scope.params = $scope.params || { tab : 'overview' };
        $scope.params.tab = $scope.params.tab || 'overview';

        $scope.start();

    }
]);

cinefms.controller("DeviceSingleController", ['$scope', '$location', '$routeParams', 'ControllerService', 'DeviceService',
    function($scope, $location, $routeParams, controllerService, deviceService) {
        $scope.init = function(deviceId){
            deviceService.get(deviceId).then(
                function(device) {
                    $scope.device = device;
                }
            );
        }
    }
]);

cinefms.controller('DeviceStatusController', ['$q','$scope', '$location',  '$routeParams','ControllerService', 'DeviceStatusService', 'ProjectSettingsService',
    function($q, $scope, $location, $routeParams, controllerService, deviceStatusService, projectSettingsService) {
        console.log("instantiating job log controller ... ");

        var bp = $q.defer();
        deviceStatusService.list($routeParams['id']).then(
            function(statusBriefs) {
                console.log("logs resolved: ... ");
                console.log(statusBriefs);
                bp.resolve(statusBriefs);
            }
        );
        var dcp = $q.defer();
        projectSettingsService.getDateConfig().then(
            function(dateConfig) {
                console.log("dateConfig resolved: ... ");
                console.log(dateConfig);
                dcp.resolve(dateConfig);
            }
        );

        var pArr = [];
        pArr.push(bp.promise);
        pArr.push(dcp.promise);

        $q.all(pArr).then(function(r){
            _.forEach(r[0],function(b) {
                m = moment(b.date).tz(r[1].timezone);
                b.date = m.format(r[1].dateFormat) +" - "+ m.format(r[1].timeFormat);
            });
            if(r[0].length>0) {
                $scope.current = {
                    statusId : r[0][0].id
                }
            };
            $scope.briefs = r[0];
        });


        $scope.$watch("current.statusId", function(x) {
            if(angular.isUndefined(x)) {
                return;
            }
            deviceStatusService.get($routeParams['id'],x).then(
                function(status) {
                    $scope.status=status;
                }
            );
        });
    }
]);



cinefms.controller('LocationListController', ['$scope', '$location', 'ControllerService', 'LocationService',
    function($scope, $location, controllerService, locationService) {
        controllerService.listController('locations',$scope,$location,locationService);
        $scope.start();
    }
]);

cinefms.controller('LocationDeviceListController', ['$scope', '$location', 'ControllerService', 'LocationService','DeviceService','DeviceStatusService',
    function($scope, $location, controllerService, locationService, deviceService, deviceStatusService) {
        $scope.locations = [];
        locationId = "";
        locationService.list({max:1000,active:true}).then(
            function(locations) {
                angular.forEach(locations, function (location) {
                    deviceService.list({max:1000,locationId:location.id}).then(
                        function(devices) {
                            angular.forEach(devices, function (device) {
                                    if(device.online == true) {
                                        if (locationId != device.locationId) {
                                            $scope.locations.push(location);
                                            _.last($scope.locations).devices = [];
                                            locationId = device.locationId;
                                        }
                                        deviceStatusService.list(device.id).then(
                                            function(statusBriefs) {
                                                device.status = statusBriefs;
                                            }
                                        );
                                        _.last($scope.locations).devices.push(device);
                                    }
                            });
                        }
                    );
                });
            });
    console.log($scope);
    }
]);

cinefms.controller("LocationController", ['$scope', '$location', '$routeParams', 'ControllerService', 'LocationService',
    function($scope, $location, $routeParams, controllerService, locationService) {
        controllerService.singleController('location',$routeParams['id'],$scope,$location,locationService);
        $scope.start();
    }
]);


cinefms.controller('SiteListController', ['$scope', '$location', 'ControllerService', 'SiteService', 
    function($scope, $location, controllerService, siteService) {
        controllerService.listController('sites',$scope,$location,siteService);
        $scope.start();
    }
]);


cinefms.controller("SiteController", ['$scope', '$location', '$routeParams', 'ControllerService', 'SiteService',
    function($scope, $location, $routeParams, controllerService, siteService) {
        controllerService.singleController('site',$routeParams['id'],$scope,$location,siteService);
        $scope.start();
    }
]);



cinefms.controller('CertificateListController', ['$scope', '$location', 'ControllerService', 'CertificateService', 
    function($scope, $location, controllerService, certificateService) {
        controllerService.listController('certificates',$scope,$location,certificateService);
        $scope.start();
    }
]);


cinefms.controller("CertificateController", ['$scope', '$location', '$routeParams', 'ControllerService', 'CertificateService',
    function($scope, $location, $routeParams, controllerService, certificateService) {
        controllerService.singleController('certificate',$routeParams['id'],$scope,$location,certificateService);
        $scope.start();
    }
]);



cinefms.controller('KeyListController', ['$scope', '$location', 'ControllerService', 'KeyService',
    function($scope, $location, controllerService, keyService) {
        controllerService.listController('keys',$scope,$location,keyService);
        $scope.start();
    }
]);

cinefms.controller('KeyListUploadController', ['$scope', '$location', 'ControllerService', 'KeyService',
    function($scope, $location, controllerService, keyService) {
        $scope.init = function(movieId){
            console.log("instantiating job KeyListUploadController ... ");
            console.log("movieId" + movieId);
            $scope.uploads = [];
            keyService.list({max:250,order:'created',asc:false,movieId:movieId}).then(
                function(keylist) {
                    angular.forEach(keylist, function (key) {
                       if(key.source.indexOf("KDM Upload") != -1){
                           $scope.uploads.push(key);
                       }
                    });
                }
            );
            console.log($scope);
            }
    }
]);

cinefms.controller('KeyController', ['$scope', '$location', '$routeParams', 'ControllerService', 'KeyService', 'MediaclipService',
    function($scope, $location, $routeParams, controllerService, keyService, mediaclipService) {
        controllerService.singleController('keys',$routeParams['id'],$scope,$location,keyService);
        keyService.get($routeParams['id']).then(
            function(keys) {
                console.log("mediaClipExternalId");
                console.log(keys.mediaClipExternalId);
                mediaclipService.list({externalId:keys.mediaClipExternalId}).then(
                    function(mediaclips) {
                        $scope.mediaclips = mediaclips;
                    }
                );
            }
        );
        console.log($scope);
        $scope.start();
    }
]);

cinefms.controller('WorkflowController', ['$scope', '$location', 'ControllerService', 'MediaclipTaskTypeService', 

    function($scope, $location, controllerService, taskTypeService) {
        controllerService.listController('tasktypes',$scope,$location,taskTypeService);
        $scope.refreshOnUpdate = false; 
        $scope.addTaskType = function() {
            var x = {
                name : $scope.newtype.name
            };
            taskTypeService.save(x).then(
                $scope.refresh
            );
        }
        $scope.start();
    }
]);


cinefms.controller('ScheduledJobListController', ['$scope', '$location', 'ControllerService', 'ScheduledJobService', 
    function($scope, $location, controllerService, scheduledJobService) {
        controllerService.listController('scheduledjobs',$scope,$location,scheduledJobService);
        $scope.start();
    }
]);


cinefms.controller("ScheduledJobController", ['$scope', '$location', '$routeParams', 'ControllerService', 'ScheduledJobService',
    function($scope, $location, $routeParams, controllerService, scheduledJobService) {
        controllerService.singleController('scheduledjob',$routeParams['id'],$scope,$location,scheduledJobService);
        $scope.params.tab = $scope.params.tab || "overview";
        $scope.start();
    }
]);


cinefms.controller('ScheduledJobLogController', ['$q','$scope', '$location',  '$routeParams','ControllerService', 'ScheduledJobService', 'ProjectSettingsService',
    function($q, $scope, $location, $routeParams, controllerService, scheduledJobService, projectSettingsService) {
        console.log("instantiating job log controller ... ");

        var bp = $q.defer();
        scheduledJobService.getLogs($routeParams['id']).then(
            function(logBriefs) {
                console.log("logs resolved: ... ");
                console.log(logBriefs);
                bp.resolve(logBriefs);
            }
        );
        var dcp = $q.defer();
        projectSettingsService.getDateConfig().then(
            function(dateConfig) {
                console.log("dateConfig resolved: ... ");
                console.log(dateConfig);
                dcp.resolve(dateConfig);
            }
        );

        var pArr = [];
        pArr.push(bp.promise);
        pArr.push(dcp.promise);

        $q.all(pArr).then(function(r){
            _.forEach(r[0],function(l) {
                m = moment(l.date).tz(r[1].timezone);
                l.date = m.format(r[1].dateFormat) +" - "+ m.format(r[1].timeFormat);
                if(l.status==0) {
                    l.date = l.date + " ! ";
                } else if(l.status==1) {
                    l.date = l.date + " ? ";
                }
            });
            if(r[0].length>0) {
                $scope.current = {
                    logId : r[0][0].id
                }
            };
            console.log($scope.current);
            $scope.briefs = r[0];
        });


        $scope.$watch("current.logId", function(x) {
            if(angular.isUndefined(x)) {
                return;
            }
            scheduledJobService.getLogDetails($routeParams['id'],x).then(
                function(log) {
                    console.log(log);
                    $scope.log=log;
                }
            );
        });
    }
]);

cinefms.controller('ScheduledJobLogTreeController', ['$scope', 
    function($scope) {
        $scope.expand = false;
    }
]);



cinefms.controller('UsersController', ['$scope', '$location', 'ControllerService', 'UserService',
    function($scope, $location, controllerService, userService) {
        controllerService.listController('users',$scope,$location,userService);
        $scope.selectUser = function(id) {
            $scope.selectedUser = {
                id : id
            };
        };
        $scope.start();
    }
]);

cinefms.controller('UserController', ['$scope', '$location', 'ControllerService', 'UserService', 'GroupService',
    function($scope, $location, controllerService, userService, groupService) {
        controllerService.singleController('user',undefined,$scope,$location,userService);
        $scope.groupService = groupService;
        $scope.reset = function(username,oldPW,newPW) {
            console.log("reset password  ... "+username+" / "+oldPW+" / "+newPW);
            userService.reset(username,oldPW,newPW);
        }

        $scope.set = {newPasswordA:"",newPasswordB:"",newPasswordOK:false};

        $scope.refreshed = function() {
            $scope.set = {newPasswordA:"",newPasswordB:"",newPasswordOK:false};
        }

        $scope.checkPassword = function() {
            $scope.set.newPasswordOK = $scope.set.newPasswordA.length > 5 && $scope.set.newPasswordA == $scope.set.newPasswordB;
        }



        $scope.$watch("set.newPasswordA", $scope.checkPassword);
        $scope.$watch("set.newPasswordB", $scope.checkPassword);

        $scope.start();
    }
]);

cinefms.controller('GroupsController', ['$scope', '$location', 'ControllerService', 'GroupService', 
    function($scope, $location, controllerService, groupService) {
        controllerService.listController('groups',$scope,$location,groupService);
        $scope.selectGroup = function(g) {
            $scope.selectedGroup = g;
        }
        $scope.start();
    }
]);


cinefms.controller('TagsManagementController', ['$scope', '$location',
    function($scope, $location) {
        $scope.params = {};
        
        $scope.tab = function(t) {
            var s = $location.search() || {} ;
            s.tab = t;
            $location.search(s);
            $scope.params.tab = t;
        };

        $scope.params.tab = $location.search().tab || "tags";
    }
]);

cinefms.controller('TagsController', ['$scope', '$location','ControllerService', 'TagService',
     function($scope, $location, controllerService, tagService) {
         controllerService.listController('tags',$scope,$location,tagService);
         $scope.start();
    }
]);


cinefms.controller('MediaClipTypesController', ['$scope', '$location','ControllerService', 'MediaClipTypeService',
     function($scope, $location, controllerService, mediaClipTypeService) {
         controllerService.listController('mediaClipTypes', $scope, $location, mediaClipTypeService);
         $scope.start();
    }
]);

cinefms.controller('FrameratesController', ['$scope', '$location','ControllerService', 'FramerateService',
     function($scope, $location, controllerService, framerateService) {
         controllerService.listController('framerates', $scope, $location, framerateService);
         $scope.start();
    }
]);

cinefms.controller('PackagetypesController', ['$scope', '$location','ControllerService', 'PackagetypeService',
     function($scope, $location, controllerService, packagetypeService) {
         controllerService.listController('packagetypes', $scope, $location, packagetypeService);
         $scope.start();
    }
]);

cinefms.controller('DevicetypesController', ['$scope', '$location','ControllerService', 'DevicetypeService',
    function($scope, $location, controllerService, devicetypeService) {
        controllerService.listController('devicetypes', $scope, $location, devicetypeService);
        $scope.start();
    }
]);

cinefms.controller('ScreenAspectsController', ['$scope', '$location','ControllerService', 'ScreenAspectService',
     function($scope, $location, controllerService, screenAspectService) {
         controllerService.listController('screenAspects', $scope, $location, screenAspectService);
         $scope.start();
    }
]);

cinefms.controller('MediaAspectsController', ['$scope', '$location','ControllerService', 'MediaAspectService',
     function($scope, $location, controllerService, MediaAspectService) {
         controllerService.listController('mediaAspects', $scope, $location, MediaAspectService);
         $scope.start();
    }
]);

cinefms.controller('AudioFormatsController', ['$scope', '$location','ControllerService', 'AudioFormatService',
     function($scope, $location, controllerService, audioFormatService) {
         controllerService.listController('audioFormats', $scope, $location, audioFormatService);
         $scope.start();
    }
]);


cinefms.controller('ProjectSettingsController', ['$scope', '$location','ProjectSettingsService', 'MailTemplateService', 
    function($scope, $location, projectSettingsService, mailTemplateService) {
        $scope.mailTemplateService = mailTemplateService;
        $scope.newObject = {
            password : ""
        }
        $scope.refresh = function() {
            projectSettingsService.get().then(
                function(projectSettings) {
                    $scope.projectSettings = projectSettings;
                }
            );
            projectSettingsService.getMailSettings().then(
                function(mailSettings) {
                    $scope.mailSettings = mailSettings;
                }
            );
            $scope.newObject = {
                password : ""
            }
        };
        $scope.save = function() {
            projectSettingsService.saveMailSettings($scope.mailSettings).then(
                $scope.refresh
            );
        };
        $scope.updateCredentials = function() {
            console.log("updating mailserver credentials ... ");
            projectSettingsService.updateCredentials(
                { 
                    "username" : $scope.mailSettings.username, 
                    "password" : $scope.newObject.password
                }
            ).then(
                $scope.refresh
            );
        }
        $scope.refresh();
    }
]);


