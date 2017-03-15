
package play.ground.core.dao.mock.oldstuctdao.impl;

import play.ground.core.dao.mock.Mockable;
import play.ground.core.dao.mock.oldstuctdao.OldStructDAO;
import play.ground.core.dao.mock.oldstuctdao.dto.OldStructDTOIn;

public class OldStructDAODefault implements OldStructDAO {

    @Mockable
    public OldStructDTOIn getdata(OldStructDTOIn dao) {
        return dao;
    }

}
