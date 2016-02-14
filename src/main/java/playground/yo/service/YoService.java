package playground.yo.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import playground.yo.dao.fake.FakeDAO;
import playground.yo.dao.fake.FakeDAOFactory;
import playground.yo.dao.fake.dto.FakeDTO;
 
public class YoService extends Service{

	HttpServletRequest req;
	HttpServletResponse resp;
	
	public YoService(HttpServletRequest req, HttpServletResponse resp){
		
		this.req = req;
		this.resp = resp;
		
	}
	
	public void doIt(){
		start();
		try {
			
			
			this.resp.getWriter().println(this.toString());
			FakeDAO dao = FakeDAOFactory.get();
			FakeDTO outdto = dao.doit("this is a test");
			this.resp.getWriter().println(String.valueOf(outdto.compute));
		
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		stop();
	}
}
