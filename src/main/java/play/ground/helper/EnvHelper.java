
package play.ground.helper;

import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.exception.DAOException;

public class EnvHelper {

    private final static Logger LOG = LoggerFactory.getLogger(EnvHelper.class);

    public static Double checkJavaVersion() {

        final Double REQUIRED_VERSION = 1.8;
        String[] xdoty;
        Double jvmversion = 0.0;

        try {

            // Some java8 specific code
            final ArrayList<String> test = new ArrayList<>();
            test.add("1");
            test.forEach((i) -> {
                LOG.trace("checking java 8 compliance... {}", i);
            });

            // Check de la version
            xdoty = System.getProperty("java.version").split("\\.");
            jvmversion = Double.valueOf(xdoty[0] + "." + xdoty[1]);
            LOG.debug("Check Java revision, got: {}", jvmversion);
            if (jvmversion < REQUIRED_VERSION) {
                LOG.error("Invalid JRE version detected {} : {} or higher is required", jvmversion, REQUIRED_VERSION);
                System.exit(-1);
            }
            LOG.debug("Check Java revision, got: {} TEST PASSED", jvmversion);
        } catch (final Exception e) {
            LOG.error("Error while checking java.version", e);
        }
        return jvmversion;
    }

    /*
     * <p>
     * set JVM property run.env (System.getPropertu("run.env") to read it)
     * Based on:
     * if not found: run.env JVM property
     * if not found: app.env in application.properties
     * </p>
     */
    public static String whatIsMyEnv() {

        String env = "void";

        // Read Env variable (return null si pas trouvée)
        env = System.getenv().get("ENV_ENV");
        if (env != null) {
            LOG.info("ENV: {} found in env variable ENV_ENV", env);
        }

        if (env == null) {

            LOG.warn("ENV: Variable d'environnement ENV_ENV non trouvée, fallback sur le parametrage JVM");
            env = System.getProperty("run.env", null);
            if (env != null) {
                LOG.info("ENV: {} found in JVM Properties -> System.getProperty('run.env')", env);
            }
        }

        if (env == null) {
            LOG.warn("ENV: System.getProperty(\"run.env\") ne renvoie rien, fallback sur la conf");
            // Read configuration
            final Properties props = ConfigurationReader.getProperties("application");
            try {
                if (props.containsKey("app.env")) {
                    env = props.getProperty("app.env");
                    LOG.info("ENV: {} found in application configuration ", env);
                } else {
                    LOG.warn("ENV Variable clé app.env non trouvée dans application.properties");
                }
            } catch (final Exception e2) {
                throw e2;
            }

        }

        if ("void".equalsIgnoreCase(env) || env == null) {
            LOG.error("FATAL ! No env value found: app.env should be present in application conf, or the ENV_ENV env var should be set on the box, or the jvm property run.env should be set");
            // Oui Sonar c'est justifié
            System.exit(1);
        } else {
            // set env in JVM variables
            env = env.toUpperCase();
            System.setProperty("run.env", env);
            LOG.info("ENV: {} now available in System.getProperty(run.env)", env);
        }
        return env;

    }

    public static void throwExInProd() throws DAOException {
        // Propriété settée à l'injection
        final String currentEnv = System.getProperty("run.env", "PRD");
        if ("PRD".equalsIgnoreCase(currentEnv) || "PROD".equalsIgnoreCase(currentEnv)) {
            throw new DAOException("Mode mock ou dao.dump.to.json activé en production");
        }
    }
}
