package play.ground.composant;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JUnitExemple {

	static final Logger log = LoggerFactory.getLogger(JUnitExemple.class);
	
	@Test
	public void call_exemple_service() throws IOException{
	

		URL url = new URL("http://localhost:7070/root/api/exemple");
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
	
	@Test
	public void call_rest_service() throws Exception{
	
		ArrayList<String> helloname = new ArrayList<String>();
		helloname.add("jack");
		helloname.add("barbenoire");
		helloname.add("barberousse");
		helloname.add("sir francis drake");
		helloname.add("charlotte de berry");
		
		helloname.forEach(name -> {
			
			HttpResponse<JsonNode> jsonResponse;
				try {
					String url = String.format("http://localhost:7070/root/rest/hello/%s",name);
					log.info("ask for string : {} {}",name, url);
					jsonResponse = Unirest.get(url)
							  .header("accept", "application/json")
							  .asJson();
					assertThat(jsonResponse.getStatus()).isEqualTo(200);
					log.info("rest service response: {}",jsonResponse.getBody().toString());
				} catch (Exception e) {
					fail(String.format("call for {}", name), e);
				}
			
		});
		
	
	}
	
}
