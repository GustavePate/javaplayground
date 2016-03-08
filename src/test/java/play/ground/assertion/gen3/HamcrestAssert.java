package play.ground.assertion.gen3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.hamcrest.Matcher;
import org.hamcrest.core.AnyOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import play.ground.assertion.AbstractTest;
import play.ground.assertion.objects.Personnage;
import play.ground.assertion.objects.Personnage.Race;

public class HamcrestAssert extends AbstractTest{

	@Test
	public void test_null(){
		assertThat(nullstr, is(nullValue()));
	}
	
	@Test
	public void test_not_null() {
		assertThat(frodon, is(not(nullValue())));
	}
	
	@Test
	public void test_string_equal() {
		assertThat(frodon.getName(),is(equalTo("Frodon")));
	}

	@Test
	public void test_string_partial() {
		assertThat(frodon.getName(), allOf(startsWith("Fro"), endsWith("don")));
		assertThat(frodon.getName(), is(equalToIgnoringCase("frodon")));
	}

	@Test
	public void test_is_in_collection() {
		assertThat(frodon, is(not(sauron)));
		assertThat(fellowshipOfTheRing, hasItem(frodon));
	}
	
	@Test
	public void test_collection_size_contains() {
		assertThat(fellowshipOfTheRing, allOf(hasItems(frodon, sam), not(hasItem(sauron))));
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test 
	public void test_exception() throws Exception {
		/* pas d'utils particulier */
		thrown.expect(Exception.class);
	    thrown.expectMessage("boom!");
	    
		throw new Exception("boom!");
	}
	
	@Test
	public void test_object_attribut_in_collection(){
		/**** variant 1 ****/
		List<String> names = (List<String>) fellowshipOfTheRing.stream().map(pers->{return pers.getName();}).collect(Collectors.toList());
		assertThat(names, hasItems("Legolas", "Boromir", "Gandalf", "Frodon"));
		assertThat(names, not(hasItems("Sauron", "Elrond")));

		/**** variant 2 ****/
		List<HashMap<String, String>> names2 = fellowshipOfTheRing.stream().map(pers->{
				HashMap<String,String> member = new HashMap<String,String>();
				member.put(pers.getName(), pers.getRace().getName());
				return member;
				}).collect(Collectors.toList());
		
		HashMap<String,String> pers = new HashMap<String,String>();
		pers.put("Boromir", "human");
		assertThat(names2, hasItem(pers));
		
		pers.clear();
		pers.put("Sam", "hobbit");
		assertThat(names2, hasItem(pers));
		
		pers.clear();
		pers.put("Legolas", "elf");
		assertThat(names2, hasItem(pers));
	}


	@Test
	public void test_filter_on_collection(){
		/**** variant 1 ****/
		// filter collection before assertion
		List<Personnage> pers = fellowshipOfTheRing.stream().filter(p->p.getRace()==Race.HOBBIT).collect(Collectors.toList());
		assertThat(pers, hasItems(sam, frodon, pippin, merry));
		assertThat(pers.size(), is(4));
		
		/**** variant 2 ****/
		// filter collection with Java 8 Predicate
		List<Personnage> pers_v2 = fellowshipOfTheRing.stream().filter(p->p.getName().contains("o")).collect(Collectors.toList());
		assertThat(pers_v2, hasItems(aragorn, frodon, legolas, boromir, gollum));
		assertThat(pers_v2.size(), is(5));
		/**** variant 3 ****/
		
		// combining filtering and extraction (no we cannot)
		List<Personnage> pers_v3 = fellowshipOfTheRing.stream().filter(
				p->p.getName().contains("o")).collect(Collectors.toList());
		assertThat(pers_v3, hasItems(aragorn, frodon, legolas, boromir, gollum));
		assertThat(pers_v3.size(), is(5));

		List<String> pers_v3_1 = pers_v3.stream().map(
				p->p.getRace().getName()).collect(Collectors.toList());
		assertThat(pers_v3_1, hasItems("hobbit", "elf","human"));
		
	}
	
	@Test
	public void test_error_wo_message() {
		List<String> names = (List<String>) fellowshipOfTheRing.stream().map(pers->{return pers.getName();}).collect(Collectors.toList());
		assertThat(names, not(hasItem("Frodon")));
	}
	
	@Test
	public void test_error_with_message() {
		List<String> names = (List<String>) fellowshipOfTheRing.stream().map(pers->{return pers.getName();}).collect(Collectors.toList());
		assertThat("La liste " + names.toString() + " doit contenir Fordon", names, not(hasItem("Frodon")));
	}
}
