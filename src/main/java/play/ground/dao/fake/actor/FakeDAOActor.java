package play.ground.dao.fake.actor;

import akka.actor.UntypedActor;
import play.ground.dao.fake.FakeDAO;
import play.ground.dao.fake.dto.FakeDTO;
import play.ground.dao.fake.impl.FakeDAODefault;
import play.ground.dao.fake.impl.FakeDAOMock;

public class FakeDAOActor extends UntypedActor  {
        FakeDTO dto;

        public void onReceive(Object message) {
     		if (message instanceof String){
            	
            	// call DAO
            	FakeDAO dao = new FakeDAODefault();
            	dto = dao.doit((String)message);
            	// tell DTO
            	getSender().tell(dto, getSelf());

			}else unhandled(message);
        }
}
