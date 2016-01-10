$(document).ready(function() 
{
	$.ajax({									  
		url: 'database.php',				  
		data:'type=0',
		dataType: 'json',				//data format	  
		success: function(data)		  //on recieve of reply
		{	
			
  		} 
	});
});