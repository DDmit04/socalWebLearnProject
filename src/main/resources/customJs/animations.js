function rotateIcon(elemName, id) {
	var icon = document.getElementById(elemName + id);
	if(icon.classList.contains('fa-rotate-90')) {
		setTimeout(function() {
			icon.classList.remove('fa-rotate-90');
		}, 350);
	} else {
		setTimeout(function() {
			icon.classList.add('fa-rotate-90');	
		}, 350);
	}
}