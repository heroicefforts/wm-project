<html>
	<#include "/layout/head.ftl"/>
	<body>
		<script type="text/javascript">
			var eventApp = angular.module('eventApp', []);
			eventApp.controller('EventController', function($scope, $log, $http, $timeout) {
				$scope.loadEvents = function(page, pageSize) {
					$http.get('/web/event').success(function(rdata) {
						if(rdata.status == 0)
							$scope.events = rdata.data;
						else
							alert("Error " + rdata.status);
					});
				}

				$scope.pageSize = 10;
				$scope.loadEvents(0, $scope.pageSize);
			});
		</script>
		<div ng-app="eventApp" ng-controller="EventController" ng-cloak>
			<h1>Upcoming Events</h1>
			<div class="event" ng-repeat="event in events" style="padding-top:1em">
				<div class="event-title"><a ng-href="/event/{{event.id}}">{{event.name}}</a></div>
				<div class="venue-title">@{{event.venue.name}}</div>
				<div>({{event.startDttm | date:'medium'}})</div>
			</div>
			<div>
				<a ng-if="event.prevPage" ng-href="{{event.prevPage}}">Previous</a> 
				<a ng-if="event.nextPage" ng-href="{{event.nextPage}}">Next</a>
			</div>
		</div>
	</body>
</html>