
package play.ground.core.dao.mock.oldstuctdao.dto;

import java.util.Vector;

import play.ground.core.dao.mock.MockableDTO;

public class OldStructDTOIn implements MockableDTO {

    public Vector<InnerDTO> vec;

    public OldStructDTOIn() {
        vec = new Vector<>();
        // SimpleInnerDTO simple = new SimpleInnerDTO();
        InnerDTO complex = new InnerDTO();
        vec.add(complex);
    }

}
