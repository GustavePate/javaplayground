package play.ground.log4j2.layout;


import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.JsonConstants;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;


abstract class KibanaJacksonFactory {

	static class JSON extends KibanaJacksonFactory {

		private final boolean encodeThreadContextAsList;

		public JSON(final boolean encodeThreadContextAsList) {
			this.encodeThreadContextAsList = encodeThreadContextAsList;
		}

		@Override
		protected String getPropertNameForContextMap() {
			return JsonConstants.ELT_CONTEXT_MAP;
		}

		@Override
		protected String getPropertNameForSource() {
			return JsonConstants.ELT_SOURCE;
		}

		@Override
		protected String getPropertNameForNanoTime() {
			return JsonConstants.ELT_NANO_TIME;
		}

		@Override
		protected PrettyPrinter newCompactPrinter() {
			return new MinimalPrettyPrinter();
		}

		@Override
		protected ObjectMapper newObjectMapper() {
			return new Log4jJsonObjectMapper(encodeThreadContextAsList);
		}

		@Override
		protected PrettyPrinter newPrettyPrinter() {
			return new DefaultPrettyPrinter();
		}
	}

	abstract protected String getPropertNameForContextMap();

	abstract protected String getPropertNameForSource();

	abstract protected String getPropertNameForNanoTime();

	abstract protected PrettyPrinter newCompactPrinter();

	abstract protected ObjectMapper newObjectMapper();

	abstract protected PrettyPrinter newPrettyPrinter();

	ObjectWriter newWriter(final boolean locationInfo, final boolean properties, final boolean compact) {
		final SimpleFilterProvider filters = new SimpleFilterProvider();
		final Set<String> except = new HashSet<>(2);
		if (!locationInfo) {
			except.add(this.getPropertNameForSource());
		}
		if (!properties) {
			except.add(this.getPropertNameForContextMap());
		}
		except.add(this.getPropertNameForNanoTime());
		filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
		final ObjectWriter writer = this.newObjectMapper().writer(compact ? this.newCompactPrinter() : this.newPrettyPrinter());
		return writer.with(filters);
	}


}
