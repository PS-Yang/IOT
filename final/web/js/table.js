$(document).ready(function() 
{
	$.ajax({									  
		url: 'database.php',				  
		data:'type=2',
		dataType: 'json',				//data format	  
		success: function(data)		  //on recieve of reply
		{	
			//console.log(data);
			var temp ="";		
			for (var i = 0; i <= data.length; i++) 
			{
				//console.log(data[i][0]);
				//console.log(data[i][1]);
				if(data[i][1]<=10)
				{
					temp="<tr> <th scope='row'>"+data[i][0]+
					"</th> <td>"+data[i][1]+"<td>GOOD</td></td> </tr>";
				}	
				else if(data[i][1]<=20)
				{
					temp="<tr class=\"info\"> <th scope='row'>"+data[i][0]+
					"</th> <td>"+data[i][1]+"<td>SO-SO</td></td> </tr>";
				}	
				else 
				{
					temp="<tr class=\"danger\"> <th scope='row'>"+data[i][0]+
					"</th> <td>"+data[i][1]+"<td>BAD</td></td> </tr>";
				}	
				
				//console.log(temp);
				$('#table_analyze').append(temp);
			}
			//console.log(temp);
			//$('#table_analyze').append(temp);
  		} 
	});
});