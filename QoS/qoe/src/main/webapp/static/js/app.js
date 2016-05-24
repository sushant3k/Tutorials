var app = angular.module('app',['ngRoute','ngSanitize']);

app.factory('Page', function() {
	var title='QoE'
	return {
		title : function() { return title; },
		setTitle: function(newTitle) { title = newTitle; }
	};
});
/*
app.factory('myHttpInterceptor', function($q) {
  return {
    // optional method
    'request': function(config) {
      
	  config.url = "/wizapp/"+config.url;
	  console.log(config);
      return config;
    }
  }
});
	*/


app.factory('csrfInterceptor', function($q) {
	  return {
	    // optional method
	    'request': function(config) {     
		  if (config.method !== "GET") {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");					
			config.headers["X-CSRF-TOKEN"]= token;		
			console.log(config.headers);
		  }
		  
	      return config;
	    }
	  }
	});


app.config(function($httpProvider, $routeProvider,$locationProvider){
	//$httpProvider.interceptors.push('myHttpInterceptor');
	$httpProvider.interceptors.push('csrfInterceptor');	
	$routeProvider
	 .when('/', {
       templateUrl: 'dashboard',        
       public: true, 
       label: 'Welcome to the QoE Dashboard'
     })
     .when('/details/:deviceId/:sessionId', {
        templateUrl: 'details',
        public: true, 
        label: 'Details View'
      })
});

