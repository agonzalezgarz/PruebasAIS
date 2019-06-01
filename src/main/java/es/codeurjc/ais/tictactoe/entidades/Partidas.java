package es.codeurjc.ais.tictactoe.entidades;

import javax.persistence.*;

@Entity
public class Partidas {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO) private int id;
	@OneToOne (mappedBy="partidas") private Player jugador;
	private int partidasGanadas;
	private int partidasPerdidas;
	private int partidasEmpatadas;
	
	public Partidas() {
		this.partidasGanadas = 0;
		this.partidasEmpatadas = 0;
		this.partidasPerdidas = 0;
	}
	
	public Partidas(Player jugador, int resultado) {
		super();
		this.jugador = jugador;
		this.actualizar(resultado);
	}
	
	 public int getIdPartidas() {
		 return id;
	 }

	 public Player getJugador() {
		 return jugador;
	 }
	 
	 public void setJugador(Player jugador) {
		 this.jugador = jugador;
	 }
	 
	 public int getPartidasGanadas() {
		 return partidasGanadas;
	 }
	 
	 public void setPartidasGanadas(int n) {
		 this.partidasGanadas = n;
	 }
	
	 public int getPartidasPerdidas() {
		 return partidasPerdidas;
	 }
	 
	 public void setPartidasPerdidas(int n) {
		 this.partidasPerdidas = n;
	 }	
	 
	 public int getPartidasEmpatadas() {
		 return partidasEmpatadas;
	 }
	 
	 public void setPartidasEmpatadas(int n) {
		 this.partidasEmpatadas = n;
	 }	
	 
	 public void actualizar(int res) {
		 if(res > 0) this.partidasGanadas++;
		 else if(res < 0) this.partidasPerdidas++;
		 else this.partidasEmpatadas++;
	 }
	 
	 @Override 
	public String toString() {
		return "Partida [idPartida=" + id + ", partidasGanadas="+ partidasGanadas +", partidasPerdidas=" + partidasPerdidas +",partidasEmpatadas=" + partidasEmpatadas + "}"; 
	}	
}