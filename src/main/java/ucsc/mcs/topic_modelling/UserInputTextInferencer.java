package ucsc.mcs.topic_modelling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONObject;

import cc.mallet.pipe.SerialPipes;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import ucsc.mcs.topic_modelling.entity.NewsFeedMessage;

public class UserInputTextInferencer
{
	/**
	 * Infer topic distribution for the provided test news instance and create and return a JSONObject with topic distribution information
	 * 
	 * @param noOfTopics
	 *            Number of topics
	 * @param feedMessage
	 *            NewsFeedMessage instance
	 * @return JSONObject containing topic labels and the respective topic probabilities
	 * @throws Exception
	 */
	public JSONObject getJSONForTopicDistribution( int noOfTopics, NewsFeedMessage feedMessage ) throws Exception
	{
		JSONObject jsonObject = new JSONObject();

		// Load the model
		File modelFile = new File( "model", "mallet-lda-model" + noOfTopics + ".ser" );
		ParallelTopicModel model = ParallelTopicModel.read( modelFile );

		// Load the pipe
		File pipesFile = new File( "model", "mallet-lda-pipes" + noOfTopics + ".ser" );

		SerialPipes serialPipes = ( SerialPipes ) loadFromFile( pipesFile );

		// if model and pipe is properly loaded
		if ( model != null && serialPipes != null )
		{
			System.out.print( "Successfully read the model and pipe list\n" );
			System.out.println( "Model LogLikelihood : " + model.modelLogLikelihood() );

			// Create instance list
			InstanceList instanceList = new InstanceList( serialPipes );
			String newsText = feedMessage.getNewsText();
			if ( newsText != null && !newsText.isEmpty() )
			{
				instanceList.addThruPipe( new Instance( feedMessage.getNewsText(), null, feedMessage.getTitle(), feedMessage.getUrl() ) );

				// Estimate the topic distribution of the instance, given the current Gibbs state.
				TopicInferencer inferencer = model.getInferencer();
				Instance instance = instanceList.get( 0 );

				double[] topicDistribution = inferencer.getSampledDistribution( instance, 0, 1, 5 );
				Map<Integer, Double> topicDistributionMap = new HashMap<Integer, Double>();

				DecimalFormat df = new DecimalFormat( "###.####" );
				Writer writer = new FileWriter( "Output_topics_on_test_instances_with_model" + noOfTopics + ".txt" );
				writer.write( "Model LogLikelihood : " + model.modelLogLikelihood() );
				for ( int topic = 0; topic < noOfTopics; topic++ )
				{
					double topicProbability = Double.valueOf( df.format( topicDistribution[topic] * 100 ) );
					System.out.println( "\nTopic probability for topic " + topic + " : " + topicProbability );
					writer.write( "\nTopic probability for topic " + topic + " : " + topicProbability );
					topicDistributionMap.put( topic, topicProbability );
				}
				writer.close();

				// Fill the JSONObject
				Iterator<Entry<Integer, Double>> topicIterator = topicDistributionMap.entrySet().iterator();
				while ( topicIterator.hasNext() )
				{
					Map.Entry<Integer, Double> pair = topicIterator.next();
					jsonObject.put( TopicLabel.getTopicLabel( pair.getKey() ), pair.getValue() );
				}
			}
		}

		return jsonObject;
	}

