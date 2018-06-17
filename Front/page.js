$(function () {
    let searchParams = new URLSearchParams(window.location.search);
    let url = searchParams.get("url");
    let token = searchParams.get("token");

    $('#form').submit(function(e){
        e.preventDefault();

        $.ajax({
            type: "GET",
            url: "http://authenticator-efrei.azurewebsites.net/api/users/websites/terminate",
            headers: { 
                "url": url,
                "Authorization" : "Bearer "+token
            },
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend:function(){console.log("Sending AJAX request")},

            // implementé ici les succes et fail
            success: function (data) {  window.location.href =url+'?token='+token },
            error: function (errMsg) { printError(errMsg); }//TODO
        });
    })

    function printError(error){
        $("#error").html("");
        if(!error.responseJSON.errors) $("#error").html(error.responseJSON.message);
        else $("#error").html(error.responseJSON.errors[0].field +": "+ error.responseJSON.errors[0].defaultMessage);
    }

});