
package play.ground.core.dao.mock.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.util.Modules;

import play.ground.core.dao.mock.Mockable;
import play.ground.core.dao.mock.annotation.MockableSupaInterceptor;
import play.ground.core.dao.mock.business.JSONSupaMockManager;
import play.ground.core.dao.mock.complexdao.ComplexDAO;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOIn;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOOut;
import play.ground.core.dao.mock.randomdao.RandomDAO;
import play.ground.core.dao.mock.randomdao.impl.RandomDAODefault;
import play.ground.core.dao.mock.simpledao.SimpleDAO;
import play.ground.core.dao.mock.simpledao.dto.SimpleDTO;
import play.ground.core.dao.mock.simpledao.impl.SimpleDAODefault;
import play.ground.utils.AbstractTest;
import play.ground.utils.JunitModule;
import play.ground.utils.World;

public class SupaDAOPlaybackTest  extends AbstractTest {

    private final static Logger LOG = LoggerFactory.getLogger(SupaDAOPlaybackTest.class);

    public static World world;

    public static Injector injector;

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    }

    public SupaDAOPlaybackTest() {

    }

    /* permet de tester l'enregistrement de mock */
    public static class ThisTestModule extends AbstractModule {

        public void configure() {
            
            bind(SimpleDAO.class).to(SimpleDAODefault.class);
            bind(RandomDAO.class).to(RandomDAODefault.class);
            // bind(DTODAO.class).to(DTODAODefault.class);
            bindInterceptor(Matchers.any(), Matchers.annotatedWith(Mockable.class), new MockableSupaInterceptor());
            requestStaticInjection(JSONSupaMockManager.class);
            requestStaticInjection(DAOMockTools.class);
        }

    }

    @BeforeClass
    public static void setUpclass() {
        LOG.info("before class");

        // Inject de World
        injector = Guice.createInjector(Modules.override(new JunitModule()).with(new ThisTestModule()));
        world = injector.getInstance(World.class);

    }

    @Before
    public void init() {
		DAOMockTools.before();
    }
    
    @After
    public void after() {
    	DAOMockTools.after();
    }
    


    /* test de la prise en compte du parametrage sleep */
    @Test
    public void test_string_mock_call_sleep() throws Exception {

        SimpleDAO dao = injector.getInstance(SimpleDAO.class);
        SimpleDTO dto;

        String SLEEP_MS = "1000";

        String mockId = "SimpleDAO.getdata";
        // deactivate record
        JSONSupaMockManager.test_getConf().setProperty("dao." + mockId + ".mockable.record", "false");
        // activate sleep
        JSONSupaMockManager.test_getConf().setProperty("dao." + mockId + ".mock.sleep", SLEEP_MS);
        // mock mod
        JSONSupaMockManager.test_getConf().setProperty("dao." + mockId + ".mockable.playback", "true");
        // activate sleep
        JSONSupaMockManager.test_getConf().setProperty("dao.all.mock.will.sleep", "true");

        
        
        // time default behavior
        long startTime = System.currentTimeMillis();
        dto = dao.getdata("sleep_please");
        assertThat(dto.value).isEqualTo("4");
        long elapsed = new Date().getTime() - startTime + 1;
        // assert
        assertThat(elapsed).isGreaterThanOrEqualTo(Long.valueOf(SLEEP_MS));

    }

    /* test de la prise en compte de l'entrée par defaut */
    @Test
    public void test_playback_default() throws Exception {

        SimpleDAO dao = injector.getInstance(SimpleDAO.class);
        SimpleDTO dto;

        String mockId = "SimpleDAO.getdata";
        world.appconf.setProperty("dao." + mockId + ".mockable.record", "false");
        world.appconf.setProperty("dao." + mockId + ".mockable.playback", "true");

        // time default behavior
        dto = dao.getdata("random_non_existant_key");
        assertThat(dto.value).isEqualTo("29");
        

    } 
    
    

}
