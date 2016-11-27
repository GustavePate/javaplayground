
package play.ground.metric.activity.objects;

import java.util.Date;

public class ActivityModel {

	public double rate = 0.0;

	public double meanSinceLastMetric = 0.0;

	public int nbcall = 0;

	public long timestamp = 0;

	public int totalCall = 0;

	public int nberror = 0;

	public ActivityModel() {
		timestamp = new Date().getTime();
	}

}
