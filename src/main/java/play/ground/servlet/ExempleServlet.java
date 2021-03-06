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

import play.ground.service.ExempleService;

@Singleton
public class ExempleServlet extends HttpServlet {

	private static final long serialVersionUID = 5338707271409245258L;
	
	@Inject
	private Provider<ExempleService> serviceProvider;
	
	@Inject
	private ExempleServlet(){ 
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ExempleService yo = serviceProvider.get();
		yo.doIt(req, resp);
		resp.setStatus(HttpStatus.OK_200);
	}
}
