package playground.yo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import playground.yo.service.AkkaService;
import playground.yo.service.YoService;

public class AkkaServlet extends HttpServlet {
	private static final long serialVersionUID = 5338707275509245258L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		
		AkkaService akka = new AkkaService(req, resp);
		akka.doIt();
		resp.setStatus(HttpStatus.OK_200);
	}

}
