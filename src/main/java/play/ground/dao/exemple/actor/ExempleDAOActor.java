package play.ground.dao.exemple.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;
import play.ground.dao.exemple.ExempleDAO;
import play.ground.dao.exemple.dto.ExempleDTO;
import play.ground.dao.exemple.impl.ExempleDAODefault;

public class ExempleDAOActor extends UntypedActor  {
	ExempleDTO dto;

	static final Logger LOG = LoggerFactory.getLogger(ExempleDAODefault.class);

	public void onReceive(Object message) {

		if (message instanceof String){

			// call DAO
			ExempleDAO dao = new ExempleDAODefault();
			try{
				dto = dao.doit((String)message);
			}catch (Exception e){
				LOG.error("DAO exploded for: {}",message, e);
			}finally{
				// tell DTO
				getSender().tell(dto, getSelf());
			}

		}else unhandled(message);
	}
}
