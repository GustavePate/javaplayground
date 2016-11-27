
package play.ground.core.dao.mock.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import play.ground.core.dao.mock.business.model.MockableDTOSupaSerializer;
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

public class SupaDAORecordTest  extends AbstractTest {

    private final static Logger LOG = LoggerFactory.getLogger(SupaDAORecordTest.class);

    public static World world;

    public static Injector injector;

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        // java object names in JSON
        // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        // pretty print
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public SupaDAORecordTest() {

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

    
    /*
     * Test l'enregistrement d'un mock json sur un dao simple (string, mockableDTO)
     * on vérifie qu'un fichier est crée
     */
    @Test
    public void test_dao_json_generation() {

        SimpleDAO dao = injector.getInstance(SimpleDAO.class);
        SimpleDTO dto;
        try {
            String mockId = "SimpleDAO.getdata";

            // activate record
            JSONSupaMockManager.conf.setProperty("dao." + mockId + ".mockable.record", "true");
            // deactivate playback
            JSONSupaMockManager.conf.setProperty("dao." + mockId + ".mockable.playback", "false");

            Path mockpath = FileSystems.getDefault().getPath(world.appconf.getProperty("mock.json.write.path"), mockId + ".json");
            // Clean context
            Files.deleteIfExists(mockpath);
            JSONSupaMockManager.test_dropData();

            dto = dao.getdata("ia");
            assertThat(dto.value).isEqualTo("2");
            assertThat(mockpath.toFile()).exists();
        } catch (Exception e) {
            LOG.error("fail", e);
            fail("Exception", e);
        }
    }

    /*
     * Test l'enregistrement d'un mock json sur un dao complex ((MockableDTO, mockableDTO)
     * on vérifie qu'un fichier est crée
     */
    @Test
    public void test_complex_dao_json_generation() throws Exception {
        RandomDAO dao = injector.getInstance(RandomDAO.class);
        ComplexDTOIn dtoin = new ComplexDTOIn();
        dtoin.indata = "io";
        try {
            String mockId = "RandomDAO.getdata";
            // activate record
            JSONSupaMockManager.conf.setProperty("dao." + mockId + ".mockable.record", "true");
            // deactivate playback
            JSONSupaMockManager.conf.setProperty("dao." + mockId + ".mockable.playback", "false");
            Path mockpath = FileSystems.getDefault().getPath(world.appconf.getProperty("mock.json.write.path"), mockId + ".json");

            // Clean context
            Files.deleteIfExists(mockpath);
            JSONSupaMockManager.test_dropData();

            ComplexDTOOut dtoout = dao.getdata(dtoin, dtoin, true);
            assertThat(dtoout.outdata).isEqualToIgnoringCase("io io");
            assertThat(Files.exists(mockpath)).isTrue();

        } catch (Exception e) {
            LOG.error("ARG", e);
            throw e;
        }
    }

    /*
     * Test l'enregistrement d'un mock json sur un dao complex ((MockableDTO, mockableDTO)
     * on vérifie qu'un fichier est crée
     * + on check la déserialisation du fichier généré
     */
    @Test
    public void test_complex_dao_json_multiple_generation() throws Exception {
        RandomDAO dao = injector.getInstance(RandomDAO.class);
        ComplexDTOIn dtoin = new ComplexDTOIn();
        ComplexDTOIn dtoin2 = new ComplexDTOIn();
        try {
            String mockId = "RandomDAO.getdata";
            // txeak conf 
            JSONSupaMockManager.conf.setProperty("dao." + mockId + ".mockable.record", "true");
            JSONSupaMockManager.conf.setProperty("dao." + mockId + ".mockable.playback", "false");
            
            // File to create
            Path mockpath = FileSystems.getDefault().getPath(JSONSupaMockManager.conf.getProperty("mock.json.write.path"), mockId + ".json");
            
            assertThat(Files.exists(mockpath)).isFalse();

            dtoin.indata = "yi";
            ComplexDTOOut dtoout = dao.getdata(dtoin, dtoin2, false);

            dtoin.indata = "ya";
            dtoout = dao.getdata(dtoin, dtoin2, false);

            // un fichier est crée
            LOG.info(mockpath.toString());
            assertThat(Files.exists(mockpath)).isTrue();

            // deserialisation verification de pas de doublons
            List<MockableDTOSupaSerializer> deser;

            File f = mockpath.toFile();

            final TypeReference<List<MockableDTOSupaSerializer>> typeRef = new TypeReference<List<MockableDTOSupaSerializer>>() {
            };

            deser = mapper.readValue(f, typeRef);

            // Il y a une valeur par defaut
            assertThat(deser.stream().filter(x -> x.defaultvalue == true).count()).isEqualTo(1);

            // Il y a 3 entrées dans le fichier
            assertThat(deser.size()).isEqualTo(3);

        } catch (Exception e) {
            LOG.error("ARG", e);
            throw e;
        }
    }
    
    
 
    
    
    
    

}
