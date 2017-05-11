/**
 * Created by rk521 on 18.03.17.
 */

var init_url = encodeURIComponent(API_URL + 'users/paginate/' + user_id + '/' + api_key);

$('document').ready(function() {
    $("#search").click(function () {
        buscar();
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
            if (response.status == 1) {
                var table = document.getElementById("tbl_eventos");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }
                var data = response.users;

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
                    var nombre = "";
                    if (expositor.name) { nombre = expositor.name; }
                    if (expositor.last_name) { nombre = nombre + " " + expositor.last_name; }

                    $('#tbl_eventos tr:last').after('<tr>' +
                        '<td>' + expositor.id + '</td>' +
                        '<td>' + nombre + '</td>' +
                        '<td><a href="mailto:' + expositor.email + '">' + expositor.email + '</a></td>' +
                        '<td>' + expositor.association + '</td>' +
                        '<td>' + expositor.type + '</td>' +
                        '<td>' + expositor.created_at + '</td>' +
                        '</tr>');
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
        type : 'POST',
        url  : API_URL + 'users/all',
        data: {
            user_id: user_id,
            api_key: api_key
        },
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status == 1) {
                var table = document.getElementById("tbl_eventos");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }

                $('#page-counter').html("<p>Mostrando todos</p>");
                $('#pagination').html("");
                $('#pagination').append('<li id="actual"><a href="#" onclick="loadExpostitores(init_url);">1</a></li>');

                response.users.forEach(function (expositor) {
                    var nombre = "";
                    if (expositor.name) { nombre = expositor.name; }
                    if (expositor.last_name) { nombre = nombre + " " + expositor.last_name; }

                    $('#tbl_eventos tr:last').after('<tr>' +
                        '<td>' + expositor.id + '</td>' +
                        '<td>' + nombre + '</td>' +
                        '<td><a href="mailto:' + expositor.email + '">' + expositor.email + '</a></td>' +
                        '<td>' + expositor.association + '</td>' +
                        '<td>' + expositor.type + '</td>' +
                        '<td>' + expositor.created_at + '</td>' +
                        '</tr>');
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

function buscar() {
    //event.preventDefault();

    $.ajax({
        type : 'POST',
        url  : API_URL + 'users/search',
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
                var table = document.getElementById("tbl_eventos");
                for (var i = table.rows.length - 1; i > 0; i--) {
                    table.deleteRow(i);
                }

                $('#page-counter').html("<p>Mostrando resultados de busqueda</p>");
                $('#pagination').html("");
                $('#pagination').append('<li id="actual"><a href="#" onclick="loadExpostitores(init_url);">1</a></li>');

                if (Object.keys(response.users).length > 0) {
                    response.users.forEach(function (expositor) {
                        var nombre = "";
                        if (expositor.name) { nombre = expositor.name; }
                        if (expositor.last_name) { nombre = nombre + " " + expositor.last_name; }

                        $('#tbl_eventos tr:last').after('<tr>' +
                            '<td>' + expositor.id + '</td>' +
                            '<td>' + nombre + '</td>' +
                            '<td><a href="mailto:' + expositor.email + '">' + expositor.email + '</a></td>' +
                            '<td>' + expositor.association + '</td>' +
                            '<td>' + expositor.type + '</td>' +
                            '<td>' + expositor.created_at + '</td>' +
                            '</tr>');
                    });
                } else {
                    $('#tbl_eventos tr:last').after('<tr>' +
                        '<td colspan="6">No se encontraron resultados para: ' + $('#q').val() + '</td>' +
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