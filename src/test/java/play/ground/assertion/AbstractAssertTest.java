package play.ground.assertion;

import java.util.ArrayList;

import play.ground.assertion.objects.Personnage;
import play.ground.assertion.objects.Personnage.Race;

public abstract class AbstractAssertTest {

	public String nullstr = null;
	public Personnage frodon = null;
	public Personnage sauron = null;
	public Personnage sam = null;
	public Personnage pippin = null;
	public Personnage merry = null;
	public Personnage boromir = null;
	public Personnage legolas = null;
	public Personnage aragorn = null;
	public Personnage gandalf = null;
	public Personnage gimli = null;
	public Personnage gollum = null;
	
	public ArrayList<Personnage> fellowshipOfTheRing = null;
	
	public abstract void test_null();
	
	public abstract void test_not_null();
	
	public abstract void test_string_equal();

	public abstract void test_string_partial();
	
	public abstract void test_is_in_collection();
	
	public abstract void  test_object_attribut_in_collection();
	
	public abstract void test_collection_size_contains();
	
	public abstract void test_exception() throws Exception;
	
	public abstract void test_error_wo_message();
	
	public abstract void test_error_with_message();
	
	public AbstractAssertTest(){
		
		frodon = new Personnage("Frodon", true, Race.HOBBIT);
		sam = new Personnage("Sam", true, Race.HOBBIT);
		pippin = new Personnage("Pippin", true, Race.HOBBIT);
		merry = new Personnage("Merry", true, Race.HOBBIT);
		sauron = new Personnage("Sauron", false, Race.UNKNOWN);
		boromir = new Personnage("Boromir", true, Race.HUMAN);
		aragorn = new Personnage("Aragorn", true, Race.HUMAN);
		gimli = new Personnage("Gimli", true, Race.DWARF);
		gollum = new Personnage("Gollum", false, Race.UNKNOWN);
		legolas = new Personnage("Legolas", true, Race.ELF);
		gandalf = new Personnage("Gandalf", true, Race.HUMAN);

		this.fellowshipOfTheRing = new ArrayList<Personnage>();
		this.fellowshipOfTheRing.add(frodon);
		this.fellowshipOfTheRing.add(sam);
		this.fellowshipOfTheRing.add(pippin);
		this.fellowshipOfTheRing.add(merry);
		this.fellowshipOfTheRing.add(boromir);
		this.fellowshipOfTheRing.add(aragorn);
		this.fellowshipOfTheRing.add(gimli);
		this.fellowshipOfTheRing.add(gollum);
		this.fellowshipOfTheRing.add(legolas);
		this.fellowshipOfTheRing.add(gandalf);
	}
}
