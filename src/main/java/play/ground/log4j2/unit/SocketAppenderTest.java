package play.ground.log4j2.unit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class SocketAppenderTest {

	
	static final Logger LOG = LoggerFactory.getLogger(SocketAppenderTest.class);
	
	/*
	 * You will need to:
	 * $netcat -l 2929 
	 * in order to run this test sequence 
	 */
	
	
	@Test
	public void run_netcat(){
		
		MDC.put("kibanaidentity", "test");
		
		LOG.info("yo");
		
		Exception ex = new Exception("my error message");
		LOG.error("arg", ex);
		
	}
	
	@Test
	public void cut_netcat_while_running_this_one() throws InterruptedException{
		
		int nb=20;
		while (nb > 0){
			LOG.info("yo");
			Thread.sleep(1000);
			nb--;
		}
		
	}
}
