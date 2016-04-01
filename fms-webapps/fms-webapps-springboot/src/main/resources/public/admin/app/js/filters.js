cinefms.filter('markdown',  function($sce){
	return function(value) {
		value = value || "*empty*";
		return $sce.trustAsHtml(markdown.toHTML(value));
	}
});


cinefms.filter('language',  function($sce){
	return function(value) {
		value = value || "*empty*";
		return $sce;
	}
});


cinefms.filter('rate', function(){
    return function(value, rate) {
        if(value==0) {
            return "0s";
        }
        if(!value || value=='null' || !rate || rate=='null' || rate == '[unknown]') {
            return "";
        }
        var seconds = Math.ceil(eval(value+"/"+(rate.replace(" ","/"))));
        var hours   = Math.floor(seconds / 3600);
        var minutes = Math.floor((seconds - (hours * 3600)) / 60);
        var seconds = seconds - (hours * 3600) - (minutes * 60);
        var time = "";

        if (hours != 0) {
          time = hours+":";
        }
        if (minutes != 0 || time !== "") {
          minutes = (minutes < 10 && time !== "") ? "0"+minutes : String(minutes);
          time += minutes+":";
        }
        if (time === "") {
          time = seconds+"s";
        }
        else {
          time += (seconds < 10) ? "0"+seconds : String(seconds);
        }
        return time;
    };
});

cinefms.filter('byteFilter', function () {
    return function (size, precision) {
        if (precision == 0 || precision == null) {
            precision = 1;
        }
        if (size == 0 || size == null) {
            return "-";
        } else if(!isNaN(size)){
            var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
            var posttxt = 0;
            if (size < 1024) {
                return Number(size) +" "+ sizes[posttxt];
            }
            while( size >= 1024 ) {
            posttxt++;
                size = size / 1024;
            }

            var power = Math.pow (10, precision);
            var poweredVal = Math.ceil (size * power);

            size = poweredVal / power;

            return size +" "+sizes[posttxt];
        } else {
            return "[NaN]";
        }

    };
});

/*
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
*/


cinefms.filter('time', [ 'DateFormattingService', 
    function (dateFormattingService) {
        return function(value) {
            console.log(dateFormattingService);
            return dateFormattingService.formatTime(value);
        }
    }
]);
cinefms.filter('date', [ 'DateFormattingService', 
    function (dateFormattingService) {
        return function(value) {
            return dateFormattingService.formatDate(value);
        }
    }
]);