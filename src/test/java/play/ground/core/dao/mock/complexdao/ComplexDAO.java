
package play.ground.core.dao.mock.complexdao;

import play.ground.core.dao.mock.complexdao.dto.ComplexDTOIn;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOOut;

public interface ComplexDAO {

    public ComplexDTOOut getdata(ComplexDTOIn in) throws Exception;
}
