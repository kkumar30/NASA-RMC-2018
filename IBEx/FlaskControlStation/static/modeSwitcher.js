function teleopMode(){
	$.ajax({
    url: "/teleop",
    method: "POST",
    success: function(response){
    	currMode = "<h2>Mode: Teleoperated</h2>"
  }});

}

function autonomousMode(){
	$.ajax({
    url: "/autonomous",
    method: "POST",
    success: function(response){
    	currMode = "<h2>Mode: Autonomous</h2>"

  }});

}

function robotStop(){
	$.ajax({
    url: "/stopProgram",
    method: "POST",
    success: function(response){
    	currMode = "<h2>Mode: Stopped</h2>"

  }});

}