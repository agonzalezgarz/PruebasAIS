package es.codeurjc.ais.tictactoe;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;
import es.codeurjc.ais.tictactoe.entidades.Player;

@DisplayName("Pruebas de dobles de la clase TicTacToeGame")
class TicTacToeGameTest {
	private static Player p1;
	private static Player p2;
	private static TicTacToeGame game;
	private static Connection c1;
	private static Connection c2;
	
	@BeforeAll
	protected static void setUp() {
		BoardTest.prepareTest();
		p1 = BoardTest.p1;
		p2 = BoardTest.p2;
		c1 = mock(Connection.class);	
		c2 = mock(Connection.class);	
	}
	
	@BeforeEach
	private void startGame() {
		//creamos juego nuevo, restablecer
		game = new TicTacToeGame();
		BoardTest.game = game;
		
		//añadir conexiones
		game.addConnection(c1);
		game.addConnection(c2);
		
		//añadir juagdores
		game.addPlayer(p1);
		game.addPlayer(p2);
	}
	
	@AfterEach
	private void endConnection() {
		//borrar registro de las capturas
		reset(c1);
		reset(c2);
	}
	
	@Test
	@DisplayName("Prueba de conexion")
	void testConnection() {	
		System.out.println("Empezando prueba de conexion de los jugadores");		
		System.out.println("\tProbando conexion 1");
		
		//capturar evento
		ArgumentCaptor<Player> argument = ArgumentCaptor.forClass(Player.class);
		verify(c1,times(2)).sendEvent(eq(EventType.JOIN_GAME),argument.capture());
		System.out.println("\t\tExito en la captura de JOIN_GAME");
		
		//obtener valor
		List<Player> pls=argument.getAllValues();
		List<Player> sol= new LinkedList<>();
		sol.add(p1);
		sol.add(p2);
		
		//comprobar valor
		Assertions.assertEquals(pls.get(0),sol);
		Assertions.assertEquals(pls.get(1),sol);
		
		System.out.println("\t\tExito en la obtención de los jugadores");
		System.out.println("\tProbando conexion 2");
		
		//capturar evento 
		verify(c2,times(2)).sendEvent(eq(EventType.JOIN_GAME),argument.capture());
		System.out.println("\t\tExito en la captura de JOIN_GAME");
		
		//obtener valor
		pls=argument.getAllValues();
		
		//comprobar valor
		Assertions.assertEquals(pls.get(0),sol);
		Assertions.assertEquals(pls.get(1),sol);
		System.out.println("\t\tExito en la obtención de los jugadores");
		System.out.println("\tPrueba de conexion superada");
	}
	
	@Test
	@DisplayName("Prueba de turnos")
	void checkTurn() {
		System.out.println("Empezando prueba de cambio de turno");
		
		//capturar evento
		ArgumentCaptor<Player> argument = ArgumentCaptor.forClass(Player.class);
		BoardTest.rellenar(3);
		verify(c1,times(9)).sendEvent(eq(EventType.SET_TURN), argument.capture());
		System.out.println("\tExito en la captura de SET_TURN");
		
		//obtener datos
		List<Player> pls = argument.getAllValues();
		Player player;

		//comprobar
		for(int i = 0; i < pls.size(); i++) {
			if(i % 2 == 0) player = p1;
			else player = p2;
			Assertions.assertEquals(pls.get(i),player);
		}
		System.out.println("\tExito en la obtención de los jugadores");
		
		//capturar evento
		ArgumentCaptor<WinnerValue> arg = ArgumentCaptor.forClass(WinnerValue.class);
		verify(c1,times(1)).sendEvent(eq(EventType.GAME_OVER),arg.capture());
		System.out.println("\tExito en la obtención GAME_OVER");
		
		//obtener datos
		List<WinnerValue> res =arg.getAllValues();
		
		//comprobar
		Assertions.assertEquals(res.get(0).player,p1);
		System.out.println("\tExito en la obtención del ganador");
		System.out.println("\tPrueba de cambio te turno superada");
	}
	
	@Test
	@DisplayName("Prueba de invocacion de mark")
	void testMark() {
		System.out.println("Empezando la prueba de invocacion de mark");

		//capturar evento
		ArgumentCaptor<Player> argument = ArgumentCaptor.forClass(Player.class);
		BoardTest.rellenar(1);
		verify(c1,times(5)).sendEvent(eq(EventType.MARK), argument.capture());
		System.out.println("\tExito en la captura de MARK, caso: gana jugador 1");
		
		reset(c1);
		startGame();
		BoardTest.rellenar(2);
		verify(c1,times(6)).sendEvent(eq(EventType.MARK), argument.capture());
		System.out.println("\tExito en la captura de MARK, caso: gana jugador 2");

		reset(c1);
		startGame();
		BoardTest.rellenar(0);
		verify(c1,times(9)).sendEvent(eq(EventType.MARK), argument.capture());
		System.out.println("\tExito en la captura de MARK, caso: empate");
				
		reset(c1);
		startGame();
		BoardTest.rellenar(3);
		verify(c1,times(9)).sendEvent(eq(EventType.MARK), argument.capture());
		System.out.println("\tExito en la captura de MARK, caso: gana jugador 1 con el ultimo movimiento");
		System.out.println("\tPrueba de invocacion de mark superada");
	}
	
	@Test
	@DisplayName("Prueba de Victoria")
	void testCheckWinnerMessage() {
		System.out.println("Empezando prueba de victoria");
		BoardTest.rellenar(1);
		
		//capturar evento
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(c1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		System.out.println("\tExito en la captura de GAME_OVER");
		
		//obtener los valores
		List<WinnerValue> res = argument.getAllValues();
		Assertions.assertTrue(res.get(0).player.equals(p1));
		System.out.println("\tExito en la obtención del ganador");
		
		//comprobar
		Assertions.assertFalse(res.get(0).player.equals(p2));
		System.out.println("\tPrueba de victoria superada");
	}
	
	@Test
	@DisplayName("Prueba de la derrota")
	void testCheckLoserMessage() {
		System.out.println("Empezando prueba de la derrota");
		BoardTest.rellenar(2);
		
		//capturar evento
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(c1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		System.out.println("\tExito en la captura de GAME_OVER");
		
		//obtener los valores
		List<WinnerValue> res = argument.getAllValues();
		Assertions.assertTrue(res.get(0).player.equals(p2));
		System.out.println("\tExito en la obtención del ganador");
		
		//comprobar
		Assertions.assertFalse(res.get(0).player.equals(p1));
		System.out.println("\tPrueba de derrota superada");
	}
	
	@Test
	@DisplayName("Prueba del empate")
	void testCheckDrawMessage() {
		System.out.println("Empezando prueba del empate");
		BoardTest.rellenar(0);
		
		//capturar evento
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(c1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
		System.out.println("\tExito en la captura de GAME_OVER");
		
		//obtener los valores
		List<WinnerValue> res = argument.getAllValues();
		Assertions.assertNull(res.get(0));
		System.out.println("\tExito en la obtención del empate");
		
		//comprobar
		System.out.println("\tPrueba del empate superada");
	}
}