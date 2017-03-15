package play.ground.mapstruct.dto;

public class FromDTO {

	public String nom = "toto";
	public int age = 12;
	public Double poids = 55.5;

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Double getPoids() {
		return poids;
	}
	public void setPoids(Double poids) {
		this.poids = poids;
	}
	
}
