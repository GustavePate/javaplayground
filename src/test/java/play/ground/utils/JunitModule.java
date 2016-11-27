
package play.ground.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.helper.ConfigurationReader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class JunitModule extends AbstractModule {

    private final static Logger LOG = LoggerFactory.getLogger(JunitModule.class);

    @Override
    protected void configure() {
        bind(World.class).in(Scopes.SINGLETON);
		Config conf = ConfigFactory.load("application");
		bind(Config.class).annotatedWith(Names.named("conf")).toInstance(conf);

    }

    /**
     * Provider de fichier de properties
     * 
     * @return Properties
     */

    @Named("props.application")
    @Provides
    @Singleton
    public Properties provideApplicationProperties() {
        return ConfigurationReader.getProperties("application");
    }

    @Named("props.test")
    @Provides
    @Singleton
    public Properties provideTestProperties() {
        return ConfigurationReader.getProperties("test");
    }
}
