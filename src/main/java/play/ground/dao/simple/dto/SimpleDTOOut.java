
package play.ground.dao.simple.dto;

import play.ground.core.dao.mock.MockableDTO;

public class SimpleDTOOut implements MockableDTO {

    public String id;

    public String name;

    public String getName() {
        return name;
    }

    public String functionnalPK() {
        return id;
    }

}
