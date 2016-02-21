package playground.yo.assertion.gen4;

import playground.yo.assertion.AbstractTest;
import playground.yo.assertion.objects.Personnage;
import playground.yo.assertion.objects.Personnage.Race;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class AssertJAssert extends AbstractTest{

	@Test
	public void test_null() {
		assertThat(nullstr).isNullOrEmpty();
	}
	
	@Test
	public void test_not_null() {
		assertThat(frodon).isNotNull();
	}
	
	@Test
	public void test_string_equal(){
		assertThat(frodon.getName()).isEqualTo("Frodon");
	}
	
	@Test
	public void test_string_partial(){
		assertThat(frodon.getName()).startsWith("Fro")
		                           .endsWith("don")
		                           .isEqualToIgnoringCase("frodon");
	}
	
	@Test
	public void test_is_in_collection(){
		assertThat(frodon).isNotEqualTo(sauron).isIn(fellowshipOfTheRing);
	}

	@Test
	public void test_collection_size_contains(){
		// collection specific assertions
		assertThat(fellowshipOfTheRing).hasSize(10)
		                               .contains(frodon, sam)
		                               .doesNotContain(sauron);
	}

	@Test
	public void test_exception(){
		assertThatThrownBy(()->{throw new Exception("boom!");}).isInstanceOf(Exception.class).hasMessageContaining("boom");

		// Java 8 BDD style exception assertion                               
		Throwable thrown = catchThrowable(() -> {throw new Exception("boom!");});
		assertThat(thrown).isInstanceOf(Exception.class).hasMessageContaining("boom");
	}
	
	@Test
	public void test_object_attribut_in_collection(){
		/**** variant 1 ****/
		// using extracting magical feature to check fellowshipOfTheRing characters name :)
		assertThat(fellowshipOfTheRing).extracting("name")
		                               .contains("Boromir", "Gandalf", "Frodon", "Legolas")
		                               .doesNotContain("Sauron", "Elrond");
		/* OR */
		// Extracting with Java 8 love (type safe)
		assertThat(fellowshipOfTheRing).extracting(Personnage::getName)
		                               .contains("Boromir", "Gandalf", "Frodon", "Legolas")
		                               .doesNotContain("Sauron", "Elrond");

		/**** variant 2 ****/
		// Extracting multiple values at once (using tuple to group them)
		assertThat(fellowshipOfTheRing).extracting("name", "race.racename")
		                               .contains(tuple("Boromir", "human"),
		                                         tuple("Sam", "hobbit"),
		                                         tuple("Legolas", "elf"));
	}
	
	@Test
	public void test_filter_on_collection(){
		/**** variant 1 ****/
		// filter collection before assertion
		assertThat(fellowshipOfTheRing).filteredOn("race", Race.HOBBIT)
		                               .containsOnly(sam, frodon, pippin, merry);

		/**** variant 2 ****/
		// filter collection with Java 8 Predicate
		assertThat(fellowshipOfTheRing).filteredOn(character -> character.getName().contains("o"))
		                               .containsOnly(aragorn, frodon, legolas, boromir, gollum);

		/**** variant 3 ****/
		// combining filtering and extraction (yes we can)
		assertThat(fellowshipOfTheRing).filteredOn(character -> character.getName().contains("o"))
		                               .containsOnly(aragorn, frodon, legolas, boromir, gollum)
		                               .extracting(character -> character.getRace().getName())
		                               .contains("hobbit", "elf", "human");
	}
	
	@Test
	public void test_error_wo_message() {
		assertThat(fellowshipOfTheRing).extracting(Personnage::getName).doesNotContain("Frodon");
	}

	@Test
	public void test_error_with_message() {
		assertThat(fellowshipOfTheRing).extracting(Personnage::getName).doesNotContain("Frodon");
	}
}