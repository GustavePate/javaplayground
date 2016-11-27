
package play.ground.metric.activity.objects;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityStorage {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityStorage.class.getName());

	protected static final int MEASURE_INTERVAL_SECONDS = 60;

	private static final int MEASURE_RETENTION_TIME_SECONDS = 86400;

	private static final int MAX_REQ_S = 3000;

	public static final int MAX_METRICS = MEASURE_RETENTION_TIME_SECONDS / MEASURE_INTERVAL_SECONDS;

	private static final ArrayBlockingQueue<ActivityModel> metrics = new ArrayBlockingQueue<ActivityModel>(MAX_METRICS);

	protected static ArrayBlockingQueue<SingleResponseTime> lastResponseTime = new ArrayBlockingQueue<SingleResponseTime>(MAX_REQ_S * (MEASURE_INTERVAL_SECONDS + 2));

	public static void logResponseTime(final Double millis) {
		final SingleResponseTime time = new SingleResponseTime(millis);
		lastResponseTime.offer(time);

	}

	public static void lastCallsMean(final ActivityModel m) {
		try {

			m.meanSinceLastMetric = -1.0;
			m.nbcall = 0;

			final ArrayList<SingleResponseTime> responseTimeList = new ArrayList<SingleResponseTime>();
			lastResponseTime.drainTo(responseTimeList);
			m.nbcall = responseTimeList.size();
			if (m.nbcall > 0) {
				m.meanSinceLastMetric = responseTimeList.stream().mapToDouble(SingleResponseTime::getResponseTimeMillis).average().getAsDouble();
			} else {
				m.meanSinceLastMetric = -1.0;
			}
		} catch (final Exception e) {
			LOGGER.warn("Problem while emptying lastCallsMean Queue", e);
			lastResponseTime.clear();
		}
	}

	public static ArrayBlockingQueue<ActivityModel> getMetrics() {
		return metrics;
	}

}
