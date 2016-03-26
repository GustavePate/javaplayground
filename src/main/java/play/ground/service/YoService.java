package play.ground.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import play.ground.dao.fake.FakeDAO;
import play.ground.dao.fake.dto.FakeDTO;
 
public class YoService extends Service{

	static final Logger log = LoggerFactory.getLogger(YoService.class);
	
	HttpServletRequest req;
	HttpServletResponse resp;

	@Inject
	@Named("fakedao.mode")
	private String mode;

	@Inject
	private FakeDAO dao;
	
	@Inject
	public YoService(){
		
	}
	
	public void doIt(HttpServletRequest req, HttpServletResponse resp){
		start();
		this.req = req;
		this.resp = resp;
		String str = req.getParameter("str");
		String strreq = "this is a test";
		if (str != null){
			strreq = str;
		}
		try {
			log.info("service mode: {}", mode);
			this.resp.getWriter().println(this.toString());
			//FakeDAO dao = this.daoProvider.get();
			FakeDTO outdto = dao.doit(strreq);
			this.resp.getWriter().println(outdto.src + " / " + outdto.compute);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		stop();
	}
}