	/**
	 * Infer topic distribution for the news data in the data source and create and return a JSONObject with topic assignment information key :
	 * 'results' , value : List<NewsArticle>
	 * 
	 * @param noOfTopics
	 *            Number of topics
	 * @param feedMessages
	 *            List of NewsFeedMessage instances
	 * @return JSONObject key : 'results' , value : List<NewsArticle>
	 * @throws Exception
	 */
	public JSONObject getJSONForTopicDistribution( int noOfTopics, List<NewsFeedMessage> feedMessages ) throws Exception
	{
		JSONObject jsonObject = new JSONObject();

		// Load the model
		File modelFile = new File( "model", "mallet-lda-model" + noOfTopics + ".ser" );
		ParallelTopicModel model = ParallelTopicModel.read( modelFile );

		// Load the pipe
		File pipesFile = new File( "model", "mallet-lda-pipes" + noOfTopics + ".ser" );

		SerialPipes serialPipes = ( SerialPipes ) loadFromFile( pipesFile );

		// if model and pipe is properly loaded
		if ( model != null && serialPipes != null )
		{
			System.out.print( "Successfully read the model and pipe list\n" );
			System.out.println( "Model LogLikelihood : " + model.modelLogLikelihood() );

			// Create instance list
			InstanceList instanceList = new InstanceList( serialPipes );
			for ( NewsFeedMessage feedMessage : feedMessages )
			{
				String newsText = feedMessage.getNewsText();
				if ( newsText != null && !newsText.isEmpty() )
				{
					instanceList.addThruPipe( new Instance( feedMessage.getNewsText(), null, feedMessage.getTitle(), feedMessage.getUrl() ) );
				}
			}

			Map<Integer, List<NewsArticle>> newsTopicMap = new HashMap<Integer, List<NewsArticle>>();
			initializeNewsTopicMap( newsTopicMap );

			TopicInferencer inferencer = model.getInferencer();
			Writer writer = new FileWriter( "Output_topics_on_test_instances_with_model" + noOfTopics + ".txt" );

			for ( Instance instance : instanceList )
			{
				double[] topicDistribution = inferencer.getSampledDistribution( instance, 0, 1, 5 );
				Map<Integer, Double> topicDistributionMap = new HashMap<Integer, Double>();

				DecimalFormat df = new DecimalFormat( "###.####" );
				writer.write( "Model LogLikelihood : " + model.modelLogLikelihood() );
				for ( int topic = 0; topic < noOfTopics; topic++ )
				{
					double topicProbability = Double.valueOf( df.format( topicDistribution[topic] * 100 ) );
					System.out.println( "\nTopic probability for topic " + topic + " : " + topicProbability );
					writer.write( "\nTopic probability for topic " + topic + " : " + topicProbability );
					topicDistributionMap.put( topic, topicProbability );
				}

				Map<Integer, Double> sortedTopicDistMap = sortMapByValue( topicDistributionMap );
				Map.Entry<Integer, Double> initialEntry = sortedTopicDistMap.entrySet().iterator().next();
				System.out.println( "Key: " + initialEntry.getKey() );
				System.out.println( "Value: " + initialEntry.getValue() );
				NewsArticle newsArticle = new NewsArticle( TopicLabel.getTopicLabel( initialEntry.getKey() ), ( String ) instance.getSource(), ( String ) instance.getName(), initialEntry.getValue() );
				newsTopicMap.get( initialEntry.getKey() ).add( newsArticle );
			}
			writer.close();
			sortNewsOnTopicProbability( newsTopicMap.values() );
			jsonObject.put( "results", newsTopicMap );
		}

		return jsonObject;
	}

	private void sortNewsOnTopicProbability( Collection<List<NewsArticle>> newsTopicArticles) 
	{
		for ( List<NewsArticle> artcles : newsTopicArticles ) 
		{
			Collections.sort(artcles);
		}
	}
	
	private Map<Integer, Double> sortMapByValue( Map<Integer, Double> unsortedMap )
	{
		Map<Integer, Double> sortedMap = new TreeMap( new ValueComparator( unsortedMap ) );
		sortedMap.putAll( unsortedMap );
		return sortedMap;
	}

	private Map<Integer, List<NewsArticle>> initializeNewsTopicMap( Map<Integer, List<NewsArticle>> newsTopicMap )
	{
		newsTopicMap.put( 0, new ArrayList<NewsArticle>() );
		newsTopicMap.put( 1, new ArrayList<NewsArticle>() );
		newsTopicMap.put( 2, new ArrayList<NewsArticle>() );
		newsTopicMap.put( 3, new ArrayList<NewsArticle>() );
		newsTopicMap.put( 4, new ArrayList<NewsArticle>() );
		newsTopicMap.put( 5, new ArrayList<NewsArticle>() );
		newsTopicMap.put( 6, new ArrayList<NewsArticle>() );
		return newsTopicMap;
	}

	class ValueComparator implements Comparator
	{

		Map map;

		public ValueComparator( Map map )
		{
			this.map = map;
		}

		public int compare( Object key1, Object key2 )
		{
			Comparable value1 = ( Comparable ) map.get( key1 );
			Comparable value2 = ( Comparable ) map.get( key2 );
			return value2.compareTo( value1 );
		}
	}

	/**
	 * de-serialize the file and create the object representation and return
	 * 
	 * @param serializedFile
	 *            File to de-serialize
	 * @return Object
	 */
	private Object loadFromFile( File serializedFile )
	{
		try
		{
			ObjectInputStream oos = new ObjectInputStream( new FileInputStream( serializedFile ) );
			Object obj = oos.readObject();
			oos.close();
			return obj;
		}
		catch ( IOException e )
		{
			System.err.println( "Problem reading file " + serializedFile + ": " + e );
		}
		catch ( ClassNotFoundException e )
		{
			e.printStackTrace();
		}
		return null;

	}

}
