package play.ground.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericService {

	long startTime = 0;
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public void start(){
		startTime = System.currentTimeMillis();
	}
	
	public void stop(){
		
		long elapsedTime = System.currentTimeMillis() - startTime;
		log.info("{} response time: {}ms", this.getClass().getSimpleName(), elapsedTime);

	}
	
	
	
}
