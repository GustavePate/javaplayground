
package play.ground.metric.activity.objects;

import java.util.Date;

public class SingleResponseTime {

  private final Date timestamp;

  private Double responseTimeMillis = 0.0;

  public SingleResponseTime(final Double responseTimeMillis) {
	timestamp = new Date();
    this.responseTimeMillis = responseTimeMillis;
  }

  public Double getResponseTimeMillis() {
    return responseTimeMillis;
  }

}
