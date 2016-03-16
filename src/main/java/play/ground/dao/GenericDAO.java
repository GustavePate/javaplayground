package play.ground.dao;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

public abstract class GenericDAO {

	@Inject
	@Named("conf")
	protected Config conf;
	
}
