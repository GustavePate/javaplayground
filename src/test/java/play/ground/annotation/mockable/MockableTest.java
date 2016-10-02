package play.ground.annotation.mockable;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.ground.annotation.mockable.object.YoupiDAO;
import play.ground.dao.meta.JSONMockManager;
import play.ground.dao.meta.Mockable;
import play.ground.dao.meta.MockableDAO;
import play.ground.log4j2.KibanaAppenderTest;

public class MockableTest {

	static final Logger LOG = LoggerFactory.getLogger(MockableTest.class);

	
	
	public class ThisTestModule extends AbstractModule {
		  protected void configure() {
			  
			  	Config conf = ConfigFactory.load("application");
				bind(Config.class).annotatedWith(Names.named("conf")).toInstance(conf);
				
				requestStaticInjection(JSONMockManager.class);
			  
			    MockableDAO mockableDAO = new MockableDAO();
			    requestInjection(mockableDAO);
			    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Mockable.class), mockableDAO);
		  }
	}
	
	
	@Test
	public void doit(){
		
		Injector injector = Guice.createInjector(new ThisTestModule());
				
		YoupiDAO dao = injector.getInstance(YoupiDAO.class);
		
		for (int i=0;i<10;i++){
			dao.ya("super test");
		}
		
	}
	
	
	
}
