package play.ground.servlet;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.google.inject.Inject;

public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener  {

	@Inject
    public HealthCheckRegistry HC_REGISTRY;

    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return HC_REGISTRY;
    }
	
	
}
