
package play.ground.dao.exemple.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.ground.core.dao.mock.AbstractMock;
import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.dto.ExempleDTO;

public class ExempleDAOMock extends AbstractMock implements ExempleDAO {

    static final Logger log = LoggerFactory.getLogger(ExempleDAOMock.class);

    public ExempleDTO doit(String data) throws Exception {

        ExempleDTO dto = new ExempleDTO();

        // dto = (ExempleDTO) getFromJson(data);

        if (props.contains("mock.exempledao.sleep")) {
            Thread.sleep((int) props.get("mock.exempledao.sleep"));
        }

        return dto;

    }

}
