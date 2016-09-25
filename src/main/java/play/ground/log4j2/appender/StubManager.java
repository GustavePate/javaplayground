package play.ground.log4j2.appender;

import java.io.OutputStream;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.OutputStreamManager;

public class StubManager extends OutputStreamManager {

	protected StubManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader) {
		super(os, streamName, layout, writeHeader);
	}

	public static StubManager getStubManager(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
