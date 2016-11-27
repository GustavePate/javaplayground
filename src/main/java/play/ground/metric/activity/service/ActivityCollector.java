
package play.ground.metric.activity.service;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.metric.activity.objects.ActivityCollectorThread;
import play.ground.metric.activity.objects.ActivityModel;
import play.ground.metric.activity.objects.ActivityStorage;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class ActivityCollector extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityCollector.class.getName());

    private static final long serialVersionUID = -6454065037438422928L;

    private static HashMap<Date, ActivityModel> activity = new HashMap<Date, ActivityModel>();

    private final MetricRegistry mr;

    private ActivityCollectorThread t;

    private ExecutorService collectorPool;

    @Named("props.application")
    @Inject
    private Properties props;

    private static ObjectMapper mapper = new ObjectMapper();
    /*
     * Démarre le thread de collecte des métrique Et sert le end-point /activity
     * qui permet de recuperer les métriques historisées
     */

    @Override
    public void init() {

        // ActivityCollector Thread
        LOG.info("Application activity monitoring started.  Visit /rest/public/activity for data");

        // open a thread
        collectorPool = Executors.newFixedThreadPool(1);
        t = new ActivityCollectorThread("ActivityCollector", mr);
        collectorPool.execute(t);

    }

    @Override
    public void destroy() {

        collectorPool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!collectorPool.awaitTermination(5, TimeUnit.SECONDS)) {
                collectorPool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!collectorPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    LOG.error("ActivityCollectorThread Pool did not terminate");
                }
            }
        } catch (final InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            collectorPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }

    }

    @Inject
    public ActivityCollector(final Provider<MetricRegistry> metrics_provider) {

        mr = metrics_provider.get();

    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {

        LOG.info("doGet sur ActivityCollector");
        String json = "{success: false}";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ActivityStorage.getMetrics());
            final PrintWriter out = response.getWriter();
            out.print(json);

        } catch (final Exception e) {
            LOG.error("problem writing ActivityCollector response", e);
        }
    }

}
