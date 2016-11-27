package play.ground.service.rest;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;

import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.dto.ExempleDTO;
import play.ground.service.GenericService;

@Path("/rest/hello")
public class RestEasyService extends GenericService{
	
	@Inject
	private ExempleDAO dao;
	
	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Timed
	private HashMap<String, Integer> work(String name){
		HashMap <String, Integer> resp = new HashMap<String, Integer>();
		try {
			ExempleDTO res = dao.doit(name);
			resp.put(res.src, res.compute);
		} catch (Exception e) {
			LOG.error("DAO exploded for: {}",name, e);
		}
		return resp;
	}
	
	
	@GET
	@Path("{name}")
	@Produces("application/json")
	public HashMap<String, Integer> hello(@PathParam("name") String name) {
		this.start();
		LOG.info("hello REST: {} call with: {}", this, name);
		HashMap <String, Integer> resp = new HashMap<String, Integer>();
		resp = work(name);
		this.stop();
		return resp;
		
	}
}
