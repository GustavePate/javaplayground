package playground.yo;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.StatisticsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

import playground.yo.servlet.YoServlet;
import playground.yo.injection.ApplicationServletModule;
import playground.yo.injection.NonServletModule;
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
    	
    	NonServletModule nonServletModule = new NonServletModule();
    	ApplicationServletModule applicationServletModule = new ApplicationServletModule();
    	Injector injector = Guice.createInjector(nonServletModule, applicationServletModule);
    	
    	Server server = new Server(7070);
    	
	
		// static ressources
//		ResourceHandler resourceHandler= new ResourceHandler();
//		resourceHandler.setResourceBase("conf");
//		resourceHandler.setDirectoriesListed(true);
//		
//		ContextHandler contextHandler= new ContextHandler("/static");
//		contextHandler.setHandler(resourceHandler);
//		server.setHandler(contextHandler);


		ServletContextHandler handler = new ServletContextHandler(server, "/yo");

		
		//Guice Filter
		handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
		handler.addServlet(DefaultServlet.class, "/");
		
		/* Replaced by GuiceModule */
		
//		handler.addServlet(YoServlet.class, "/exemple");
//		handler.addServlet(AkkaServlet.class, "/akka");
//		handler.addServlet(StatisticsServlet.class, "/stat");
		try {
			server.start();
		} catch (Exception e) {
			log.error("arg!", e);
		}

        log.info("Up and running !");

        
        HelloAkka ha = new HelloAkka();
        //ha.doit();
        
        try {
			server.join();
		} catch (InterruptedException e) {
			log.error("couic !", e);
		}

    }
}
