package es.codeurjc.ais.tictactoe.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.ais.tictactoe.entidades.Partidas;
import es.codeurjc.ais.tictactoe.entidades.Player;

public interface StatisticsService extends JpaRepository<Partidas, Long> {
	List<Partidas> findById(int id);
	List<Partidas> findOrderedById(int id);//Ordenar por id
	List<Partidas> findByJugador(Player jugador);
	List<Partidas> findOrderedByJugador(Player jugador);//Ordenar por jugador
	List<Partidas> findByPartidasGanadas(int PartidasGanadas);
	List<Partidas> findOrderedByPartidasGanadas(int PartidasGanadas);// Ordenador por Partidas ganadas
	List<Partidas> findByPartidasPerdidas(int PartidasPerdidas);
	List<Partidas> findOrderedByPartidasPerdidas(int PartidasPerdidas);// Ordenador por Partidas perdidas
	List<Partidas> findByPartidasEmpatadas(int PartidasEmpatadas);
	List<Partidas> findOrderedByPartidasEmpatadas(int PartidasEmpatadas);// Ordenador por Partidas empatadas
}