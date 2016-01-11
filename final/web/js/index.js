$(document).ready(function() 
{
	
	$.ajax({									  
		url: 'database.php',				  
		data:'type=0',
		dataType: 'json',				//data format	  
		success: function(data)		  //on recieve of reply
		{	
			var temp ="";		
			for (var i = 0; i <data.length; i++) 
			{
				temp+='<option value="'+(i+1)+'">'+data[i][0]+'</option>'
			}
			//console.log(temp);
			$("#count").html(temp);
  		} 
	});
	$('#count').click(function(){
        var count2 = $('#count').val(); //抓到測驗對應ID
	    //console.log(count);
	    $.ajax({									  
		url: 'database.php',				  
		data:'type=1&count='+count2,
		dataType: 'json',				//data format	  
		success: function(data)		  //on recieve of reply
		{	
			//console.log(data);
			var time = [];
			var shock_array = [];
			var val = [];
			var val_shock = [];
			var val_light = [];
			var count = 0;
			for (var i = 0; i <data.length; i++) 
			{
				time.push(data[i][5]);
				shock_array.push(data[i][2]);
                soundvalue = parseInt(data[i][1]);
                //shockvalue = parseInt(data[i][2]);
                if(data[i][2]>0)
                {
                	shockvalue = 1;
                	count=0;
                }	
            	else
            	{
            		shockvalue = 0;
            	}
            	lightvalue = parseInt(data[i][3]);
                //onoff = parseInt(data[i][4]);
                //if(onoff == 'O')
                
                if(count>=2)
                {
                	val.push({ y: soundvalue, color: 'green' });
                    val_shock.push({ y: shockvalue, color: 'red' });
                    val_light.push({ y: lightvalue, color: 'blue' });
                }
                else
                {
                	val.push({ y: soundvalue, color: 'red' });
                	val_shock.push({ y: shockvalue, color: 'purple' });
                	val_light.push({ y: lightvalue, color: 'yellow' });
                }
                count++;
                //console.log(count);     
			}
			
			$('#time_sound').highcharts({
				title: {
					text: 'Sound Sensor data',
					x: -20 //center
				},
				subtitle: {
					text: '', //顯示地點
					x: -20
				},
				xAxis: {
					title: {
					text: 'Time'
					},
					categories: time,
					labels:{
						enabled: false,
					}
				
				},
				yAxis: {
					title: {
						text: 'Sound'
					}
				},
				series: [{
					name: 'Sensor-sound',
					data: val
				}]
				});
			$('#time_shock').highcharts({
				title: {
					text: 'Shock Sensor data',
					x: -20 //center
				},
				subtitle: {
					text: '', //顯示地點
					x: -20
				},
				xAxis: {
					title: {
					text: 'Time'
					},
					categories: time,
					labels:{
						enabled: false,
					}
				
				},
				yAxis: {
					title: {
						text: 'Shock'
					}
				},
				series: [{
					name: 'Sensor-shock',
					data: val_shock,
					color: 'green'
				}]
				});
			$('#time_light').highcharts({
				title: {
					text: 'Light Sensor data',
					x: -20 //center
				},
				subtitle: {
					text: '', //顯示地點
					x: -20
				},
				xAxis: {
					title: {
					text: 'Time'
					},
					categories: time,
					labels:{
						enabled: false,
					}
				
				},
				yAxis: {
					title: {
						text: 'Light'
					}
				},
				series: [{
					name: 'Sensor-light',
					data: val_light,
					color: 'black'
				}]
				});
  		} 

	});
	$.ajax({							  
		url: 'database.php',				  
		data:'type=3&count='+count2,
		dataType: 'json',				//data format	  
		success: function(data)		  //on recieve of reply
		{	
			//console.log(data);
			var temp ="";
			if(data<=10)
				temp="<h3> Rate: &nbsp;"+data+" &nbsp;&nbsp;GOOD</h3>";
			else if(data<20)		
				temp="<h3> Rate: &nbsp;"+data+" &nbsp;&nbsp;SOSO</h3>";
			else
				temp="<h3> Rate: &nbsp;"+data+" &nbsp;&nbsp;&nbsp;BAD</h3>";
			$('#sleep_analyze').html(temp);
  		} 
	});
    });

});