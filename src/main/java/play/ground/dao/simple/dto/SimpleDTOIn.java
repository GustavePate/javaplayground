
package play.ground.dao.simple.dto;

import play.ground.core.dao.mock.MockableDTO;

public class SimpleDTOIn implements MockableDTO {

    public String id = "123456";

    public String functionnalPK() {
        return id;
    }

}
