package playground.yo.injection;

import com.google.inject.servlet.ServletModule;

import playground.yo.servlet.IndexServlet;
import playground.yo.servlet.AkkaServlet;
import playground.yo.servlet.YoServlet;
	
public class ApplicationServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(AkkaServlet.class);
		bind(YoServlet.class);
		bind(IndexServlet.class);

		serve("/exemple").with(YoServlet.class);
		serve("/akka").with(AkkaServlet.class);
		serve("/*").with(IndexServlet.class);
	}
}
