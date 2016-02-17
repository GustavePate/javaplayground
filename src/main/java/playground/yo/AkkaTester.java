package playground.yo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.StatisticsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import playground.yo.servlet.YoServlet;
import playground.yo.servlet.AkkaServlet;

/**
 * Hello world!
 *
 */
public class AkkaTester
{
	static final Logger log = LoggerFactory.getLogger(AkkaTester.class);

    public static void main( String[] args )
    {
    	Server server = new Server(7070);
		ServletContextHandler handler = new ServletContextHandler(server, "/yo");
		handler.addServlet(YoServlet.class, "/exemple");
		handler.addServlet(AkkaServlet.class, "/akka");
		handler.addServlet(StatisticsServlet.class, "/stat");
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

        log.info("Up and running !");

        HelloAkka ha = new HelloAkka();
        //ha.doit();

    }
}
