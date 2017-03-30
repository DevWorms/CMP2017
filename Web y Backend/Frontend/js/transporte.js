/**
 * Created by rk521 on 18.03.17.
 */

var init_url = encodeURIComponent(API_URL + 'ruta/paginate/' + user_id + '/' + api_key);

$('document').ready(function() {
    $('#search').click(function(){
        buscar();
    });

    $("form#crearRuta").submit(function() {
        $("#error").fadeIn(1000, function() {
            $("#error").html("");
        });

        var formData = new FormData($(this)[0]);
        formData.append('user_id', user_id);
        formData.append('api_key', api_key);

        $.ajax({
            url: API_URL + 'ruta/create',
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

function loadExpostitores(url) {
    $.ajax({
        type : 'GET',
        url  : decodeURIComponent(url),
        success :  function(response) {
            if (response.status == 1) {
                var table = document.getElementById("tbl_eventos");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }
                var data = response.rutas;

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
                    var fecha = expositor.fecha;
                    if (expositor.hora_inicio) {
                        fecha = fecha + " " + expositor.hora_inicio
                    }

                    if (expositor.hora_fin) {
                        fecha = fecha + " - " + expositor.hora_fin
                    }
                    $('#tbl_eventos tr:last').after('<tr>' +
                        '<td>' + expositor.titulo + '</td>' +
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

function showAll() {
    event.preventDefault();

    $.ajax({
        type : 'GET',
        url  : API_URL + 'ruta/all/' + user_id + '/' + api_key,
        success :  function(response) {
            if (response.status == 1) {
                var table = document.getElementById("tbl_eventos");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }

                $('#page-counter').html("<p>Mostrando todos</p>");
                $('#pagination').html("");
                $('#pagination').append('<li id="actual"><a href="#" onclick="loadExpostitores(init_url);">1</a></li>');

                response.rutas.forEach(function (expositor) {
                    var fecha = expositor.fecha;
                    if (expositor.hora_inicio) {
                        fecha = fecha + " " + expositor.hora_inicio
                    }

                    if (expositor.hora_fin) {
                        fecha = fecha + " - " + expositor.hora_fin
                    }
                    $('#tbl_eventos tr:last').after('<tr>' +
                        '<td>' + expositor.titulo + '</td>' +
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

function createModal(item) {
    var actionsTemplate = _.template($('#modal_detalle_expositor').text());
    $('#modalsExpositores').append(actionsTemplate(item));
}

function openModal(id) {
    $("#DetalleExpositor-" + id).modal("show");
}

function deletePrograma(id) {
    var r = window.confirm("¿Deseas eliminar la ruta?");

    if (r === true) {
        $.ajax({
            type : 'GET',
            url  : API_URL + 'ruta/delete/' + user_id + '/' + api_key + '/' + id,
            success :  function(response) {
                if (response.status == 1) {
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