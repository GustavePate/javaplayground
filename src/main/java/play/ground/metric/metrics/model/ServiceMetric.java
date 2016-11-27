
package play.ground.metric.metrics.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServiceMetric {

  public String name;

  public String functionalId;

  public long duration;

  public String status;

  @JsonIgnore
  public HashMap<String, ArrayList<DependencyMetric>> daoCalls = new HashMap<String, ArrayList<DependencyMetric>>();

  public ArrayList<DependencyMetric> aggregatedDaoCalls = new ArrayList<DependencyMetric>();

  public void aggregateDaoCall() {
    if (daoCalls.size() > 0) {
      for (final String daoname : daoCalls.keySet()) {
        final DependencyMetric aggregatedDaoMetric = new DependencyMetric(daoname, "all", 0);
        for (final DependencyMetric daocall : daoCalls.get(daoname)) {
          aggregatedDaoMetric.nbCall++;
          aggregatedDaoMetric.totalDuration += daocall.totalDuration;
        }
        aggregatedDaoCalls.add(aggregatedDaoMetric);
      }

    }
  }

}
