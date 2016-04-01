

cinefms.factory('ErrorService', [ function() {

    var errors = {

        current : [],
        callbacks : []

    };

    return {

        addCallback : function(fn) {
            errors.callbacks.push(fn);
        },

        reportError : function(status, error) {
            _.forEach(errors.callbacks,function(c) {
                c(status,error);
            })
        }


    }

}]);



//
//        /movie
//

cinefms.factory('MovieService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('movies/movies');
    }
]);

cinefms.factory('MovieVersionService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('movies/versions');
    }
]);

//
//        /mediaclip
//

cinefms.factory('MediaclipService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/clips');
    }
]);

cinefms.factory('MediaclipTypeService', [ 'ServiceService',
    function(serviceService) {
        return serviceService.createService('mediaclips/types');
    }
]);

cinefms.factory('MediaclipFramerateService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/framerates');
    }
]);

cinefms.factory('MediaclipMediaAspectService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/mediaaspects');
    }
]);

cinefms.factory('MediaclipScreenAspectService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/screenaspects');
    }
]);

//
//        /mediaclips/tasks
//

cinefms.factory('MediaclipTaskService', [ 'ServiceService', 'Restangular', 
    function(serviceService, restangular) {
        var out = serviceService.createService('mediaclips/tasks');
        out.getProgress = function(id) {
            return restangular.all('mediaclips/tasks').one(id).all('progress').getList();
        }
        out.saveProgress = function(id,p) {
            return restangular.all('mediaclips/tasks').one(id).all('progress').customPOST(p);
        }
        return out;
    }
]);

cinefms.factory('MediaclipTaskTypeService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/tasktypes');
    }
]);


//
//        /packages
//

cinefms.factory('PackageService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('packages/packages');
    }
]);

cinefms.factory('PackageFileService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('packages/files');
    }
]);

cinefms.factory('PackageCopyService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('packages/packagecopies');
    }
]);

cinefms.factory('PackageFileCopyService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('packages/filecopies');
    }
]);

cinefms.factory('PackageTypeService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('packages/packagetypes');
    }
]);
cinefms.factory('StorageSystemService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('packages/storagesystems');
    }
]);


//
//        /crypto
//

cinefms.factory('KeyService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('crypto/keys');
    }
]);

cinefms.factory('CertificateService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('crypto/certificates');
    }
]);

cinefms.factory('KeyRequestService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('crypto/keyrequests');
    }
]);


//
//        /playback
//

cinefms.factory('EventService', [ 'ServiceService',
    function(serviceService) {
        return serviceService.createService('playback/events');
    }
]);


cinefms.factory('ScheduleOverviewService' ['EventService','LocationService','SiteService',
    function(eventService,locationService,siteService) {
        return {
            


        };
    }
]);





cinefms.factory('LocationService', [ 'ServiceService',
    function(serviceService) {
        return serviceService.createService('playback/locations');
    }
]);

cinefms.factory('SiteService', [ 'ServiceService',
    function(serviceService) {
        return serviceService.createService('playback/sites');
    }
]);

cinefms.factory('DeviceService', [ 'ServiceService',
    function(serviceService) {
        return serviceService.createService('playback/devices');
    }
]);

cinefms.factory('DevicetypeService', [ 'ServiceService',
    function(serviceService) {
        return serviceService.createService('playback/devices_types');
    }
]);

cinefms.factory('DeviceStatusService', [ 'ServiceService', 'Restangular',
    function(serviceService,restangular) {
        return {
            list : function(deviceId) {
                return restangular.one("playback/devices",deviceId).all("status").getList({max:100});
            },
            get : function(deviceId,statusId) {
                return restangular.one("playback/devices",deviceId).one("status/"+statusId,"details").get();
            }
        }
    }
]);

cinefms.factory('DeviceProtocolService', [ 'ServiceService',
    function(serviceService) {
        return serviceService.createService('playback/deviceprotocols');
    }
]);


cinefms.factory('ProjectSettingsService', [ 'DebounceService', 'Restangular', '$http', '$q',
    function(debounceService, restangular, $http, $q) {
        var a = {

            getDateConfig : debounceService.debounce(
                function() {
                    var d = $q.defer();
                    restangular.one("project","settings").get().then(
                        function(ps) {
                            a.dateConfig = {
                                timezone : ps.timezone || "Europe/Berlin",
                                timeFormat : ps.timeFormat || "HH:mm",
                                dateFormat : ps.dateFormat || "YYYY-MM-DD",
                            };
                            d.resolve(a.dateConfig);
                        }
                    );
                    return d.promise;
                },100)



        };
        return {
            get : function(id) {
                return restangular.one("project","settings").get();
            },
            save : function(object) {
                var def = $q.defer();
                return object.save();
            },
            getMailSettings : function(id) {
                return restangular.one("project","mailserver").get();
            },
            saveMailSettings : function(mailSettings) {
                var def = $q.defer();
                return restangular.one("project","mailserver").customPUT(mailSettings);
            },
            updateCredentials : function(credentials) {
                return restangular.one("project/mailserver/credentials").customPUT(credentials);
            },
            getDateConfig :  function() {
                var d = $q.defer();
                if(!angular.isUndefined(a.dateConfig)) {
                    d.resolve(a.dateConfig);
                } else {
                    a.getDateConfig().then(d.resolve);
                }
                return d.promise;
            }
        }
    }
]);

