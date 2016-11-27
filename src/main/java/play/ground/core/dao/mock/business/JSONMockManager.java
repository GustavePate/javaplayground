
package play.ground.core.dao.mock.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.codec.digest.Md5Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.core.dao.mock.MockableDTO;
import play.ground.core.dao.mock.business.model.MockableDTOSerializer;
import play.ground.exception.DAOException;
import play.ground.helper.EnvHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class JSONMockManager {

    static final Logger LOG = LoggerFactory.getLogger(JSONMockManager.class);

    protected static String DAO_SALT = "daosalt";

    protected static final String DEFAULT_KEY = "default";

    @Inject
    @Named("props.application")
    private static Properties conf;

    protected static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    // configuration local storage
    protected static ConcurrentHashMap<String, Boolean> tomock = new ConcurrentHashMap<>();

    protected static ConcurrentHashMap<String, Boolean> torecord = new ConcurrentHashMap<>();

    // mock data storage
    protected static ConcurrentHashMap<String, HashMap<String, MockableDTOSerializer>> dtoserializers = new ConcurrentHashMap<>();

    protected static ConcurrentHashMap<String, HashMap<String, MockableDTO>> dtosimple = new ConcurrentHashMap<>();

    // locks
    protected static ConcurrentHashMap<String, ReadWriteLock> locks = new ConcurrentHashMap<>();

    // pour les tests
    public static void dropData() {
        locks = new ConcurrentHashMap<>();
        dtosimple = new ConcurrentHashMap<>();
        dtoserializers = new ConcurrentHashMap<>();
    }

    public static <Q> String getHash(final Q query) throws JsonProcessingException {
        // get query hash
        final String jsonQuery = mapper.writeValueAsString(query);
        final String hash = Md5Crypt.apr1Crypt(jsonQuery, DAO_SALT).split("\\$")[3];
        return hash;
    }

    /*
     * Récuperation de l'inputStream de LECTURE des mocks
     */
    private static InputStream getMockInputStream(final String mockId) {

        InputStream in = null;
        final String mockPath = conf.getProperty("dao.all.mockable.playback.json.path") + mockId + ".json";
        try {
            if (conf.containsKey("dao.all.mockable.playback.json.path")) {
                LOG.warn("Lecture du json des mocks depuis {}", mockPath);
                in = new FileInputStream(mockPath);
            } else {
                // Mode nominal on va chercher ça dans le classpath
                LOG.warn("Lecture du json des mocks depuis le classpath {}", mockId + ".json");
                in = JSONMockManager.class.getClassLoader().getResourceAsStream(mockId + ".json");
            }
        } catch (final FileNotFoundException e) {
            LOG.error("Impossible de lire le json des mocks depuis {}", mockPath, e);

        }
        return in;
    }

    /*
     * Récupération du fichier d'ECRITURE des mocks (différent du path de lecture
     */

    private static File getMockWriteFile(final String mockId) {
        // Recuperation du chemin du JSON
        String mockDir = "";
        if (conf.containsKey("dao.all.mockable.record.json.path")) {
            mockDir = conf.getProperty("dao.all.mockable.record.json.path");
        } else {
            mockDir = System.getProperty("java.io.tmpdir");
        }
        final Path path = Paths.get(mockDir, mockId + ".json");
        LOG.warn("Ecriture du json des mocks dans le classpath {}", path.toString());
        return path.toFile();
    }

    private static HashMap<String, MockableDTO> readDTOsFromJsonString(final InputStream in, final String daoName) throws DAOException {
        // Deserialisation du fichier json
        final TypeFactory typeFactory = mapper.getTypeFactory();
        final MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MockableDTO.class);
        HashMap<String, MockableDTO> deser;
        try {
            deser = mapper.readValue(in, mapType);
        } catch (final Exception e) {
            throw new DAOException(e);
        }

        return deser;
    }

    // if(deser.containsKey(query))
    //
    // {
    // res = deser.get(query);
    // LOG.debug("Mock --> Deserialize {} ok: specific value returned", daoName);
    // } else
    //
    // {
    // res = deser.get(DEFAULT_KEY);
    // LOG.debug("Mock --> Deserialize {} ok: default value returned", daoName);
    // } return res;
    //
    // }

    private static HashMap<String, MockableDTOSerializer> readDTOsFromJsonMockableDTO(final InputStream in, final String daoName) throws DAOException {

        // Deserialisation du fichier json
        final TypeFactory typeFactory = mapper.getTypeFactory();
        HashMap<String, MockableDTOSerializer> deser;
        try {
            final MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, MockableDTOSerializer.class);
            deser = mapper.readValue(in, mapType);
        } catch (final Exception e) {
            throw new DAOException(e);
        }

        return deser;

        // String hash = getHash(query);
        //
        // if (deser.containsKey(hash)) {
        // res = deser.get(hash);
        // LOG.debug("Mock --> Deserialize {} ok: specific value returned", daoName);
        // } else {
        // res = deser.get(DEFAULT_KEY);
        // LOG.debug("Mock --> Deserialize {} ok: default value returned", daoName);
        // }
        // return res.out;

    }

    private static <Q> void initMockContext(final String daoName, final String methodname, final Q query, final boolean loaddatafromfile) throws DAOException, IOException {
        final String mockId = daoName + "." + methodname;

        // Explose si production
        EnvHelper.throwExInProd();

        if (!locks.containsKey(mockId)) {

            // entry exists ?
            final boolean stringreq = query instanceof String;
            final boolean dtoreq = query instanceof MockableDTO;

            if (!(stringreq || dtoreq)) {
                throw new DAOException("Not possible to manage MockData for a " + query.getClass().getSimpleName() + " object, first argument of DAO should be a request, either a String or a MockableDTO");
            }

            // if not exists create empty entry
            if (dtoreq && !dtoserializers.containsKey(mockId)) {

                HashMap<String, MockableDTOSerializer> data = null;

                // Pour les mocks
                if (loaddatafromfile) {
                    final InputStream in = getMockInputStream(mockId);
                    if (in != null) {
                        data = readDTOsFromJsonMockableDTO(in, mockId);
                        in.close();
                    }
                }

                if (data == null) {
                    data = new HashMap<String, MockableDTOSerializer>();
                }

                dtoserializers.put(mockId, data);

            }

            if (stringreq && !dtosimple.containsKey(mockId)) {

                HashMap<String, MockableDTO> data = null;

                // Pour les mocks
                if (loaddatafromfile) {
                    final InputStream in = getMockInputStream(mockId);
                    if (in != null) {
                        data = readDTOsFromJsonString(in, mockId);
                        in.close();
                    }
                }

                if (data == null) {
                    data = new HashMap<String, MockableDTO>();
                }
                dtosimple.put(mockId, data);

            }

            locks.put(mockId, new ReentrantReadWriteLock());
        }

    }

    private static void recordStringReq(final String classname, final String methodname, final String req, final MockableDTO resp) {
        final String mockId = classname + "." + methodname;

        // get datamap for this dao/method
        final HashMap<String, MockableDTO> datamap = dtosimple.get(mockId);
        // remove record if exists
        if (datamap.containsKey(req)) {
            datamap.remove(req);
        }
        // create default if empty
        if (datamap.size() == 0) {
            datamap.put(DEFAULT_KEY, resp);
        }

        datamap.put(req, resp);

        try {
            final File in = getMockWriteFile(mockId);
            mapper.writeValue(in, datamap);
        } catch (final Exception e) {
            LOG.error("Impossible d'enregistrer le JSON pour {}", mockId, e);
        }
    }

    private static void recordMockableDTOReq(final String classname, final String methodname, final MockableDTO req, final MockableDTO resp) throws JsonProcessingException {
        final String mockId = classname + "." + methodname;

        // get datamap for this dao/method
        final HashMap<String, MockableDTOSerializer> datamap = dtoserializers.get(mockId);

        // get hash for req
        final String hash = getHash(req);

        // remove record if exists
        if (datamap.containsKey(hash)) {
            datamap.remove(hash);
        }

        final MockableDTOSerializer serialized = new MockableDTOSerializer(req, resp);

        // create default if empty
        if (datamap.size() == 0) {
            datamap.put(DEFAULT_KEY, serialized);
        }

        datamap.put(hash, serialized);

        try {
            final File in = getMockWriteFile(mockId);
            mapper.writeValue(in, datamap);
        } catch (final Exception e) {
            LOG.error("Impossible d'enregistrer le JSON pour {}", mockId, e);
        }

    }

    public static <Q> void record(final String classname, final String methodname, final Q req, final Object resp) throws DAOException, IOException {
        final String mockId = classname + "." + methodname;

        initMockContext(classname, methodname, req, false);
        final ReadWriteLock lock = locks.get(mockId);
        lock.writeLock().lock();
        try {

            final boolean stringreq = req instanceof String;
            final boolean dtoreq = req instanceof MockableDTO;

            // if mockdata exists
            if (stringreq) {
                recordStringReq(classname, methodname, (String) req, (MockableDTO) resp);
            } else if (dtoreq) {
                recordMockableDTOReq(classname, methodname, (MockableDTO) req, (MockableDTO) resp);
            } else {
                LOG.error("Mock Record disabled: the first argument of your DAO method should be the DTOIn or a String");
            }

        } finally {
            lock.writeLock().unlock();
        }
    }

    public static <Q> MockableDTO getDTOFromJson(final String daoName, final String methodname, final Q query) throws DAOException, IOException {
        final String mockId = daoName + "." + methodname;
        initMockContext(daoName, methodname, query, true);
        final ReadWriteLock lock = locks.get(mockId);
        lock.readLock().lock();
        MockableDTO res = null;

        try {

            final boolean stringreq = query instanceof String;
            final boolean dtoreq = query instanceof MockableDTO;

            if (!(stringreq || dtoreq)) {
                throw new DAOException("Not possible to retrieve MockData for a " + query.getClass().getSimpleName() + " object, request should be a String or a MockableDTO");
            }

            // Test if file exists, if not throw DAOException

            if (stringreq) {
                res = getMockDataForString(mockId, (String) query);
            } else if (dtoreq) {
                res = getMockDataForDTO(mockId, (MockableDTO) query);
            } else {
                LOG.error("Mock --> Deserialize {} ko: seuls les mockableDTO et les String sont deserialisable", daoName);
            }

            if ("true".equalsIgnoreCase(conf.getProperty("dao.all.mock.will.sleep", "false"))) {

                if (conf.containsKey("dao." + mockId + ".mock.sleep")) {
                    try {
                        final Long sleeptime = Long.valueOf(conf.getProperty("dao." + mockId + ".mock.sleep"));
                        LOG.info("Mock --> Sleep de {} ms", sleeptime);
                        Thread.sleep(sleeptime);
                    } catch (final Exception e) {
                        LOG.error("Mock -> sleep error", e);
                    }
                }
            }

        } catch (final Exception e) {
            LOG.error("Mock --> deserialize {} failed: ", daoName, e);
            throw new DAOException(e);

        } finally {
            lock.readLock().unlock();
        }

        if (res == null) {
            throw new DAOException("pas de réponse appropriée et valeur par defaut non trouvée pour " + mockId);
        }

        return res;
    }

    private static MockableDTO getMockDataForDTO(final String mockId, final MockableDTO req) throws JsonProcessingException {

        MockableDTO res = null;

        final String hash = getHash(req);

        if (dtoserializers.get(mockId).containsKey(hash)) {
            res = (MockableDTO) dtoserializers.get(mockId).get(hash).getResp();
            LOG.debug("Mock --> Deserialize {} ok: specific value returned", mockId);
        } else {
            final MockableDTOSerializer ser = dtoserializers.get(mockId).get(DEFAULT_KEY);
            if (ser != null) {
                res = (MockableDTO) ser.getResp();
                LOG.debug("Mock --> Deserialize {} ok: default value returned", mockId);
            } else {
                LOG.error("Mock --> Deserialize {} KO: no default value found", mockId);
            }
        }

        return res;
    }

    private static MockableDTO getMockDataForString(final String mockId, final String req) {
        // TODO Auto-generated method stub
        MockableDTO res = null;

        if (dtosimple.get(mockId).containsKey(req)) {
            res = dtosimple.get(mockId).get(req);
            LOG.debug("Mock --> Deserialize {} ok: specific value returned", mockId);
        } else {
            res = dtosimple.get(mockId).get(DEFAULT_KEY);
            LOG.debug("Mock --> Deserialize {} ok: default value returned", mockId);
        }
        return res;
    }

    /*
     * if not in tomock Map read conf and store in torecord Map
     */
    public static boolean shouldIMock(final String classname, final String methodname) {
        // TODO Auto-generated method stub
        boolean res = false;

        final String PREFIX = "dao.";
        final String SUFFIX = ".mockable.playback";

        final String key = classname + "." + methodname;
        if (tomock.contains(key)) {
            res = tomock.get(key);
        } else {
            // Read conf

            if (conf.containsKey(PREFIX + "all" + SUFFIX)) {
                if ("true".equalsIgnoreCase(conf.getProperty(PREFIX + "all" + SUFFIX))) {
                    res = true;
                }
            }

            if (conf.containsKey(PREFIX + classname + SUFFIX)) {
                if ("true".equalsIgnoreCase(conf.getProperty(PREFIX + classname + SUFFIX))) {
                    res = true;
                }
            }

            if (conf.containsKey(PREFIX + key + SUFFIX)) {
                if ("true".equalsIgnoreCase(conf.getProperty(PREFIX + key + SUFFIX))) {
                    res = true;
                }
            }

            // Store result
            tomock.put(key, res);
        }

        return res;
    }

    /*
     * if not in torecord Map read conf and store in torecord Map
     */
    public static boolean shouldIRecord(final String classname, final String methodname) {
        boolean res = false;

        final String PREFIX = "dao.";
        final String SUFFIX = ".mockable.record";

        final String key = classname + "." + methodname;
        if (torecord.contains(key)) {
            res = torecord.get(key);
        } else {
            // Read conf
            if (conf.containsKey(PREFIX + "all" + SUFFIX)) {
                if ("true".equalsIgnoreCase(conf.getProperty(PREFIX + "all" + SUFFIX))) {
                    res = true;
                }
            }
            if (conf.containsKey(PREFIX + classname + SUFFIX)) {
                if ("true".equalsIgnoreCase(conf.getProperty(PREFIX + classname + SUFFIX))) {
                    res = true;
                }
            }

            if (conf.containsKey(PREFIX + key + SUFFIX)) {
                if ("true".equalsIgnoreCase(conf.getProperty(PREFIX + key + SUFFIX))) {
                    res = true;
                }

                // Store result
                torecord.put(key, res);
            }

        }

        return res;

    }
}
