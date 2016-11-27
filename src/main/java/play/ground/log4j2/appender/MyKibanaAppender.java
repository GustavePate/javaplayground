package play.ground.log4j2.appender;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name="KibanaAppender", category="Core", elementType="appender", printObject=true)
public class MyKibanaAppender extends AbstractAppender {

	private static final long serialVersionUID = -1029014549421667891L;

	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock readLock = rwLock.readLock();
	private JsonSocketManager socketmanager;
	private long lastfailure;
	private boolean ko;

	protected MyKibanaAppender(String name, String server, String port, Layout layout, Filter filter,JsonSocketManager socketmanager, final boolean ignoreExceptions) {
		super(name, filter, layout, ignoreExceptions);
		this.socketmanager = socketmanager;
		LOGGER.info("Appender constructor !!!!!!!!!!");
	}
	@PluginFactory
	public static MyKibanaAppender createAppender(
			@PluginAttribute("name") String name,
			@PluginAttribute("host") @Required String host,
			@PluginAttribute("port") @Required String port,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Filter") final Filter filter)
			{
		MyKibanaAppender res;
		LOGGER.info("Appender creator !!!!!!!!!!");
		if (name == null) {
			LOGGER.error("No name provided for KibanaAppender");
			return null;
		}
		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
		try{
			JsonSocketManager socman = JsonSocketManager.getInstance(host, port);
			res = new MyKibanaAppender(name, host, port, layout,filter, socman, false);
		}catch(Exception e){
			LOGGER.error(e);
			res = null;
		}
		
		return res;

	}
	@Override
	public void append(LogEvent event) {
		LOGGER.info("Appender append !!!!!!!!!!");
		readLock.lock();
		try {    
			
			if (ko){
				tryToRecover();
			}
			
			if (!ko){
				final byte[] bytes = getLayout().toByteArray(event);
				socketmanager.write(bytes);
				System.out.println("LOG: " + event.getLevel().toString() + " "+event.getLoggerFqcn() + " " + new String(bytes, "UTF-8"));
			}
		} catch (Exception ex) {
			this.setKO();
			if (!ignoreExceptions()) {
				LOGGER.error("Exception", ex);
			}
		} finally {
			readLock.unlock();
		}
	}

	private void tryToRecover(){
		
		LOGGER.error("TRY 2 RECOVER");
		// don't even try if last failure was one sec ago or less
		if (lastfailure < (System.currentTimeMillis() - 1000)){
			try{
				socketmanager.close();
			}catch(Exception e){
				//Whatever...
			}finally{
				try{
					socketmanager.startsocket();
					this.setOK();
				}catch(Exception e){
					
					LOGGER.error("Exception in recovery");
					this.setKO();
				}
			}
		}
	}
	
	private void setOK(){
		LOGGER.error("OK detected");
		ko = false;
	}
	
	private void setKO(){
		LOGGER.error("KO detected");
		ko = true;
		lastfailure = System.currentTimeMillis();
	}
	
	public void stop(){
		try{
			socketmanager.close();
			LOGGER.info("Socket closed");
		}catch(IOException e){
			LOGGER.warn("error while closing socket", e);
		}finally{
			super.stop();
		}
	}
	

}

