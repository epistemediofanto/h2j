function h2j_replace(formName, tagDest, url) {
	
	var form = document.getElementById(formName);
	var query = "", element;
	for (var i = 0; i < form.elements.length; i++) {
		element = form.elements[i];
		if (element.name) {
			query += encodeURIComponent(element.name) + "=" + encodeURIComponent(element.value) + "&";
		}
	}

	var request = new XMLHttpRequest();
	request.onreadystatechange = doReplace;
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(query); // Form data is here

	function doReplace() {
		if (request.readyState == 4) {
			if (request.status == 200) {
				var resultText = request.responseText;
				var tag = document.getElementById(tagDest);
			tag.outerHTML = resultText;
			
			} else {
				// Error
			}
		}
	}
}
