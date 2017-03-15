package play.ground.mapstruct;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.ground.mapstruct.dto.FromDTO;
import play.ground.mapstruct.dto.ToDTO;
import play.ground.utils.AbstractTest;
import static org.assertj.core.api.Assertions.*;

public class MapStructTest extends AbstractTest{

    private final static Logger LOG = LoggerFactory.getLogger(MapStructTest.class);
	
	public class ThisTestModule extends AbstractModule {
		  protected void configure() {
			  
			  	Config conf = ConfigFactory.load("application");
				bind(Config.class).annotatedWith(Names.named("conf")).toInstance(conf);
				
		  }
	}
	
		
	@Test
	@Ignore
	public void test_struct(){
		try{

			Injector injector = Guice.createInjector(new ThisTestModule());
			injector.injectMembers(this);
			//ToDTO tdto = mapper.fromTo(new FromDTO());
			ToDTO tdto  = FromToMapper.INSTANCE.fromTo(new FromDTO());
			assertThat(tdto.age).isEqualTo(12);
			assertThat(tdto.name).isEqualTo("toto");
			assertThat(tdto.weight).isEqualTo(55.5);
			LOG.info("ok");
		}catch(Throwable e){
			LOG.error("arg",e);
			throw e;
		}
		
		
	}

}
