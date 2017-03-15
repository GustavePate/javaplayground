package play.ground.assertion.gen1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

import play.ground.assertion.AbstractAssertTest;
import play.ground.assertion.objects.Personnage;
import play.ground.assertion.objects.Personnage.Race;

public class SimpleAssert extends AbstractAssertTest{

	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * NEEDS -ea in JVM option to work
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	
	@Test
	public void test_null(){
		assert nullstr == null;
	}
	
	@Test
	public void test_not_null() {
		assert frodon != null;
	}
	
	@Test
	public void test_string_equal() {
		assert frodon.getName() == "Frodon";
	}

	@Test
	public void test_string_partial() {
		assert frodon.getName().startsWith("Fro");
		assert frodon.getName().endsWith("don");
		assert frodon.getName().equalsIgnoreCase("frodon");
	}

	@Test
	public void test_is_in_collection() {
		assert frodon != sauron;
		assert fellowshipOfTheRing.contains(frodon);
	}
	
	@Test
	public void test_collection_size_contains() {
		assert fellowshipOfTheRing.contains(frodon);
		assert fellowshipOfTheRing.contains(sam);
		assert !fellowshipOfTheRing.contains(sauron);
	}

	@Test
	public void test_exception() {
		try{
			throw new Exception("boom!");
		} catch (Exception e){
				assert e instanceof Exception;
				assert e.getMessage().contains("boom");
		}
	}
	
	@Test
	public void test_object_attribut_in_collection(){
		/**** variant 1 ****/
		List<String> names = (List<String>) fellowshipOfTheRing.stream().map(pers->{return pers.getName();}).collect(Collectors.toList());
		assert names.containsAll(Arrays.asList("Legolas", "Boromir", "Gandalf", "Frodon"));
		assert !names.containsAll(Arrays.asList("Sauron","Elrond"));

		/**** variant 2 ****/
		List<HashMap<String, String>> names2 = fellowshipOfTheRing.stream().map(pers->{
				HashMap<String,String> member = new HashMap<String,String>();
				member.put(pers.getName(), pers.getRace().getName());
				return member;
				}).collect(Collectors.toList());
		
		HashMap<String,String> pers = new HashMap<String,String>();
		pers.put("Boromir", "human");
		assert names2.contains(pers);
		
		pers.clear();
		pers.put("Sam", "hobbit");
		assert names2.contains(pers);
		
		pers.clear();
		pers.put("Legolas", "elf");
		assert names2.contains(pers);
	}

	@Test
	public void test_filter_on_collection(){
		/**** variant 1 ****/
		// filter collection before assertion
		List<Personnage> pers = fellowshipOfTheRing.stream().filter(p->p.getRace()==Race.HOBBIT).collect(Collectors.toList());
		assert pers.containsAll(Arrays.asList(sam, frodon, pippin, merry));
		assert pers.size() == 4;
		
		/**** variant 2 ****/
		// filter collection with Java 8 Predicate
		List<Personnage> pers_v2 = fellowshipOfTheRing.stream().filter(p->p.getName().contains("o")).collect(Collectors.toList());
		assert pers_v2.containsAll(Arrays.asList(aragorn, frodon, legolas, boromir, gollum));
		assert pers_v2.size() == 5;
		
		/**** variant 3 ****/
		// combining filtering and extraction (yes we can)
		List<Personnage> pers_v3 = fellowshipOfTheRing.stream().filter(p->p.getName().contains("o")).collect(Collectors.toList());
		assert pers_v3.containsAll(Arrays.asList(aragorn, frodon, legolas, boromir, gollum));
		assert pers_v3.size() == 5;
		List<String> pers_v3_1 = pers_v3.stream().map(p->p.getRace().getName()).collect(Collectors.toList());
		assert pers_v3_1.containsAll(Arrays.asList("hobbit", "elf","human"));
	}
	
	@Test
	public void test_error_wo_message() {
		List<String> names = (List<String>) fellowshipOfTheRing.stream().map(pers->{return pers.getName();}).collect(Collectors.toList());
		assert !names.contains("Frodon"): "La liste " + names.toString() + " contient Fordon";
	}
	
	@Test
	public void test_error_with_message() {
		List<String> names = (List<String>) fellowshipOfTheRing.stream().map(pers->{return pers.getName();}).collect(Collectors.toList());
		assert !names.contains("Frodon"): "La liste " + names.toString() + " contient Fordon";
	}
}