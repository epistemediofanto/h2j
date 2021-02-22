function update(idSelect, idTo) {

	var value = document.getElementById(idSelect).value;

	alert(value);

	var url = "./oggettoRMI.select(" + value + ")";

	let dropdown = document.getElementById(idTo);
	dropdown.length = 0;

	let defaultOption = document.createElement('option');
	defaultOption.text = 'Seleziona la lettera';

	dropdown.add(defaultOption);
	dropdown.selectedIndex = 0;

	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			const data = JSON.parse(this.responseText);
			for (let i = 0; i < data.length; i++) {
				option = document.createElement('option');
				option.text = data[i].descrizione;
				option.value = data[i].valore;
				dropdown.add(option);
			}
		}
	};

	xhttp.open("POST", url + ".xhtml", true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send("_json=true");

}


function valida(url, idForm) {

	var formEl = document.getElementById(idForm);
	var formData = new FormData(formEl);
	var json = JSON.stringify(JSON.stringify(Object.fromEntries(formData)));

	var xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			alert(this.responseText);
		}
	};

	xhttp.open("POST", url + ".xhtml", true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send("_json=true&oggettoRMI.jsonObject=" + json);
}
