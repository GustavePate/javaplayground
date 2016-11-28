
package play.ground.injection;

import java.lang.management.ManagementFactory;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.google.inject.AbstractModule;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;

import play.ground.servlet.metrics.HealthCheckServletContextListener;
import play.ground.servlet.metrics.InstFilterContextListener;
import play.ground.servlet.metrics.MetricsServletContextListener;

public class MetricsModule extends AbstractModule {

    static final Logger log = LoggerFactory.getLogger(MetricsModule.class);

    private void registerAll(String prefix, MetricSet metricSet, MetricRegistry registry) {
        for (Entry<String, Metric> entry : metricSet.getMetrics().entrySet()) {
            if (entry.getValue() instanceof MetricSet) {
                registerAll(prefix + "." + entry.getKey(), (MetricSet) entry.getValue(), registry);
            } else {
                registry.register(prefix + "." + entry.getKey(), entry.getValue());
            }
        }
    }

    protected void configure() {
        MetricRegistry mr = new MetricRegistry();

        registerAll("gc", new GarbageCollectorMetricSet(), mr);
        registerAll("buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()), mr);
        registerAll("memory", new MemoryUsageGaugeSet(), mr);
        registerAll("threads", new ThreadStatesGaugeSet(), mr);

        bind(MetricRegistry.class).toInstance(mr);
        log.info("MetricsModule mr:" + mr.toString());

        HealthCheckRegistry hr = new HealthCheckRegistry();
        bind(HealthCheckRegistry.class).toInstance(hr);
        log.info("MetricsModule hr:" + hr.toString());

        bind(MetricsServletContextListener.class);
        bind(HealthCheckServletContextListener.class);
        bind(InstFilterContextListener.class);
        install(new MetricsInstrumentationModule(mr));
    }
}
