app.service('DashboardService', function($http, $q) {

	this.getAllSessions = function() {
		return $http.get("api/v1/sessions/")
		.then(function(result){							
			return result.data;
		});
	}
	
	this.getAllDevices = function() {
		return $http.get("api/v1/devices/")
		.then(function(result){							
			return result.data;
		});
	}
	
	this.getDetailsByDeviceId = function(deviceIdentifier) {
		return $http.get("api/v1/devices/"+deviceIdentifier)
		.then(function(result){							
			return result.data;
		});
		
	}
	
	this.findByDeviceAndSession = function(deviceIdentifier, sessionId) {
		return $http.get("api/v1/devices/"+deviceIdentifier+"/sessions/" + sessionId)
		.then(function(result){							
			return result.data;
		});
	}
});