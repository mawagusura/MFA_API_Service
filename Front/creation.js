/**
*
* Sript de la création de compte
*/

//getter login 
function recupLogin(){
	var username =$('#username').val();
	return username;
}
//getter email 
function recupMail(){
	var mail =$('#email').val();
	return email;
}
//getter mdp
function recupMDP(){
    var password =$('#password').val();
    return password;
}
//getter login 
function recupComfirmMDP(){
	var comfirmPassword =$('#comfirmPassword').val();
	return comfirmPassword;
}

function recupPIN(){
	var pin =$('#pin').val();
	return pin;
}

function recupNbDigit(){
	var nbDigit =$('#pin').length();
	return nbDigit;
}


function checkMDP(password, comfirmPassword){
		//On check le log, on envoie le log a l'api
		if (password == comfirmPassword) {
			sendInfo(recupMail(),recupLogin(),password, recupPIN());
		}else{
			alert("Mauvaises info, les mots de passe ne correspondent pas");
		}
	};

function checkPIN(pin){
	if (recupPIN < 4) {
		alert("Le pin doit etre compose de 4 chiffre");
	}
}

function sendInfo(email,login,password,pin){
	var markers = [{"email": email, "usernameOrEmail": login, "password": password, "pin": pin}];
	$.ajax({
        type: "POST",
        url: "http://authenticator-efrei.azurewebsites.net/api/auth",
        data: JSON.stringify(markers),
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        // implementé ici les succes et fail
        success: function(data){document.location.href="page.html";},
        failure: function(errMsg){alert("nop");}
  });
}


//Event du click
$('#button').click(function(){
	document.location.reload(false);
	var password = recupMDP();
    var comfirmPassword = recupComfirmMDP();
    checkMDP(password,comfirmPassword);
    var pin = recupPIN();
    checkPIN(pin);
});

$(document).keyup(function(touche){ // on écoute l'évènement keyup()
    var appui = touche.which || touche.keyCode; // le code est compatible tous navigateurs grâce à ces deux propriétés

    if(appui == 13){ // si le code de la touche est égal à 13 (entrer)
    	var password = recupMDP();
    	var comfirmPassword = recupComfirmMDP();
    	checkMDP(password,comfirmPassword);
    	var pin = recupPIN();
    	checkPIN(pin);
    }
});