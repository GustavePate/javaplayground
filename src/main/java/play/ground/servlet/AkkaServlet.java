package play.ground.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import play.ground.service.AkkaService;

@Singleton
public class AkkaServlet extends HttpServlet {
	private static final long serialVersionUID = 5338707275509245258L;
	
	@Inject
	private Provider<AkkaService> service;
	
	@Inject
	public AkkaServlet(){
		
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		service.get().process(req, resp);
		resp.setStatus(HttpStatus.OK_200);
	}

}
