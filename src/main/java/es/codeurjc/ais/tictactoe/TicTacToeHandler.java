package es.codeurjc.ais.tictactoe;

import java.util.List;
//imports para juego
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.codeurjc.ais.tictactoe.TicTacToeGame.Event;
import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;

//imports para base de datos
import es.codeurjc.ais.tictactoe.entidades.Partidas;
import es.codeurjc.ais.tictactoe.entidades.Player; // estaria en los dos :)
import es.codeurjc.ais.tictactoe.repositorios.RepositorioJugador;
import es.codeurjc.ais.tictactoe.repositorios.StatisticsService;

//This class is a component. It can autowire any other component using @Autowired annotation 
public class TicTacToeHandler extends TextWebSocketHandler {
	
	enum ClientToServerAction {
		JOIN_GAME, MARK, RESTART
	}

	static class ServerToClientMsg {
		EventType action;
		Object data;
	}

	static class ClientToServerMsg {
		ClientToServerAction action;
		Data data;
	}

	static class Data {
		int playerId;
		int cellId;
		String name;
	}

	private ObjectMapper json = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

	private TicTacToeGame game;
	private ConcurrentMap<WebSocketSession, Connection> connections = new ConcurrentHashMap<>();

	//agegamos repositorios
	@Autowired RepositorioJugador repJugador;
	@Autowired StatisticsService repPartidas;
	
	public TicTacToeHandler() {
		newGame();
	}

	private void newGame() {
		game = new TicTacToeGame();
	}

	@Override
	public synchronized void afterConnectionEstablished(WebSocketSession session) throws Exception {
		if (this.connections.size() < 2) {
			Connection connection = new Connection(json, session);
			this.connections.put(session, connection);
			this.game.addConnection(connection);
		} else {
			System.err.println(
					"Error: Trying to connect more than 2 players at the same time. Rejecting incoming client");
			session.close();
		}
	}

	@Override
	public synchronized void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		this.connections.remove(session);
		
		if (!this.connections.isEmpty()) {
			Event reconnectEvent = new Event();
			reconnectEvent.type = EventType.RECONNECT;
			this.connections.values().iterator().next().sendEvent(reconnectEvent);
		}

		this.newGame();
	}

	@Override
	public synchronized void handleTextMessage(WebSocketSession session, TextMessage wsMsg) throws Exception {		
		String jsonMsg = wsMsg.getPayload();
		Player player1 = null, player2 = null, aux = null;
		Partidas result1, result2;

		System.out.println("Received message '" + jsonMsg + "' from client " + session.getId());
		ClientToServerMsg msg;

		try {
			msg = json.readValue(jsonMsg, ClientToServerMsg.class);
		} catch (Exception e) {
			showError(jsonMsg, e);
			return;
		}
		try {
			switch (msg.action) {
			case JOIN_GAME:
				int numPlayers = game.getPlayers().size();
				String letter = numPlayers == 0 ? "X" : "O";
				Boolean found = false;
				List<Player> players = repJugador.findAll();
				for(Player p: players) { 
					if(p.getName().equals(msg.data.name)) {
						aux = p;
						aux.setLabel(letter);	
						found = true;
					}
				}
				if(!found) {
					aux = new Player(players.size()+1, letter, msg.data.name);	
					repPartidas.save(aux.getPartidas());
					repJugador.save(aux);
				}
				game.addPlayer(aux);
				break;
				
			case MARK:
				if (game.checkTurn(msg.data.playerId)) {
					game.mark(msg.data.cellId);	
				}
				
				//obtener jugadores y los resultados finales (historial: partidas ganadas, perdidad, empatadas) de sus partidas
				player1 = game.getPlayers().get(0);
				player2 = game.getPlayers().get(1);
				result1 = player1.getPartidas();
				result2 = player2.getPartidas();
				
				if(game.checkDraw()) {
					result1.actualizar(0);
					result2.actualizar(0);
				}
				else if(game.checkWinner().win) {
					if(game.checkTurn(player1.getId())) {
						//si el jugador 1 gana
						result1.actualizar(1);
						result2.actualizar(-1);
					} else {
						//si el jugador 2 gana
						result2.actualizar(1);
						result1.actualizar(-1);
					}
				}
				//guardar cambios
				repPartidas.save(result1);
				repPartidas.save(result2);
				repJugador.save(player1);
				repJugador.save(player2);
				break;
				
			case RESTART:
				game.restart();
				break;
			}
			
		} catch (Exception e) {
			showError(jsonMsg, e);
		}
	}

	private void showError(String jsonMsg, Exception e) {
		System.err.println("Exception processing message: " + jsonMsg);
		e.printStackTrace(System.err);
	}
}