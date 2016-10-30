package play.ground.log4j2.layout;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

	/*
	 * </pre>
	 * <p>
	 * If {@code complete="false"}, the appender does not write the JSON open array character "[" at the start
	 * of the document, "]" and the end, nor comma "," between records.
	 * </p>
	 * <p>
	 * This approach enforces the independence of the JsonLayout and the appender where you embed it.
	 * </p>
	 * <h3>Encoding</h3>
	 * <p>
	 * Appenders using this layout should have their {@code charset} set to {@code UTF-8} or {@code UTF-16}, otherwise
	 * events containing non ASCII characters could result in corrupted log files.
	 * </p>
	 * <h3>Pretty vs. compact XML</h3>
	 * <p>
	 * By default, the JSON layout is not compact (a.k.a. "pretty") with {@code compact="false"}, which means the
	 * appender uses end-of-line characters and indents lines to format the text. If {@code compact="true"}, then no
	 * end-of-line or indentation is used. Message content may contain, of course, escaped end-of-lines.
	 * </p>
	 */
	@Plugin(name = "KibanaJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
	 public class KibanaJsonLayout extends KibanaAbstractJacksonLayout{

	    private static final String DEFAULT_FOOTER = "]";

	    private static final String DEFAULT_HEADER = "[";

	    static final String CONTENT_TYPE = "application/json";

	    protected KibanaJsonLayout(final Configuration config, final boolean locationInfo, final boolean properties,
	            final boolean encodeThreadContextAsList,
	            final boolean complete, final boolean compact, final boolean eventEol, final String headerPattern,
	            final String footerPattern, final Charset charset) {
	        super(config, new KibanaJacksonFactory.JSON(encodeThreadContextAsList).newWriter(locationInfo, properties, compact),
	                charset, compact, complete, eventEol,
	                PatternLayout.createSerializer(config, null, headerPattern, DEFAULT_HEADER, null, false, false),
	                PatternLayout.createSerializer(config, null, footerPattern, DEFAULT_FOOTER, null, false, false));
	    }

	    /**
	     * Returns appropriate JSON header.
	     *
	     * @return a byte array containing the header, opening the JSON array.
	     */
	    @Override
	    public byte[] getHeader() {
	        if (!this.complete) {
	            return null;
	        }
	        final StringBuilder buf = new StringBuilder();
	        final String str = serializeToString(getHeaderSerializer());
	        if (str != null) {
	            buf.append(str);
	        }
	        buf.append(this.eol);
	        return getBytes(buf.toString());
	    }

	    /**
	     * Returns appropriate JSON footer.
	     *
	     * @return a byte array containing the footer, closing the JSON array.
	     */
	    @Override
	    public byte[] getFooter() {
	        if (!this.complete) {
	            return null;
	        }
	        final StringBuilder buf = new StringBuilder();
	        buf.append(this.eol);
	        final String str = serializeToString(getFooterSerializer());
	        if (str != null) {
	            buf.append(str);
	        }
	        buf.append(this.eol);
	        return getBytes(buf.toString());
	    }

	    @Override
	    public Map<String, String> getContentFormat() {
	        final Map<String, String> result = new HashMap<>();
	        result.put("version", "2.0");
	        return result;
	    }

	    @Override
	    /**
	     * @return The content type.
	     */
	    public String getContentType() {
	        return CONTENT_TYPE + "; charset=" + this.getCharset();
	    }

	    /**
	     * Creates a JSON Layout.
	     * @param config
	     *           The plugin configuration.
	     * @param locationInfo
	     *            If "true", includes the location information in the generated JSON.
	     * @param properties
	     *            If "true", includes the thread context map in the generated JSON.
	     * @param propertiesAsList
	     *            If true, the thread context map is included as a list of map entry objects, where each entry has
	     *            a "key" attribute (whose value is the key) and a "value" attribute (whose value is the value).
	     *            Defaults to false, in which case the thread context map is included as a simple map of key-value
	     *            pairs.
	     * @param complete
	     *            If "true", includes the JSON header and footer, and comma between records.
	     * @param compact
	     *            If "true", does not use end-of-lines and indentation, defaults to "false".
	     * @param eventEol
	     *            If "true", forces an EOL after each log event (even if compact is "true"), defaults to "false". This
	     *            allows one even per line, even in compact mode.
	     * @param headerPattern
	     *            The header pattern, defaults to {@code "["} if null.
	     * @param footerPattern
	     *            The header pattern, defaults to {@code "]"} if null.
	     * @param charset
	     *            The character set to use, if {@code null}, uses "UTF-8".
	     * @return A JSON Layout.
	     */
	    @PluginFactory
	    public static KibanaJsonLayout createLayout(
	            // @formatter:off
	            @PluginConfiguration final Configuration config,
	            @PluginAttribute(value = "locationInfo", defaultBoolean = false) final boolean locationInfo,
	            @PluginAttribute(value = "properties", defaultBoolean = false) final boolean properties,
	            @PluginAttribute(value = "propertiesAsList", defaultBoolean = false) final boolean propertiesAsList,
	            @PluginAttribute(value = "complete", defaultBoolean = false) final boolean complete,
	            @PluginAttribute(value = "compact", defaultBoolean = false) final boolean compact,
	            @PluginAttribute(value = "eventEol", defaultBoolean = false) final boolean eventEol,
	            @PluginAttribute(value = "header", defaultString = DEFAULT_HEADER) final String headerPattern,
	            @PluginAttribute(value = "footer", defaultString = DEFAULT_FOOTER) final String footerPattern,
	            @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset
	            // @formatter:on
	    ) {
	        final boolean encodeThreadContextAsList = properties && propertiesAsList;
	        return new KibanaJsonLayout(config, locationInfo, properties, encodeThreadContextAsList, complete, compact, eventEol,
	                headerPattern, footerPattern, charset);
	    }

	    /**
	     * Creates a JSON Layout using the default settings. Useful for testing.
	     *
	     * @return A JSON Layout.
	     */
	    public static KibanaJsonLayout createDefaultLayout() {
	        return new KibanaJsonLayout(new DefaultConfiguration(), false, false, false, false, false, false,
	                DEFAULT_HEADER, DEFAULT_FOOTER, StandardCharsets.UTF_8);
	    }

	    @Override
	    public void toSerializable(final LogEvent event, final Writer writer) throws IOException {
	        if (complete && eventCount > 0) {
	            writer.append(", ");
	        }
	        super.toSerializable(event, writer);
	    }

}
