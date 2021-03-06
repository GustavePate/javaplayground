package play.ground.log4j2;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KibanaAppenderTest {

	
	static final Logger LOG = LoggerFactory.getLogger(KibanaAppenderTest.class);
	
	/*
	 * You will need to:
	 * $netcat -l 2929 
	 * in order to run this test sequence 
	 */
	
	
	@Test
	public void run_netcat(){
		LOG.info("yo");
		
		Exception ex = new Exception("my error message");
		LOG.error("JUST LOGGING AN EXCEPTION", ex);
		
	}
	
	@Test
	@Ignore
	public void cut_netcat_while_running_this_one() throws InterruptedException{
		
		int nb=2;
		while (nb > 0){
			LOG.info("yo");
			Thread.sleep(1000);
			nb--;
		}
		
	}
}
