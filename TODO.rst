# En cours

* Arbo Junit / Cucumber avec Injection
* Modèle de tests de charge


# TODO

* Surcharge de l'injection sur les tests uniatires
	* [http://stackoverflow.com/questions/483087/overriding-binding-in-guice](http://stackoverflow.com/questions/483087/overriding-binding-in-guice)
* Utiliser les annotations metrics
* Découpages des DAO Thrift
* H2 db pour les tests
* Versioning des schémas de base de données
   * outil ?
	* [http://martinfowler.com/articles/evodb.html](http://martinfowler.com/articles/evodb.html)
   * [coding horro](http://blog.codinghorror.com/get-your-database-under-version-control/)
   
   create table VersionHistory (
      Version int primary key,
      UpgradeStart datetime not null,
      UpgradeEnd datetime,
      script_name vachar2(500),
      tests_passed boolean (default false)
    );   
   
* Mockito
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
* Codelabs sur les tech utilisées (Junit / Mockito / Guice / Cucumber / Jackson)  
* Minisite publiable / appli
* Archetype Batch / Test des batchs

# Done

* conf accessible depuis les tests unit / composants
* injection accessible depuis les tests unit / composants
* world accessible depuis les tests unit / composants
* Service != singleton
* Metrics temps de réponse par défaut
* Metrics nb error par défaut
* Metriques agrégées évolutions des temps de réponse / nb appels
* resitution graphique des métriques