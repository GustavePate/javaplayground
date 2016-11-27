
package play.ground.core.dao.mock.simpledao.impl;

import play.ground.core.dao.AbstractDAO;
import play.ground.core.dao.mock.Mockable;
import play.ground.core.dao.mock.simpledao.SimpleDAO;
import play.ground.core.dao.mock.simpledao.dto.SimpleDTO;

public class SimpleDAODefault extends AbstractDAO implements SimpleDAO {

    @Mockable
    public SimpleDTO getdata(String key) throws Exception {

        SimpleDTO dto = new SimpleDTO();

        try {

            dto.value = String.valueOf(key.length());

        } catch (Exception e) {
            throw e;
        }

        return dto;

    }
}
