package es.codeurjc.ais.tictactoe;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.codeurjc.ais.tictactoe.entidades.Player;

@DisplayName("Pruebas unitarias de la clase Board")
public class BoardTest {
	protected static TicTacToeGame game;
	protected static Player p1;
	protected static Player p2;
	private Board b;
	
	@BeforeAll
	protected static void prepareTest() {
		p1 = new Player(10,"O","Player 1");
		p2= new Player(11,"X","Player 2");
	}
	
	@BeforeEach
	private void empezarJuego() {
		game = new TicTacToeGame();
		game.addPlayer(p1);
		game.addPlayer(p2);
	}
	
	@Test
	@DisplayName("Prueba de GetCellsIfWinner\nSituación 1: Jugador 1 gana")
	void testGetCellsIfWinner1() {
		System.out.println("Prueba de GetCellsIfWinner");
		rellenar(1);
		b = game.getBoard();
		
		// comprobar resultado
		System.out.println("\tSituación 1: Gana Jugador1");
		assertNotNull(b.getCellsIfWinner(p1.getLabel()));
		assertNull(b.getCellsIfWinner(p2.getLabel()));
		System.out.println("\tSituación 1 superada!");
	}
	
	@Test
	@DisplayName("Prueba de GetCellsIfWinner\nSituación 2: Jugador 2 gana")
	void testGetCellsIfWinner2() {
		rellenar(2);
		b = game.getBoard();
		
		//comprobar resultado
		System.out.println("\tSituación 2: Gana Jugador2");
		assertNotNull(b.getCellsIfWinner(p2.getLabel()));
		assertNull(b.getCellsIfWinner(p1.getLabel()));
		System.out.println("\tSituación 2 superada!");
	}
	
	@Test
	@DisplayName("Prueba de GetCellsIfWinner\nSituación 3: Empate")
	void testGetCellsIfWinner3() {
		rellenar(0);
		b = game.getBoard();

		//comprobar resultado
		System.out.println("\tSituación 3: Empate");
		assertNull(b.getCellsIfWinner(p1.getLabel()));
		assertNull(b.getCellsIfWinner(p2.getLabel()));
		System.out.println("\tSituación 3 superada");
	}
	
	@Test
	@DisplayName("Prueba de GetCellsIfWinner\nSituación 4: Jugador 1 gana con la ultima jugada")
	void testGetCellsIfWinner4() {
		rellenar(3);
		b = game.getBoard();

		//comprobar resultado
		System.out.println("\tSituación 4: Gana Jugador1 con el ultimo movimiento");
		assertNotNull(b.getCellsIfWinner(p1.getLabel()));
		assertNull(b.getCellsIfWinner(p2.getLabel()));
		System.out.println("\tSituación 4 superada");
	}
	
	@Test
	@DisplayName("Prueba de CheckDraw\nSituación 1: Jugador 1 gana")
	void testCheckDraw1() {
		System.out.println("Prueba de CheckDraw");
		rellenar(1);
		b = game.getBoard();

		// comprobar resultado
		System.out.println("\tSituación 1: Gana Jugador1");
		assertTrue(b.checkDraw() == false);
		System.out.println("\tSituación 1 superada");
	}
	
	@Test
	@DisplayName("Prueba de CheckDraw\nSituación 2: Jugador 2 gana")
	void testCheckDraw2() {
		rellenar(2);
		b = game.getBoard();

		//comprobar resultado
		System.out.println("\tSituación 2: Gana Jugador2");
		assertTrue(b.checkDraw() == false);
		System.out.println("\tSituación 2 superada");
	}
	
	@Test
	@DisplayName("Prueba de CheckDraw\nSituación 3: Empate")
	void testCheckDraw3() {
		rellenar(0);
		b = game.getBoard();

		//comprobar resultado
		System.out.println("\tSituación 3: Empate");
		assertTrue(b.checkDraw());
		System.out.println("\tSituación 3 superada");
	}
	
	@Test
	@DisplayName("Prueba de CheckDraw\nSituación 4: Jugador 1 gana con el ultimo movimento")
	void testCheckDraw4() {
		rellenar(3);
		b = game.getBoard();

		//comprobar resultado
		System.out.println("\tSituación 4: Gana Jugador1 con la ultima jugada");
		assertFalse(b.checkDraw());
		System.out.println("\tSituación 4 superada");
	}

	protected static void rellenar(int winner) {
		int m1,m2;
		switch(winner){
			case 0:
					//O X O  	 		
					//O O X
					//X O X 
				for(int i = 0; i< 9; i++) {
					if(game.checkTurn(10)) {
						if(i == 0 || i==2|| i==4 || i== 7) game.mark(i);
						else {
							if(i==6) game.mark(3);
							else if(i==8)game.mark(7);
						}
					} else {
						if(i==1 || i==5) game.mark(i);
						else {
							if(i==3) game.mark(6);
							else if(i==7) game.mark(8);
						}
					}
				}		
				break;
			case 1:
					//O O O
					//X X -
					//- - -
				m1=-1;
				m2=2;
				for(int i=0; i < 6; i++) {
					if(game.checkTurn(10)) {
						m1 = m1+1;
						game.mark(m1);
					}
					else {
						m2= m2+1;
						game.mark(m2);
					}				
				}
				break;
			case 2:
					//X X X
					//- O -
					//O - O
				m1=10;
				m2=-1;
				for(int i=0; i < 6; i++) {
					if(game.checkTurn(11)) {
						m2 = m2+1;
						game.mark(m2);
					}
					else {
						m1= m1-2;
						game.mark(m1);
					}	
					
				}
				break;
			case 3:
					//O X O
					//X O X
					//X O O
				for (int i = 0; i < 9; i++) {
					if(i==6) game.mark(7);
					else if(i==7) game.mark(6);
					else game.mark(i);
				}
				break;
		}
	}
}