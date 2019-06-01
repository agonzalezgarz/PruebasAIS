package es.codeurjc.ais.tictactoe.entidades;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Player {
	@Id private int id;
	private String label;
	private String name;	
	@OneToOne private Partidas partidas;
	
	public Player() {};
	
	public Player(int id, String label, String name) {
		this.id = id;
		this.label = label;
		this.name = name;
		this.partidas = new Partidas();
	}
	
	public Player(int id, String label, String name, Partidas p) {
		this.id = id;
		this.label = label;
		this.name = name;
		this.partidas = p;
	}

	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Partidas getPartidas() {
		return this.partidas;
	}
	
	public void setPartidas(Partidas partidas) {
		this.partidas = partidas;
	}
	
	public String toString() {
		return this.getName();
	}
}