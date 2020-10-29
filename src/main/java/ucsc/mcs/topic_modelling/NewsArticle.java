package ucsc.mcs.topic_modelling;

public class NewsArticle implements Comparable<NewsArticle>
{
	private String topicLabel;
	private String newsUrl;
	private String newsTitle;
	private double topicProbability;

	public NewsArticle( String topicLabel, String newsUrl, String newsTitle, double topicProbability )
	{
		super();
		this.topicLabel = topicLabel;
		this.newsUrl = newsUrl;
		this.newsTitle = newsTitle;
		this.topicProbability = topicProbability;
	}

	public String getTopicLabel()
	{
		return topicLabel;
	}

	public void setTopicLabel( String topicLabel )
	{
		this.topicLabel = topicLabel;
	}

	public String getNewsUrl()
	{
		return newsUrl;
	}

	public void setNewsUrl( String newsUrl )
	{
		this.newsUrl = newsUrl;
	}

	public String getNewsTitle()
	{
		return newsTitle;
	}

	public void setNewsTitle( String newsTitle )
	{
		this.newsTitle = newsTitle;
	}

	public double getTopicProbability()
	{
		return topicProbability;
	}

	public void setTopicProbability( double topicProbability )
	{
		this.topicProbability = topicProbability;
	}

    @Override
    public int compareTo( NewsArticle o )
    {
        return topicProbability < o.getTopicProbability() ? 1 : topicProbability > o.getTopicProbability()? -1 : 0;
    }
}
