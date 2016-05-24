
app.controller('DashboardController',function($scope,DashboardService,$location) {	
	
	/*$scope.availableSessions = DashboardService.getAllSessions().then(function(data){	
		$scope.sessions =  data;		
	});*/

	$scope.availableDevices = DashboardService.getAllDevices().then(function(data){	
		//$scope.devices =  data.devices;
		if (typeof(data) !== 'undefined' && typeof(data.devices) !== 'undefined' && data.devices !== null && data.devices.length >0 ) {
			var devices = data.devices;
			var device = [];
			for (var d in devices) {
				var id = devices[d].deviceIdentifier;
				var sessions = devices[d].session;
				for (var i  in sessions) {
					device.push({"deviceIdentifier" : id, "sessionId" : sessions[i].id });
				}
			}
			$scope.devices = device;
		}
	});
	/*
	$scope.availableDevices = function(deviceIdentifier){
		for (var d in $scope.devices) {
			if (d.deviceIdentifer === deviceIdentifer) {
				$scope.device = d;
				break;
			}
		}				
	};*/
	
	$scope.findStatistics = function(device) {
		DashboardService.getDetailsByDeviceId(device.deviceIdentifier).then(function(data) {
			$scope.data = data.qoePacket;
			var tsParams = data.qoePacket.params.tsParams;
			if (typeof(tsParams) !== "undefined" && tsParams.length > 0) {
				$scope.tsParams = tsParams[0];
				$scope.streams = tsParams[0].streams;
			}
			
			var hlsParams = data.qoePacket.params.httpParams;
			
			if (typeof(hlsParams) != "undefined" && hlsParams.length > 0) {
				$scope.hlsParams = hlsParams[0];
			}
			
		});
	}
	
	$scope.findByDeviceAndSession = function(device, session) {
		var devId = device.deviceIdentifier;
		var sessId = session.id;
		DashboardService.findByDeviceAndSession(devId, sessId).then(function(data) {
			console.log(data);
		});
	}
});

