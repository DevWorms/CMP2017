//var baseUrl = "http://congreso.digital";
var baseUrl = "http://localhost:8000";
var api_base = "http://cmp.devworms.com/api/";
//var api_base = "http://localhost:8001/api/";

$("#login").click(
    function () {
        var usuario = $("#id_usuario").val();
        var pass = $("#contrasena").val();

        if (usuario && pass) {
            $.ajax({
                type: 'POST',
                url: api_base + 'user/login/root',
                crossDomain: true,
                data: {
                    user: usuario,
                    password: pass
                },
                dataType: "json",
                success: function (response) {
                    if (response.status === 1) {
                        $("#error").html('<div class="alert alert-success"> &nbsp; Bienvenido</div>');
                        writeSession(response);
                    }
                    else {
                        $("#error").html('<div class="alert alert-warning"> &nbsp; ' + response.mensaje + '</div>');
                    }
                },
                error : function (response) {
                    response = response.responseJSON;
                    $("#error").html('<div class="alert alert-warning"> &nbsp; ' + response.mensaje + '</div>');
                }
            });
        } else {
            $("#error").html('<div class="alert alert-warning"> &nbsp; Ingresa usuario y contraseña</div>');
            setTimeout(function() {
                $("#error").html('');
            }, 3000);
        }
    }
);

function writeSession(data) {
    $.ajax({
        type: 'POST',
        url: baseUrl + '/sesion/iniciar.php',
        crossDomain: true,
        data: {
            id: data.user_id,
            api_key: data.api_key,
            key: data.key
        },
        success:function(response) {
            response = JSON.parse(response);
            setTimeout('window.location.href = "' + baseUrl +  response.url + '";', 1000);
        }
    });
}