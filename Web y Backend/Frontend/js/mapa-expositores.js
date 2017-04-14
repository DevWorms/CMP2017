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
});

function seating(xml) {
    var svg = d3.select(xml);
    var seats = svg.select("#layer3");
    var data = [];
    seats.selectAll("rect").each(function() {
        var r = d3.select(this);
        data.push({
            id: r.attr("id"),
            title: r.attr("id"),
            reserved: false
        });
    });

    var room = d3.select(".room");
    room.node().appendChild(xml.documentElement);

    handleResevations(data);

}

function handleResevations(data) {

    data.forEach(function(value, index) {
        var seat = d3.select("#" + value.id);
        seat.datum(value);

        seat.on("click", function(d) {
            console.log("rect");
            reserve(seat, d, data);
        });

        seat.append("title")
            .text(function(d) {
                return d.title;
            });

    });
}

function saveResevations(data) {
    var res = d3.select(".resevations")
        .selectAll("div")
        .data(data);

    res.enter().append("div");

    res.text(function(d) {
        return d.title + " (" + d.id + ") : " + d.reserved;
    });
}


function reserve(o, data, allData) {
    data.reserved = !data.reserved;
    o.style("fill", seatColor(data, o));
    saveResevations(allData);
}

function seatColor(data, node) {
    if (!data.origColor) {
        data.origColor = node.style("fill");
    }
    node.style("fill");
    return data.reserved ? "red" : data.origColor;
}

function type(d) {
    //d.x = +d.x; // coerce to number
    //d.y = +d.y;
    d.reserved = d.reserved == 'R';
    return d;
}