
app.controller('DetailsController',function($scope,$routeParams, DetailsService,$location) {	
	
	var devId = $routeParams.deviceId;
	var sessId = $routeParams.sessionId;
	var stompClient = null;
	var ht = 300;
	
	if (stompClient != null) {
		stompClient.disconnect();
	}
	
	var socket = new SockJS('/qoe/receive');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/'+sessId+devId, function(data){        	
        	$scope.details = JSON.parse(data.body);
        	$scope.createChartData();
        	$scope.createStreamGraphData();
        	$scope.createStallGraphData();
        	$scope.paintCharts();
        });
    });
    
    
    

	$scope.availableDevices = DetailsService.getDetails(devId, sessId).then(function(data){	
		$scope.details =  data;		
		$scope.createChartData();
		$scope.createStreamGraphData();
		$scope.createStallGraphData();
		$scope.paintCharts();
	});
	
	$scope.paintCharts = function() {
		$scope.paintBandwidthChart();
		$scope.paintStreamGraph();
		//$scope.paintMediaErrorsChart();
		$scope.paintStallGraph();
		$scope.paintSecondStallGraph();
	};
	
	$scope.paintSecondStallGraph = function() {
		
		var se = $scope.stallGraphData2;
		
		if (se === null || typeof(se) === "undefined" || se.length === 0) {
			return;
		}
		
		var margin = {top: 20, right: 20, bottom: 30, left: 50},
	    width = 600 - margin.left - margin.right,
	    height = ht - margin.top - margin.bottom,
		 xRoundBands = 0.2;
//		var parseDate = d3.time.format("%d-%b-%y %H:%M");
		var parseDate = d3.time.format("%a %b %d %Y %H:%M:%S");
		
		//var x = d3.scale.linear().domain([-1000, 1000]).range([0, width]);
		//var y = d3.time.scale().range([height, 0]);
		
		
		
		var x = d3.time.scale().range([0, width]);
		var y = d3.scale.linear().range([height, 0]).nice();
			
		//var minDate = se[0].videoPlayTime;
		//var maxDate = sg[sg.length - 1].nextPacketRequestTime;
		
		var minDate = se[0].packetTime;
		var maxDate = se[se.length - 1].packetTime;
	

			
		var xAxis = d3.svg.axis()
		    .scale(x) .ticks(d3.time.seconds, 30).tickFormat(d3.time.format("%X"))
		    .orient("bottom");
	
		var yAxis = d3.svg.axis()
		    .scale(y)
		    .orient("left");
	
		/**
		 * Line for ActualGraphData
		 */
		var line = d3.svg.line().x(function(d) { return x(d.packetTime); })
						    	.y(function(d) { return y(d.value);	})
						    	

		$('#stallChart2').empty();
		
		var div = d3.select("body").append("div")
		.attr("class", "tooltip")
		.style("opacity", 0);
		
		
		var svg = d3.select("#stallChart2").append("svg")
		    .attr("width", width + margin.left + margin.right)
		    .attr("height", height + margin.top + margin.bottom)
		  .append("g")
		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
	/*
		var gEnter = svg.enter().append("svg").append("g");
		  gEnter.append("g").attr("class", "y axis");
		  gEnter.append("g").attr("class", "x axis");
		  gEnter.append("g").attr("class", "x axis zero"); */
	  
		se.forEach(function(d) {
			
			d.packetTime = +d.packetTime;
			d.value = +d.value;
		});
		
		x.domain(d3.extent(se, function(d) { return d.packetTime; }));
		//y.domain(d3.extent(se, function(d) { return d.value; }));
		y.domain([d3.min(se, function(d) { return d.value; }), d3.max(se, function(d) { return d.value; })]);
		//y.domain([0, d3.max(se, function(d) { return d.value; })]);
		//y.domain([0, d3.min(se, function(d) { return d.value; })]);
		
		svg.append("g")
		  .attr("class", "grid")
	      .attr("class", "x axis")
	      .attr("transform", "translate(0," + height + ")")
	      .call(xAxis).selectAll("text")
			.style("text-anchor", "end")
			.attr("dx", "-.8em")
			.attr("dy", ".15em")
			.attr("transform", function(d) {
			return "rotate(-65)"
			});
	      
		svg.append("g").attr("class", "x axis zero");
		
		  svg.append("g")
		  .attr("class", "grid")
		      .attr("class", "y axis")
		      .call(yAxis)
		    .append("text")
		      .attr("transform", "rotate(-90)")
		      .attr("y", 6)
		      .attr("dy", ".71em")
		      .style("text-anchor", "end")
		      .text("Value");
	
		  svg.append("path")
		      .datum(se)
		      .attr("class", "line")
		      .attr("d", line);
		  
		/* var g = svg.select("g")
          .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
		 */ 
		 svg.select(".x.axis.zero")
        .attr("transform", "translate(0," + y(0) + ")")
        .call(xAxis.tickFormat("").tickSize(0)); 
		  
		  //svg.addTimeAxis();
		     // .attr("d", x(function(d) { return x(d.x) }));
		  
		  svg.selectAll("dot")
			.data(se)
			.enter().append("circle")
			.attr("r", 3.5)
			.attr("cx", function(d) { return x(d.packetTime); })
			.attr("cy", function(d) { return y(d.value); })
			.on("mouseover", function(d) {
				div.transition()
				.duration(200)
				.style("opacity", .9);
				div.html(parseDate(new Date(d.packetTime)) + "<br/>")
				.style("left", (d3.event.pageX) + "px")
				.style("top", (d3.event.pageY - 28) + "px");
			})
			.on("mouseout", function(d) {
				div.transition()
				.duration(500)
				.style("opacity", 0);
			});
			
	};
	
	$scope.paintStallGraph = function() {
		
		var se = $scope.actualGraphData;
		
		if (se === null || typeof(se) === "undefined" || se.length === 0) {
			return;
		}
		
		var sg = $scope.stallGraphData;
		
		if (sg === null || typeof(sg) === "undefined" || sg.length === 0) {
			return;
		}
		
		var margin = {top: 20, right: 20, bottom: 30, left: 50},
	    width = 600 - margin.left - margin.right,
	    height = ht - margin.top - margin.bottom;

//		var parseDate = d3.time.format("%d-%b-%y %H:%M");
		var parseDate = d3.time.format("%a %b %d %Y %H:%M:%S");
		
//		var reverseParse = d3.time.format("%Y%m%d %H:%M:%S").parse;
		var reverseParse = d3.time.format("%d-%b-%y $H:%M").parse;
		var x = d3.scale.linear().range([0, width]);
		var y = d3.time.scale().range([height, 0]);
		
		var minDate = se[0].videoPlayTime;
		var maxDate = sg[sg.length - 1].nextPacketRequestTime;
		
//		var x = d3.time.scale().domain([minDate, maxDate]).range([0, width]);
		
		/*var m = [80, 80, 80, 80]; 
		var w = 850 - m[1] - m[3]; 
		var h = 400 - m[0] - m[2]; */ 
		
		
	
		var yAxis = d3.svg.axis()
		    .scale(y) .ticks(d3.time.seconds, 30).tickFormat("")//.tickFormat(d3.time.format("%X"))
		    .orient("left");
	
		var xAxis = d3.svg.axis()
		    .scale(x)
		    .orient("bottom").tickFormat("");
	
		/**
		 * Line for ActualGraphData
		 */
		var line = d3.svg.line().x(function(d) { return x(d.packetNumber); })
						    	.y(function(d) { return y(d.videoPlayTime);	})
						    	

		var line2 = d3.svg.line().x(function(d) { return x(d.packetNumber); })
    							 .y(function(d) { return y(d.nextPacketRequestTime);	})
//    							 .interpolate("basis");
		
		var div = d3.select("body").append("div")
		.attr("class", "tooltip")
		.style("opacity", 0);
		
		$('#stallChart').empty();
		var svg = d3.select("#stallChart").append("svg")
		    .attr("width", width + margin.left + margin.right)
		    .attr("height", height + margin.top + margin.bottom)
		  .append("g")
		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
		sg.forEach(function(d) {
			d.packetNumber = +d.packetNumber;
			d.nextPacketRequestTime = +d.nextPacketRequestTime;
		});
		se.forEach(function(d) {
			
			d.packetNumber = +d.packetNumber;
			d.videoPlayTime = +d.videoPlayTime;
		});
		
		x.domain(d3.extent(se, function(d) { return d.packetNumber; }));
		y.domain(d3.extent(se, function(d) { return d.videoPlayTime; }));
		
		y.domain(d3.extent(sg, function(d) { return d.nextPacketRequestTime; }));
		  
		svg.append("g")
	      .attr("class", "x axis")
	      .attr("transform", "translate(0," + height + ")")
	      .call(xAxis)
	      .append("text")
			.style("text-anchor", "end")
			.attr("dx", "25em")
			.attr("dy", "2em")
			.attr("class", "legend")
			.text("Total number of stream packets");
	      

		  svg.append("g")
		      .attr("class", "y axis")
		      .call(yAxis)
		    .append("text")
		    .attr("transform", "rotate(-90)")   
		      .attr("y", 6)
		      .attr("class", "legend")
		      .attr("dy", "-1.5em")
		      .attr("dx", "-5em")
		      .style("text-anchor", "end")
		      .text("Timeline");
	
		  svg.append("path")
		      .datum(se)
		      .attr("class", "line")
		      .attr("d", line);
		  
		  svg.append("path")
	      .datum(sg)	      
	      .attr("d", line2)
	      .attr("stroke","red")
	      .attr("stroke-width", 1)
	      .attr("fill", "none"); 
		  
		  
		  //svg.addTimeAxis();
		     // .attr("d", x(function(d) { return x(d.x) }));
		  
		  svg.selectAll("dot")
			.data(se)
			.enter().append("circle")
			.attr("r", 3.5)
			.attr("cx", function(d) { return x(d.packetNumber); })
			.attr("cy", function(d) { return y(d.videoPlayTime); })
			.on("mouseover", function(d) {
				div.transition()
				.duration(200)
				.style("opacity", .9);
				div.html(parseDate(new Date(d.videoPlayTime)) + "<br/>" + d.filename)
				.style("left", (d3.event.pageX) + "px")
				.style("top", (d3.event.pageY - 28) + "px");
			})
			.on("mouseout", function(d) {
				div.transition()
				.duration(500)
				.style("opacity", 0);
			});
	};
	
	$scope.paintMediaErrorsChart = function() {

		var se = $scope.syncErrorDataChart;
		if (se === null || typeof(se) === "undefined" || se.length === 0) {
			return;
		}
		var margin = {top: 20, right: 20, bottom: 30, left: 50},
	    width = 600 - margin.left - margin.right,
	    height = ht - margin.top - margin.bottom;

//		var parseDate = d3.time.format("%d-%b-%y %H:%M");
		var parseDate = d3.time.format("%a %b %d %Y %H:%M:%S");
		
//		var reverseParse = d3.time.format("%Y%m%d %H:%M:%S").parse;
		var reverseParse = d3.time.format("%d-%b-%y $H:%M").parse;
		var x = d3.time.scale().range([0, width]);
		var y = d3.scale.linear()
		    .range([height, 0]);
			
		var minDate = $scope.syncErrorDataChart[0].x;
		var maxDate = $scope.syncErrorDataChart[$scope.syncErrorDataChart.length - 1].x;
		
//		var x = d3.time.scale().domain([minDate, maxDate]).range([0, width]);
		
		/*var m = [80, 80, 80, 80]; 
		var w = 850 - m[1] - m[3]; 
		var h = 400 - m[0] - m[2]; */ 
		
		
	
		var xAxis = d3.svg.axis()
		    .scale(x) .ticks(d3.time.minute, 1).tickFormat(d3.time.format("%b-%d-%Y %X"))
		    .orient("bottom");
	
		var yAxis = d3.svg.axis()
		    .scale(y)
		    .orient("left");
	
		var line = d3.svg.line().x(function(d) { return x(d.x); })
						    	.y(function(d) { return y(d.y);	});

		var div = d3.select("body").append("div")
		.attr("class", "tooltip")
		.style("opacity", 0);
		
		$('#errorsChart').empty();
		var svg = d3.select("#errorsChart").append("svg")
		    .attr("width", width + margin.left + margin.right)
		    .attr("height", height + margin.top + margin.bottom)
		  .append("g")
		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
		
		$scope.syncErrorDataChart.forEach(function(d) {
			//d.date = parseDate(new Date(d.x));			
//			d.x = reverseParse(new Date(d.x));
			//d.x = reverseParse(parseDate(new Date(d.x)));
//			d.x = parseDate.parse(parseDate(new Date(d.x)));
			d.x = +d.x;
			d.y = +d.y;
		});
		
		x.domain(d3.extent($scope.syncErrorDataChart, function(d) { return d.x; }));
		y.domain(d3.extent($scope.syncErrorDataChart, function(d) { return d.y; }));
		  
		svg.append("g")
	      .attr("class", "x axis")
	      .attr("transform", "translate(0," + height + ")")
	      .call(xAxis).selectAll("text")
			.style("text-anchor", "end")
			.attr("dx", "-.8em")
			.attr("dy", ".15em")
			.attr("transform", function(d) {
			return "rotate(-65)"
			});
	      

		  svg.append("g")
		      .attr("class", "y axis")
		      .call(yAxis)
		    .append("text")
		      .attr("transform", "rotate(-90)")
		      .attr("y", 6)
		      .attr("dy", ".71em")
		      .style("text-anchor", "end")
		      .text("Error Count");
	
		  svg.append("path")
		      .datum($scope.syncErrorDataChart)
		      .attr("class", "line")
		      .attr("d", line);
		  //svg.addTimeAxis();
		     // .attr("d", x(function(d) { return x(d.x) }));
		  
		  svg.selectAll("dot")
			.data($scope.syncErrorDataChart)
			.enter().append("circle")
			.attr("r", 3.5)
			.attr("cx", function(d) { return x(d.x); })
			.attr("cy", function(d) { return y(d.y); })
			.on("mouseover", function(d) {
				div.transition()
				.duration(200)
				.style("opacity", .9);
				div.html(parseDate(new Date(d.x)) + "<br/>" + d.y + "<br/>" + d.filename)
				.style("left", (d3.event.pageX) + "px")
				.style("top", (d3.event.pageY - 28) + "px");
			})
			.on("mouseout", function(d) {
				div.transition()
				.duration(500)
				.style("opacity", 0);
			});
		
	};
	
	$scope.paintStreamGraph = function() {
		
		if ($scope.streamGraphData === null || typeof($scope.streamGraphData) === "undefined" || $scope.streamGraphData.length == 0 ) {
			return;
		}
		
		var margin = {top: 20, right: 20, bottom: 30, left: 50},
	    width = 600 - margin.left - margin.right,
	    height = ht - margin.top - margin.bottom;

//		var parseDate = d3.time.format("%d-%b-%y %H:%M");
		var parseDate = d3.time.format("%a %b %d %Y %H:%M:%S");
		
//		var reverseParse = d3.time.format("%Y%m%d %H:%M:%S").parse;
		var reverseParse = d3.time.format("%d-%b-%y $H:%M").parse;
		
		var x = d3.time.scale().range([0, width]);
		
		var minDate = $scope.streamGraphData[0].packetRequestTime;
		var maxDate = $scope.streamGraphData[$scope.streamGraphData.length - 1].packetRequestTime;
		var maxValue = $scope.streamGraphData[$scope.streamGraphData.length - 1].bandwidth;
//		var x = d3.time.scale().domain([minDate, maxDate]).range([0, width]);
		
		/*var m = [80, 80, 80, 80]; 
		var w = 850 - m[1] - m[3]; 
		var h = 400 - m[0] - m[2]; */ 
		
		var y = d3.scale.linear()
		    .range([height, 0]);
	
//		d3.scale().domain([0, maxValue]);
		
		var xAxis = d3.svg.axis()
		    .scale(x).tickFormat("")//  .ticks(d3.time.second, 30).tickFormat(d3.time.format("%X"))
		    .orient("bottom");
	
		var yAxis = d3.svg.axis()
		    .scale(y)		    
		    .orient("left").tickFormat("");
	
			
			
		var line = d3.svg.line().x(function(d) { return x(d.packetRequestTime); })
						    	.y(function(d) { return y(d.bandwidth);	});

		
		var div = d3.select("body").append("div")
		.attr("class", "tooltip")
		.style("opacity", 0);
		
		$('#streamGraph').empty();
		var svg = d3.select("#streamGraph").append("svg")
		    .attr("width", width + margin.left + margin.right)
		    .attr("height", height + margin.top + margin.bottom)
		    .attr("style", "margin-left:10px;")
		  .append("g")
		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
		
		$scope.streamGraphData.forEach(function(d) {
			//d.date = parseDate(new Date(d.x));			
//			d.x = reverseParse(new Date(d.x));
			//d.x = reverseParse(parseDate(new Date(d.x)));
//			d.x = parseDate.parse(parseDate(new Date(d.x)));
			d.packetRequestTime = +d.packetRequestTime;
			d.bandwidth = +d.bandwidth;
		});
		
		x.domain(d3.extent($scope.streamGraphData, function(d) { return d.packetRequestTime; }));
		y.domain(d3.extent($scope.streamGraphData, function(d) { return d.bandwidth; }));
		  
		svg.append("g")
	      .attr("class", "x axis")	      
	      .attr("transform", "translate(0," + height + ")")
	      .call(xAxis)
	      .append("text")
			.style("text-anchor", "end")
			.attr("dx", "20em")
			.attr("dy", "2em")
			.attr("class", "legend")
			.text("Timeline");
//			.attr("transform", function(d) {
//			return "rotate(-65)"
//			});
	      

		  svg.append("g")
		      .attr("class", "y axis")		      
		      .call(yAxis)
		    .append("text")
		      .attr("transform", "rotate(-90)")
		      .attr("y", 6)
		      .attr("class", "legend")
		      .attr("dy", "-1.5em")
		      .attr("dx", "-5em")
		      .style("text-anchor", "end")
		      .text("Stream Bitrate");
	
		  svg.append("path")
		      .datum($scope.streamGraphData)
		      .attr("class", "line")
		      .attr("d", line);
		  //svg.addTimeAxis();
		     // .attr("d", x(function(d) { return x(d.x) }));
		  
		  svg.selectAll("dot")
			.data($scope.streamGraphData)
			.enter().append("circle")
			.attr("r", 3.5)
			.attr("cx", function(d) { return x(d.packetRequestTime); })
			.attr("cy", function(d) { return y(d.bandwidth); })
			.on("mouseover", function(d) {
				div.transition()
				.duration(200)
				.style("opacity", .9);
				div.html(parseDate(new Date(d.packetRequestTime)) + "<br/>" + d.bandwidth + "<br/>" + d.filename)
				.style("left", (d3.event.pageX) + "px")
				.style("top", (d3.event.pageY - 28) + "px");
			})
			.on("mouseout", function(d) {
				div.transition()
				.duration(500)
				.style("opacity", 0);
			});
	};
	
	$scope.paintBandwidthChart = function() {
		
		var bw = $scope.bandwidth;
		if (bw == null || typeof(bw) === "undefined" || bw.length == 0) {
			return;
		}
		var margin = {top: 20, right: 20, bottom: 30, left: 50},
	    width = 600 - margin.left - margin.right,
	    height = ht - margin.top - margin.bottom;

//		var parseDate = d3.time.format("%d-%b-%y %H:%M");
		var parseDate = d3.time.format("%a %b %d %Y %H:%M:%S");
		
//		var reverseParse = d3.time.format("%Y%m%d %H:%M:%S").parse;
		var reverseParse = d3.time.format("%d-%b-%y $H:%M").parse;
		var x = d3.time.scale().range([0, width]);
		var minDate = $scope.bandwidth[0].x;
		var maxDate = $scope.bandwidth[$scope.bandwidth.length - 1].x;
		
//		var x = d3.time.scale().domain([minDate, maxDate]).range([0, width]);
		
		/*var m = [80, 80, 80, 80]; 
		var w = 850 - m[1] - m[3]; 
		var h = 400 - m[0] - m[2]; */ 
		
		var y = d3.scale.linear()
		    .range([height, 0]);
	
		var xAxis = d3.svg.axis()
		    .scale(x)
		    .ticks(d3.time.second, 30).tickFormat("");//.tickFormat(d3.time.format("%X")).orient("bottom");
		    
		    
		    //.tickFormat(""); //.tickSize(-height, 0, 0).tickFormat("").ticks(10)
	
		var yAxis = d3.svg.axis()
		    .scale(y)
		    .orient("left").tickFormat(""); //.tickSize(-height, 0, 0).tickFormat("").ticks(10)
	
		var line = d3.svg.line().x(function(d) { return x(d.x); })
						    	.y(function(d) { return y(d.y);	});

		var div = d3.select("body").append("div")
		.attr("class", "tooltip")
		.style("opacity", 0);
		
		$('#bandwidthChart').empty();
		var svg = d3.select("#bandwidthChart").append("svg")
		    .attr("width", width + margin.left + margin.right)
		    .attr("height", height + margin.top + margin.bottom)
		  .append("g")
		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	
		
		$scope.bandwidth.forEach(function(d) {
			d.x = +d.x;
			d.y = +d.y;
		});
		
		x.domain(d3.extent($scope.bandwidth, function(d) { return d.x; }));
		y.domain(d3.extent($scope.bandwidth, function(d) { return d.y; }));
		  
		svg.append("g")
	      .attr("class", "x axis")
//	      .attr("class", "grid")
	      .attr("transform", "translate(0," + height + ")")
	      .call(xAxis)
	      	.append("text")
			.style("text-anchor", "end")			
			.attr("dx", "20em")
			.attr("dy", "2em")
			.attr("opacity" , ".71")
			.attr("class", "legend")
			.text("Timeline")
			;
	      

		  svg.append("g")
		      .attr("class", "y axis")
		      //.attr("class", "grid")
		      .call(yAxis)
		    .append("text")
		      .attr("transform", "rotate(-90)")
		      .attr("y", 6)
		      .attr("class", "legend")
		      .attr("dy", "-1.5em")
		      .attr("dx", "-5em")
		      .style("text-anchor", "end")
		      .text("Bandwidth");
	
		  svg.append("path")
		      .datum($scope.bandwidth)
		      .attr("class", "line")
		      .attr("d", line);
		  //svg.addTimeAxis();
		     // .attr("d", x(function(d) { return x(d.x) }));
		  
		  svg.selectAll("dot")
			.data($scope.bandwidth)
			.enter().append("circle")
			.attr("r", 3.5)
			.attr("cx", function(d) { return x(d.x); })
			.attr("cy", function(d) { return y(d.y); })
			.on("mouseover", function(d) {
				div.transition()
				.duration(200)
				.style("opacity", .9);
				div.html(parseDate(new Date(d.x)) + "<br/>" + d.y)
				.style("left", (d3.event.pageX) + "px")
				.style("top", (d3.event.pageY - 28) + "px");
			})
			.on("mouseout", function(d) {
				div.transition()
				.duration(500)
				.style("opacity", 0);
			});
		  
	}
	

	
	$scope.createStallGraphData = function() {
		
		var sg = $scope.details.streamGraph; 
		
		var actualGraphData = [];
		var stallGraphData = [];
		var stallGraphData2 = [];
		var counterArray = 1;
		
		if (typeof(sg) !== "undefined" && sg !== null && sg.length > 0) {
			
			var packetCount = 1;
			var nextPacketRequestTime = 0;
			for (var i in sg) {		
				
				var t = sg[i];
				var prt = t.packetRequestTime;
				var receivedTime = t.lastPacketReceivedTime;
				
				/**
				 * For the first packet, 
				 * video starts playing after the last packet is received.
				 * So we are considering the receivedTime here as the starting point for both
				 * actual and nextExpectedPackets.
				 */
				var duration = t.duration * 1000;
				if (counterArray === 1) {
					var obj = { "packetNumber" : packetCount , "videoPlayTime" : receivedTime ,  "filename" : t.filename };
					actualGraphData.push(obj);					
					nextPacketRequestTime = receivedTime + duration;
					obj = { "packetNumber" : packetCount , "nextPacketRequestTime" : receivedTime  };
					stallGraphData.push(obj);
					
					stallGraphData2.push({"packetTime" : receivedTime, "value" : 0});
				}
				else {
					
					/**
					 * If the packetRequest time is less than the nextPacket Request Time. 
					 * Then there is a possibility of buffer.
					 * if it is not, then there is a possible problem in the network. 
					 */
					
					var obj = { "packetNumber" : packetCount , "videoPlayTime" : prt ,  "filename" : t.filename };
					actualGraphData.push(obj);
					
					obj = { "packetNumber" : packetCount , "nextPacketRequestTime" : nextPacketRequestTime  };
					stallGraphData.push(obj);					
					stallGraphData2.push({ "packetTime" : prt , "value" : (nextPacketRequestTime - prt)/1000 });
					
					if (prt < nextPacketRequestTime) {
						//nextPacketRequestTime = nextPacketRequestTime - prt + duration;						
						nextPacketRequestTime = nextPacketRequestTime  + duration;						
					}
					else {						
						nextPacketRequestTime = prt + duration;						
					}
					
				}
				
				packetCount++;
				counterArray = counterArray + 1;
			}
			
		}		 
		
		$scope.stallGraphData = stallGraphData;
		$scope.actualGraphData = actualGraphData;
		$scope.stallGraphData2 = stallGraphData2;
		
	};
	
	$scope.createStreamGraphData = function() {
	
		var sg = $scope.details.streamGraph; 
		
		var streamGraphData = [];
		
		if (typeof(sg) !== "undefined" && sg !== null && sg.length > 0) {
			
			for (var i in sg) {		
				var t = sg[i];
				var prt = t.packetRequestTime;
				var obj = { "packetRequestTime" : prt ,  "bandwidth" : t.bandwidth, "filename" : t.filename };		
				streamGraphData.push(obj);				
			}			
		}		 
		
		
		$scope.streamGraphData = streamGraphData;
	};
	
	
	
	$scope.createChartData = function() {
		
		var ts = $scope.details.ts; // ts is an array
		
		var syncErrorDataChart = [];
		var scrambledCountChart = [];
		var malformedCountChart = [];
		var patErrorsCountChart = [];
		var pmtErrorsCountChart = [];
		var nullPacketCountChart = [];
		var unsupportedPMTCountChart = [];
		
		if (typeof(ts) !== "undefined" && ts !== null && ts.length > 0) {
			for (var i in ts) {		
				var t = ts[i];
				var prt = t.packetReceivedTime;
				var fname = t.filename;
				
				var obj = { "x" : prt ,  "y" : t.syncErrorCount , "filename" : fname };				
				syncErrorDataChart.push(obj);
				
				obj = { "x" : prt ,  "y" : t.scrambledPayloadCount , "filename" : fname };
				scrambledCountChart.push(obj);
				
				obj = { "x" : prt ,  "y" : t.malformedPacketCount , "filename" : fname };
				malformedCountChart.push(obj);
				
				obj = { "x" : prt ,  "y" : t.patErrors , "filename" : fname};
				patErrorsCountChart.push(obj);
				
				obj = { "x" : prt ,  "y" : t.pmtErrors , "filename" : fname};
				pmtErrorsCountChart.push(obj);
				
				obj = { "x" : prt ,  "y" : t.nullPacketCount , "filename" : fname};
				nullPacketCountChart.push(obj);
				
				obj = { "x" : prt ,  "y" : t.unsupportedPMT , "filename" : fname};
				unsupportedPMTCountChart.push(obj);
			}			
		}
		
		$scope.syncErrorDataChart = syncErrorDataChart;
		$scope.scrambledCountChart = scrambledCountChart;
		$scope.malformedCountChart = malformedCountChart;
		
		$scope.patErrorsCountChart = patErrorsCountChart;
		$scope.pmtErrorsCountChart = pmtErrorsCountChart;
		$scope.nullPacketCountChart = nullPacketCountChart;
		$scope.unsupportedPMTCountChart = unsupportedPMTCountChart;
		
		
		/** Get Client Bandwidth Now **/
		var tcp = $scope.details.tcp; // tcp is an array
		var bandwidth = [];
		if (typeof(tcp) !== "undefined" && tcp !== null && tcp.length > 0) {
			for (var i in tcp) {		
				var t = tcp[i];
				var prt = t.firstPacketReceivedTime;
				var lprt = t.lastPacketReceivedTime;
				var bytes = t.bytesInPayload;
				var bw = (bytes/((lprt - prt)/1000))/1024; // Total number of KB per second
				
				var obj = { "x" : prt ,  "y" : bw };				
				bandwidth.push(obj);				
			}
		}
		// Dummy 
	/*	bandwidth.push( {"x" : 1446344590694, "y" : 100});
		bandwidth.push( {"x" : 1446344590794, "y" : 110});
		bandwidth.push( {"x" : 1446344590894, "y" : 120});
		bandwidth.push( {"x" : 1446344590994, "y" : 130});
		bandwidth.push( {"x" : 1446344591694, "y" : 140});
		bandwidth.push( {"x" : 1446344592694, "y" : 150});		
		bandwidth.push( {"x" : 1446344593694, "y" : 160});
		bandwidth.push( {"x" : 1446344594694, "y" : 170});
		bandwidth.push( {"x" : 1446344595694, "y" : 180});
		bandwidth.push( {"x" : 1446344596694, "y" : 190});
		
		
		bandwidth.push( {"x" : 1446444590694, "y" : 200});
		bandwidth.push( {"x" : 1446544590694, "y" : 300});
		bandwidth.push( {"x" : 1446644590694, "y" : 150});
		bandwidth.push( {"x" : 1446744590694, "y" : 350});
		bandwidth.push( {"x" : 1446844590694, "y" : 400}); */
		
		
		$scope.bandwidth = bandwidth;
	};
	
});

