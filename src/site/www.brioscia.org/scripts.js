// USATO tasto alto a sinistra
// Side navigation
function w3_open() {
  var x = document.getElementById("mySidebar");
	x.style.paddingTop = "10px";
  x.style.paddingLeft = "10px";
  x.style.display = "block";
}

function w3_close() {
  document.getElementById("mySidebar").style.display = "none";
}


// USATO nei tab HTML JAVA OUT
// Tabs
//
// evt:				oggetto che ha generato l'evento (oggetto nome del tab)
// tabMarkClass:	classe assegnata ad in nomi dei tab
// tabContentMark:	classe assegnata ai contentuti dei tab
// idTab:			tab da attivare
// NOTA: ricordarsi di invocare il click sul primo elemento
function selectTab(evt, tabMarkClass, tabContentClass, idTab) {
	var i;
	// disattivo tutti i contenuti
	var tabsContent = document.getElementsByClassName(tabContentClass);
	for (i = 0; i < tabsContent.length; i++) {
		tabsContent[i].style.display = "none";
	}
	// deseleziono tutti i tab
	var activebtn = document.getElementsByClassName(tabMarkClass);
	for (i = 0; i < activebtn.length; i++) {
		activebtn[i].className = activebtn[i].className.replace(" w3-theme-dark", "");
	}
	// visualizzo il contenuto selezionato
	document.getElementById(idTab).style.display = "block";
	// seleziono il tab cliccato
	evt.currentTarget.className += " w3-theme-dark";
}






// da verificare l'uso



// Tabs
function openTab(evt, tab, tabClass) {
  var i;
  var x = document.getElementsByClassName(tabClass);
  for (i = 0; i < x.length; i++) {
    x[i].style.display = "none";
  }
  var activebtn = document.getElementsByClassName("testbtn");
  for (i = 0; i < x.length; i++) {
    activebtn[i].className = activebtn[i].className.replace(" w3-theme-dark", "");
  }
  document.getElementById(tab).style.display = "block";
  evt.currentTarget.className += " w3-theme-dark";
}







// Accordions
function myAccFunc(id) {
  var x = document.getElementById(id);
  if (x.className.indexOf("w3-show") == -1) {
    x.className += " w3-show";
  } else { 
    x.className = x.className.replace(" w3-show", "");
  }
}

// Slideshows
var slideIndex = 1;

function plusDivs(n) {
  slideIndex = slideIndex + n;
  showDivs(slideIndex);
}

function showDivs(n) {
  var x = document.getElementsByClassName("mySlides");
  if (n > x.length) {slideIndex = 1}    
  if (n < 1) {slideIndex = x.length} ;
  for (i = 0; i < x.length; i++) {
    x[i].style.display = "none";  
  }
  x[slideIndex-1].style.display = "block";  
}

showDivs(1);

// Progress Bars
function move() {
  var elem = document.getElementById("myBar");   
  var width = 1;
  var id = setInterval(frame, 10);
  function frame() {
    if (width == 100) {
      clearInterval(id);
    } else {
      width++; 
      elem.style.width = width + '%'; 
      elem.innerHTML = width * 1  + '%';
    }
  }
}