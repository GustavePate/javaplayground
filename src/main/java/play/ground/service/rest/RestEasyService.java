package play.ground.service.rest;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.dto.ExempleDTO;

@Path("/rest/hello")
public class RestEasyService {
	
	@Inject
	private ExempleDAO dao;
	
	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@GET
	@Path("{name}")
	@Produces("application/json")
	public HashMap<String, Integer> hello(@PathParam("name") String name) {

		LOG.info("hello REST: {} call with: {}", this, name);
		
		HashMap <String, Integer> resp = new HashMap<String, Integer>();
		try {
			ExempleDTO res = dao.doit(name);
			resp.put(res.src, res.compute);
		} catch (Exception e) {
			LOG.error("DAO exploded for: {}",name, e);
		}
		return resp;
	}
}
