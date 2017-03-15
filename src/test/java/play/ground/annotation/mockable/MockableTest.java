package play.ground.annotation.mockable;

import org.junit.Ignore;
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
import play.ground.core.dao.mock.Mockable;
import play.ground.core.dao.mock.annotation.MockableInterceptor;
import play.ground.core.dao.mock.business.JSONMockManager;

public class MockableTest {

	static final Logger LOG = LoggerFactory.getLogger(MockableTest.class);

	
	
	public class ThisTestModule extends AbstractModule {
		  protected void configure() {
			  
			  	Config conf = ConfigFactory.load("application");
				bind(Config.class).annotatedWith(Names.named("conf")).toInstance(conf);
				
				requestStaticInjection(JSONMockManager.class);
			  
			    MockableInterceptor mockableInter = new MockableInterceptor();
			    requestInjection(mockableInter);
			    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Mockable.class), mockableInter);
		  }
	}
	
	
	@Test
	@Ignore
	public void doit(){
		
		Injector injector = Guice.createInjector(new ThisTestModule());
				
		YoupiDAO dao = injector.getInstance(YoupiDAO.class);
		
		for (int i=0;i<10;i++){
			dao.ya("super test");
		}
		
	}
	
	
	
}
