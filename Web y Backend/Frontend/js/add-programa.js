/**
 * Created by rk521 on 18.03.17.
 */

var hash_num = null;

$('document').ready(function() {
    loadCategorias();

    if (isUpdate()) {
        $("#btn_crearEvento").html('<i class="fa fa-plus-circle"></i> &nbsp; Actualizar evento');
        if (window.location.hash !== '') {
            hash_num = parseInt(window.location.hash.substring(4));
            $("#crearEvento input, textarea, button").prop("disabled", true);
            if (hash_num > 0) {
                getElement(hash_num);
            }
        }
    }

    $("form#crearEvento").submit(function() {
        $("#error").fadeIn(1000, function() {
            $("#error").html("");
        });

        var formData = new FormData($(this)[0]);
        formData.append('user_id', user_id);
        formData.append('api_key', api_key);

        var url = API_URL + 'programa/create';
        if (isUpdate()) {
            formData.append('id', $("#id").val());
            url = API_URL + 'programa/update';
        }

        $.ajax({
            url: url,
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
});

function loadCategorias() {
    $.ajax({
        url: API_URL + 'categoria/all/' + user_id + '/' + api_key,
        type: 'GET',
        beforeSend: function () {
            $("#wait").show();
        },
        success: function (data) {
            data.categorias.forEach(function (categoria) {
                $('#categoria_id').append($('<option>', {
                    value: categoria.id,
                    text: categoria.nombre
                }));
            });
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
}

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
        url  : API_URL + 'programa/detail/' + user_id + '/' + api_key + '/' + id,
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status === 1) {
                var el = response.programa;
                $("#id").val(el.id);
                $("#nombre").val(el.nombre);
                $("#lugar").val(el.lugar);
                $("#fecha").val(el.fecha);
                $("#hora_inicio").val(el.hora_inicio);
                $("#hora_fin").val(el.hora_fin);
                $("#recomendaciones").val(el.recomendaciones);
                $("#maps_url").val(el.maps_url);

                if (el.foto.nombre) {
                    $("label[for='archivo']").text("Actualizar imágen");
                    $("#file_img").html("<br><img height='250px' src='" + el.foto.url + "' title='" + el.foto.nombre + "'>");
                }

                $("#crearEvento input, textarea, button").prop("disabled", false);
            } else {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });

                $("#crearEvento input, textarea, button").prop("disabled", true);
            }
        },
        error : function (response) {
            response = $.parseJSON(response.responseText);
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
            $("#crearEvento input, textarea, button").prop("disabled", true);
        },
        complete: function () {
            $("#wait").hide();
        }
    });
}