package play.ground.dsiday;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SampleTest {
	 @BeforeClass
	 public static void initialisation_donnee(){
		 /* ce code ne sera executé qu'une fois */}
	 @Before
	 public void initialisation_donnee_ou_etat(){
		 /* ce code sera executé avant chaque test */}
	 @Test
	 public void test1(){
		 /* action / verification */}
	 @Test
	 public void test2(){
		 /* action / verification */}
	 @After
	 public void mise_au_propre(){
		 /* ce code sera executé après chaque test */}
	 @AfterClass
	 public static void remise_au_propre_finale(){
		 /* ce code sera executé 1x après tous les tests */}	 
}



