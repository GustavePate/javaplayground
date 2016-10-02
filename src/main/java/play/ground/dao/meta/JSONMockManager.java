package play.ground.dao.meta;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

import play.ground.dao.MockableDTO;

public class JSONMockManager {

	protected static final String DEFAULT_KEY="default";

	@Inject
	@Named("conf")
	protected static Config conf;	

	protected static final ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	// configuration local storage
	protected static ConcurrentHashMap<String, Boolean> tomock= new ConcurrentHashMap<>();
	protected static ConcurrentHashMap<String, Boolean> torecord= new ConcurrentHashMap<>();

	// mock data storage
	protected static ConcurrentHashMap<String, HashMap<String, MockableDTOSerializer>> dtoserializers = new ConcurrentHashMap<>();
	protected static ConcurrentHashMap<String, HashMap<String, MockableDTO>> dtosimple = new ConcurrentHashMap<>();
	//locks
	protected static ConcurrentHashMap<String, ReadWriteLock> locks = new ConcurrentHashMap<>(); 





	protected static void recordStringReq(String classname, String methodname, String req, MockableDTO resp){
		String mockId =  classname + "." + methodname;




		// get datamap for this dao/method
		HashMap<String, MockableDTO> datamap = dtosimple.get(mockId);

		// create or replace record
		if (datamap.containsKey(req)){
			datamap.remove(req);
		}
		datamap.put(req, resp);

		// serialize and store data map in file


	}

	protected static String getHash(MockableDTO dto){
		return "TODO";
	}


	protected static void initMockContext(String classname, String methodname, Object[] req) throws Exception{
		String mockId =  classname + "." + methodname;
		if (!locks.containsKey(mockId)){

			// entry exists ?
			boolean stringreq = (req[0] instanceof String);
			boolean dtoreq = (req[0] instanceof MockableDTO);

			if (!( stringreq || dtoreq)){
				throw new Exception("Not possible to record MockData for a " + req[0].getClass().getSimpleName() + " object, request should be a String or a MockableDTO");
			}

			// if not exists create empty entry
			if (dtoreq && !dtoserializers.containsKey(mockId)){

				// TODO: Load data from file

				// else
				dtoserializers.put(mockId, new HashMap<String, MockableDTOSerializer>());
			}	


			if (stringreq && !dtosimple.containsKey(mockId)){
				// TODO:  Load data from file

				// else
				dtosimple.put(mockId, new HashMap<String, MockableDTO>());
			}



			locks.put(mockId, new ReentrantReadWriteLock());
		}


	}





	protected static void recordMockableDTOReq(String classname, String methodname, MockableDTO req, MockableDTO resp){
		String mockId =  classname + "." + methodname;

		// get datamap for this dao/method
		HashMap<String, MockableDTOSerializer> datamap = dtoserializers.get(mockId);

		// get hash for req
		String hash = getHash(req);
		if (datamap.containsKey(hash)){
			datamap.remove(hash);
		}
		datamap.put(hash, new MockableDTOSerializer(req, resp));

		// TODO store datamap to file



	}


	public static void record(String classname, String methodname, Object[] req, Object resp) throws Exception {
		String mockId =  classname + "." + methodname;

		initMockContext(classname, methodname, req);
		ReadWriteLock lock = locks.get(mockId);
		lock.writeLock().lock();
		try{

			boolean stringreq = (req[0] instanceof String);
			boolean dtoreq = (req[0] instanceof MockableDTO);

			// if mockdata exists
			if (stringreq){
				recordStringReq(classname, methodname, (String) req[0], (MockableDTO) resp);
			}else if(dtoreq){
				recordMockableDTOReq(classname, methodname, (MockableDTO) req[0], (MockableDTO) resp);
			}

		}finally{
			lock.writeLock().unlock();
		}
	}


	public static MockableDTO getMockData(String classname, String methodname, Object[] req) throws Exception{
		String mockId =  classname + "." + methodname;

		initMockContext(classname, methodname, req);
		ReadWriteLock lock = locks.get(mockId);
		lock.readLock().lock();
		
		MockableDTO res = null;
		try{

			boolean stringreq = (req[0] instanceof String);
			boolean dtoreq = (req[0] instanceof MockableDTO);
			
			if (!(stringreq || dtoreq)){
				throw new Exception("Not possible to retrieve MockData for a " + req[0].getClass().getSimpleName() + " object, request should be a String or a MockableDTO");
			}
			if (stringreq){
				res = getMockDataFromString(mockId, (String) req[0]);
			}else if(dtoreq){
				res = getMockDataFromDTO(mockId, (MockableDTO) req[0]);
			}
			
		}catch(Exception e){
			throw e;

		}finally{
			lock.readLock().unlock();
		}
		return res;
	}

	private static MockableDTO getMockDataFromDTO(String mockId, MockableDTO req) {
		MockableDTO res = null;
		
		String hash = getHash(req);
		
		if (dtoserializers.get(mockId).containsKey(hash)){
			res = (MockableDTO) dtoserializers.get(mockId).get(hash).getResp();
		}else{
			res = (MockableDTO) dtoserializers.get(mockId).get(DEFAULT_KEY).getResp();
		};
		
		return res;
	}

	private static MockableDTO getMockDataFromString(String mockId, String req) {
		// TODO Auto-generated method stub
		MockableDTO res = null;
		
		if (dtosimple.get(mockId).containsKey(req)){
			res = (MockableDTO) dtosimple.get(mockId).get(req);
		}else{
			res = (MockableDTO) dtosimple.get(mockId).get(DEFAULT_KEY);
		};
		return res;
	}

	public static boolean shouldIMock(String classname, String methodname) {
		// TODO Auto-generated method stub
		boolean res = false;


		String key = classname + "." + methodname;
		if (tomock.contains(key)){
			res = tomock.get(key);
		}else{
			// Read conf


			if (conf.hasPath("dao.mock.all")){
				if (conf.getBoolean("dao.mock.all")){
					res = true;
				}
			}

			if (conf.hasPath("dao.mock." + classname)){
				if (conf.getBoolean("dao.mock." + classname)){
					res = true;
				}
			}

			if (conf.hasPath("dao.mock." + classname + "." + methodname)){
				if (conf.getBoolean("dao.mock." + classname + "." + methodname)){
					res = true;
				}
			}

			// Store result
			tomock.put(key, res);
		}

		return res;
	}

	public static boolean shouldIRecord(String classname, String methodname) {
		// TODO Auto-generated method stub
		boolean res = false;

		String key = classname + "." + methodname;
		if (torecord.contains(key)){
			res = torecord.get(key);
		}else{
			// Read conf

			if (conf.hasPath("dao.record.all")){
				if (conf.getBoolean("dao.record.all")){
					res = true;
				}
			}

			if (conf.hasPath("dao.record." + classname)){
				if (conf.getBoolean("dao.record." + classname)){
					res = true;
				}
			}

			if (conf.hasPath("dao.record." + classname + "." + methodname)){
				if (conf.getBoolean("dao.record." + classname + "." + methodname)){
					res = true;
				}
			}
			// Store result
			torecord.put(key, res);
		}

		return res;

	}





}
