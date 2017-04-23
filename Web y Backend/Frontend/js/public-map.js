/**
 * Created by rk521 on 18.03.17.
 */
var stands = null;
var data = [];

function seating(xml) {
    var svg = d3.select(xml);
    var seats = svg.select("#layer3");
    var i = 1;

    $.ajax({
        url: API_URL + 'mapa/expositores/public',
        type: 'GET',
        success: function (response) {
            stands = response.stands;
            seats.selectAll("rect").each(function() {
                var r = d3.select(this);

                if (r.attr('height') == 27.9 || ((r.attr('height') == 28.2) && (r.attr('width') == 27.9) ) ) {
                    var stand = stands[i-1];
                    var locked = false;

                    if (stand.available == 0) {
                        locked = true;
                    }

                    var color = null;
                    if (stand.color) {
                        color = stands[i-1].color;
                    }

                    r.attr("id", 'estante_' + i);

                    r.append("text").text("sample!!!").attr("transform", "matrix(144,542,172,568)");

                    data.push({
                        id: r.attr("id"),
                        title: "Estante " + i,
                        locked: locked,
                        color: color,
                        coords: stand.coords
                    });

                    i = i+1;
                }
            });

            var room = d3.select(".room");
            room.node().appendChild(xml.documentElement);

            d3.select("#Layer_2").call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom));

            handleResevations(data);
        },
        error : function (response) {
            response = response.responseJSON;
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
        }
    });

    if (window.location.hash !== '') {
        var id = parseInt(window.location.hash.substring(1));
        if (id > 0) {
            if (id) {
                loadExpositorLocation(id);
            }
        }
    }
}

function loadExpositorLocation(id) {
    $.ajax({
        url: API_URL + 'mapa/expositores/public/expositor/' + id,
        type: 'GET',
        success: function (response) {
            var expositor = response.expositor;
            g.attr("transform", "translate(" + expositor.coords + ")scale(" + 4 + ")");

            //TODO Mostrar info del expositor
        },
        error : function (response) {
            response = response.responseJSON;
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
        }
    });
}

function zoom() {
    g.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
}

function handleResevations(data) {
    svg = d3.select("#Layer_2");
    g = d3.select("#layer3");

    data.forEach(function(value, index) {
        var seat = d3.select("#" + value.id);
        seat.datum(value);

        if (value.locked === true) {
            seat.style("fill", value.color);
        }

        seat.on("click", function (d) {
            g.attr("transform", "translate(" + value.coords + ")scale(" + 4 + ")");
        });

        seat.append("title")
            .text(function(d) {
                return d.title;
            });

    });
}

function type(d) {
    //d.x = +d.x; // coerce to number
    //d.y = +d.y;
    d.reserved = d.reserved == 'R';
    return d;
}

