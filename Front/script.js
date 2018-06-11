var loginOK = 'root';
var passwordOK = 'root';

//Test en local
function checkInfo(login, password){
		//On check le log, on envoie le log a l'api
		if (login == loginOK && password == passwordOK) {
			//alert("connecté");
			document.location.href = './page.html';
		}else{
			alert("Mauvaises info, veuillez entrer un login et un mdp valide");
		}
	};

//Envoie du JSON a l'api pour check si le login et le mdp est corect
//Manque une entete, probleme de CORS
function sendInfo(login,password){
	var markers = [{ "usernameOrEmail": login, "password": password}];

	$.ajax({
        type: "POST",
        url: "http://authenticator-efrei.azurewebsites.net/api/auth/login",
        data: JSON.stringify(markers),
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        // implementé ici les succes et fail
        success: function(data){alert(data);},
        failure: function(errMsg){alert(errMsg);}
  });
}

//getter mdp
function recupMDP(){
    var password =$('#password').val();
    return password;
}

//getter login 
function recupLogin(){
	var login =$('#usernameOrEmail').val();
	return login;
}

//Event du click
$('#button').click(function(){
	var login = recupLogin();
	var password = recupMDP();
	sendInfo(login,password);
	//checkInfo(login,password);
});

$(document).keyup(function(touche){ // on écoute l'évènement keyup()
    var appui = touche.which || touche.keyCode; // le code est compatible tous navigateurs grâce à ces deux propriétés

    if(appui == 13){ // si le code de la touche est égal à 13 (entrer)
    	var login = recupLogin();
    	var password = recupMDP();
    	sendInfo(login,password);
    	//checkInfo(login,password);
    }
});


//recup le form le transformer en JSON 
//requete
//http://authenticator-efrei.azurewebsites.net/api/auth/login

//fonction ajax qui att la reponse de l'api

//le data correspond au form en json
/*
ici le markers sera le login et le password

var markers = [{ "position": "128.3657142857143", "markerPosition": "7" },
               { "position": "235.1944023323615", "markerPosition": "19" },
               { "position": "42.5978231292517", "markerPosition": "-3" }];

 $.ajax({
        type: "POST",
        url: "http://authenticator-efrei.azurewebsites.net/api/auth/login",
        data: markers,
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        // implementé ici les succes et fail
        success: function(data){alert(data);},
        failure: function(errMsg) {
            alert(errMsg);
        }
  });*/
