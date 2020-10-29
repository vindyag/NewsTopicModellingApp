function submitText()
{
	var text = $("#newsText").val();
	var url = "/TopicInference";
	$.ajax(
			{
				url: url,				
				data:"inputText="+ encodeURIComponent( text ),
				type: 'POST',
				success:  function(result)
							{
								successFunction(result);
							},
				error : function(result)
							{
								errorFunction(result);
							}
			}
		);
}

function clearText()
{
	$("#newsText").val('');
	$("#resultsDiv").hide();
}

function successFunction( result )
{
	var jsonObject = jQuery.parseJSON( result ); 
	if( jsonObject.Status == 1 )
	{
		$("#businessAndFinance1").html( jsonObject.BUSINESS_AND_FINANACE_1 + "%");
		$("#politics").html( jsonObject.POLITICS + "%");
		$("#entertainment").html( jsonObject.ENTERTAINMENT + "%");
		$("#general").html( jsonObject.GENERAL+ "%");
		$("#travelAndTourism").html( jsonObject.TRAVEL_AND_TOURISM + "%");
		$("#sports").html( jsonObject.SPORTS + "%");
		$("#businessAndFinance2").html( jsonObject.BUSINESS_AND_FINANACE_2 + "%");
		$("#resultsDiv").show();
		$("#errorDiv").hide();
	}
	else
	{
		errorFunction( result );
	}
}

function errorFunction( result )
{
	var jsonObject = jQuery.parseJSON( result ); 
	$("#newsText").val('');
	$("#resultsDiv").hide();
	$("#errorDiv").show();
	$("#errorText").text(jsonObject.msg );
}
