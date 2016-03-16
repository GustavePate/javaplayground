package play.ground.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IndexServlet  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4771386446534975600L;

	@Inject
	public IndexServlet(){
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setStatus(HttpStatus.NOT_FOUND_404);
	}
	
}
