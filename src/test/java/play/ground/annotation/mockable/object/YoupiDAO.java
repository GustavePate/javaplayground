package play.ground.annotation.mockable.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.dao.meta.Mockable;


public class YoupiDAO {
	
	static final Logger LOG = LoggerFactory.getLogger(YoupiDAO.class);
	
	public YoupiDAO(){
		
	}
	
	@Mockable
	public boolean ya(String input){
		
		LOG.info("ya: {}", input);
		
		return false;
	}
	
	
}