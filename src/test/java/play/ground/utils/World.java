
package play.ground.utils;

import java.util.Properties;

import play.ground.helper.EnvHelper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class World {

    /*
     * classe singleton utils qui permettra de passer des paramètres / metriques
     * entre différents tests
     */
    @Named("props.test")
    @Inject
    public Properties testconf;

    @Named("props.application")
    @Inject
    public Properties appconf;

    public String suiteName = "unit tests";

    public String env;

    @Inject
    private World() {
        env = EnvHelper.whatIsMyEnv();
    }

}
