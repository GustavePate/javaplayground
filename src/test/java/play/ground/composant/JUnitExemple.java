package play.ground.composant;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JUnitExemple {

	static final Logger log = LoggerFactory.getLogger(JUnitExemple.class);
	
	@Test
	public void call_exemple_service() throws IOException{
	

		URL url = new URL("http://localhost:7070/root/exemple");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		//conn.setRequestProperty("Accept", "application/json");
		
		assertThat(conn.getResponseCode()).isEqualTo(200);
		
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String out = "";
		String output;
		while ((output = br.readLine()) != null) {
			out += output; 
		}
		log.info("service output: {}", out);
		conn.disconnect();
		
	}
}
