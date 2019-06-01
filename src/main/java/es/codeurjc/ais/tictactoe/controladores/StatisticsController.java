package es.codeurjc.ais.tictactoe.controladores;

//para controlador
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//para BD
import es.codeurjc.ais.tictactoe.repositorios.StatisticsService;
import es.codeurjc.ais.tictactoe.repositorios.RepositorioJugador;

//para init
import javax.annotation.PostConstruct;
import es.codeurjc.ais.tictactoe.entidades.Partidas;
import es.codeurjc.ais.tictactoe.entidades.Player;

@Controller
public class StatisticsController {	
	@Autowired private StatisticsService partidas;
	@Autowired private RepositorioJugador players;
	
	@PostConstruct
	private void init() {
		for(int i = 0; i < 5; i++) {
			this.buildFakeStats("player "+i, i);
		}
	}
	
	@RequestMapping("/")
	public String jugar() {
		return "../static/index";
	}
	
	// Mostrar partidas
	@RequestMapping("/stats")
	public String mostrarPartidas(Model model) {
		model.addAttribute("partidas", partidas.findAll());
		return "mostrar";
	}
	
	//metodo para probar el funcionamiento de la BD
	private void buildFakeStats(String name, int id) {
		Player player;
		Partidas p;
		
		player = new Player();
		player.setName(name);
		player.setId(id);
		
		p = new Partidas();
		player.setLabel("X");
		partidas.save(p);
		
		player.setPartidas(p);
		players.save(player);
	}
}