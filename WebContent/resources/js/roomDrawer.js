var salasCriadas = [];

function Sala(htmlElement,id,x,y,h,w,color) {
	this.htmlElement = htmlElement;
	this.id = id;
	this.x = x;
	this.y = y;
	this.h = h;
	this.w = w;
	this.color = color;
}

function criarSala(idElementoCanvas, idSala, xPos, yPos, height, width){
	
	//sala ja existe?
	var salaCriadaAnteriormente = findSalaById(idSala);
	if(salaCriadaAnteriormente != null){
		console.log("Sala already exists. > " + idSala);
		return;
	}
	
	//encontrar o elemento no html
	var htmlElement = document.querySelector("#" + idElementoCanvas);
	if(htmlElement == null){
		console.log("Html element not found. > " + idElementoCanvas);
		return;
	}
	
	//encontrar o tamanho deste elemento html
	var htmlElementWidth = htmlElement.offsetWidth;
	var htmlElementHeight = htmlElement.offsetHeight;
	
	//pegar contexto 2d do canvas
	var ctx2d = htmlElement.getContext("2d");
	if(ctx2d == null){
		console.log("Html element does not have a 2d context. > " + idElementoCanvas);
		return;
	}
	
	//desenhar as bordas da sala no elemento html passado
	ctx2d.fillStyle = "white";
	ctx2d.fillRect(xPos, yPos, width, height);
	ctx2d.strokeRect(xPos, yPos, width, height);
	
	//criar um objeto sala
	var salaCriada = new Sala(htmlElement, idSala, xPos, yPos, height, width, "white");
	
	//adicionar ele no array de salas criadas
	salasCriadas.push(salaCriada);
}

//color is a hexa string (#FFFFFF)
function colorirSala(idSala, color){
	if(idSala == null){
		console.log("Null sala id");
		return;
	}
	
	if(color == null){
		console.log("Null sala color");
		return;
	}
	
	var salaCriada = findSalaById(idSala);
	if(salaCriada == null){
		console.log("Unknow sala id " + idSala);
		return;
	}
	
	//da sala encontrada, pegar contexto 2d do canvas do elemento que a sala foi desenhada
	var ctx2d = salaCriada.htmlElement.getContext("2d");
	//colorir a sala
	ctx2d.fillStyle = color;
	ctx2d.fillRect(salaCriada.x, salaCriada.y, salaCriada.w, salaCriada.h);
	ctx2d.strokeRect(salaCriada.x, salaCriada.y, salaCriada.w, salaCriada.h);
	
	//setar cor no objeto sala
	salaCriada.color = color;
}

//busca uma sala criada por id
function findSalaById(idSala){
	var salaCriada = null;
	for (i = 0; i < salasCriadas.length; i++) { 
		salaCriada = salasCriadas[i];
		if(salaCriada.id == idSala){
			return salaCriada;
		}
	}
	return null;
}

function resizeCanvas(){
	var canvas = document.getElementById("roomsCanvas");
	canvas.style.width='100%';
	canvas.style.height='100%';
	canvas.width  = canvas.offsetWidth;
	canvas.height = canvas.offsetHeight;
	salasCriadas = [];
}