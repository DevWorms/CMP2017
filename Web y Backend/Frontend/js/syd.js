/**
 * Created by rk521 on 18.03.17.
 */

var init_url = encodeURIComponent(API_URL + 'deportivos/paginate/' + user_id + '/' + api_key);

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
        url  : API_URL + 'deportivos/all/' + user_id + '/' + api_key,
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status == 1) {
                var table = document.getElementById("tbl_syd");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }

                $('#page-counter').html("<p>Mostrando todos</p>");
                $('#pagination').html("");
                $('#pagination').append('<li id="actual"><a href="#" onclick="loadExpostitores(init_url);">1</a></li>');

                response.eventos.forEach(function (expositor) {
                    var fecha = expositor.fecha;
                    if (expositor.hora_inicio) {
                        fecha = fecha + " " + expositor.hora_inicio
                    }

                    if (expositor.hora_fin) {
                        fecha = fecha + " - " + expositor.hora_fin
                    }
                    $('#tbl_syd tr:last').after('<tr>' +
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
        },
        complete: function () {
            $("#wait").hide();
        }
    });
}

function loadExpostitores(url) {
    $.ajax({
        type : 'GET',
        url  : decodeURIComponent(url),
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status == 1) {
                var table = document.getElementById("tbl_syd");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }
                var data = response.eventos;

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
                    $('#tbl_syd tr:last').after('<tr>' +
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
        },
        complete: function () {
            $("#wait").hide();
        }
    });
}

function buscar() {
    //event.preventDefault();

    $.ajax({
        type : 'POST',
        url  : API_URL + 'deportivos/search',
        data : {
            user_id: user_id,
            api_key: api_key,
            search: $('#q').val()
        },
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status == 1) {
                var table = document.getElementById("tbl_syd");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }

                $('#page-counter').html("<p>Mostrando resultados de busqueda</p>");
                $('#pagination').html("");
                $('#pagination').append('<li id="actual"><a href="#" onclick="loadExpostitores(init_url);">1</a></li>');

                if (Object.keys(response.eventos).length > 0) {
                    response.eventos.forEach(function (expositor) {
                        var fecha = expositor.fecha;
                        if (expositor.hora_inicio) {
                            fecha = fecha + " " + expositor.hora_inicio
                        }

                        if (expositor.hora_fin) {
                            fecha = fecha + " - " + expositor.hora_fin
                        }
                        $('#tbl_syd tr:last').after('<tr>' +
                            '<td>' + expositor.nombre + '</td>' +
                            '<td>' + fecha + '</td>' +
                            '<td align="center"><a href="#" onclick="openModal(' + expositor.id + ')" class="btn btn-primary">Ver</a></td>' +
                            '<td align="center"><a href="#" onclick="openEdit(' + expositor.id + ')" class="btn btn-primary">Editar</a></td>' +
                            '<td align="center"><a href="#" onclick="deletePrograma(' + expositor.id + ')" class="btn btn-danger">Eliminar</a></td>' +
                            '</tr>');
                        createModal(expositor);
                    });
                } else {
                    $('#tbl_syd tr:last').after('<tr>' +
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
        },
        complete: function () {
            $("#wait").hide();
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
            url  : API_URL + 'deportivos/delete/' + user_id + '/' + api_key + '/' + id,
            beforeSend: function () {
                $("#wait").show();
            },
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
            },
            complete: function () {
                $("#wait").hide();
            }
        });
    }
}

function openEdit(id) {
    window.location.href = "syd-add-evento.php#id=" + id;
}