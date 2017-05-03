/**
 * Created by rk521 on 18.03.17.
 */

$('document').ready(function() {
    $("form#crearEvento").submit(function() {
        $("#error").fadeIn(1000, function() {
            $("#error").html("");
        });

        var formData = new FormData($(this)[0]);
        formData.append('user_id', user_id);
        formData.append('api_key', api_key);

        $.ajax({
            url: API_URL + 'puebla/mapa/upload',
            type: 'POST',
            data: formData,
            beforeSend: function () {
                $("#wait").show();
            },
            success: function (data) {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-success"> &nbsp; ' + data.mensaje + '</div>');
                });

                $('#crearEvento')[0].reset();
            },
            cache: false,
            contentType: false,
            processData: false,
            error : function (response) {
                response = response.responseJSON;
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });
            },
            complete: function () {
                $("#wait").hide();
            }
        });
        return false;
    });

    $.ajax({
        url: API_URL + 'puebla/mapa/' + user_id + '/' + api_key,
        type: 'GET',
        beforeSend: function () {
            $("#wait").show();
        },
        success: function (data) {
            if (data.status === 1) {
                $("label[for='mapa']").text("Actualizar mapa");
                $("#file_map").html("<a href='" + data.mapa.url + "' target='_blank'>" + data.mapa.nombre + "</a>");
            }
        },
        error : function (response) {
            response = response.responseJSON;
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
        },
        complete: function () {
            $("#wait").hide();
        }
    });
});