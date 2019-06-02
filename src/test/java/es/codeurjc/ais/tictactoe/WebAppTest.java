package es.codeurjc.ais.tictactoe;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import es.codeurjc.ais.tictactoe.Player;
import io.github.bonigarcia.wdm.WebDriverManager;

@DisplayName("Pruebas del sistema de la App")
class WebAppTest {
	private WebDriver browser1, browser2;
	private WebDriverWait w1, w2;
	private static Player p1,p2;
	private static int n1,n2;
	
	@BeforeAll
	protected static void setUp() {
		WebDriverManager.chromedriver().setup();
		WebApp.start();
		BoardTest.prepareTest();
		p1= BoardTest.p1;
		p2= BoardTest.p2;
		n1=0;
		n2=50;
	}
	
	@AfterAll
	protected static void setDown() {
		WebApp.stop();
	}
	
	@BeforeEach
	private void prepareDrivers() {
		WebElement name,name2, startbtn1, startbtn2;
		String s;
		//acceder a navegadores
		browser1 = new ChromeDriver();
		browser2 = new ChromeDriver();
		
		//añadir tiempo de espera para los elementos
		browser1.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		browser2.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		w1 = new WebDriverWait(browser1,30);
		w2 = new WebDriverWait(browser2,30);
		
		//conectar
		browser1.get("localhost:8081");
		browser2.get("localhost:8081");
		
		//esperar que se cargen
		w1.until(ExpectedConditions.visibilityOf(browser1.findElement(By.id("nickname"))));
		w2.until(ExpectedConditions.visibilityOf(browser2.findElement(By.id("nickname"))));
		w1.until(ExpectedConditions.visibilityOf(browser1.findElement(By.id("startBtn"))));
		w2.until(ExpectedConditions.visibilityOf(browser2.findElement(By.id("startBtn"))));
		
		//obtener la casilla de identificación
		name = browser1.findElement(By.id("nickname"));
		name2 = browser2.findElement(By.id("nickname"));
		name.clear();
		name2.clear();
		
		//insertar Nombre del jugador1
		w1.until(ExpectedConditions.elementToBeClickable(name));
		name.click();
		s = p1.getName().split(" ")[0]+n1;
		n1++;
		name.sendKeys(s);
		
		//insertar nombre del jugador 2
		w2.until(ExpectedConditions.elementToBeClickable(name2));
		name2.click();
		s = p2.getName().split(" ")[0]+n2;
		n2++;
		name2.sendKeys(s);
		
		//empezar juego
		startbtn1 = browser1.findElement(By.id("startBtn"));
		w1.until(ExpectedConditions.elementToBeClickable(startbtn1));
		startbtn1.click();
		
		//empezar el juego
		startbtn2 = browser2.findElement(By.id("startBtn"));
		w2.until(ExpectedConditions.elementToBeClickable(startbtn2));
		startbtn2.click();
	}
	
	@AfterEach
	public void teardown() {
		if (browser1 != null) {
			browser1.quit();
		}
		if (browser2 != null) {
			browser2.quit();
		}
	}
	
	@Test
	@DisplayName("Prueba de los valores de nickname")
	void testNames(){
		String value,sol;
		System.out.println("Empezando la prueba de nickname");
		System.out.println("\tProbando nickname del jugador 1");
		
		//obtener valor
		value = browser1.findElement(By.id("nickname")).getAttribute("value");
		sol = p1.getName().split(" ")[0]+(n1-1);
		
		//comprobar
		Assertions.assertEquals(sol, value);
		System.out.println("\tExito a la hora de obtener el nombre del jugador1");
		
		//obtener valor
		sol = p2.getName().split(" ")[0]+(n2-1);
		value = browser2.findElement(By.id("nickname")).getAttribute("value");
		
		//comprobar
		Assertions.assertEquals(sol, value);
		System.out.println("\tExito a la hora de obtener el nombre del jugador1");
		System.out.println("\tPrueba de los valores de nickname superada");
	}
	
	@Test
	@DisplayName("Prueba de Victoria")
	void testWinner() {
		String message, winnerMessage;
		winnerMessage = p1.getName().split(" ")[0]+(n1-1)+" wins! "+p2.getName().split(" ")[0]+(n2-1)+" looses.";
		System.out.println("Empezando prueba de victoria");
		
		try {
			play(1);
			w1.until(ExpectedConditions.alertIsPresent());
			w2.until(ExpectedConditions.alertIsPresent());
		
		}catch(UnhandledAlertException e) {
			System.out.println("\tGana el jugador 1");
			System.out.println("\tComprobando mensaje de victoria para el jugador 1");
			
			//obtener mensaje de alerta
			message = browser1.switchTo().alert().getText();
			//comprobar mensaje del primer navegador
			Assertions.assertEquals(winnerMessage, message);
			System.out.println("\tExito al obtener el mensaje de victoria para el jugador 1");
			
			System.out.println("\tComprobando el mensaje de derrota para el jugador 2");
			message = browser1.switchTo().alert().getText();
			//comprobar mensaje del segundo navegador
			Assertions.assertEquals(winnerMessage, message);
			System.out.println("\tExito al obtener el mensaje de derrota para el jugador 2");
		}
		System.out.println("\tPrueba de victoria superada");
	}
	
