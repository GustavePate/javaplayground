package play.ground.servlet.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilterContextListener;
import com.google.inject.Inject;

public class InstFilterContextListener extends InstrumentedFilterContextListener {
    
	@Inject
    public MetricRegistry METRIC_REGISTRY;
	
    protected MetricRegistry getMetricRegistry() {
        return METRIC_REGISTRY;
    }
}
