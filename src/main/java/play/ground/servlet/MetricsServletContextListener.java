package play.ground.servlet;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import com.google.inject.Inject;

public class MetricsServletContextListener extends MetricsServlet.ContextListener  {

	@Inject
    public MetricRegistry METRIC_REGISTRY;

    @Override
    protected MetricRegistry getMetricRegistry() {
        return METRIC_REGISTRY;
    }
	
	
}
