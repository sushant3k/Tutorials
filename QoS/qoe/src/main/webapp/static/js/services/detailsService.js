app.service('DetailsService', function($http, $q) {

	this.getDetails = function(deviceIdentifier, sessionId) {
		return $http.get("api/v1/devices/"+deviceIdentifier+"/sessions/" + sessionId)
		.then(function(result){							
			return result.data;
		});

	}

});