
package play.ground.core.dao.mock.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
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
import play.ground.core.dao.mock.complexdao.ComplexDAO;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOIn;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOOut;
import play.ground.core.dao.mock.oldstuctdao.OldStructDAO;
import play.ground.core.dao.mock.oldstuctdao.dto.InnerDTO;
import play.ground.core.dao.mock.oldstuctdao.dto.OldStructDTOIn;
import play.ground.core.dao.mock.oldstuctdao.impl.OldStructDAODefault;
import play.ground.core.dao.mock.randomdao.RandomDAO;
import play.ground.core.dao.mock.randomdao.impl.RandomDAODefault;
import play.ground.core.dao.mock.simpledao.SimpleDAO;
import play.ground.core.dao.mock.simpledao.dto.SimpleDTO;
import play.ground.core.dao.mock.simpledao.impl.SimpleDAODefault;
import play.ground.core.dao.mock.staticdao.dto.StaticDTO;
import play.ground.core.dao.mock.staticdao.impl.StaticDAODefault;
import play.ground.utils.AbstractTest;
import play.ground.utils.JunitModule;
import play.ground.utils.World;

public class SupaDAORecAndPlaybackTest extends AbstractTest {

    private final static Logger LOG = LoggerFactory.getLogger(SupaDAORecAndPlaybackTest.class);

    public static World world;

