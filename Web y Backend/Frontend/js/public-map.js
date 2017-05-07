/**
 * Created by rk521 on 18.03.17.
 */
var data = [];

function seating(xml) {
    var svg = d3.select(xml);
    var seats = svg.select("#layer3");
    var gE = svg.select("#gE");
    var i = 1;

    $.ajax({
        url: API_URL + 'mapa/v2',
        type: 'GET',
        beforeSend: function () {
            $("#wait").show();
        },
        success: function (response) {
            var expositores = response.expositores;

            var i = 1;
            seats.selectAll("rect").each(function() {
                var r = d3.select(this);
                if (r.attr('height') == 27.9 || ((r.attr('height') == 28.2) && (r.attr('width') == 27.9) ) ) {
                    r.attr("id", 'estante_' + i);
                    i = i+1;
                }
            });

            var room = d3.select(".room");
            room.node().appendChild(xml.documentElement);

            d3.select("#Layer_2").call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom));

            var secciones = [];
            expositores.forEach(function (expositor) {
                var estantes = expositor.estantes;
                estantes.forEach(function (stand) {
                    var locked = false;

                    if (stand.available == 0) {
                        locked = true;
                    }

                    var color = null;
                    if (stand.color) {
                        color = stand.color;
                    }

                    var title = "Estante " + stand.id;
                    title = title + ": " + expositor.nombre;

                    data.push({
                        id: stand.id,
                        title: title,
                        locked: locked,
                        color: color,
                        coords: stand.coords,
                        expositor: expositor.id
                    });

                    if (!contains.call(secciones, stand.coords)) {
                        secciones.push(stand.coords);
                    }
                });

                var r = seats.select("#estante_" + estantes[0].id);

                var yPosition = parseInt(r.attr("y")) + 10;
                var strings = expositor.nombre.split(" ");

                console.log(expositor.nombre + ": " + secciones);
                strings.forEach(function (string) {
                    gE
                        .append("text")
                        .text(string)
                        //.attr("transform", "matrix(" + expositor.coords + ")")
                        .attr("font-size", 5)
                        .attr("id", "txt_" + expositor.id)
                        .attr("x", r.attr("x"))
                        .attr("y", yPosition);

                    yPosition = yPosition + 10;
                });
            });

            handleResevations(data);
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

var contains = function(needle) {
    // Per spec, the way to identify NaN is that it is not equal to itself
    var findNaN = needle !== needle;
    var indexOf;

    if(!findNaN && typeof Array.prototype.indexOf === 'function') {
        indexOf = Array.prototype.indexOf;
    } else {
        indexOf = function(needle) {
            var i = -1, index = -1;

            for(i = 0; i < this.length; i++) {
                var item = this[i];

                if((findNaN && item !== item) || item === needle) {
                    index = i;
                    break;
                }
            }

            return index;
        };
    }

    return indexOf.call(this, needle) > -1;
};

function loadExpositorLocation(id) {
    $.ajax({
        url: API_URL + 'mapa/expositores/public/expositor/' + id,
        type: 'GET',
        beforeSend: function () {
            $("#wait").show();
        },
        success: function (response) {
            if (response.status == 1) {
                var expositor = response.expositor;
                g.attr("transform", "translate(" + expositor.coords + ")scale(" + 4 + ")");
                svg.select("#gE").attr("transform", "translate(" + expositor.coords + ")scale(" + 4 + ")");

                svg.selectAll("text").each(function() {
                    d3.select(this).attr("font-size", 8);
                });

                svg.select("#txt_" + expositor.id).attr("font-size", 14);

                expositor.estantes.forEach(function (stand) {
                    data.forEach(function(value, index) {
                        var seat = d3.select("#estante_" + value.id);
                        seat.datum(value);
                        seat.style("fill", "#A0A0A0");

                    });

                });

                expositor.estantes.forEach(function (stand) {
                    var seat = d3.select("#estante_" + stand.id);
                    seat.style("fill", stand.color);
                });
            }
/*
            if (response.status == 2) {
                var expositor = response.expositor;
                if (expositor.maps_url) {
                    window.location.href = expositor.maps_url;
                } else {
                    $("#error").fadeIn(1000, function() {
                        $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                    });
                }
            }
*/
            //if (response.status == 0) {
            else {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });
            }
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

function zoom() {
    g.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
    svg.select("#gE").attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");

    if (d3.event.scale > 3 && d3.event.scale < 4) {
        svg.selectAll("text").each(function() {
            d3.select(this).attr("font-size", 9);
        });
    }

    if (d3.event.scale > 4) {
        svg.selectAll("text").each(function() {
            d3.select(this).attr("font-size", 11);
        });
    }
}

function handleResevations(data) {
    svg = d3.select("#Layer_2");
    g = d3.select("#layer3");

    if (window.location.hash !== '') {
        var id = parseInt(window.location.hash.substring(1));
        if (id > 0) {
            if (id) {
                loadExpositorLocation(id);
            }
        }
    }

    data.forEach(function(value, index) {
        var seat = d3.select("#estante_" + value.id);
        seat.datum(value);

        if (value.locked === true) {
            seat
                .style("fill", value.color)
                .attr("stroke-width", 1)
                .style("stroke", ColorLuminance(value.color, -0.2));

            seat.on("click", function (d) {
                //g.attr("transform", "translate(" + value.coords + ")scale(" + 4 + ")");
                //svg.select("#gE").attr("transform", "translate(" + value.coords + ")scale(" + 4 + ")");
                window.location.href = 'expositor.php#' + value.expositor;
            });
        }

        seat.append("title")
            .text(function(d) {
                return d.title;
            });

    });
}

function openEdit(id) {
    window.location.href = 'expositor.php#' + id;
}

function ColorLuminance(hex, lum) {

    // validate hex string
    hex = String(hex).replace(/[^0-9a-f]/gi, '');
    if (hex.length < 6) {
        hex = hex[0]+hex[0]+hex[1]+hex[1]+hex[2]+hex[2];
    }
    lum = lum || 0;

    // convert to decimal and change luminosity
    var rgb = "#", c, i;
    for (i = 0; i < 3; i++) {
        c = parseInt(hex.substr(i*2,2), 16);
        c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
        rgb += ("00"+c).substr(c.length);
    }

    return rgb;
}

function type(d) {
    //d.x = +d.x; // coerce to number
    //d.y = +d.y;
    d.reserved = d.reserved == 'R';
    return d;
}

