/**
 * Filename : chatRoomController.js
 * --------------------------------
 * Licensed Under MIT:
 * 
 * This is the AngularJS Controller for controlling the chat rooms.
 */
app.controller('ChatRoomController',function($scope,ChatRoomService,$location) {	
	
	var stompClient = null;
	var channelId = null; 
	
	$scope.availableDevices = ChatRoomService.getAllChatRooms().then(function(data){	
		
		$scope.chatrooms =  data;		
	});
	
	
	/**
	 * Function to subscribe to a channel and init the chat.
	 */
	$scope.initChat = function() {
		
		channelId = $scope.channelId;
		
		if (typeof(channelId) === "undefined" || channelId === null) {
			$scope.msg = "Please select a channel";
			return;
		}
		if (stompClient != null) {
			stompClient.disconnect();
		}
		/**
		** Clean update
		*/
		
		
		$scope.channel = $("#cid option:selected").text();
		$scope.channelId = $scope.channelId;
		
		$scope.msg = "Subscribed to channel: " + $scope.channel;
		
		var socket = new SockJS('/simpleWebSocket/receive');
	    stompClient = Stomp.over(socket);
		
	    stompClient.connect({}, function(frame) {	        
	        console.log('Connected: ' + frame);
	        stompClient.subscribe('/topic/'+channelId, function(data){	        	
	        	$('.receiveText').val( $('.receiveText').val() + JSON.parse(data.body).message + "\n" );	        	
	        });
	    });
	    
	}
	
	/**
	 * Clean up the text areas
	 */
	$scope.cleanUp = function() {
		$('#txtMessage').val("");
		$('.receiveText').val("");
	};
	
	/**
	 * Send message over the subscribed channel
	 */
	$scope.sendMessage = function() {
		if (stompClient == null) {
			$scope.msg = "Please subscribe to a channel first.";
			return;
		}
		var message = $('#txtMessage').val();
		stompClient.send("/app/receive", {}, JSON.stringify({ 'content': message , 'channelId' : channelId }));
	}
	
	/**
	 * Disconnect from channel
	 */
	$scope.disconnect = function() {
		if (stompClient !== null) {
			stompClient.disconnect();
			stompClient = null;			
		}
		$scope.channelId = null;
		$scope.channel = null;
		channelId = null;
		$scope.cleanUp();
		
	}
	
});