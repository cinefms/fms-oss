cinefms.directive("commentsFull", [ '$q', '$timeout', 'CommentService', 
	function($q,$timeout,commentService) {
		var a = {
			replace: false,
			transclude: true,
			templateUrl: 'app/partial/fms-comments-edit.html',
			scope : {
				object: "=?",
				id: "=?",
				type: "@?"
			},
			link : function(scope, element, attrs) {

				if(angular.isUndefined(scope.object)) {
					scope.object = {
						id : scope.id,
						objectTypeName : scope.type
					}


				}

				scope.showInput = function() {
					scope.input = true;
					scope.comment = {
						objectType : scope.object.objectTypeName,
						objectId : scope.object.id,
						text : ""
					}
				}
				scope.hideInput = function() {
					scope.input = false;
				}
				scope.send = function() {
					commentService.save(scope.comment).then(
						function() {
							scope.hideInput();
							scope.refresh();
						}
					)
				}
				scope.refresh = function() {
					commentService.list({objectId:scope.object.id,asc:false}).then(
						function(comments) {
							scope.comments = comments;
						}
					)

				}

				scope.$watch('object', scope.refresh);
			}
		}
		return a;

	}
]);


cinefms.directive("comment", [ 
	function() {
		var a = {
			replace: true,
			transclude: true,
			templateUrl: 'app/partial/fms-comment-display.html',
			scope : {
				comment: "=object"
			},
			link : function(scope, element, attrs) {
			}
		}
		return a;

	}
]);


