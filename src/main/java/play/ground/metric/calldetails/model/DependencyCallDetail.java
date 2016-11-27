
package play.ground.metric.calldetails.model;

public class DependencyCallDetail {

    public String dependencyName = "";

    public String methodName = "";

    public long totalDuration = 0;

    public int nbCall = 0;

    public DependencyCallDetail(final String dependencyName, final String methodName, final long totalDuration) {
        super();
        this.dependencyName = dependencyName;
        this.methodName = methodName;
        this.totalDuration = totalDuration;
    }

}
