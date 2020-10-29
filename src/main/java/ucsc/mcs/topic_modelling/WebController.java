package ucsc.mcs.topic_modelling;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ucsc.mcs.topic_modelling.UserInputTextInferencer;
import ucsc.mcs.topic_modelling.entity.NewsFeedMessage;
import ucsc.mcs.topic_modelling.repository.NewsFeedRepository;

@RestController
public class WebController
{
	@Autowired
	private NewsFeedRepository newsFeedRepository;

	/**
	 * @param inputText
	 *            input news text
	 * @return JSON representing topic distribution for the specified news text
	 */
	// @RequestMapping(value = "/TopicInference", method = RequestMethod.GET)
	@RequestMapping(value = "/TopicInference", method = RequestMethod.POST)
	public String getTopicDistribution( @RequestParam("inputText") String inputText )
	{
		System.out.println( "Input: " + inputText );

		NewsFeedMessage newsFeedMessage = new NewsFeedMessage();
		newsFeedMessage.setNewsText( inputText );
		newsFeedMessage.setTitle( "User Input" );
		newsFeedMessage.setUrl( "User Input" );

		UserInputTextInferencer inferencer = new UserInputTextInferencer();

		JSONObject jsonObject = null;
		try
		{
			jsonObject = inferencer.getJSONForTopicDistribution( 7, newsFeedMessage );
			if ( jsonObject.length() == 7 )
			{
				jsonObject.put( "Status", 1 );
			}
			else
			{
				jsonObject.put( "Status", -1 );
				jsonObject.put( "msg", "Error occured while processing the news text..." );
			}
		}
		catch ( Exception e )
		{
			jsonObject = new JSONObject();
			jsonObject.put( "Status", -1 );
			jsonObject.put( "msg", "Error occured while processing the news text..." + e.getMessage() );
		}

		System.out.println( "JSON response: " + jsonObject.toString() );
		return jsonObject.toString();
	}

	/**
	 * @return JSON representing topic assignments for the news text in data source
	 */
	@RequestMapping(value = "/TopicAssignment", method = RequestMethod.POST)
	public @ResponseBody String getTopicAssignments()
	{
		List<NewsFeedMessage> newsFeedMessages = new ArrayList<NewsFeedMessage>();
		newsFeedMessages = ( List<NewsFeedMessage> ) newsFeedRepository.findAll();

		UserInputTextInferencer inferencer = new UserInputTextInferencer();

		JSONObject jsonObject = null;
		try
		{
			jsonObject = inferencer.getJSONForTopicDistribution( 7, newsFeedMessages );
			System.out.println( jsonObject.toString() );
			jsonObject.put( "Status", 1 );
		}
		catch ( Exception e )
		{
			jsonObject = new JSONObject();
			jsonObject.put( "Status", -1 );
			jsonObject.put( "msg", "Error occured while processing the news text..." + e.getMessage() );
		}

		System.out.println( "JSON response: " + jsonObject.toString() );
		return jsonObject.toString();
	}
		
	@RequestMapping(value = "/topicModeller", method = RequestMethod.GET)
	public ModelAndView viewTopicAssignments()
	{
		ModelAndView model = new ModelAndView();
		model.setViewName( "topics" );
		return model;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index()
	{
		ModelAndView model = new ModelAndView();
		model.setViewName( "index" );
		return model;
	}	
	

}