
package play.ground.core.dao.mock.randomdao.impl;

import play.ground.core.dao.mock.Mockable;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOIn;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOOut;
import play.ground.core.dao.mock.randomdao.RandomDAO;

public class RandomDAODefault implements RandomDAO {

    @Mockable
    public ComplexDTOOut getdata(ComplexDTOIn in1, ComplexDTOIn in2, boolean randomcheck) throws Exception {
        ComplexDTOOut out = new ComplexDTOOut();
        if ((in1.indata != null) && (in2.indata!=null)){
        	out.outdata = in1.indata+" "+in2.indata;
        }else{
        	out.outdata = "random stuff";
        }
        return out;
    }

}