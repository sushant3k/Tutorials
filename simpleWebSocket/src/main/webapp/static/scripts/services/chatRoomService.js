/**
 * filename : chatRoomService.js
 * -------------------------------
 * Licensed Under MIT.
 * 
 * ChatRoom Service for managing chat rooms
 */
app.service('ChatRoomService', function($http, $q) {


	/**
	 * Fetch all available chat rooms
	 */
	
	this.getAllChatRooms = function() {
		return $http.get("chatrooms/")
		.then(function(result){							
			return result.data;
		});
	}
		
});