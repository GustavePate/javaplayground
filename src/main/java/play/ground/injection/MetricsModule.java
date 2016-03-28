package play.ground.injection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.json.HealthCheckModule;
import com.google.inject.AbstractModule;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;

public class MetricsModule extends AbstractModule {

	static final Logger log = LoggerFactory.getLogger(DAOModule.class);
	
	protected void configure() {		
		MetricRegistry mr = new MetricRegistry();
		bind(MetricRegistry.class).toInstance(mr);
		
		HealthCheckRegistry hr = new HealthCheckRegistry();
		bind(HealthCheckRegistry.class).toInstance(hr);
		
		this.install(new MetricsInstrumentationModule(mr));
		//this.install(new HealthCheckModule());
	}
}
