
package play.ground.core.dao.mock;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.core.dao.AbstractDAO;
import play.ground.core.dao.mock.business.model.MockableDTOSerializer;
import play.ground.exception.DAOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public abstract class AbstractMock extends AbstractDAO {

    static final Logger LOG = LoggerFactory.getLogger(AbstractMock.class);

    public final static String STUB_KEY = "default";

    public static class DefaultMockableDTO implements MockableDTO {

        public static final String defaultValue = STUB_KEY;
    }

    private InputStream readMockData(final String daoName) {

        InputStream in;
        final String mockPath = props.getProperty("mock.json.read.path") + daoName + ".json";
        try {
            if (props.containsKey("mock.json.read.path")) {
                LOG.warn("Lecture du json des mocks dans {}", mockPath);
                in = new FileInputStream(mockPath);
            } else {
                // Mode nominal on va chercher ça dans le classpath
                in = this.getClass().getClassLoader().getResourceAsStream(daoName + ".json");
            }
        } catch (final FileNotFoundException e) {
            LOG.warn("Impossible de lire le json des mocks depuis {}", mockPath, e);
            in = null;
        }
        return in;
    }

    private MockableDTO getFromJsonString(final String query, final InputStream in, final String daoName) throws JsonParseException, JsonMappingException, IOException {
        MockableDTO res = null;
        // Deserialisation du fichier json
        final TypeFactory typeFactory = mapper.getTypeFactory();
        final MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MockableDTO.class);

        final HashMap<String, MockableDTO> deser = mapper.readValue(in, mapType);

        if (deser.containsKey(query)) {
            res = deser.get(query);
            LOG.debug("Mock --> Deserialize {} ok: specific value returned", daoName);
        } else {
            res = deser.get(DEFAULT_KEY);
            LOG.debug("Mock --> Deserialize {} ok: default value returned", daoName);
        }
        return res;

    }

    private MockableDTO getFromJsonMockableDTO(final MockableDTO query, final InputStream in, final String daoName) throws JsonParseException, JsonMappingException, IOException {

        MockableDTOSerializer res = null;
        // Deserialisation du fichier json
        final TypeFactory typeFactory = mapper.getTypeFactory();
        final MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MockableDTOSerializer.class);

        final HashMap<String, MockableDTOSerializer> deser = mapper.readValue(in, mapType);

        final String hash = getHash(query);

        if (deser.containsKey(hash)) {
            res = deser.get(hash);
            LOG.debug("Mock --> Deserialize {} ok: specific value returned", daoName);
        } else {
            res = deser.get(DEFAULT_KEY);
            LOG.debug("Mock --> Deserialize {} ok: default value returned", daoName);
        }
        return (MockableDTO) res.resp;

    }

    protected <Q> MockableDTO getFromJson(final Q query) throws DAOException {

        final String daoName = getMockFileName();
        MockableDTO res = null;

        try {
            final InputStream in = readMockData(daoName);

            // Explose si production
            checkenv();

            if (query instanceof MockableDTO) {
                res = getFromJsonMockableDTO((MockableDTO) query, in, daoName);
            } else if (query instanceof String) {
                res = getFromJsonString((String) query, in, daoName);
            } else {
                LOG.debug("Mock --> Deserialize {} ko: seuls les mockableDTO et les String sont deserialisable", daoName);
            }

        } catch (final Exception e) {
            LOG.error("Mock --> deserialize {} failed: ", daoName);
            throw new DAOException(e);
        }

        if (res == null) {
            throw new DAOException("pas de réponse appropriée (valeur par defaut non trouvée");
        }
        return res;
    }

}
