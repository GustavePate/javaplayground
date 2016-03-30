package play.ground.servlet.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import com.google.inject.Inject;

public class MetricsServletContextListener extends MetricsServlet.ContextListener  {

	static final Logger log = LoggerFactory.getLogger(MetricsServletContextListener.class);
	
	
	@Inject
    public MetricRegistry METRIC_REGISTRY;

	@Inject
	private MetricsServletContextListener(){
		//log.debug("MetricsServletContextListener mr" + METRIC_REGISTRY.toString());
	}
	
    @Override
    protected MetricRegistry getMetricRegistry() {
		log.debug("MetricsServletContextListener mr" + METRIC_REGISTRY.toString());
        return METRIC_REGISTRY;
    }
	
	
}
