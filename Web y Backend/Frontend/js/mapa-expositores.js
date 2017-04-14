/**
 * Created by rk521 on 18.03.17.
 */

$('document').ready(function() {
    $.ajax({
        url: API_URL + 'mapa/expositores/' + user_id + '/' + api_key,
        type: 'GET',
        success: function (data) {
            data.stands.forEach(function (stand) {
                $("#expositores").append('<area shape="rect" alt="" title="" class="||" coords="' + stand.coords + '" href="#' + stand.id + '" target="" />');
            });
        },
        error : function (response) {
            response = response.responseJSON;
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
        }
    });

    window.onload = function () {
        var ImageMap = function (map) {
                var n,
                    areas = map.getElementsByTagName('area'),
                    len = areas.length,
                    coords = [],
                    previousWidth = 1920;
                for (n = 0; n < len; n++) {
                    coords[n] = areas[n].coords.split(',');
                }
                this.resize = function () {
                    var n, m, clen,
                        x = document.body.clientWidth / previousWidth;
                    for (n = 0; n < len; n++) {
                        clen = coords[n].length;
                        for (m = 0; m < clen; m++) {
                            coords[n][m] *= x;
                        }
                        areas[n].coords = coords[n].join(',');
                    }
                    previousWidth = document.body.clientWidth;
                    return true;
                };
                window.onresize = this.resize;
            },
            imageMap = new ImageMap(document.getElementById('map_ID'));
        imageMap.resize();
    }
});