package playground.yo.dao.fake.actor;

import akka.actor.UntypedActor;
import playground.yo.dao.fake.FakeDAO;
import playground.yo.dao.fake.FakeDAOFactory;
import playground.yo.dao.fake.dto.FakeDTO;

public class FakeDAOActor extends UntypedActor  {
        FakeDTO dto;

        public void onReceive(Object message) {
     		if (message instanceof String){
            	
            	// call DAO
            	FakeDAO dao = FakeDAOFactory.get();
            	dto = dao.doit((String)message);
            	// tell DTO
            	getSender().tell(dto, getSelf());

			}else unhandled(message);
        }
}
