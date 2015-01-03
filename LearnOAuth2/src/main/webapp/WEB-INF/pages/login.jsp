<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    

    <title>Login Page</title>

    <!-- Bootstrap core CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>	

	<script>
		$(document).ready(function(){
	
			  var params = location.search.substr(location.search.indexOf("?")+1);
			  if(params==="error"){
				  $("#login_error_div").show();
				  
			  }
			});
	</script>
</head>
<body>
	<div style="width:500px;height:400px;margin:auto;">
			<div style="display:none;"  id ="login_error_div" class=" error form-signin">Login failed! Please try again.</div>
          	  <div align="center">
          	  	<h3 class="form-signin-heading form-signin login-label" >Welcome to OAuth2 login</h3>
          	  </div>	
        
          	<div >
				<button class="btn btn-primary btn-lg" style="margin-left:150px;" type="button" onclick="location.href='${linkedin_login_url}'">Sign up with Linkedin</button>
			</div>			
			
			<div >
				<button class="btn btn-primary btn-lg" style="margin-left:150px;margin-top:20px;" type="button" onclick="location.href='${facebook_login_url}'">Sign up with Facebook</button>
			</div>
			
      </div>

</body>
</html>