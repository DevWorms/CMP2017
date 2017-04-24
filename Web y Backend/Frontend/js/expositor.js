/**
 * Created by rk521 on 24/04/17.
 */
$().ready(function () {
    if (window.location.hash !== '') {
        var id = parseInt(window.location.hash.substring(1));
        if (id > 0) {
            if (id) {
                loadExpositor(id);
            }
        }
    }
});

function loadExpositor(id) {
    $.ajax({
        url: API_URL + 'mapa/expositores/public/expositor/' + id,
        type: 'GET',
        success: function (response) {
            $("#error").html("");
            if (response.status == 1) {
                var expositor = response.expositor;

                if (expositor.logo) {
                    $("#logo").html('<img src="' + expositor.logo.url + '" title="' + expositor.logo.nombre + '" width="300px">');
                }
                $("#nombre").html('<p><strong>' + expositor.nombre + '</strong></p>');
                $("#contacto").html('<p><strong>Contacto: </strong><br>' +
                    '<a href="' + expositor.url + '">' + expositor.url + '</a><br>' +
                    expositor.telefono + '<br>' +
                    '<a href="mailto:' + expositor.email + '">' + expositor.email + '</a></p>');
                $("#acerca").html('<p><strong>Contacto: </strong><br>' + expositor.acerca + '</p>');

                console.log(expositor);
                if (expositor.pdf) {
                    $("#presentacion").html(
                        '<a type="button" href="' + expositor.pdf.url + '" class="btn btn-primary btn-lg" style="background-color: #2b3f9c">' +
                        '<img src="img/ic_picture_as_pdf_white_24dp_1x.png"> &nbsp;&nbsp;&nbsp;Presentación de la empresa' +
                        '</a>'
                    );
                } else {
                    $("#presentacion").html('');
                }

            } else {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });
            }
        },
        error: function (response) {
            response = response.responseJSON;
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
        }
    });
}