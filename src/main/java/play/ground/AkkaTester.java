package play.ground;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.AdminServlet;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

import play.ground.injection.ApplicationServletModule;
import play.ground.injection.DAOModule;
import play.ground.injection.MetricsModule;
import play.ground.injection.ServiceModule;
import play.ground.service.rest.RestEasyService;
import play.ground.servlet.HealthCheckServletContextListener;
import play.ground.servlet.MetricsServletContextListener;

/**
 * Hello world!
 *
 */
public class AkkaTester
{
	static final Logger log = LoggerFactory.getLogger(AkkaTester.class);

    public static void main( String[] args )
    {
    	
    	DAOModule daoModule = new DAOModule();
    	ServiceModule serviceModule = new ServiceModule();
    	ApplicationServletModule applicationServletModule = new ApplicationServletModule();
    	MetricsModule mm = new MetricsModule(); 
    	Injector injector = Guice.createInjector(serviceModule, applicationServletModule, daoModule, mm);
    	
    	Server server = new Server(7070);
    	
	
		// static ressources
//		ResourceHandler resourceHandler= new ResourceHandler();
//		resourceHandler.setResourceBase("conf");
//		resourceHandler.setDirectoriesListed(true);
//		
//		ContextHandler contextHandler= new ContextHandler("/static");
//		contextHandler.setHandler(resourceHandler);
//		server.setHandler(contextHandler);

    	HandlerList handlers = new HandlerList();

    	
    	
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase(".");
        resourceHandler.setDirectoriesListed(true);
        
        
        ServletContextHandler metricsContextHandler = new ServletContextHandler();
        metricsContextHandler.setContextPath("/metrics");
        metricsContextHandler.addEventListener(injector.getInstance(MetricsServletContextListener.class));
        metricsContextHandler.addEventListener(injector.getInstance(HealthCheckServletContextListener.class));
        metricsContextHandler.addServlet(AdminServlet.class,"/admin");
    	
    	
    	
    	
		ServletContextHandler handler = new ServletContextHandler(server, "/root");
		
		// Conf RestEasy
		
		handler.addEventListener(injector.getInstance(GuiceResteasyBootstrapServletContextListener.class));
		ServletHolder sh = new ServletHolder(HttpServletDispatcher.class);
		// security filter
		//handler.setInitParameter("resteasy.role.based.security", "true");
		//handler.addFilter(new FilterHolder(injector.getInstance(HelloFilter.class)), "/*", null);
		handler.addServlet(sh, "/rest/*");

		// Conf Guice
		
		handler.addFilter(GuiceFilter.class, "/api/*", EnumSet.allOf(DispatcherType.class));
		handler.addServlet(DefaultServlet.class, "/");
		
		
		
		//handler.addServlet(HttpServletDispatcher.class, "/rest/*");
		
		/* Replaced by GuiceModule */
		
//		handler.addServlet(YoServlet.class, "/exemple");
//		handler.addServlet(AkkaServlet.class, "/akka");
//		handler.addServlet(StatisticsServlet.class, "/stat");
		try {
			
			handlers.setHandlers(new Handler[]{handler,metricsContextHandler, resourceHandler});
			server.setHandler(handlers);
			//server.setDumpAfterStart(true);
			//server.setDumpBeforeStop(true);
			
			server.start();
		} catch (Exception e) {
			log.error("arg!", e);
		}

        log.info("Up and running !");

        //Fireup database
        try {
			Connection conn = DriverManager.getConnection("jdbc:h2:./db/h2/test", "sa", "");
		} catch (SQLException e1) {
			log.error("pb with db", e1);
		}
        
        
        
        HelloAkka ha = new HelloAkka();
        //ha.doit();
        
        try {
			server.join();
		} catch (InterruptedException e) {
			log.error("couic !", e);
		}

    }
}
