
package play.ground.core.dao.mock.oldstuctdao.dto;

import play.ground.core.dao.mock.MockableDTO;

public class SimpleInnerDTO implements MockableDTO {

    public String val1 = "";

    public String val2 = "";

    public boolean check = false;

    public SimpleInnerDTO() {
        val1 = "default1";
        val2 = "default2";
        check = true;
    }

}
