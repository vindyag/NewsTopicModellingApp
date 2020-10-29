
$( document ).ready( viewTestData() );

function viewTestData()
{		
	var url = "/TopicAssignment";
	$.ajax(
			{
				url: url,				
				type: 'POST',
				success:  function(result)
							{
								successOnTestData(result);
							},
				error : function(result)
							{
								errorOnTestData(result);
							}
			}
		);
}

function successOnTestData( result )
{
	var jsonObject = jQuery.parseJSON( result );
	if( jsonObject.Status == 1 )
	{
		var newsTopicMap = jsonObject.results;
		
		//Business And Finance News
		var buzAndFinanceNews1 = newsTopicMap["0"];
		var buzAndFinanceNews2 = newsTopicMap["6"];
		var buzAndFinanceNews = buzAndFinanceNews1.concat(buzAndFinanceNews2);
		for ( i = 0; i < buzAndFinanceNews.length; i++ ) 
		{			
			var buzNFinanceId = "#buzNFinance_"+i;
			$(buzNFinanceId).text( buzAndFinanceNews[i].newsTitle );
			$(buzNFinanceId).attr("href", buzAndFinanceNews[i].newsUrl)			
		}
		
		//Political News
		var politicsNews = newsTopicMap["1"];
		for ( i = 0; i < politicsNews.length; i++ ) 
		{			
			var politicsId = "#politics_"+i;
			$(politicsId).text( politicsNews[i].newsTitle );
			$(politicsId).attr("href", politicsNews[i].newsUrl)			
		}
		
		//Entertainment
		var entertainmentNews = newsTopicMap["2"];
		for ( i = 0; i < entertainmentNews.length; i++ ) 
		{			
			var entertainmentId = "#entertainment_"+i;
			$(entertainmentId).text( entertainmentNews[i].newsTitle );
			$(entertainmentId).attr("href", entertainmentNews[i].newsUrl)			
		}
		
		//General
		var generalNews = newsTopicMap["3"];
		for ( i = 0; i < generalNews.length; i++ ) 
		{			
			var generalId = "#general_"+i;
			$(generalId).text( generalNews[i].newsTitle );
			$(generalId).attr("href", generalNews[i].newsUrl)			
		}
		
		//Travel and Tourism
		var travelNTourismNews = newsTopicMap["4"];
		for ( i = 0; i < travelNTourismNews.length; i++ ) 
		{			
			var travelNTourismId = "#travelNTourism_"+i;
			$(travelNTourismId).text( travelNTourismNews[i].newsTitle );
			$(travelNTourismId).attr("href", travelNTourismNews[i].newsUrl)			
		}
		
		//Sports
		var sportsNews = newsTopicMap["5"];
		for ( i = 0; i < sportsNews.length; i++ ) 
		{			
			var sportsId = "#sports_"+i;
			$(sportsId).text( sportsNews[i].newsTitle );
			$(sportsId).attr("href", sportsNews[i].newsUrl)			
		}
	}
	else
	{
		errorOnTestData( result );
	}
}

function errorOnTestData( result )
{
	
}

