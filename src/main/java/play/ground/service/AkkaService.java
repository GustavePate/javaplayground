package play.ground.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import play.ground.AkkaWorld;
import play.ground.dao.fake.actor.FakeDAOActor;
import play.ground.dao.fake.dto.FakeDTO;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class AkkaService extends Service {
	HttpServletRequest req;
	HttpServletResponse resp;

	public AkkaService(HttpServletRequest req, HttpServletResponse resp){

		this.req = req;
		this.resp = resp;

	}

	public void doIt(){
		start();
		try {
			// recuperation du system
			final ActorSystem system = AkkaWorld.getSystem();
			// should be done just one time
			final ActorRef fakedao = system.actorOf(Props.create(FakeDAOActor.class), "fakedao");
			
			
			final Inbox inbox = Inbox.create(system);
			FakeDTO daoresp;
			inbox.send(fakedao, "c'est genial akka");
			try {
				daoresp = (FakeDTO) inbox.receive(Duration.create(5, TimeUnit.SECONDS));
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
