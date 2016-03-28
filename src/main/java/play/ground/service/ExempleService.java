package play.ground.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.dto.ExempleDTO;
 
public class ExempleService extends GenericService{

	static final Logger log = LoggerFactory.getLogger(ExempleService.class);
	
	HttpServletRequest req;
	HttpServletResponse resp;


	@Inject
	private ExempleDAO dao;
	
	@Inject
	private ExempleService(){
		
	}
	
	public void doIt(HttpServletRequest req, HttpServletResponse resp){
		start();
		this.req = req;
		this.resp = resp;
		String str = req.getParameter("str");
		String strreq = "this is a test again";
		if (str != null){
			strreq = str;
		}
		try {
			this.resp.getWriter().println(this.toString());
			//FakeDAO dao = this.daoProvider.get();
			ExempleDTO outdto;
			try {
				outdto = dao.doit(strreq);
				this.resp.getWriter().println(outdto.src + " / " + outdto.compute);
			} catch (Exception e) {
				log.error("DAO exploded for: {}",strreq, e);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		stop();
	}
}
