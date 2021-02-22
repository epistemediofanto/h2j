
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
