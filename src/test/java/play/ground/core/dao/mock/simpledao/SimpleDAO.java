
package play.ground.core.dao.mock.simpledao;

import play.ground.core.dao.mock.simpledao.dto.SimpleDTO;

public interface SimpleDAO {

    public SimpleDTO getdata(String key) throws Exception;
}
