package play.ground;

import akka.actor.ActorSystem;

public class AkkaWorld {
	
		/** Constructeur privé */	
		private AkkaWorld()
		{}
	 
		/** Holder */
		private static class SingletonHolder
		{		
						
			private final static ActorSystem instance = ActorSystem.create("helloakka");

		}
	 
		/** Point d'accès pour l'instance unique du singleton */
		public static ActorSystem getSystem()
		{
			return SingletonHolder.instance;
		}

}
