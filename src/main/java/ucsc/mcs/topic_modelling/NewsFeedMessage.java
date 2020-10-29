package ucsc.mcs.topic_modelling;

import java.io.Serializable;
import java.util.Date;

public class NewsFeedMessage implements Serializable
{
	private long id;

	private String url;
	private String title;
	private Date date;
	private String newsText;

	public NewsFeedMessage()
	{
	}

	public NewsFeedMessage( String url, String title, Date date )
	{
		super();
		this.url = url;
		this.title = title;
		this.date = date;
	}

	public long getId()
	{
		return id;
	}

	public void setId( long id )
	{
		this.id = id;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl( String url )
	{
		this.url = url;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle( String title )
	{
		this.title = title;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate( Date date )
	{
		this.date = date;
	}

	public String getNewsText()
	{
		return newsText;
	}

	public void setNewsText( String newsText )
	{
		this.newsText = newsText;
	}

}
