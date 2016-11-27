
package play.ground.dao.exemple.dto;

import play.ground.core.dao.mock.MockableDTO;

public class ExempleDTO implements MockableDTO {

    public int compute = 0;

    public String src = "";

    public String functionnalPK() {
        return src;
    }

}
