
package play.ground.core.dao.mock.business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import play.ground.core.dao.mock.business.SupaDAOPlaybackTest.ThisTestModule;
import play.ground.core.dao.mock.business.model.MockableDTOSupaSerializer;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOIn;
import play.ground.core.dao.mock.randomdao.RandomDAO;
import play.ground.core.dao.mock.randomdao.impl.RandomDAODefault;
import play.ground.core.dao.mock.simpledao.SimpleDAO;
import play.ground.core.dao.mock.simpledao.impl.SimpleDAODefault;
import play.ground.utils.AbstractTest;
import play.ground.utils.JunitModule;
import play.ground.utils.World;

public class SimpleTestOnJacksonMapper extends AbstractTest{

    private final static Logger LOG = LoggerFactory.getLogger(SimpleTestOnJacksonMapper.class);

    public static World world;

    public static Injector injector; 
    
    
    /* permet de tester l'enregistrement de mock */
    public static class ThisTestModule extends AbstractModule {

        public void configure() {
            
            bind(SimpleDAO.class).to(SimpleDAODefault.class);
            bind(RandomDAO.class).to(RandomDAODefault.class);
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
    
    
    @Ignore
    @Test
    public void testJacksonMapper() {

        ComplexDTOIn dtoin = new ComplexDTOIn();
        dtoin.indata = "dto1";
        ComplexDTOIn dtoin2 = new ComplexDTOIn();
        dtoin.indata = "dto2";

        Object[] arr = new Object[2];
        arr[0] = dtoin;
        arr[1] = dtoin2;

        ArrayList<ComplexDTOIn> arrl = new ArrayList<>();
        arrl.add(dtoin);
        arrl.add(dtoin2);

        ArrayList<MockableDTOSupaSerializer> suparr = new ArrayList<>();
        suparr.add(new MockableDTOSupaSerializer(arr, dtoin, dtoin.getClass().getCanonicalName()));
        suparr.add(new MockableDTOSupaSerializer(arr, dtoin, dtoin.getClass().getCanonicalName()));

        ObjectMapper thismapper = new ObjectMapper();
        // thismapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        thismapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            String res = "";
            // res = thismapper.writeValueAsString(arr);
            // LOG.info(res);
            // res = thismapper.writeValueAsString(arrl);
            // LOG.info(res);

            res = thismapper.writeValueAsString(suparr);
            LOG.info(res);

            final TypeReference<List<MockableDTOSupaSerializer>> typeRef = new TypeReference<List<MockableDTOSupaSerializer>>() {
            };

            try {
                List<MockableDTOSupaSerializer> deser = thismapper.readValue(res, typeRef);
                LOG.info("size: {}", deser.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    
    @Test
    public void confchanger(){
    
    	LOG.info("world: {}", world.appconf);
    	world.appconf.put("test","xxxxxxxxxxxxxxxxxxxxxxxx");
    	LOG.info("Manager: {}", JSONSupaMockManager.test_getConf());
    	
    	
    	
    }
}
