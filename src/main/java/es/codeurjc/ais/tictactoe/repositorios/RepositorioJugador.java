package es.codeurjc.ais.tictactoe.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.ais.tictactoe.entidades.Player;

public interface RepositorioJugador extends JpaRepository<Player, Long> {
	List<Player> findById(int id);
	List<Player> findByName(String name);	
	List<Player> findOrderedById(int id);
	List<Player> findOrderedByName(String name);
}