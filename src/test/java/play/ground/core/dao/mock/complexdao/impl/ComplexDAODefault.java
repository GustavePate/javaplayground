
package play.ground.core.dao.mock.complexdao.impl;

import play.ground.core.dao.mock.Mockable;
import play.ground.core.dao.mock.complexdao.ComplexDAO;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOIn;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOOut;

public class ComplexDAODefault implements ComplexDAO {

    @Mockable
    public ComplexDTOOut getdata(ComplexDTOIn in) throws Exception {
        ComplexDTOOut out = new ComplexDTOOut();
        out.outdata = "sortie";
        return out;
    }

}