cinefms.factory('DateFormattingService', [ '$q', 'ProjectSettingsService', 
    function($q,projectSettingsService) {
        var s = $q.defer();
        projectSettingsService.getDateConfig().then(
            function(dateConfig) {
                a = {
                    formatTime : function(x) {
                        var m =moment(x).tz(dateConfig.timezone);
                        return m.format(dateConfig.timeFormat);
                    },
                    formatDate : function(x) {
                        var m =moment(x).tz(dateConfig.timezone);
                        return m.format(dateConfig.dateFormat);
                    }
                }
                s.resolve(a);
            }

        )
        return s.promise;
    }
]);



//
//        /admin
//



cinefms.factory('MailTemplateService', [ 'ServiceService', 'Restangular',  
    function(serviceService, restangular) {
        var out = serviceService.createService('project/mailtemplates');

        out.sendTest = function(id,tokens,recipient) {
            console.log(tokens);
            return restangular.one("project/mailtemplates/"+id,"test").customPOST(tokens,undefined,{"recipient" :recipient});
        };


        return out;
    }
]);


cinefms.factory('ProjectService', [ 'DebounceService', 'ServiceService', 'Restangular', '$q', 
    function(debounceService,serviceService,restangular,$q) {
        var s = serviceService.createService('admin/projects');
        s.currentProject = undefined;
        s.getCurrentProjectInternal = debounceService.debounce(function(){ 
            var d = $q.defer();
            if(angular.isUndefined(s.currentProject)) {
                restangular.all('admin').one('currentproject').get().then(
                    function(cp) {
                        s.currentProject = cp;
                        d.resolve(cp);
                    }
                );
            } else {
                d.resolve(s.currentProject);
            }
            return d.promise;
        },200);
        s.getCurrentProject = function() {
            if(s.currentProject) {
                var d = $q.defer();
                d.resolve(s.currentProject);
                return d.promise;
            } else {
                return s.getCurrentProjectInternal();
            }
        }

        s.dateFormat = {

        };

        return s;
    }
]);


cinefms.factory('ScheduledJobService', [ 'ServiceService', 'Restangular', 
    function(serviceService, restangular) {
        var out = serviceService.createService('global/scheduledjobs');
        out.getLogs = function(id) {
            return restangular.all('global/scheduledjobs/'+id+'/logs').getList();
        };
        out.getLogDetails = function(id,detailId) {
            return restangular.all('global/scheduledjobs/'+id+'/logs/'+detailId).one("details").get();
        };
        return out;
    }
]);




cinefms.factory('ScheduledJobTypeService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('global/scheduledjobtypes');
    }
]);



cinefms.factory('UserService', [ 'ServiceService', 'Restangular', 
    function(serviceService, restangular) {
        var s = serviceService.createService('global/users');
        s.getCurrentUser = function() {
            return  restangular.one('admin/authentication/login').customGET();

        }
        s.reset = function(username,oldPassword,newPassword) {
            var ppp = {
                username : username,
                password : oldPassword,
                newPassword : newPassword
            };
            restangular.one('global/users/1/reset').customPOST(ppp);
        }
        return s;
    }
]);

cinefms.factory('GroupService', [ 'ServiceService', 'Restangular', 
    function(serviceService, restangular) {
        out = serviceService.createService('global/groups');
        out.getEntityGroups = function() {
            return restangular.all('global/entitygroups').getList();
        }
        out.getAccessTypes = function() {
            return restangular.all('global/accesstypes').getList();
        }
        return out;
    }
]);

cinefms.factory('AccessTypeService', [ 'ServiceService', 'Restangular', 
    function(serviceService, restangular) {
        out = serviceService.createService('global/accesstypes');
        return out;
    }
]);

cinefms.factory('EntityGroupService', [ 'ServiceService', 'Restangular', 
    function(serviceService, restangular) {
        out = serviceService.createService('global/entitygroups');
        return out;
    }
]);

cinefms.factory('MOTDService', [ 'ServiceService', 'Restangular', 
    function(serviceService, restangular) {
        out = serviceService.createService('global/motd');
        return out;
    }
]);

cinefms.factory('LanguageService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('global/languages');
    }
]);

cinefms.factory('TagService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('global/tags');
    }
]);

cinefms.factory('MediaClipTypeService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/types');
    }
]);

cinefms.factory('FramerateService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/framerates');
    }
]);

cinefms.factory('PackagetypeService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('packages/packagetypes');
    }
]);

cinefms.factory('ScreenAspectService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/screenaspects');
    }
]);

cinefms.factory('MediaAspectService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/mediaaspects');
    }
]);

cinefms.factory('AudioFormatService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('mediaclips/audioformats');
    }
]);

cinefms.factory('CountryService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('global/countries');
    }
]);

cinefms.factory('CommentService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('global/comments');
    }
]);

cinefms.factory('NotificationService', [ 'ServiceService', 
    function(serviceService) {
        return serviceService.createService('global/notifications');
    }
]);

