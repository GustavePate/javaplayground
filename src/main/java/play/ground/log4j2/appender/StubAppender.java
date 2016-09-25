package play.ground.log4j2.appender;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name = "Stub", category = "Core", elementType = "appender", printObject = true)
public final class StubAppender extends AbstractOutputStreamAppender<StubManager> {
 

 
    protected StubAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions,
			boolean immediateFlush, StubManager manager) {
		super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
		// TODO Auto-generated constructor stub
	}

	@PluginFactory
    public static StubAppender createAppender(@PluginAttribute("name") String name,
                                              @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
                                              @PluginElement("Layout") Layout layout,
                                              @PluginElement("immediateFlush") boolean iflush,
                                              @PluginElement("Filters") Filter filter) {
 
        if (name == null) {
            LOGGER.error("No name provided for StubAppender");
            return null;
        }
 
        StubManager manager = StubManager.getStubManager(name);
        if (manager == null) {
            LOGGER.error("No manager provided for StubAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new StubAppender(name, layout, filter, ignoreExceptions,iflush, manager);
    }
}