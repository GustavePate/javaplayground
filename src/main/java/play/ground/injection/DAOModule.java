package play.ground.injection;

import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.impl.ExempleDAODefault;
import play.ground.dao.exemple.impl.ExempleDAOMock;

public class DAOModule extends AbstractModule {
	
	static final Logger log = LoggerFactory.getLogger(DAOModule.class);
	
	protected void configure() {		
		
		//makes properties available via @Inject @Named("property.name")
		Properties props = null;
		try {
			props = loadProperties();
			Names.bindProperties(binder(),props);
		} catch (Exception e) {
			log.error("pb chargement conf", e);
		}
		
		// make Config object available from @Inject @Named("conf)
		
		Config conf = ConfigFactory.load("application");
		bind(Config.class).annotatedWith(Names.named("conf")).toInstance(conf);
		
		// Bind DAOs
		if ("true".equalsIgnoreCase(props.getProperty("mock.exempledao", "false")) || "true".equalsIgnoreCase(props.getProperty("mock.all.dao", "false")) ){
			log.info("ExempleDAO mode mock");
			bind(ExempleDAO.class).to(ExempleDAOMock.class);
		}else{
			log.info("ExempleDAO mode regular");
			bind(ExempleDAO.class).to(ExempleDAODefault.class);
		}
	}
	
//  @Provides
//	@Named("FakeDAO")
//    FakeDAO provideFakeDAO() {
//    	FakeDAO dao = new FakeDAODefault();
//    	return dao;
//    }
	private static Properties loadProperties() throws Exception {
		Properties properties = new Properties();
		ClassLoader loader = DAOModule.class.getClassLoader();
		URL url = loader.getResource("application.properties");
		properties.load(url.openStream());
		return properties;
	}	
}
