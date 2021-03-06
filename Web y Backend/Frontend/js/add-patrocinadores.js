/**
 * Created by rk521 on 18.03.17.
 */

$('document').ready(function() {
    if (isUpdate()) {
        $("#btn_patrocinador").html('<i class="fa fa-plus-circle"></i> &nbsp; Actualizar patrocinador');
        if (window.location.hash !== '') {
            hash_num = parseInt(window.location.hash.substring(4));
            $("#crearPatrocinador input, textarea, button").prop("disabled", true);
            if (hash_num > 0) {
                getElement(hash_num);
            }
        }
    }

    $("form#crearPatrocinador").submit(function() {
        $("#error").fadeIn(1000, function() {
            $("#error").html("");
        });

        var formData = new FormData($(this)[0]);
        formData.append('user_id', user_id);
        formData.append('api_key', api_key);

        var url = API_URL + 'patrocinador/create';
        if (isUpdate()) {
            formData.append('id', $("#id").val());
            url = API_URL + 'patrocinador/update';
        }

        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            success: function (data) {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-success"> &nbsp; ' + data.mensaje + '</div>');
                });

                $('#crearPatrocinador')[0].reset();
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
        url  : API_URL + 'patrocinador/detail/' + user_id + '/' + api_key + '/' + id,
        success :  function(response) {
            if (response.status == 1) {
                var el = response.patrocinador;
                $("#id").val(el.id);
                $("#nombre").val(el.nombre);
                $("#acerca").val(el.acerca);
                $("#email").val(el.email);
                $("#stand").val(el.stand);
                $("#telefono").val(el.telefono);
                $("#tipo").val(el.tipo);
                $("#url").val(el.url);

                if (el.logo.nombre) {
                    $("label[for='archivo_logo']").text("Actualizar imágen");
                    $("#file_img").html("<br><img height='250px' src='" + el.logo.url + "' title='" + el.logo.nombre + "'>");
                }

                if (el.pdf.nombre) {
                    $("label[for='archivo_pdf']").text("Actualizar PDF");
                    $("#file_pdf").html("<br><a href='" + el.pdf.url + "' target='_blank'>" + el.pdf.nombre + "</a>");
                }

                $("#crearPatrocinador input, textarea, button").prop("disabled", false);
            } else {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });

                $("#crearPatrocinador input, textarea, button").prop("disabled", true);
            }
        },
        error : function (response) {
            response = $.parseJSON(response.responseText);
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
            $("#crearPatrocinador input, textarea, button").prop("disabled", true);
        }
    });
}