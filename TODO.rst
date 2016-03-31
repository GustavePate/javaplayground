# En cours

* AssertJ everywhere
* Doc Mock
* Surcharge de l'injection sur les tests uniatires: ok a tester
	* [http://stackoverflow.com/questions/483087/overriding-binding-in-guice](http://stackoverflow.com/questions/483087/overriding-binding-in-guice)
* Découpages des DAO Thrift
* Découpages des DAO BDD

# TODO

* Dump des métriques chez les système
* Dump des métriques dans Kibana
* Test de charge des métriques (tout doit arriver dans la resitution)
* Documentation (forma md) pour tous les niveaux
	* services
	* dao
   * Metriques
	* unit test
	* test composant
	* test perf
   * Jacoco: lancement depuis eclipse
* Cucumber servlet
* Exemple Jenkins
* Codelabs sur les tech utilisées (Junit + Guice + Cucumber + Jackson)  
* H2 db pour les tests
* Versioning des schémas de base de données
   * outil ?
	* [http://martinfowler.com/articles/evodb.html](http://martinfowler.com/articles/evodb.html)
   * [coding horror](http://blog.codinghorror.com/get-your-database-under-version-control/)
   
   create table VersionHistory (
      Version int primary key,
      UpgradeStart datetime not null,
      UpgradeEnd datetime,
      script_name vachar2(500),
      tests_passed boolean (default false)
    );   
   
   DB Workflow
   
   * Create DB
   * Apply Change
   * Test
   * If ok:
      * mark change as valid
      * dump db  
   
* Hystrix
* Mockito
* Akka
* Minisite publiable / appli
* Archetype Batch / Test des batchs
* Junit Scope (ignorer certains tests)
* Injection de module par annotation (Jukito / GuiceBerry)


# Mis de coté
   
* Utiliser les annotations metrics: plus value bof + marche pas avec RestEasy
* ID Request à propager

# Done

* DAO: Refactoring structure des DAO (nommage)
* DAO: ne sont plus des singletons (métriques)
* DAO: Record / Playback des données renvoyées par les DAO
* TEST: Modèle de tests de charge
* TEST: Arbo Junit / Cucumber avec Injection
* TEST: conf accessible depuis les tests unit / composants
* TEST: injection accessible depuis les tests unit / composants
* TEST: world accessible depuis les tests unit / composants
* TEST: Jacoco junit
* TEST: Jacoco cucumber
* TEST: info-build.json (pour savoir quand c'est deployé)
* SERVICES: Service != singleton (pour métriques)
* METRIQUES: Metrics temps de réponse par défaut
* METRIQUES: Metrics nb error par défaut
* METRIQUES: Metriques agrégées évolutions des temps de réponse / nb appels
* METRIQUES: Detail du temps de réponse de chaque DAO pour chaque appel
* METRIQUES: resitution graphique des métriques
