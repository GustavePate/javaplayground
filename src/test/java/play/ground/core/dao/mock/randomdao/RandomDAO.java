
package play.ground.core.dao.mock.randomdao;

import play.ground.core.dao.mock.complexdao.dto.ComplexDTOIn;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOOut;

public interface RandomDAO {

    public ComplexDTOOut getdata(ComplexDTOIn in1, ComplexDTOIn in2, boolean randomcheck) throws Exception;

}
