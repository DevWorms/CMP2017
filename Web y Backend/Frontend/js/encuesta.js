/**
 * Created by rk521 on 18.03.17.
 */

var init_url = encodeURIComponent(API_URL + 'encuesta/paginate/' + user_id + '/' + api_key);

$('document').ready(function() {

    if (isUpdate()) {
        $("#btn_crearEvento").html('<i class="fa fa-plus-circle"></i> &nbsp; Actualizar encuesta');
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

        var url = API_URL + 'encuesta/create';
        if (isUpdate()) {
            formData.append('encuesta_id', $("#id").val());
            url = API_URL + 'encuesta/update';
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
                $("#file_sm").html("");
                $("#file_xl").html("");
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

function loadExpostitores(url) {
    $.ajax({
        type : 'GET',
        url  : decodeURIComponent(url),
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status === 1) {
                var table = document.getElementById("tbl_eventos");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }
                var data = response.encuestas;

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
                        '<td><img src="' + expositor.filesm.url + '" width="10%"></td>' +
                        '<td align="center"><a href="#" onclick="openEncuesta(' + expositor.id + ')" class="btn btn-primary">Ver</a></td>' +
                        '<td align="center"><a href="#" onclick="openEdit(' + expositor.id + ')" class="btn btn-primary">Editar</a></td>' +
                        '<td align="center"><a href="#" onclick="deletePrograma(' + expositor.id + ')" class="btn btn-danger">Eliminar</a></td>' +
                        '</tr>');

                    var media = 0, media1 = 0, media2 = 0, media3 = 0;
                    if (expositor.usuariosRespondidos.length > 0) {
                        expositor.usuariosRespondidos.forEach(function (user) {
                            media1 = media1 + parseFloat(user.respuestas[0].respuesta);
                            media2 = media2 + parseFloat(user.respuestas[1].respuesta);
                            media3 = media3 + parseFloat(user.respuestas[2].respuesta);

                            media += (parseFloat(user.respuestas[0].respuesta) + parseFloat(user.respuestas[1].respuesta) + parseFloat(user.respuestas[2].respuesta)) / 3;
                        });

                        console.log(media + ": " + expositor.usuariosRespondidos.length);
                        media = media / expositor.usuariosRespondidos.length;
                        media1 = media1 / expositor.usuariosRespondidos.length;
                        media2 = media2 / expositor.usuariosRespondidos.length;
                        media3 = media3 / expositor.usuariosRespondidos.length;
                    }

                    expositor.media = Math.round(media * 100) / 100;
                    expositor.media1 = Math.round(media1 * 100) / 100;
                    expositor.media2 = Math.round(media2 * 100) / 100;
                    expositor.media3 = Math.round(media3 * 100) / 100;

                    var actionsTemplate = _.template($('#modal_detalle_expositor').text());
                    $('#modalsExpositores').append(actionsTemplate(expositor));

                    if (expositor.usuariosRespondidos.length > 0) {
                        expositor.usuariosRespondidos.forEach(function (user) {
                            var nombre = "";
                            if (user.name) { nombre = user.name; }
                            if (user.last_name) { nombre = nombre + " " + user.last_name; }

                            var general = parseFloat(user.respuestas[0].respuesta) + parseFloat(user.respuestas[1].respuesta) +
                                parseFloat(user.respuestas[2].respuesta);
                            general = Math.round((parseFloat(general) / 3) * 100) / 100;

                            $("#tbl_users_respondidos_" + expositor.id + " tr:last").after(
                                "<tr>" +
                                "<td>" + nombre + "</td>" +
                                "<td align='center'>" + Math.round(parseFloat(user.respuestas[0].respuesta) * 100) / 100 + "</td>" +
                                "<td align='center'>" + Math.round(parseFloat(user.respuestas[1].respuesta) * 100) / 100 + "</td>" +
                                "<td align='center'>" + Math.round(parseFloat(user.respuestas[2].respuesta) * 100) / 100 + "</td>" +
                                "<td align='center'>" + general + "</td>" +
                                "</tr>"
                            );
                        });
                    }

                    if (expositor.usuariosFaltantes.length > 0) {
                        expositor.usuariosFaltantes.forEach(function (us) {
                            var nombre = "";
                            if (us.name) { nombre = us.name; }
                            if (us.last_name) { nombre = nombre + " " + us.last_name; }

                            $("#tbl_users_faltantes_" + expositor.id + " tr:last").after(
                                "<tr>" +
                                "<td>" + nombre + "</td>" +
                                "</tr>"
                            );
                        });
                    }
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

function showAll() {
    event.preventDefault();

    $.ajax({
        type : 'GET',
        url  : API_URL + 'encuesta/admin/all/' + user_id + '/' + api_key,
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status === 1) {
                var table = document.getElementById("tbl_eventos");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }

                $('#page-counter').html("<p>Mostrando todos</p>");
                $('#pagination').html("");
                $('#pagination').append('<li id="actual"><a href="#" onclick="loadExpostitores(init_url);">1</a></li>');

                response.encuestas.forEach(function (expositor) {
                    $('#tbl_eventos tr:last').after('<tr>' +
                        '<td><img src="' + expositor.filesm.url + '" width="10%"></td>' +
                        '<td align="center"><a href="#" onclick="openEncuesta(' + expositor.id + ')" class="btn btn-primary">Ver</a></td>' +
                        '<td align="center"><a href="#" onclick="openEdit(' + expositor.id + ')" class="btn btn-primary">Editar</a></td>' +
                        '<td align="center"><a href="#" onclick="deletePrograma(' + expositor.id + ')" class="btn btn-danger">Eliminar</a></td>' +
                        '</tr>');

                    var media = 0, media1 = 0, media2 = 0, media3 = 0;
                    if (expositor.usuariosRespondidos.length > 0) {
                        expositor.usuariosRespondidos.forEach(function (user) {
                            media1 = media1 + parseFloat(user.respuestas[0].respuesta);
                            media2 = media2 + parseFloat(user.respuestas[1].respuesta);
                            media3 = media3 + parseFloat(user.respuestas[2].respuesta);

                            media += (media1 + media2 + media3) / 3;
                        });

                        media = media / expositor.usuariosRespondidos.length;
                        media1 = media1 / expositor.usuariosRespondidos.length;
                        media2 = media2 / expositor.usuariosRespondidos.length;
                        media3 = media3 / expositor.usuariosRespondidos.length;
                    }

                    expositor.media = Math.round(media * 100) / 100;
                    expositor.media1 = Math.round(media1 * 100) / 100;
                    expositor.media2 = Math.round(media2 * 100) / 100;
                    expositor.media3 = Math.round(media3 * 100) / 100;

                    var actionsTemplate = _.template($('#modal_detalle_expositor').text());
                    $('#modalsExpositores').append(actionsTemplate(expositor));

                    if (expositor.usuariosRespondidos.length > 0) {
                        expositor.usuariosRespondidos.forEach(function (user) {
                            var nombre = "";
                            if (user.name) { nombre = user.name; }
                            if (user.last_name) { nombre = nombre + " " + user.last_name; }

                            var general = parseFloat(user.respuestas[0].respuesta) + parseFloat(user.respuestas[1].respuesta) +
                                parseFloat(user.respuestas[2].respuesta);
                            general = Math.round((parseFloat(general) / 3) * 100) / 100;

                            $("#tbl_users_respondidos_" + expositor.id + " tr:last").after(
                                "<tr>" +
                                "<td>" + nombre + "</td>" +
                                "<td align='center'>" + Math.round(parseFloat(user.respuestas[0].respuesta) * 100) / 100 + "</td>" +
                                "<td align='center'>" + Math.round(parseFloat(user.respuestas[1].respuesta) * 100) / 100 + "</td>" +
                                "<td align='center'>" + Math.round(parseFloat(user.respuestas[2].respuesta) * 100) / 100 + "</td>" +
                                "<td align='center'>" + general + "</td>" +
                                "</tr>"
                            );
                        });
                    }

                    if (expositor.usuariosFaltantes.length > 0) {
                        expositor.usuariosFaltantes.forEach(function (us) {
                            var nombre = "";
                            if (us.name) { nombre = us.name; }
                            if (us.last_name) { nombre = nombre + " " + us.last_name; }

                            $("#tbl_users_faltantes_" + expositor.id + " tr:last").after(
                                "<tr>" +
                                "<td>" + nombre + "</td>" +
                                "</tr>"
                            );
                        });
                    }
                });
            } else {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + ' what</div>');
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

function deletePrograma(id) {
    var r = window.confirm("¿Deseas eliminar la encuesta?");

    if (r === true) {
        $.ajax({
            type : 'GET',
            url  : API_URL + 'encuesta/delete/' + user_id + '/' + api_key + '/' + id,
            beforeSend: function () {
                $("#wait").show();
            },
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
            },
            complete: function () {
                $("#wait").hide();
            }
        });
    }
}

function isUpdate() {
    if (window.location.hash !== '') {
        var check = parseInt(window.location.hash.substring(4));
        return check > 0;
    } else {
        return false;
    }
}

function openEdit(id) {
    window.location.href = "agregar-encuestas.php#id=" + id;
}

function openEncuesta(id) {
    $("#DetalleExpositor-" + id).modal("show");
}

function getElement(id) {
    $.ajax({
        type : 'GET',
        url  : API_URL + 'encuesta/detail/' + user_id + '/' + api_key + '/' + id,
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status === 1) {
                var el = response.encuesta;

                if (el.filesm.nombre) {
                    $("label[for='archivo_sm']").text("Actualizar imágen");
                    $("#file_sm").html("<br><a href='" + el.filesm.url + "' title='" + el.filesm.nombre + "' target='_blank'><img height='250px' src='" + el.filesm.url + "' title='" + el.filesm.nombre + "'></a>");
                }

                if (el.filexl.nombre) {
                    $("label[for='archivo_xl']").text("Actualizar imágen");
                    $("#file_xl").html("<br><img height='250px' src='" + el.filexl.url + "' title='" + el.filexl.nombre + "'>");
                }

                $("#id").val(el.id);

                var preguntas = el.preguntas;

                for (var i = 1; i < 4; i++) {
                    $('#pregunta' + i).val(
                        preguntas[i-1].pregunta
                    );
                }

                $("#archivo_sm").removeAttr('required');
                $("#archivo_xl").removeAttr('required');
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