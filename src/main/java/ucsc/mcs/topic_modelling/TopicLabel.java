package ucsc.mcs.topic_modelling;

public class TopicLabel
{
	private static String BUSINESS_AND_FINANACE_1 = "BUSINESS_AND_FINANACE_1";
	private static String BUSINESS_AND_FINANACE_2 = "BUSINESS_AND_FINANACE_2";
	private static String POLITICS = "POLITICS";
	private static String ENTERTAINMENT = "ENTERTAINMENT";
	private static String GENERAL = "GENERAL";
	private static String TRAVEL_AND_TOURISM = "TRAVEL_AND_TOURISM";
	private static String SPORTS = "SPORTS";

	/**
	 * Given a valid topic number, return the assigned Topic Label
	 * 
	 * @param topicNumber
	 *            Number of topic
	 * @return Assigned Topic Label
	 */
	public static String getTopicLabel( int topicNumber )
	{
		switch ( topicNumber )
		{
			case 0:
				return BUSINESS_AND_FINANACE_1;
			case 1:
				return POLITICS;
			case 2:
				return ENTERTAINMENT;
			case 3:
				return GENERAL;
			case 4:
				return TRAVEL_AND_TOURISM;
			case 5:
				return SPORTS;
			case 6:
				return BUSINESS_AND_FINANACE_2;
			default:
				return null;
		}
	}
}
