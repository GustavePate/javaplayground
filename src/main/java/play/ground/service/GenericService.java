package play.ground.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.inject.Inject;

public abstract class GenericService {

	long startTime = 0;
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Inject
    public MetricRegistry mreg;
	
	private Timer.Context timerContext;
	
	public void start(){
		startTime = System.currentTimeMillis();
		Timer timer = mreg.timer(MetricRegistry.name("service", this.getClass().getSimpleName(), "duration"));
		timerContext = timer.time();
		
	}
	
	public void stop(){
		
		long elapsedTime = System.currentTimeMillis() - startTime;
		timerContext.stop();
		log.info("{} response time: {}ms", this.getClass().getSimpleName(), elapsedTime);

	}
	
	
	
}
