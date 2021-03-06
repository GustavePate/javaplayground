package play.ground.injection;

import com.google.inject.servlet.ServletModule;

import play.ground.servlet.AkkaServlet;
import play.ground.servlet.IndexServlet;
import play.ground.servlet.ExempleServlet;
	
public class ApplicationServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(AkkaServlet.class);
		bind(ExempleServlet.class);
		bind(IndexServlet.class);

		serve("/api/exemple").with(ExempleServlet.class);
		serve("/api/akka").with(AkkaServlet.class);
		serve("/api/*").with(IndexServlet.class);
	}
}
