/**
 * Created by rk521 on 18.03.17.
 */

var init_url = encodeURIComponent(API_URL + 'acompanantes/paginate/' + user_id + '/' + api_key);

$('document').ready(function() {
    loadExpostitores(init_url);
    $('#search').click(function(){
        buscar();
    });
});

function showAll() {
    event.preventDefault();

    $.ajax({
        type : 'GET',
        url  : API_URL + 'acompanantes/all/' + user_id + '/' + api_key,
        success :  function(response) {
            if (response.status == 1) {
                var table = document.getElementById("tbl_acompanantes");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }

                $('#page-counter').html("<p>Mostrando todos</p>");
                $('#pagination').html("");
                $('#pagination').append('<li id="actual"><a href="#" onclick="loadExpostitores(init_url);">1</a></li>');

                response.acompanantes.forEach(function (expositor) {
                    var fecha = expositor.fecha;
                    if (expositor.hora_inicio) {
                        fecha = fecha + " " + expositor.hora_inicio
                    }

                    if (expositor.hora_fin) {
                        fecha = fecha + " - " + expositor.hora_fin
                    }
                    $('#tbl_acompanantes tr:last').after('<tr>' +
                        '<td>' + expositor.nombre + '</td>' +
                        '<td>' + fecha + '</td>' +
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
            if (response.status == 1) {
                var table = document.getElementById("tbl_acompanantes");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }
                var data = response.acompanantes;

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
                    $('#tbl_acompanantes tr:last').after('<tr>' +
                        '<td>' + expositor.nombre + '</td>' +
                        '<td>' + fecha + '</td>' +
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

function buscar() {
    //event.preventDefault();

    $.ajax({
        type : 'POST',
        url  : API_URL + 'acompanantes/search',
        data : {
            user_id: user_id,
            api_key: api_key,
            search: $('#q').val()
        },
        success :  function(response) {
            if (response.status == 1) {
                var table = document.getElementById("tbl_acompanantes");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }

                $('#page-counter').html("<p>Mostrando resultados de busqueda</p>");
                $('#pagination').html("");
                $('#pagination').append('<li id="actual"><a href="#" onclick="loadExpostitores(init_url);">1</a></li>');

                if (Object.keys(response.acompanantes).length > 0) {
                    response.acompanantes.forEach(function (expositor) {
                        var fecha = expositor.fecha;
                        if (expositor.hora_inicio) {
                            fecha = fecha + " " + expositor.hora_inicio
                        }

                        if (expositor.hora_fin) {
                            fecha = fecha + " - " + expositor.hora_fin
                        }
                        $('#tbl_acompanantes tr:last').after('<tr>' +
                            '<td>' + expositor.nombre + '</td>' +
                            '<td>' + fecha + '</td>' +
                            '<td align="center"><a href="#" onclick="openModal(' + expositor.id + ')" class="btn btn-primary">Ver</a></td>' +
                            '<td align="center"><a href="#" onclick="openEdit(' + expositor.id + ')" class="btn btn-primary">Editar</a></td>' +
                            '<td align="center"><a href="#" onclick="deletePrograma(' + expositor.id + ')" class="btn btn-danger">Eliminar</a></td>' +
                            '</tr>');
                        createModal(expositor);
                    });
                } else {
                    $('#tbl_acompanantes tr:last').after('<tr>' +
                        '<td>No se encontraron resultados para: ' + $('#q').val() + '</td>' +
                        '<td align="right"></td>' +
                        '</tr>');
                }
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
    var r = window.confirm("¿Deseas eliminar el programa?");

    if (r === true) {
        $.ajax({
            type : 'GET',
            url  : API_URL + 'acompanantes/delete/' + user_id + '/' + api_key + '/' + id,
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

function openEdit(id) {
    window.location.href = "acompanantes-add-evento.php#id=" + id;
}