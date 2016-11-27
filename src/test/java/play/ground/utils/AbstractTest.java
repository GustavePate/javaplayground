package play.ground.utils;

import java.lang.invoke.MethodHandles;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractTest {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractTest.class);
   
    
    @BeforeClass
    public static void logClassName(){
	    LOG.info("");
		LOG.info("================================================");
    }
    
    
    @Rule
    public TestRule watcher = new TestWatcher() {
       protected void starting(Description description) {
    	   LOG.info("");
    	   LOG.info("===============> run: " + description.getMethodName());
       }
       
       protected void failed(Throwable e, Description description) {
    	   LOG.info(e.getMessage());
    	   LOG.info("============> ✖ FAIL ✖");
       }
       
       protected void succeeded(Description description) {
    	   LOG.info("============> ✓ ok ✓");
       }
       
       
       
    };
    
	
	
}
