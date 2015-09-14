<html>
	<#include "/layout/head.ftl"/>
	<body>
		<script type="text/javascript">
			var ticketApp = angular.module('ticketApp', []);
			ticketApp.controller('TicketController', function($scope, $log, $http, $timeout) {
				$scope.loadEventVenue = function(eventId) {
					$http.get('/web/event/' + eventId + '/venue').success(function(rdata) {
						if(rdata.status == 0)
							$scope.event = rdata.data;
						else
							alert("Error " + rdata.status);
					});
				}

				$scope.getRange = function(n) {
        			var range = []
        			for(var i = 0; i < n; i++)
        				range.push(i);
        			return range;
    			};

				$scope.onHold = function() {
					$scope.hold.eventId = $scope.eventId;
					$http.post('/service/hold/event/' + $scope.eventId, $scope.hold).success(function(rdata) {
						if(rdata.status == 0)
							$scope.loadEventVenue($scope.eventId);
						else
							alert("Error:  " + rdata.errorMsg);
					});
				}

				$scope.taken = function(seating, row, seat) {
					var n = row * seating.venueSeating.seatsPerRow + (row+1 * seat+1) - row;
					var taken = seating.venueSeating.seatsPerRow*seating.venueSeating.rows - seating.availableSeats;
					return n <= taken;
				}

				$scope.hold = {};
				$scope.eventId = ${eventId};
				$scope.loadEventVenue($scope.eventId);
			});
		</script>
		<div ng-app="ticketApp" ng-controller="TicketController" ng-cloak>
			<div class="event">
				<div class="hold-form">
					<div><input type="text" placeholder="Email" ng-model="hold.customerEmail" /></div>
					<div>
						<input type="text" placeholder="Seats" size="4" ng-model="hold.criteria.minSeats" />
						<input type="text" placeholder="Min" size="2" ng-model="hold.criteria.minLevel" /> - 
						<input type="text" placeholder="Max" size="2" ng-model="hold.criteria.maxLevel" />Level
					</div>
					<button ng-click="onHold()">Hold Seats</button>
				</div>
				<div>
					<a href="/">Home</a> :: {{event.name}}				
				</div>
				<div ng-if="event" ng-cloak style="padding-top:2em">
					<span class="event-title">{{event.name}}</span>
					<div class="venue-title">@{{event.venue.name}}</div>
					<div>({{event.startDttm | date:'medium'}})</div>
				</div>
				<div ng-repeat="seating in event.seating" style="padding-top:1em">
					<div class="level-title">{{seating.venueSeating.name}} (Level {{seating.venueSeating.level}})</div>
					<div class="venue-row" ng-repeat="r in getRange(seating.venueSeating.rows)">
						<ul>
							<li ng-repeat="s in getRange(seating.venueSeating.seatsPerRow)" ng-class="taken(seating, r, s) ? 'seat-taken' : 'seat-free'">O</li>
						</ul>
						</div>
					</div>
					
				</div>
			</div>
		</div>
	</body>
</html>