
package play.ground.metric.calldetails.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServiceCallDetail {

	public String name;

	public String functionalId;

	public long duration;

	public String status;

	public String versionClient;

	@JsonIgnore
	public HashMap<String, ArrayList<DependencyCallDetail>> daoCalls = new HashMap<String, ArrayList<DependencyCallDetail>>();

	public ArrayList<DependencyCallDetail> aggregatedDaoCalls = new ArrayList<DependencyCallDetail>();

	public void aggregateDaoCall() {
		if (daoCalls.size() > 0) {
			for (final String daoname : daoCalls.keySet()) {
				final DependencyCallDetail aggregatedDaoMetric = new DependencyCallDetail(daoname, "all", 0);
				for (final DependencyCallDetail daocall : daoCalls.get(daoname)) {
					aggregatedDaoMetric.nbCall++;
					aggregatedDaoMetric.totalDuration += daocall.totalDuration;
				}
				aggregatedDaoCalls.add(aggregatedDaoMetric);
			}

		}
	}

}
