
package play.ground.metric.annotation;

import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.metric.calldetails.CallDetailManager;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MetricsManager {

    @Inject
    MetricRegistry metrics;

    @Named("props.application")
    @Inject
    protected Properties props;

    @Inject
    private void MetricsManager() {
    }

    private final static Logger LOG = LoggerFactory.getLogger(MetricsManager.class);

    private boolean workingmetrics = false;

    private long startTime = 0;

    private Context timerContext = null;

    private Context globalTimerContext = null;

    private String metricName = "defaultmetricname";

    private String className = "defaultclass";

    private String methodName = "defaultmethod";

    public void start(final String className, final String methodName) {
        // Start Metric
        try {

            if ("true".equalsIgnoreCase(props.getProperty("metrics.record", "false"))) {

                this.className = className;
                this.methodName = methodName;
                metricName = className.concat(".").concat(methodName);

                final Timer timer = metrics.timer(MetricRegistry.name("out", metricName, "requests"));
                final Timer globalTimer = metrics.timer(MetricRegistry.name("out", "all_daos", "requests"));

                // init des Timers
                timerContext = timer.time();
                globalTimerContext = globalTimer.time();

                // stockage du startTime pour CallDetailManager
                startTime = System.currentTimeMillis();
                workingmetrics = true;
            }
        } catch (final Exception e) {
            LOG.warn("Fail to start Metric", e);
        }

    }

    public void stop(final Boolean success) {

        if (workingmetrics) {

            // stop timers
            timerContext.stop();
            globalTimerContext.stop();
            final long elapsed = new Date().getTime() - startTime;

            // Ajout de l'appel au CallDetailManager
            CallDetailManager.addDependencyCall(className, methodName, elapsed);

            // Si il ya des exception lors de l'appel du DAO, increment de la métrique erreur
            if (!success) {
                final Counter counter = metrics.counter(MetricRegistry.name("out", metricName, "nb-error"));
                final Counter globalCounter = metrics.counter(MetricRegistry.name("out", "all_daos", "nb-error"));
                counter.inc();
                globalCounter.inc();
            }
        }
    }

}
