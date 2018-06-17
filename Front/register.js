$(function () {

    function sendInfo(login,email, password,pincode) {
        var markers = { "username": login, "email" : email, "password": password, "pinCode" : pincode };
        let searchParams = new URLSearchParams(window.location.search);
        let url = searchParams.get("url");
        console.log(url);

        $.ajax({
            type: "POST",
            url: "https://authenticator-efrei.azurewebsites.net/api/auth/",
            data: JSON.stringify(markers),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend:function(){console.log("Sending AJAX request")},

            // implementé ici les succes et fail
            success: function (data) { window.location.href ="https://mawagusura.github.io/Systeme-Information-EFREI-S6/index.html?url="+url },
            error: function (errMsg) { printError(errMsg); }//TODO
        });
    }

    //Event du click
    $('#form-register').submit(function(e) {

        e.preventDefault();

        var login = $("#username").val();
        var email = $("#email").val();
        var pincode = $("#pinCode").val();
        var password = $("#password").val();
        sendInfo(login,email,password,pincode);
        //checkInfo(login,password);
    });

    function printError(error){
        $("#error").html("");
        if(!error.responseJSON.errors) $("#error").html(error.responseJSON.message);
        else $("#error").html(error.responseJSON.errors[0].field +": "+ error.responseJSON.errors[0].defaultMessage);
    }

});



