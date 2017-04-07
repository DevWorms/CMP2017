/**
 * Created by rk521 on 18.03.17.
 */

var init_url = encodeURIComponent(API_URL + 'puebla/telefono/paginate/' + user_id + '/' + api_key);

$('document').ready(function() {
    if (isUpdate()) {
        $("#save").html('<i class="fa fa-plus-circle"></i> &nbsp; Actualizar teléfono');
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

        var url = API_URL + 'puebla/telefono/create';
        if (isUpdate()) {
            formData.append('id', $("#id").val());
            url = API_URL + 'puebla/telefono/update';
        }

        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
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
            }
        });
        return false;
    });
});

function showAll() {
    event.preventDefault();

    $.ajax({
        type : 'GET',
        url  : API_URL + 'puebla/telefono/all/' + user_id + '/' + api_key,
        success :  function(response) {
            if (response.status === 1) {
                var table = document.getElementById("tbl_eventos");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }

                $('#page-counter').html("<p>Mostrando todos</p>");
                $('#pagination').html("");
                $('#pagination').append('<li id="actual"><a href="#" onclick="loadExpostitores(init_url);">1</a></li>');

                response.telefonos.forEach(function (expositor) {
                    $('#tbl_eventos tr:last').after('<tr>' +
                        '<td>' + expositor.titulo + '</td>' +
                        '<td>' + expositor.telefono + '</td>' +
                        '<td align="center"><a href="#" onclick="openModal(' + expositor.id + ')" class="btn btn-primary">Ver</a></td>' +
                        '<td align="center"><a href="#" onclick="openEdit(' + expositor.id + ')" class="btn btn-primary">Editar</a></td>' +
                        '<td align="center"><a href="#" onclick="deletePrograma(' + expositor.id + ')" class="btn btn-danger">Eliminar</a></td>' +
                        '</tr>');
                    createModal(expositor);
                });
            } else {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });
            }
        },
        error : function (response) {
            console.log(response);
        }
    });
}

function loadExpostitores(url) {
    $.ajax({
        type : 'GET',
        url  : decodeURIComponent(url),
        success :  function(response) {
            if (response.status === 1) {
                var table = document.getElementById("tbl_eventos");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }
                var data = response.telefonos;

                $('#page-counter').html("<p>" + data.current_page + " de " + data.last_page + "</p>");
                $('#pagination').html("");
                if (data.current_page != 1) {
                    $('#pagination').append('<li id="previos" ><a href="#" onclick="loadExpostitores(\'' + encodeURIComponent(data.prev_page_url) + '\');">Anterior</a></li>');
                }
                $('#pagination').append('<li id="actual" class="active"><a href="#">' + data.current_page + '</a></li>');
                if (data.current_page != data.last_page) {
                    $('#pagination').append('<li id="next" ><a href="#" onclick="loadExpostitores(\'' + encodeURIComponent(data.next_page_url) + '\');">Siguiente</a></li>');
                }

                data.data.forEach(function (expositor) {
                    $('#tbl_eventos tr:last').after('<tr>' +
                        '<td>' + expositor.titulo + '</td>' +
                        '<td>' + expositor.telefono + '</td>' +
                        '<td align="center"><a href="#" onclick="openModal(' + expositor.id + ')" class="btn btn-primary">Ver</a></td>' +
                        '<td align="center"><a href="#" onclick="openEdit(' + expositor.id + ')" class="btn btn-primary">Editar</a></td>' +
                        '<td align="center"><a href="#" onclick="deletePrograma(' + expositor.id + ')" class="btn btn-danger">Eliminar</a></td>' +
                        '</tr>');
                    createModal(expositor);
                });
            } else {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });
            }
        },
        error : function (response) {
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
        }
    });
}

function createModal(item) {
    var actionsTemplate = _.template($('#modal_detalle_expositor').text());
    $('#modalsExpositores').append(actionsTemplate(item));
}

function openModal(id) {
    $("#DetalleExpositor-" + id).modal("show");
}

function deletePrograma(id) {
    var r = window.confirm("¿Deseas eliminar el sitio?");

    if (r === true) {
        $.ajax({
            type : 'GET',
            url  : API_URL + 'puebla/telefono/delete/' + user_id + '/' + api_key + '/' + id,
            success :  function(response) {
                if (response.status === 1) {
                    $("#error").fadeIn(1000, function() {
                        $("#error").html('<div class="alert alert-success"> &nbsp; ' + response.mensaje + '</div>');
                    });

                    loadExpostitores(init_url);

                    setTimeout(function () {
                        $("#error").html('');
                    }, 3000);
                } else {
                    $("#error").fadeIn(1000, function() {
                        $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                    });
                }
            },
            error : function (response) {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });
            }
        });
    }
}

function openEdit(id) {
    window.location.href = "agregar-puebla-tels.php#id=" + id;
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
        url  : API_URL + 'puebla/telefono/detail/' + user_id + '/' + api_key + '/' + id,
        success :  function(response) {
            if (response.status === 1) {
                var el = response.telefono;
                $("#id").val(el.id);
                $("#titulo").val(el.titulo);
                $("#telefono").val(el.telefono);

                if (el.imagen.nombre) {
                    $("label[for='imagen']").text("Actualizar imágen");
                    $("#file_img").html("<br><img height='250px' src='" + el.imagen.url + "' title='" + el.imagen.nombre + "'>");
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
        }
    });
}