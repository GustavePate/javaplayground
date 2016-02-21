package playground.yo.assertion.gen2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import playground.yo.assertion.AbstractTest;
import playground.yo.assertion.objects.Personnage;
import playground.yo.assertion.objects.Personnage.Race;

public class JunitAssert extends AbstractTest{

	@Test
	public void test_null(){
		assertNull(nullstr);
	}
	
	@Test
	public void test_not_null() {
		assertNotNull(frodon);
	}
	
	@Test
	public void test_string_equal() {
		assertEquals(frodon.getName(),"Frodon");
	}

	@Test
	public void test_string_partial() {
		assertTrue(frodon.getName().startsWith("Fro"));
		assertTrue(frodon.getName().endsWith("don"));
		assertTrue(frodon.getName().equalsIgnoreCase("frodon"));
	}

	@Test
	public void test_is_in_collection() {
		assertNotSame(frodon, sauron);
		assertTrue(fellowshipOfTheRing.contains(frodon));
	}
	
	@Test
	public void test_collection_size_contains() {
		assertTrue(fellowshipOfTheRing.contains(frodon));
		assertTrue(fellowshipOfTheRing.contains(sam));
		assertFalse(fellowshipOfTheRing.contains(sauron));
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test 
	public void test_exception() throws Exception {
		thrown.expect(Exception.class);
	    thrown.expectMessage("boom!");
	    
		throw new Exception("boom!");
	}
	
	@Test
	public void test_object_attribut_in_collection(){
		/**** variant 1 ****/
		List<String> names = (List<String>) fellowshipOfTheRing.stream().map(pers->{return pers.getName();}).collect(Collectors.toList());
		assertTrue(names.containsAll(Arrays.asList("Legolas", "Boromir", "Gandalf", "Frodon")));
		assertFalse(names.containsAll(Arrays.asList("Sauron","Elrond")));

		/**** variant 2 ****/
		List<HashMap<String, String>> names2 = fellowshipOfTheRing.stream().map(pers->{
				HashMap<String,String> member = new HashMap<String,String>();
				member.put(pers.getName(), pers.getRace().getName());
				return member;
				}).collect(Collectors.toList());
		
		HashMap<String,String> pers = new HashMap<String,String>();
		pers.put("Boromir", "human");
		assertTrue(names2.contains(pers));
		
		pers.clear();
		pers.put("Sam", "hobbit");
		assertTrue(names2.contains(pers));
		
		pers.clear();
		pers.put("Legolas", "elf");
		assertTrue(names2.contains(pers));
	}

	@Test
	public void test_filter_on_collection(){
		/**** variant 1 ****/
		// filter collection before assertion
		List<Personnage> pers = fellowshipOfTheRing.stream().filter(p->p.getRace()==Race.HOBBIT).collect(Collectors.toList());
		assertTrue(pers.containsAll(Arrays.asList(sam, frodon, pippin, merry)));
		assertEquals(pers.size(), 4);
		
		/**** variant 2 ****/
		// filter collection with Java 8 Predicate
		List<Personnage> pers_v2 = fellowshipOfTheRing.stream().filter(p->p.getName().contains("o")).collect(Collectors.toList());
		assertTrue(pers_v2.containsAll(Arrays.asList(aragorn, frodon, legolas, boromir, gollum)));
		assertEquals(pers_v2.size(), 5);
		
		/**** variant 3 ****/
		// combining filtering and extraction (yes we can)
		List<Personnage> pers_v3 = fellowshipOfTheRing.stream().filter(p->p.getName().contains("o")).collect(Collectors.toList());
		assertTrue(pers_v3.containsAll(Arrays.asList(aragorn, frodon, legolas, boromir, gollum)));
		assertEquals(pers_v3.size(), 5);
		
		List<String> pers_v3_1 = pers_v3.stream().map(p->p.getRace().getName()).collect(Collectors.toList());
		assertTrue(pers_v3_1.containsAll(Arrays.asList("hobbit", "elf","human")));
	}
	
	@Test
	public void test_error_wo_message() {
		List<String> names = (List<String>) fellowshipOfTheRing.stream().map(pers->{return pers.getName();}).collect(Collectors.toList());
		assertFalse(names.contains("Frodon"));
	}
	
	@Test
	public void test_error_with_message() {
		List<String> names = (List<String>) fellowshipOfTheRing.stream().map(pers->{return pers.getName();}).collect(Collectors.toList());
		assertFalse("La liste " + names.toString() + " contient Fordon", names.contains("Frodon"));
	}
}
