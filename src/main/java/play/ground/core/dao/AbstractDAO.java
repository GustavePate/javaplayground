
package play.ground.core.dao;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.codec.digest.Md5Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.core.dao.mock.AbstractMock;
import play.ground.core.dao.mock.MockableDTO;
import play.ground.core.dao.mock.business.model.MockableDTOSerializer;
import play.ground.exception.DAOException;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

public abstract class AbstractDAO {

    protected static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    }

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractDAO.class);

    @Inject
    protected MetricRegistry metrics;

    protected boolean iserror = false;

    @Named("props.application")
    @Inject
    protected Properties props;
    
	@Inject
	@Named("conf")
	protected Config conf;

    protected String DAO_SALT = "daosalt";

    public static final String DEFAULT_KEY = "default";

    public static String getDAOInterfaceName(Class<?> klass) {

        // get DAO Interface name
        String realDaoInterfaceSimpleName = "randomDao";

        LOG.debug("getDAOName for: " + klass.getCanonicalName());
        if (klass.getCanonicalName().contains("EnhancerByGuice")) {
            klass = klass.getSuperclass();
            LOG.debug("guiceenhancer found, now gettingDAOName for: " + klass.getCanonicalName());
        }

        final Class<?>[] interfaceList = klass.getInterfaces();
        if (interfaceList.length > 1) {
            String candidate = "";
            for (final Class<?> element : interfaceList) {
                candidate = element.getSimpleName();
                if (candidate.endsWith("DAO") && candidate != "AbstractDAO") {
                    realDaoInterfaceSimpleName = candidate;
                    break;
                }
            }
        } else if (interfaceList.length == 1) {
            realDaoInterfaceSimpleName = klass.getInterfaces()[0].getSimpleName();
        } else {
            LOG.warn("Your DAO should implement an interface which ends with DAO, like this 'MyNameForThisDAO' data stored in " + realDaoInterfaceSimpleName + ".json");
        }
        return realDaoInterfaceSimpleName;
    }

    // a overrider si DAO contenant plusieurs méthodes à mocker dans des fichiers json séparés
    protected String getMockFileName() {
        return getDAOInterfaceName(this.getClass());
    }

    protected <Q> String getHash(final Q query) throws JsonProcessingException {
        // get query hash
        final String jsonQuery = mapper.writeValueAsString(query);
        final String hash = Md5Crypt.apr1Crypt(jsonQuery, DAO_SALT).split("\\$")[3];
        return hash;
    }

    protected void checkenv() throws DAOException {

        // Propriété settée à l'injection
        final String currentEnv = System.getProperty("run.env", "PRD");
        if ("PRD".equalsIgnoreCase(currentEnv) || "PROD".equalsIgnoreCase(currentEnv)) {
            throw new DAOException("Mode mock ou dao.dump.to.json activé en production");
        }

    }

    private File getMockWriteFile(final String daoName) {
        // Recuperation du chemin du JSON
        String mockDir = "";
        if (props.containsKey("mock.json.write.path")) {
            mockDir = props.getProperty("mock.json.write.path");
        } else {
            mockDir = System.getProperty("java.io.tmpdir");
        }
        final Path path = Paths.get(mockDir, daoName + ".json");
        return path.toFile();
    }

    private void dump2jsonMockableDTO(final MockableDTO query, final MockableDTO resp, final File in) throws DAOException {

        final MockableDTOSerializer toser = new MockableDTOSerializer();
        toser.req = query;
        toser.resp = resp;

        final TypeFactory typeFactory = mapper.getTypeFactory();
        final MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MockableDTOSerializer.class);

        try {

            HashMap<String, MockableDTOSerializer> deser;

            // si le fichier existe
            if (in.exists()) {
                deser = mapper.readValue(in, mapType);
            } else {
                deser = new HashMap<String, MockableDTOSerializer>();
            }

            final String hash = getHash(query);

            // delete eventual existing key
            if (deser.containsKey(hash)) {
                deser.remove(hash);
            }

            // create default entry if none found
            if (!deser.containsKey(DEFAULT_KEY)) {

                // Create a default entry is default one
                final MockableDTOSerializer toser_default = new MockableDTOSerializer();
                toser_default.req = new AbstractMock.DefaultMockableDTO();
                toser_default.resp = resp;
                deser.put(DEFAULT_KEY, toser);
            }
            deser.put(hash, toser);

            // Then Serialize it
            mapper.writeValue(in, deser);
            LOG.info("Sauvegarde la reponse {} en json dans {}", resp.getClass().getSimpleName(), in.getAbsolutePath());
        } catch (final Exception e) {
            throw new DAOException(String.format("Impossible de sauvegarder la reponse %s en json dans %s}", resp.getClass().getSimpleName(), in.getAbsolutePath()), e);
        }
    }

    private void dump2jsonString(final String query, final MockableDTO resp, final File in) throws DAOException {

        final TypeFactory typeFactory = mapper.getTypeFactory();
        final MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MockableDTO.class);

        try {

            HashMap<String, MockableDTO> deser;

            // si le fichier existe
            if (in.exists()) {
                deser = mapper.readValue(in, mapType);
            } else {
                deser = new HashMap<String, MockableDTO>();
            }

            // delete eventual existing key
            if (deser.containsKey(query)) {
                deser.remove(query);
            }

            // create default entry if none found
            if (!deser.containsKey(DEFAULT_KEY)) {
                deser.put(DEFAULT_KEY, resp);
            }
            deser.put(query, resp);

            // Then Serialize it
            mapper.writeValue(in, deser);
            LOG.info("Sauvegarde la reponse {} en json dans {}", resp.getClass().getSimpleName(), in.getAbsolutePath());
        } catch (final Exception e) {
            throw new DAOException(String.format("Impossible de sauvegarder la reponse %s en json dans %s", resp.getClass().getSimpleName(), in.getAbsolutePath()), e);
        }
    }

    protected <Q> void dump2json(final Q query, final MockableDTO resp) throws DAOException {
        if (props.getProperty("dao.dump.to.json", "false").equals("true")) {

            checkenv();

            try {

                final String daoName = getMockFileName();
                final File in = getMockWriteFile(daoName);
                if (query instanceof MockableDTO) {
                    dump2jsonMockableDTO((MockableDTO) query, resp, in);
                } else if (query instanceof String) {
                    dump2jsonString((String) query, resp, in);
                } else {
                    throw new DAOException("On ne peut serialiser en json que les dto entrée de type String ou MockableDTO");
                }

            } catch (final Exception e) {
                throw e;
            }
        }

    }

}
