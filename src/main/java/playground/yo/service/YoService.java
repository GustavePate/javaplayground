package playground.yo.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import playground.yo.dao.fake.FakeDAO;
import playground.yo.dao.fake.dto.FakeDTO;
 
public class YoService extends Service{

	HttpServletRequest req;
	HttpServletResponse resp;
	
	@Inject
	@Named("FakeDAO")
	private Provider<FakeDAO> daoProvider;
	
	@Inject
	public YoService(){
		
	}
	
	public void doIt(HttpServletRequest req, HttpServletResponse resp){
		start();
		this.req = req;
		this.resp = resp;
		try {
			
			this.resp.getWriter().println(this.toString());
			FakeDAO dao = this.daoProvider.get();
			FakeDTO outdto = dao.doit("this is a test");
			this.resp.getWriter().println(outdto.src + " / " + outdto.compute);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		stop();
	}
}
