
package play.ground.metric.metrics;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.metric.activity.objects.ActivityStorage;
import play.ground.metric.metrics.model.DependencyMetric;
import play.ground.metric.metrics.model.ServiceMetric;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MetricManager {

    public enum Status {
        ERROR, OK
    }

    private static final Logger LOG = LoggerFactory.getLogger(MetricManager.class.getName());

    private static final ThreadLocal<ServiceMetric> metric = new ThreadLocal<ServiceMetric>();

    /**
     * @return une instance de rapport par thread et donc par requête à monitorer
     */
    private static ServiceMetric getMetric() {
        if (metric.get() == null) {
            metric.set(new ServiceMetric());
        }

        return metric.get();
    }

    /**
     * supprime le rapport pour le thread courant !! Doit être appellé en finally
     * de chaque service
     */
    public static void clear() {
        if (metric.get() != null) {
            metric.remove();
        }
    }

    public static void setServiceName(final String sname) {
        getMetric().name = sname;
    }

    private static String getServiceName() {
        return getMetric().name;
    }

    public static void setServiceStatus(final Status status) {
        if (status == Status.ERROR) {
            getMetric().status = "ERROR";
        } else {
            getMetric().status = "OK";
        }
    }

    private static String getServiceStatus() {
        return getMetric().status;
    }

    public static void setFunctionalId(final String functionalId) {
        getMetric().functionalId = functionalId;
    }

    private static String getFunctionalId() {
        return getMetric().functionalId;
    }

    public static void addDependencyCall(final String daoSimpleName, final String daoMethod, final long callDuration) {
        final DependencyMetric daoCall = new DependencyMetric(daoSimpleName, daoMethod, callDuration);
        if (getMetric().daoCalls.containsKey(daoSimpleName)) {
            getMetric().daoCalls.get(daoSimpleName).add(daoCall);
        } else {
            final ArrayList<DependencyMetric> depMetrics = new ArrayList<DependencyMetric>();
            depMetrics.add(daoCall);
            getMetric().daoCalls.put(daoSimpleName, depMetrics);
        }
    }

    public static HashMap<String, ArrayList<DependencyMetric>> getDependenciesCall() {
        return getMetric().daoCalls;
    }

    public static void dump(final long serviceCallDurationMillis) {
        ActivityStorage.logResponseTime(new Double(serviceCallDurationMillis));
        getMetric().duration = serviceCallDurationMillis;
        final ObjectMapper mapper = new ObjectMapper();
        getMetric().aggregateDaoCall();

        try {
            LOG.info(mapper.writeValueAsString(getMetric()));
        } catch (final JsonProcessingException e) {
            LOG.error("Impossible to serialize Metrics to Json", e);
            final StringBuffer buffer = new StringBuffer();
            buffer.append(getServiceName());
            buffer.append(";");
            buffer.append(getFunctionalId());
            buffer.append(";");
            // TODO: convert in ms
            buffer.append(serviceCallDurationMillis);
            buffer.append(";");
            buffer.append(getServiceStatus());
            buffer.append(";");
            final HashMap<String, ArrayList<DependencyMetric>> dependencies = getDependenciesCall();
            if (null != dependencies) {
                for (final String depname : dependencies.keySet()) {
                    buffer.append(depname + " - ");
                }

            }

            final String consolidated = buffer.toString();
            LOG.info(consolidated);
        }

    }

}
