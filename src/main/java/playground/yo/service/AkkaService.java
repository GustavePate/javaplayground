package playground.yo.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AkkaService extends Service {
	HttpServletRequest req;
	HttpServletResponse resp;
	
	public AkkaService(HttpServletRequest req, HttpServletResponse resp){
		
		this.req = req;
		this.resp = resp;
		
	}
	
	public void doIt(){
		start();
		try {
			this.resp.getWriter().println(this.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		stop();
	}
}
