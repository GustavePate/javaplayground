
package play.ground.core.dao.mock.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.ground.core.dao.mock.MockableDTO;

public class MockableDTOSupaSerializer {

    public boolean defaultvalue = false;

    public String responseCanonicalName = "default";
    
    public Object[] request;

    public Object response;
    
    

    public MockableDTOSupaSerializer() {

    }

    public MockableDTOSupaSerializer(final Object[] zereq, final MockableDTO zeresp, final String responseType) {
        request = zereq;
        response = zeresp;
        responseCanonicalName = responseType;
    }
    
    //bad not immutable but enough for this case (change default value)
    public MockableDTOSupaSerializer(MockableDTOSupaSerializer src){
    	defaultvalue=src.defaultvalue;
    	request = src.request;
    	response = src.response;
    	responseCanonicalName = src.responseCanonicalName;
    }

    @JsonIgnore
    public Object getResp() {
        return response;
    }
}
