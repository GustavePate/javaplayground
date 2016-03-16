package play.ground.assertion.objects;

public class Personnage {
	
	public enum Race {
		HOBBIT ("hobbit"), 
		ELF ("elf"), 
		HUMAN ("human"), 
		DWARF ("dwarf"), 
		UNKNOWN ("unknown");
		
		private final String racename;
		
		Race(String racename){
			this.racename=racename;
		}
		
		public String getName(){
			return racename;
		}
	}
	

	public String name = "";
	public Boolean nice = null;
	public Race race = null;
	
	public Personnage(String name, Boolean nice, Race race){
		this.name = name;
		this.nice = nice;
		this.race = race;
	}

	public String getName() {
		return name;
	}

	public Boolean getNice() {
		return nice;
	}

	public Race getRace() {
		return race;
	}
	
}
