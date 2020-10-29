package ucsc.mcs.topic_modelling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class WebApplicationMain extends SpringBootServletInitializer
{

	@Override
	protected SpringApplicationBuilder configure( SpringApplicationBuilder application )
	{
		return application.sources( WebApplicationMain.class );
	}

	public static void main( String[] args ) throws Exception
	{
		SpringApplication.run( WebApplicationMain.class, args );
	}

}