    public static Injector injector;

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    }

    public SupaDAORecAndPlaybackTest() {

    }

    /* permet de tester l'enregistrement de mock */
    public static class ThisTestModule extends AbstractModule {

        public void configure() {

            bind(SimpleDAO.class).to(SimpleDAODefault.class);
            bind(OldStructDAO.class).to(OldStructDAODefault.class);
            bind(RandomDAO.class).to(RandomDAODefault.class);
            bind(StaticDAODefault.class);
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

    @Test
    public void test_dto_mock_call() {

        RandomDAO dao = injector.getInstance(RandomDAO.class);
        String mockId = "RandomDAO.getdata";

        ComplexDTOIn in1;
        ComplexDTOIn in2;
        ComplexDTOOut out;
        try {

            world.appconf.setProperty("mock.json.read.path", "/tmp");
            world.appconf.setProperty("mock.json.write.path", "/tmp");

            // Drop eventually existing file
            Path json = Paths.get("/tmp", mockId + ".json");
            Files.deleteIfExists(json);

            /**
             * Record something
             */

            world.appconf.setProperty("dao." + mockId + ".mockable.record", "true");
            world.appconf.setProperty("dao" + mockId + ".mock.sleep", "0");
            world.appconf.setProperty("dao." + mockId + ".mockable.playback", "false");
            in1 = new ComplexDTOIn();
            in2 = new ComplexDTOIn();

            // first record == default value
            in1.indata = "default";
            in2.indata = "value";
            // verify recorded behavior
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("default value");

            // other record ==> specific value
            in1.indata = "specific";
            in2.indata = "value";
            // verify recorded behavior
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("specific value");

            // Empty stored data
            JSONSupaMockManager.test_dropData();

            /***
             * Play it back
             */

            world.appconf.setProperty("dao." + mockId + ".mockable.record", "false");
            world.appconf.setProperty("dao" + mockId + ".mock.sleep", "0");
            world.appconf.setProperty("dao." + mockId + ".mockable.playback", "true");

            // hit the default value
            in1.indata = null;
            in2.indata = null;
            // verify recorded behavior
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("default value");

            // hit the specific value
            in1.indata = "specific";
            in2.indata = "value";
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("specific value");

        } catch (Exception e) {
            LOG.error("fail", e);
            fail("Exception", e);
        }
    }

    private class ComplexDTORequestStorage {

        ComplexDTOIn in1;

        ComplexDTOIn in2;

        boolean boul;

    }

    /*
     * Test l'enregistrement d'un mock json sur un dao complex ((MockableDTO, mockableDTO)
     * + on check la déserialisation du fichier généré
     * + on reuse les variables d'entrée pour verifier qu'on ne garde par de référence
     * au niveau JSONMockManager des variables utilisées dans le code appelant
     */
    @Test
    @Ignore
    public void test_decouplage_persistence() throws Exception {
        RandomDAO dao = injector.getInstance(RandomDAO.class);
        String mockId = "RandomDAO.getdata";

        ComplexDTOIn in1;
        ComplexDTOIn in2;
        ComplexDTOOut out;
        try {

            world.appconf.setProperty("mock.json.read.path", "/tmp");
            world.appconf.setProperty("mock.json.write.path", "/tmp");

            /**
             * Record something
             */

            world.appconf.setProperty("dao." + mockId + ".mockable.record", "true");
            world.appconf.setProperty("dao" + mockId + ".mock.sleep", "0");
            world.appconf.setProperty("dao." + mockId + ".mockable.playback", "false");
            in1 = new ComplexDTOIn();
            in2 = new ComplexDTOIn();

            // first record == default value
            in1.indata = "yi";
            in2.indata = "yi";
            // verify recorded behavior
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("yi yi");

            // other record ==> specific value
            in1.indata = "yo";
            in2.indata = "yo";
            // verify recorded behavior
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("yo yo");

            // other record ==> specific value
            in1.indata = "ya";
            in2.indata = "ya";
            // verify recorded behavior
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("ya ya");

            // Empty stored data
            JSONSupaMockManager.test_dropData();

            /***
             * Play it back
             */

            world.appconf.setProperty("dao." + mockId + ".mockable.record", "false");
            world.appconf.setProperty("dao" + mockId + ".mock.sleep", "0");
            world.appconf.setProperty("dao." + mockId + ".mockable.playback", "true");

            // hit the default value
            in1.indata = null;
            in2.indata = null;
            // verify recorded behavior
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("yi yi");

            Collection<MockableDTOSupaSerializer> objs = JSONSupaMockManager.test_getRawData().get(mockId).values();

            // une seule valeur par defaut
            assertThat(objs.stream().filter(x -> x.defaultvalue).count()).isEqualTo(1);

            // transformation des inputs en type de base
            // fonctionnalité inutile de base uniquement pour les test

            ArrayList<ComplexDTORequestStorage> tmpStorage = new ArrayList<>();

            for (MockableDTOSupaSerializer ser : objs) {

                LOG.info("{}", ser.request);

                // mapper for
                ComplexDTOIn in_deser = mapper.convertValue(ser.request[0], ComplexDTOIn.class);

            }

            // hit a specific value
            in1.indata = "yo";
            in2.indata = "yo";
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("yo yo");

            // hit a specific value
            in1.indata = "ya";
            in2.indata = "ya";
            out = dao.getdata(in1, in2, true);
            assertThat(out.outdata).isEqualTo("ya ya");

            // il y a une seule entrée yi

            // il y a une seule entrée ya

            // il y a une seule entrée yo

        } catch (Exception e) {
            LOG.error("ARG", e);
            throw e;
        }

    }


    @Test
    public void test_very_complex_object_serdeser() throws Exception {
        assertThat(false).isTrue();
    }

    @Test
    public void test_vector_record_playback() throws Exception {
        OldStructDAO dao = injector.getInstance(OldStructDAO.class);
        String mockId = "OldStructDAO.getdata";

        OldStructDTOIn in;
        OldStructDTOIn out;
        try {

            world.appconf.setProperty("mock.json.read.path", "/tmp");
            world.appconf.setProperty("mock.json.write.path", "/tmp");

            /**
             * Record something
             */

            world.appconf.setProperty("dao." + mockId + ".mockable.record", "true");
            world.appconf.setProperty("dao" + mockId + ".mock.sleep", "0");
            world.appconf.setProperty("dao." + mockId + ".mockable.playback", "false");
            in = new OldStructDTOIn();

            assertThat(((InnerDTO) in.vec.get(0)).arr.size()).isEqualTo(5);

            out = dao.getdata(in);

            JSONSupaMockManager.test_dropData();

            /***
             * Play it back
             */

            world.appconf.setProperty("dao." + mockId + ".mockable.record", "false");
            world.appconf.setProperty("dao" + mockId + ".mock.sleep", "0");
            world.appconf.setProperty("dao." + mockId + ".mockable.playback", "true");

            out = dao.getdata(in);
            assertThat(out.vec.size()).isEqualTo(1);

            // check d'un objet complex à l'interieur de la structure
            assertThat(((InnerDTO) out.vec.get(0)).arr).hasSize(5);

        } catch (Exception e) {
            LOG.error("ARG", e);
            throw e;
        }

    }

    /**
     * Test de l'annotation mockable sur une méthode statique
     * 
     * @throws Exception
     */

    @Test
    public void test_static_record_playback() throws Exception {
        StaticDAODefault dao = injector.getInstance(StaticDAODefault.class);
        String mockId = "StaticDAO.isodd";

        StaticDTO out;
        try {

            world.appconf.setProperty("mock.json.read.path", "/tmp");
            world.appconf.setProperty("mock.json.write.path", "/tmp");

            /**
             * Record something
             */

            world.appconf.setProperty("dao." + mockId + ".mockable.record", "true");
            world.appconf.setProperty("dao" + mockId + ".mock.sleep", "0");
            world.appconf.setProperty("dao." + mockId + ".mockable.playback", "false");

            out = dao.isodd("test");
            assertThat(out.res).isTrue();

            JSONSupaMockManager.test_dropData();

            /***
             * Play it back
             */

            world.appconf.setProperty("dao." + mockId + ".mockable.record", "false");
            world.appconf.setProperty("dao" + mockId + ".mock.sleep", "0");
            world.appconf.setProperty("dao." + mockId + ".mockable.playback", "true");

            out = dao.isodd("yop");
            assertThat(out.res).isTrue();

        } catch (Exception e) {
            LOG.error("ARG", e);
            throw e;
        }

    }

    @Test
    public void no_type_vector() {

        ObjectMapper tmpMapper = new ObjectMapper();
        tmpMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        tmpMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

    }


}
