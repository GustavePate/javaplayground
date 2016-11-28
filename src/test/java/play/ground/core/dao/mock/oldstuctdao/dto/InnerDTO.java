
package play.ground.core.dao.mock.oldstuctdao.dto;

import java.util.ArrayList;
import java.util.HashMap;

import play.ground.core.dao.mock.MockableDTO;
import play.ground.core.dao.mock.complexdao.dto.ComplexDTOIn;

public class InnerDTO implements MockableDTO {

    public HashMap<String, String> keyval;

    public ComplexDTOIn in;

    public ArrayList<String> arr;

    public ArrayList<ComplexDTOIn> arrObj;

    public InnerDTO() {

        keyval = new HashMap<>();
        in = new ComplexDTOIn();
        arr = new ArrayList<>();
        arrObj = new ArrayList<>();

        int cpt = 3;
        while (cpt > 0) {
            keyval.put("key" + String.valueOf(cpt), "value" + String.valueOf(cpt));
            cpt--;
        }
        cpt = 5;
        while (cpt > 0) {
            arr.add("arr" + String.valueOf(cpt));
            cpt--;
        }

        in.indata = String.valueOf(this.hashCode());

        cpt = 5;

        while (cpt > 0) {
            ComplexDTOIn toadd = new ComplexDTOIn();
            toadd.indata = "instance" + String.valueOf(cpt);
            arrObj.add(toadd);
            cpt--;
        }

    }

}
