
package play.ground.core.dao.mock.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.codec.digest.Md5Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import play.ground.core.dao.mock.MockableDTO;
import play.ground.core.dao.mock.business.model.MockableDTOSerializer;
import play.ground.core.dao.mock.business.model.MockableDTOSupaSerializer;
import play.ground.exception.DAOException;
import play.ground.helper.EnvHelper;

public class JSONSupaMockManager {

    static final Logger LOG = LoggerFactory.getLogger(JSONSupaMockManager.class);

    protected static String DAO_SALT = "daosalt";

    protected static final String DEFAULT_KEY = "default";

    @Inject
    @Named("props.application")
    protected static Properties conf;

    protected static final ObjectMapper mapper = new ObjectMapper();

    static {
        // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    // configuration local storage
    protected static ConcurrentHashMap<String, Boolean> tomock = new ConcurrentHashMap<>();

    protected static ConcurrentHashMap<String, Boolean> torecord = new ConcurrentHashMap<>();

    // mock data storage
    protected static ConcurrentHashMap<String, HashMap<String, MockableDTOSupaSerializer>> objArraySerializers = new ConcurrentHashMap<>();

    /**
     * Mock data storage
     * key: mockid = "classname.methodname"
     * value: map hash / MockableDTOSerializer
     */
    protected static ConcurrentHashMap<String, HashMap<String, MockableDTOSerializer>> dtoserializers = new ConcurrentHashMap<>();

    protected static ConcurrentHashMap<String, HashMap<String, MockableDTO>> dtosimple = new ConcurrentHashMap<>();

    // locks
    protected static ConcurrentHashMap<String, ReadWriteLock> locks = new ConcurrentHashMap<>();

    // 4 tests
    public static ConcurrentHashMap<String, HashMap<String, MockableDTOSupaSerializer>> test_getRawData() {
        return objArraySerializers;
    }

    // 4 tests
    public static Properties test_getConf() {
        return conf;
    }

    // pour les tests
    public static void test_dropData() {
        locks = new ConcurrentHashMap<>();
        dtosimple = new ConcurrentHashMap<>();
        dtoserializers = new ConcurrentHashMap<>();
        objArraySerializers = new ConcurrentHashMap<>();
    }

    public static <Q> String getHash(final Q query) throws JsonProcessingException {
        // get query hash
        final String jsonQuery = mapper.writeValueAsString(query);
        // LOG.debug("TODO GP: json base for hash: **{}**", jsonQuery);
        final String hash = Md5Crypt.apr1Crypt(jsonQuery, DAO_SALT).split("\\$")[3];
        // LOG.debug("TODO GP: hash is **{}**", hash);
        return hash;
    }

    /*
     * Récuperation de l'inputStream de LECTURE des mocks
     */
    private static InputStream getMockInputStream(final String mockId) {

        InputStream in = null;

        try {
            if (conf.containsKey("mock.json.read.path")) {
                final Path mockPath = Paths.get(conf.getProperty("mock.json.read.path"), mockId + ".json");
                LOG.warn("Lecture du json des mocks depuis {}", mockPath.getFileName().toAbsolutePath().toString());
                if (mockPath.toFile().exists()) {
                    in = new FileInputStream(mockPath.toFile());
                } else {
                    LOG.error("Impossible de lire le json des mocks dans le fichier {}", mockPath.getFileName().toAbsolutePath());
                }
            } else {
                // Mode nominal on va chercher ça dans le classpath
                LOG.warn("Lecture du json des mocks depuis le classpath {}", mockId + ".json");
                in = JSONSupaMockManager.class.getClassLoader().getResourceAsStream(mockId + ".json");
                if (in == null) {
                    LOG.error("Impossible de lire le json des mocks depuis le classpath {}", mockId + ".json");
                }
            }
        } catch (final FileNotFoundException e) {
            LOG.error("Impossible de trouver le json des mocks: {}", mockId + ".json");
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

    private static HashMap<String, Object> readDTOsFromJsonString(final InputStream in, final String daoName) throws DAOException {
        // Deserialisation du fichier json
        final TypeFactory typeFactory = mapper.getTypeFactory();
        final MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        HashMap<String, Object> deser;
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

    private static HashMap<String, MockableDTOSupaSerializer> readObjectArraysFromJsonString(final InputStream in, final String daoName) throws DAOException {

        // Deserialisation du fichier json
        // final TypeFactory typeFactory = mapper.getTypeFactory();
        ArrayList<MockableDTOSupaSerializer> deser;
        try {
            final TypeReference<List<MockableDTOSupaSerializer>> typeRef = new TypeReference<List<MockableDTOSupaSerializer>>() {
            };
            deser = mapper.readValue(in, typeRef);

        } catch (final Exception e) {
            throw new DAOException(e);
        }

        // TODO GP: recalculate hash and put in a
        HashMap<String, MockableDTOSupaSerializer> res = new HashMap<>();

        int cpt = 0;
        String hash = "";
        for (MockableDTOSupaSerializer x : deser) {
            cpt++;
            try {
                // get request hash
                if (x.defaultvalue) {
                    hash = DEFAULT_KEY;
                } else {
                    hash = getHash(x.request);
                }

                // Convert deser response LinkedHashMap to expected POJO
                Class<?> responseClass = Class.forName(x.responseCanonicalName);
                x.response = mapper.convertValue(x.response, responseClass);

                // Store
                res.put(hash, x);
            } catch (Exception e) {
                LOG.error("While reading JSON the {}th mock response for", cpt);
                LOG.error("Unable to cast JSON mock response attribut in {} Maybe the object has change and not the recorded data", x.responseCanonicalName, e);
            }
        }

        return res;

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

    private static <Q> void initMockContext(final String daoName, final String methodname, final Q[] query, final boolean loaddatafromfile) throws DAOException, IOException {
        final String mockId = daoName + "." + methodname;

        // Explose si production
        EnvHelper.throwExInProd();

        if (!locks.containsKey(mockId)) {

            boolean complexreq = false;
            boolean stringreq = false;
            boolean dtoreq = false;

            // entry exists ?
            // if (query.length == 1) {
            // stringreq = query[0] instanceof String;
            // dtoreq = query[0] instanceof MockableDTO;
            // if (!(stringreq || dtoreq)) {
            // complexreq = true;
            // // throw new DAOException("Not possible to manage MockData for a " + query.getClass().getSimpleName() + " object, first argument of DAO should be a request, either a String or a MockableDTO");
            // }
            // } else {

            complexreq = true;

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

                final HashMap<String, MockableDTO> data = new HashMap<>();

                // Pour les mocks
                if (loaddatafromfile) {
                    final InputStream in = getMockInputStream(mockId);
                    if (in != null) {
                        HashMap<String, Object> raw = readDTOsFromJsonString(in, mockId);
                        raw.forEach((k, v) -> {
                            data.put(k, (MockableDTO) v);

                        });
                        in.close();
                    }
                }

                dtosimple.put(mockId, data);

            }

            if (complexreq && !objArraySerializers.containsKey(mockId)) {

                HashMap<String, MockableDTOSupaSerializer> data = null;

                // Pour les mocks
                if (loaddatafromfile) {
                    final InputStream in = getMockInputStream(mockId);
                    if (in != null) {
                        data = readObjectArraysFromJsonString(in, mockId);
                        in.close();
                    }
                }

                if (data == null) {
                    data = new HashMap<String, MockableDTOSupaSerializer>();
                }
                objArraySerializers.put(mockId, data);

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

    // private static void recordMockableDTOReq(final String classname, final String methodname, final MockableDTO req, final MockableDTO resp) throws JsonProcessingException {
    // final String mockId = classname + "." + methodname;
    //
    // // get datamap for this dao/method
    // final HashMap<String, MockableDTOSerializer> datamap = dtoserializers.get(mockId);
    //
    // // get hash for req
    // final String hash = getHash(req);
    //
    // // remove record if exists
    // if (datamap.containsKey(hash)) {
    // datamap.remove(hash);
    // }
    //
    // final MockableDTOSerializer serialized = new MockableDTOSerializer(req, resp);
    //
    // // create default if empty
    // if (datamap.size() == 0) {
    // datamap.put(DEFAULT_KEY, serialized);
    // }
    //
    // datamap.put(hash, serialized);
    //
    // try {
    // throw new RuntimeException("rien à faire ici");
    // // final File in = getMockWriteFile(mockId);
    // // mapper.writeValue(in, datamap);
    // } catch (final Exception e) {
    // LOG.error("Impossible d'enregistrer le JSON pour {}", mockId, e);
    // }
    // throw new RuntimeException("rien à faire ici");
    //
    // }

    private static void recordObjectArrayReq(final String classname, final String methodname, final Object[] req, final MockableDTO resp) throws JsonProcessingException {
        final String mockId = classname + "." + methodname;

        // get datamap for this dao/method
        final HashMap<String, MockableDTOSupaSerializer> datamap = objArraySerializers.get(mockId);

        // get hash for req
        final String hash = getHash(req);

        // remove record if exists
        if (datamap.containsKey(hash)) {
            datamap.remove(hash);
        }

        final MockableDTOSupaSerializer serialized = new MockableDTOSupaSerializer(req, resp, resp.getClass().getCanonicalName());

        // create default entry if empty
        if (datamap.size() == 0) {
            final MockableDTOSupaSerializer defaultentry = new MockableDTOSupaSerializer(serialized);
            defaultentry.defaultvalue = true;
            datamap.put(DEFAULT_KEY, defaultentry);
        }

        datamap.put(hash, serialized);

        try {
            final File in = getMockWriteFile(mockId);

            /***
             * ObjectMapper thismapper = new ObjectMapper();
             * thismapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
             * thismapper.configure(SerializationFeature.INDENT_OUTPUT, true);
             * thismapper.writeValue(in, new ArrayList<MockableDTOSupaSerializer>(datamap.values()));
             * String res = thismapper.writeValueAsString(new ArrayList<MockableDTOSupaSerializer>(datamap.values()));
             **/

            // don't record hash
            mapper.writeValue(in, new ArrayList<MockableDTOSupaSerializer>(datamap.values()));
            String res = mapper.writeValueAsString(new ArrayList<MockableDTOSupaSerializer>(datamap.values()).toArray());
            // LOG.debug(res);
        } catch (final Exception e) {
            LOG.error("Impossible d'enregistrer le JSON pour {}", mockId, e);
        }

    }

    public static <Q> void record(final String classname, final String methodname, final Q[] req, final Object resp) throws DAOException, IOException {
        final String mockId = classname + "." + methodname;

        initMockContext(classname, methodname, req, false);
        final ReadWriteLock lock = locks.get(mockId);
        lock.writeLock().lock();
        try {

            boolean stringreq = false;

            // if ((req.length == 1) && (req[0] instanceof String)) {
            // stringreq = true;
            // }
            // if mockdata exists
            if (stringreq) {
                recordStringReq(classname, methodname, (String) req[0], (MockableDTO) resp);
            } else {
                recordObjectArrayReq(classname, methodname, req, (MockableDTO) resp);
            }

        } finally {
            lock.writeLock().unlock();
        }
    }

    public static <Q> MockableDTO getDTOFromJson(final String daoName, final String methodname, final Object[] query) throws DAOException, IOException {
        final String mockId = daoName + "." + methodname;
        initMockContext(daoName, methodname, query, true);
        final ReadWriteLock lock = locks.get(mockId);
        lock.readLock().lock();
        MockableDTO res = null;

        try {

            res = getMockDataForObjectArray(mockId, query);

            // sleep management

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

    private static MockableDTO getMockDataForObjectArray(final String mockId, final Object[] req) throws JsonProcessingException {

        MockableDTO res = null;

        final String hash = getHash(req);

        if (objArraySerializers.get(mockId).containsKey(hash)) {

            res = (MockableDTO) objArraySerializers.get(mockId).get(hash).getResp();
            LOG.debug("Mock --> Deserialize {} ok: specific value returned", mockId);
        } else {
            final MockableDTOSupaSerializer ser = objArraySerializers.get(mockId).get(DEFAULT_KEY);
            if (ser != null) {
                res = (MockableDTO) ser.getResp();
                LOG.debug("Mock --> Deserialize {} ok: default value returned", mockId);
            } else {
                LOG.error("Mock --> Deserialize {} KO: no default value found", mockId);
            }
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
