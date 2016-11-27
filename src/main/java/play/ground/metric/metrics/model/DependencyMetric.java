
package play.ground.metric.metrics.model;

public class DependencyMetric {

  public String dependencyName = "";

  public String methodName = "";

  public long totalDuration = 0;

  public int nbCall = 0;

  public DependencyMetric(final String dependencyName, final String methodName, final long totalDuration) {
    super();
    this.dependencyName = dependencyName;
    this.methodName = methodName;
    this.totalDuration = totalDuration;
  }

}
