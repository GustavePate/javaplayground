# En cours

* config stockage / lecture mock
* ID Request à propager

* AssertJ everywhere:
* Doc Mock

* Exemple Jenkins

* Surcharge de l'injection sur les tests uniatires: ok a tester
	* [http://stackoverflow.com/questions/483087/overriding-binding-in-guice](http://stackoverflow.com/questions/483087/overriding-binding-in-guice)
   
* Utiliser les annotations metrics: bof + marche pas avec RestEasy

# TODO

* Découpages des DAO Thrift
* Mockito
* Cucumber servlet
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
   
* Dump des métriques chez les système
* Dump des métriques dans Kibana
* Akka
* Test de charge des métriques (tout doit arriver dans la resitution)
* Documentation (forma md) pour tous les niveaux
	* services
	* dao
	* unit test
	* test composant
	* test perf
   * Jacoco: lancement depuis eclipse
* Codelabs sur les tech utilisées (Junit / Mockito / Guice / Cucumber / Jackson)  
* Minisite publiable / appli
* Archetype Batch / Test des batchs
* Junit Scope (ignorer certains tests)
* Injection de module par annotation (Jukito / GuiceBerry)
* Hystrix



# Done

* Modèle de tests de charge
* Arbo Junit / Cucumber avec Injection
* conf accessible depuis les tests unit / composants
* injection accessible depuis les tests unit / composants
* world accessible depuis les tests unit / composants
* Service != singleton
* Metrics temps de réponse par défaut
* Metrics nb error par défaut
* Metriques agrégées évolutions des temps de réponse / nb appels
* resitution graphique des métriques
* Jacoco junit
* Jacoco cucumber
* info-build.json