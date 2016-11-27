
package play.ground.metric.activity.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

public class ActivityCollectorThread extends Thread implements Runnable

{

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityCollectorThread.class.getName());

	private final MetricRegistry mr;

	public ActivityCollectorThread(final String name, final MetricRegistry registry) {
		super(name);
		mr = registry;
	}

	// Lecture et Aggregation des metriques metrics pour affichage de graphe
	// d'activité

	@Override
	public void run() {

		boolean res = true;

		while (res) {

			// LOGGER.debug("I am " + this.getName() + " am I a singleton ?" + this.getId());

			try { // read metrics

				if (mr.getTimers().size() > 0) {

					if (mr.getTimers().containsKey("in.all_services.requests")) {
						final ActivityModel m = new ActivityModel();
						m.rate = mr.getTimers().get("in.all_services.requests").getOneMinuteRate();

						m.totalCall = (int) mr.getTimers().get("in.all_services.requests").getCount();

						final Counter errors = mr.getCounters().get("in.all_services.nb-error");
						if (null != errors) {
							m.nberror = (int) errors.getCount();
						}

						ActivityStorage.lastCallsMean(m);

						if (!ActivityStorage.getMetrics().offer(m)) {
							LOGGER.warn("Metrics queue is full");
						}
						LOGGER.debug("theoric capacity: " + ActivityStorage.MAX_METRICS);
						LOGGER.debug("remaining capacity: " + ActivityStorage.getMetrics().remainingCapacity());
						LOGGER.debug("nb metrics: " + ActivityStorage.getMetrics().size());
						LOGGER.debug("nb response time: " + ActivityStorage.lastResponseTime.size());

					}
				}

			} catch (final Exception e) {
				LOGGER.warn("Problem while building metrics", e);
			}

			try {
				Thread.sleep((long) ActivityStorage.MEASURE_INTERVAL_SECONDS * 1000);
			} catch (final InterruptedException e) {
				LOGGER.info("Stopping Activity Collecting now !");
				res = false;
			}

		}
	}
}
