
package play.ground.helper;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationReader {

  private final static Logger LOG = LoggerFactory.getLogger(ConfigurationReader.class);

  private static ConcurrentHashMap<String, Properties> conf = new ConcurrentHashMap<String, Properties>();

  public static Properties getProperties(final String nomFichier) {

    if (!conf.containsKey(nomFichier)) {

      // chargement du fichier properties
      final ResourceBundle bundle = ResourceBundle.getBundle(nomFichier);

      // Boucle sur les properties et stockage pour l'injection
      final Properties properties = new Properties();
      LOG.debug("Chargement du fichier de properties : " + nomFichier);
      for (final Object key : bundle.keySet()) {
        properties.put(key, bundle.getString((String) key));
        LOG.debug("conf from {}: {}: {} ", nomFichier, key, bundle.getString((String) key));
      }
      conf.put(nomFichier, properties);
    }

    return conf.get(nomFichier);
  }
}
