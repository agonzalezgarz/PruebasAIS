document.body.onload=addElement();

//generar evento
var stats = document.getElementById("newButton");

function addElement(){
	
	//crear un form
	var stats = document.createElement("FORM");
	
	//crear un input
	var boton = document.createElement("INPUT");
	boton.setAttribute("ID","newButton");
	boton.setAttribute("type","submit");
	
	//cambiar contenido del boton
	boton.setAttribute("value", "Ver Estadisticas");
	
	//agregar submit al form
	stats.appendChild(boton);
	stats.setAttribute("ACTION","/stats");
	
	//añadir boton al final del body
	document.body.appendChild(stats);
}