Simple Learning Projects
===========================

LearnOAuth2
---------------------------

This project demonstrates how a web application can implement OAuth2 Client to connect to Facebook or LinkedIn. 

To start with, the application developer has to register the application with LinkedIn as well as Facebook.

Once registered, the API Secret, API Key and the relevant URLs (redirect URLs) can be configured in the following java files:

- com.learning.oauth2.service.FacebookOAuthService.java
- com.learning.oauth2.service.LinkedinOAuthService.java

If the application is deployed on the localhost and the server listens on port 8080, then it can be accessed from browser by typing the url: http://localhost:8080/LearnOAuth2/login

