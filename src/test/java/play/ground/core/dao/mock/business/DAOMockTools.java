package play.ground.core.dao.mock.business;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import play.ground.core.dao.mock.business.JSONSupaMockManager;
import play.ground.utils.World;

public class DAOMockTools {

	@Inject
	private static World world;
	
	
	
    private final static Logger LOG = LoggerFactory.getLogger(DAOMockTools.class);
	
    
    
    
	public static void before(){
		
		LOG.debug("************* CLEAN ****************");

		
		// read from classpath
		
		if (world.appconf.containsKey("mock.json.read.path")){
			world.appconf.remove("mock.json.read.path");
		}
		
		//no sleep
        world.appconf.setProperty("dao.all.mock.will.sleep", "false");
		
    	// clean mock manager
    	JSONSupaMockManager.test_dropData();
    
    	// clean mock data
    	if (world.appconf.getProperty("mock.json.write.path")!=null){
			// erase json files in write dir
			Path mockpath = FileSystems.getDefault().getPath(world.appconf.getProperty("mock.json.write.path"));
			File folder = mockpath.toFile();
			
			Arrays.asList(folder.listFiles()).stream()
				.filter(x -> x.isFile() && x.getName().endsWith(".json") && x.canWrite())
				.collect(Collectors.toList()).forEach(x->{
					LOG.info("delete {}", x.getName());
					x.delete();
				});
    	}
    	

		
	}
	
	public static void after(){
		
		
	}
	
	
}
