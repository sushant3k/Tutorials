Simple WebSocket using AngularJS + BootStrap + Thymeleaf + SockJS + Spring Boot
===============================================================================

Simple Web Socket
---------------------------

This is a small application for a group chat on the preconfigured channels. 

Build and Deploy
----------------------------
You can run maven compile package options to package your application. 
Then you can deploy this application in any servlet container. I tested this application using 'Tomcat version 8.0.x' and JDK 1.8. 

How to access
----------------------------
Once you deploy the application on tomcat or any other servlet container, it can then be accessed as http://<server IP>:<server Port>/simpleWebSocket
If the application is deployed on the localhost and the server listens on port 8080, then it can be accessed from browser by typing the url: http://localhost:8080/LearnOAuth2/login

After landing on the page, you should see two jumbotrons. On the left jumbotron, you can subscribe to a channel and then send messages over that channel. 

On the right jumbotron, you can see the messages appearing over the chat. 

To test this, you can subscribe to the same channel from another browser window and send messages over the same channel. 

Explanation of the implementation
------------------------------
* app.html is the first html file that is loaded when you access the application over the http URL. 
* app.js is the entry point for the Angular and the routes are configured in this javascript file. 

* After the app.js is loaded, the URL for the 'home' is requested and the home page is rendered over the browser. 
* The home page invokes the 'ChatRoomController' in the chatRoomController.js and loads all the channels in the single select dropdown over the REST interface. The REST request is sent 	using the 'ChatRoomService' from the chatRoomService.js file.

* Once you subscribe to the channel from the UI, a subscription request is sent to the server over the WebSocket interface.
* And, when a message is sent to this channel, the server publishes the message for all the subscribers that have subscribed to the same channels. 

Happy Learning !!!


