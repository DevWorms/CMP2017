/**
 * Created by rk521 on 18.03.17.
 */

var hash_num = null;

$('document').ready(function() {

    if (isUpdate()) {
        $("#guardar").html('<i class="fa fa-plus-circle"></i> &nbsp; Actualizar ruta');
        if (window.location.hash !== '') {
            hash_num = parseInt(window.location.hash.substring(4));
            $("#crearRuta input, textarea, button").prop("disabled", true);
            if (hash_num > 0) {
                getElement(hash_num);
            }
        }
    }

    $("form#crearRuta").submit(function() {
        $("#error").fadeIn(1000, function() {
            $("#error").html("");
        });

        var formData = new FormData($(this)[0]);
        formData.append('user_id', user_id);
        formData.append('api_key', api_key);

        var url = API_URL + 'ruta/create';
        if (isUpdate()) {
            formData.append('id', $("#id").val());
            url = API_URL + 'ruta/update';
        }

        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            success: function (data) {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-success"> &nbsp; ' + data.mensaje + '</div>');
                });

                $('#crearRuta')[0].reset();
            },
            cache: false,
            contentType: false,
            processData: false,
            error : function (response) {
                response = response.responseJSON;
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });
            }
        });
        return false;
    });
});

function isUpdate() {
    if (window.location.hash !== '') {
        var check = parseInt(window.location.hash.substring(4));
        return check > 0;
    } else {
        return false;
    }
}

function getElement(id) {
    $.ajax({
        type : 'GET',
        url  : API_URL + 'ruta/detail/' + user_id + '/' + api_key + '/' + id,
        success :  function(response) {
            if (response.status === 1) {
                var el = response.ruta;
                $("#id").val(el.id);
                $("#titulo").val(el.titulo);
                $("#descripcion").val(el.descripcion);

                if (el.image.nombre) {
                    $("label1[for='archivo_pdf']").text("Actualizar PDF");
                    $("#file_pdf").html("<br><a href='" + el.image.url + "' target='_blank'><img height='250px' src='" + el.image.url + "'></a>");
                }

                $("#crearRuta input, textarea, button").prop("disabled", false);
            } else {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });

                $("#crearRuta input, textarea, button").prop("disabled", true);
            }
        },
        error : function (response) {
            response = $.parseJSON(response.responseText);
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
            $("#crearRuta input, textarea, button").prop("disabled", true);
        }
    });
}