	@Test
	@DisplayName("Prueba de derrota")
	void testLoser() {
		String message, winnerMessage;
		winnerMessage = p2.getName().split(" ")[0]+(n2-1)+" wins! "+p1.getName().split(" ")[0]+(n1-1)+" looses.";
		System.out.println("Empezando prueba de derrota");
		
		try {
			//assegurar que la pagina se ha cargado
			play(2);
			w1.until(ExpectedConditions.alertIsPresent());
			w2.until(ExpectedConditions.alertIsPresent());
			
		}catch(UnhandledAlertException e) {
			System.out.println("\tGana el jugador 2");
			System.out.println("\tComprobando mensaje de derrota para el jugador 1");
			
			//obtener mensaje de alerta
			message = browser1.switchTo().alert().getText();
			//comprobar mensaje del primer navegador
			Assertions.assertEquals(winnerMessage, message);
			System.out.println("\tExito al obtener el mensaje de derrota para el jugador 1");
			
			System.out.println("\tComprobando el mensaje de victoria para el jugador 2");
			message = browser2.switchTo().alert().getText();
			//comprobar mensaje del segundo navegador
			Assertions.assertEquals(winnerMessage, message);
			System.out.println("\tExito al obtener el mensaje de victoria para el jugador 2");
		}
		System.out.println("\tPrueba de derrota superada");
	}
	
	@Test
	@DisplayName("Prueba de Empate")
	void testDraw() {
		String message, winnerMessage;
		winnerMessage = "Draw!";
		System.out.println("Empezando prueba de empate");
		play(0);
		
		System.out.println("\tNo hay ganador");
		System.out.println("\tComprobando mensaje de empate para el jugador 1");
		
		//obtener mensaje de alerta
		w1.until(ExpectedConditions.alertIsPresent());
		message = browser1.switchTo().alert().getText();
		//comprobar mensaje del primer navegador
		Assertions.assertEquals(winnerMessage, message);
		System.out.println("\tExito al obtener el mensaje de empate para el jugador 1");

		System.out.println("\tComprobando el mensaje de empate para el jugador 2");
		w2.until(ExpectedConditions.alertIsPresent());
		message = browser1.switchTo().alert().getText();
		//comprobar mensaje del segundo navegador
		Assertions.assertEquals(winnerMessage, message);
		System.out.println("\tExito al obtener el mensaje de empate para el jugador 2");
		
		System.out.println("\tPrueba de empate superada");
	}
	
	private void play(int n) {
		WebElement el;
		int cell=0;
		int a,b;
		String cellId;
		
		//esperar a que se hayan caragado las celdas
		waitForCells();
		
		switch(n) {
		case 0:
			while(cell<9) {
				//obtener celda
				if(cell%2==0) {
					//turno del jugador 1
					if(cell == 6) a = 3;
					else if(cell == 8) a = 7;
					else a = cell;
					cellId="cell-"+a;
					el = browser1.findElement(By.id(cellId));
					w1.until(ExpectedConditions.elementToBeClickable(el));
				}
				else {
					//turno del jugador 2
					if(cell == 3) b = 6;
					else if(cell == 7) b = 8;
					else b = cell;
					cellId="cell-"+b;
					el = browser2.findElement(By.id(cellId));
					w2.until(ExpectedConditions.elementToBeClickable(el));
				}
				cell++;
				el.click();
			}
			break;
		
		case 1://gana jugador 1
			a=-1; b=10;
			
			while(cell<9) {
				//obtener celda
				if(cell%2==0) {
					a=a+1;
					cellId="cell-"+a;
					el = browser1.findElement(By.id(cellId));
					w1.until(ExpectedConditions.elementToBeClickable(el));
				}
				else {
					b=b-2;
					cellId="cell-"+b;
					el = browser2.findElement(By.id(cellId));
					w2.until(ExpectedConditions.elementToBeClickable(el));
				}
				
				cell++;
				//modificar celda
				el.click();
			}
			break;
		
		case 2://gana el jugador 2
			a=10; b=-1;
			while(cell < 9) {
				//obtener celda
				if(cell%2==0) {
					//turno del jugador 1
					a=a-2;
					cellId="cell-"+a;
					el = browser1.findElement(By.id(cellId));
					w1.until(ExpectedConditions.elementToBeClickable(el));
				}
				else {
					//turno del jugador 2
					b=b+1;
					cellId="cell-"+b;
					el = browser2.findElement(By.id(cellId));
					w2.until(ExpectedConditions.elementToBeClickable(el));
				}
				
				cell++;
				//modificar celda
				el.click();
			}
			break;
		
		case 3:
			while(cell < 9) {
				//obtener celda
				if(cell%2==0) {
					//turno del jugador 1
					if(cell == 6) a = 7;
					else a = cell;
					cellId="cell-"+a;
					el = browser1.findElement(By.id(cellId));
					w1.until(ExpectedConditions.elementToBeClickable(el));
				}
				else {
					//turno del jugador 2
					if(cell == 7) b = 6;
					else b = cell;
					cellId="cell-"+b;
					el = browser2.findElement(By.id(cellId));
					w2.until(ExpectedConditions.elementToBeClickable(el));
				}
				
				cell++;
				//modificar celda
				el.click();
			}
			break;
		}
	}
	
	private void waitForCells() {	
		String aux, cellId="cell-";
		for (int i = 0; i < 9; i++) {
			aux =cellId;
			aux=aux+i;
			w1.until(ExpectedConditions.visibilityOf(browser1.findElement(By.id(aux))));
			w2.until(ExpectedConditions.visibilityOf(browser1.findElement(By.id(aux))));
		}
	}
}