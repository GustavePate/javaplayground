package play.ground.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codahale.metrics.annotation.Counted;
import com.google.inject.Inject;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import play.ground.AkkaWorld;
import play.ground.dao.exemple.actor.ExempleDAOActor;
import play.ground.dao.exemple.dto.ExempleDTO;
import scala.concurrent.duration.Duration;

public class AkkaService extends GenericService {
	HttpServletRequest req;
	HttpServletResponse resp;

	@Inject
	public AkkaService(){


	}

	@Counted
	public void process(HttpServletRequest req, HttpServletResponse resp){
		start();
		this.req = req;
		this.resp = resp;
		try {
			// recuperation du system
			final ActorSystem system = AkkaWorld.getSystem();
			// should be done just one time
			final ActorRef fakedao = system.actorOf(Props.create(ExempleDAOActor.class), "fakedao");
			
			
			final Inbox inbox = Inbox.create(system);
			ExempleDTO daoresp;
			inbox.send(fakedao, "c'est genial akka");
			try {
				daoresp = (ExempleDTO) inbox.receive(Duration.create(5, TimeUnit.SECONDS));
				this.resp.getWriter().println(String.valueOf(daoresp.compute));
			} catch (TimeoutException e) {
				log.error("Timout", e);
				this.resp.getWriter().println("ERROR");
			}

		} catch (IOException e) {
			log.error("IOException", e);
		}
		stop();
	}
}
