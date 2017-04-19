/**
 * Created by rk521 on 18.03.17.
 */
var stands = null;
var data = [];

var width = 960,
    height = 500,
    active = d3.select(null);

var projection = d3.geo.albersUsa()
    .scale(1000)
    .translate([width / 2, height / 2]);

var path = d3.geo.path()
    .projection(projection);

var svg = null;
var g = null;

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

                if (r.attr('height') == 27.9) {
                    var locked = false;

                    if (stands[i-1].available === 0) {
                        locked = true;
                    }

                    r.attr("id", 'estante_' + i);
                    data.push({
                        id: r.attr("id"),
                        title: r.attr("id"),
                        reserved: false,
                        locked: locked
                    });

                    i = i+1;
                }
            });

            var room = d3.select(".room");
            room.node().appendChild(xml.documentElement);

            handleResevations(data);
        },
        error : function (response) {
            response = response.responseJSON;
            $("#error").fadeIn(1000, function() {
                $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
            });
        }
    });
}

function handleResevations(data) {
    svg = d3.select("#Layer_2");
    g = d3.select("#layer3");

    data.forEach(function(value, index) {
        var seat = d3.select("#" + value.id);
        seat.datum(value);

        if (value.locked === false) {
            /*seat.on("click", function (d) {
                console.log(svg);
            });
            */
            seat.on("click", clicked);

        } else {
            seat.style("fill", "#DF0101");
        }
        seat.append("title")
            .text(function(d) {
                return d.title;
            });

    });
}


function clicked(d) {
    if (active.node() === this) return reset();
    active.classed("active", false);
    active = d3.select(this).classed("active", true);

    var bounds = path.bounds(d),
        dx = bounds[1][0] - bounds[0][0],
        dy = bounds[1][1] - bounds[0][1],
        x = (bounds[0][0] + bounds[1][0]) / 2,
        y = (bounds[0][1] + bounds[1][1]) / 2,
        scale = .9 / Math.max(dx / width, dy / height),
        translate = [width / 2 - scale * x, height / 2 - scale * y];

    console.log("Valor: " + bounds);
    g.transition()
        .duration(750)
        .style("stroke-width", 1.5 / 2 + "px")
        .attr("transform", "translate(" + 2 + ")scale(" + 2 + ")");
}

function reset() {
    active.classed("active", false);
    active = d3.select(null);

    g.transition()
        .duration(750)
        .style("stroke-width", "1.5px")
        .attr("transform", "");
}

function seatColor(data, node) {
    if (!data.origColor) {
        data.origColor = node.style("fill");
    }
    node.style("fill");
    return data.reserved ? "#086A87" : data.origColor;
}

function type(d) {
    //d.x = +d.x; // coerce to number
    //d.y = +d.y;
    d.reserved = d.reserved == 'R';
    return d;
}
