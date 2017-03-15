
package play.ground.core.dao.mock.staticdao.impl;

import play.ground.core.dao.mock.Mockable;
import play.ground.core.dao.mock.staticdao.StaticDAO;
import play.ground.core.dao.mock.staticdao.dto.StaticDTO;

public class StaticDAODefault implements StaticDAO {

    @Mockable
    public StaticDTO isodd(String input) {

        StaticDTO res = new StaticDTO();
        if ((input.length() % 2) == 0) {
            res.res = true;
        } else {
            res.res = false;
        }
        return res;
    }

}
