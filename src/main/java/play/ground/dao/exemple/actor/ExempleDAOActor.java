package play.ground.dao.exemple.actor;

import akka.actor.UntypedActor;
import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.dto.ExempleDTO;
import play.ground.dao.exemple.impl.ExempleDAODefault;

public class ExempleDAOActor extends UntypedActor  {
        ExempleDTO dto;

        public void onReceive(Object message) {
     		if (message instanceof String){
            	
            	// call DAO
            	ExempleDAO dao = new ExempleDAODefault();
            	dto = dao.doit((String)message);
            	// tell DTO
            	getSender().tell(dto, getSelf());

			}else unhandled(message);
        }
}
