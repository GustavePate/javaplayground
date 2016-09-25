package play.ground.log4j2.appender;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class JsonSocketManager {


	private static HashMap<String, JsonSocketManager> connectors = new HashMap<>();

	public static JsonSocketManager getInstance(String host, String port) throws Exception{
		
		if (!connectors.containsKey(host+port)){
			// Thread Safe. Might be costly operation in some case
			synchronized (JsonSocketManager.class) {
				if (!connectors.containsKey(host+port)){
					connectors.put(host+port, new JsonSocketManager(host,port));
				}
			}
		}
		return connectors.get(host+port);
	}
	
	private String host;
	private int port;
	private Socket socket; 
	private ObjectOutputStream oos;
	
	private void setHost(String host) {
		this.host = host;
	}

	private void setPort(String port) {
		this.port = Integer.valueOf(port);
	}

	
	public JsonSocketManager(String host,String port) throws Exception {
		this.host = host;
		this.port = Integer.valueOf(port);	
		try {
			startsocket();
		} catch (Exception e) {
			throw e;
		}	
			
	}

	public void startsocket() throws UnknownHostException, IOException{
		synchronized (this) {
			if (socket == null){
				socket = new Socket(this.host, this.port);
				oos = new ObjectOutputStream(socket.getOutputStream());
			}
		}
	}
	
	
	public void write(byte[] data) throws InterruptedException, IOException, Exception{
		
		try{
				oos.write(data);
				oos.flush();
				Thread.sleep(100);
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		
		System.err.println(new String(data));
	}
	
	public void close() throws IOException{
		synchronized (socket) {
			try{
				oos.close();
			}finally{
				socket.close();
				socket = null;
			}
		}
	}
	
	
	


	
}
