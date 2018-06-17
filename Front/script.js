$(function () {
    

    //Envoie du JSON a l'api pour check si le login et le mdp est corect
    //Manque une entete, probleme de CORS
    function sendInfo(login, password) {
        var markers = { "usernameOrEmail": login, "password": password };


        $.ajax({
            type: "POST",
            url: "https://authenticator-efrei.azurewebsites.net/api/auth/login",
            headers: { "url": referrer() },
            data: JSON.stringify(markers),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend:function(){
                console.log("Sending AJAX request");
                $('#button').addClass('disabled');
            },

            // implementé ici les succes et fail
            success: function (data) { window.location.href ="Front/waiting-validation.html?url="+referrer()+'&token='+data.accessToken },
            error: function (errMsg) { printError(errMsg); }//TODO
        });
    }

    //getter mdp
    function recupMDP() {
        var password = $('#password').val();
        return password;
    }

    //getter login 
    function recupLogin() {
        var login = $('#usernameOrEmail').val();
        return login;
    }

    var referrer = function() {
        let searchParams = new URLSearchParams(window.location.search);
        let url = searchParams.get("url");
        console.log(url);
        if(!url && document.referrer){
            return document.referrer;
        }
        else return url;
    }
    console.log(referrer());

    //Event du click
    $('#form').submit(function(e) {

        e.preventDefault();

        var login = recupLogin();
        var password = recupMDP();
        sendInfo(login, password);
        //checkInfo(login,password);
    });

    $('#register').click(function(e){
        e.preventDefault();
        window.location.href = "Front/register.html?url="+referrer();
    })

    
    function printError(error){
        $('button').removeClass('disabled');
        $("#error").html("");
        if(!error.responseJSON.errors) $("#error").html(error.responseJSON.message);
        else $("#error").html(error.responseJSON.errors[0].field +": "+ error.responseJSON.errors[0].defaultMessage);
    }

});


