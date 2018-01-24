function getMotorData(){
	var arr = [];

	$.get("/getServerData", function(data) {
    	console.log($.parseJSON(data))

	    for(var i = 0; i < 5; i++){
	    	var m = $.parseJSON(data)[i];
	    	arr.push(m);
	    }
	})
	
	return arr;
}

function record() {
  // ajax the JSON to the server
  $.ajax({
    url: "/receiver",
    method: "POST",
    success: function(response){

  }});
}

