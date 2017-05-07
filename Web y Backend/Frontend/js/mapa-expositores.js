/**
 * Created by rk521 on 18.03.17.
 */
var stands = null;
var data = [];

$("document").ready(function () {
    loadExpositores();

    $.widget( "custom.combobox", {
        _create: function() {
            this.wrapper = $( "<span>" )
                .addClass( "custom-combobox expositores" )
                .insertAfter( this.element );

            this.element.hide();
            this._createAutocomplete();
            this._createShowAllButton();
        },

        _createAutocomplete: function() {
            var selected = this.element.children( ":selected" ),
                value = selected.val() ? selected.text() : "";

            this.input = $( "<input>" )
                .appendTo( this.wrapper )
                .val( value )
                .attr( "title", "" )
                .addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
                .autocomplete({
                    delay: 0,
                    minLength: 0,
                    source: $.proxy( this, "_source" )
                })
                .tooltip({
                    classes: {
                        "ui-tooltip": "ui-state-highlight"
                    }
                });

            this._on( this.input, {
                autocompleteselect: function( event, ui ) {
                    ui.item.option.selected = true;
                    this._trigger( "select", event, {
                        item: ui.item.option
                    });
                },

                autocompletechange: "_removeIfInvalid"
            });
        },

        _createShowAllButton: function() {
            var input = this.input,
                wasOpen = false;

            $( "<a>" )
                .attr( "tabIndex", -1 )
                .attr( "title", "Mostrar todos los expositores" )
                .tooltip()
                .appendTo( this.wrapper )
                .button({
                    icons: {
                        primary: "ui-icon-triangle-1-s"
                    },
                    text: false
                })
                .removeClass( "ui-corner-all" )
                .addClass( "custom-combobox-toggle ui-corner-right" )
                .on( "mousedown", function() {
                    wasOpen = input.autocomplete( "widget" ).is( ":visible" );
                })
                .on( "click", function() {
                    input.trigger( "focus" );

                    // Close if already visible
                    if ( wasOpen ) {
                        return;
                    }

                    // Pass empty string as value to search for, displaying all results
                    input.autocomplete( "search", "" );
                });
        },

        _source: function( request, response ) {
            var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
            response( this.element.children( "option" ).map(function() {
                var text = $( this ).text();
                if ( this.value && ( !request.term || matcher.test(text) ) )
                    return {
                        label: text,
                        value: text,
                        option: this
                    };
            }) );
        },

        _removeIfInvalid: function( event, ui ) {

            // Selected an item, nothing to do
            if ( ui.item ) {
                return;
            }

            // Search for a match (case-insensitive)
            var value = this.input.val(),
                valueLowerCase = value.toLowerCase(),
                valid = false;
            this.element.children( "option" ).each(function() {
                if ( $( this ).text().toLowerCase() === valueLowerCase ) {
                    this.selected = valid = true;
                    return false;
                }
            });

            // Found a match, nothing to do
            if ( valid ) {
                return;
            }

            // Remove invalid value
            this.input
                .val( "" )
                .attr( "title", value + " no es una opción valida" )
                .tooltip( "open" );
            this.element.val( "" );
            this._delay(function() {
                this.input.tooltip( "close" ).attr( "title", "" );
            }, 2500 );
            this.input.autocomplete( "instance" ).term = "";
        },

        _destroy: function() {
            this.wrapper.remove();
            this.element.show();
        }
    });

    $( "#expositores" ).combobox();

    $("#asignar").click(function () {
        event.preventDefault();

        //var rgb = hexToRgb("#" + $("#color").val());
        //color = "#" + $("#color").val();
        //var r = $('rect[style*="fill: rgb(' + rgb.r + ', ' + rgb.g + ', ' + rgb.b + ');"]');
        var expositor = $( "#expositores" ).val();

        var reservados = data.filter(function(x) { return x.reserved == true; });
        if ((reservados.length > 0) && expositor) {
            var expositores = "";
            reservados.forEach(function (r) {
                expositores = expositores + r.id + ",";
            });
            expositores = expositores.substring(0, expositores.length - 1);
            saveEstantes(expositores, expositor);
        } else {
            if (reservados.length <= 0) {
                alert("Selecciona al menos un estante");
            } else {
                alert("Selecciona un expositor");
            }
        }
    });

    $("#color").on("change paste keyup", function() {
        var backColor = color;
        var rgb = hexToRgb(backColor);
        color = "#" + $("#color").val();

        var r = $('rect[style*="fill: rgb(' + rgb.r + ', ' + rgb.g + ', ' + rgb.b + ');"]');
        if (r.length > 0) {
            r.each(function (i) {
                var seat = d3.select("#" + r.attr("id"));
                seat.style("fill", color);
            });
        }
    });
});

function hexToRgb(hex) {
    // Expand shorthand form (e.g. "03F") to full form (e.g. "0033FF")
    var shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
    hex = hex.replace(shorthandRegex, function(m, r, g, b) {
        return r + r + g + g + b + b;
    });

    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}


function saveEstantes(estantes, id) {
    $.ajax({
        type : 'POST',
        url  : API_URL + 'mapa/expositores/reservar',
        data: {
            user_id: user_id,
            api_key: api_key,
            estantes: estantes,
            expositor_id: id,
            color: "#" + $("#color").val()
        },
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status === 1) {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-success"> &nbsp; ' + response.mensaje + '</div>');
                });

                $(".room").html("");
                d3.xml("img/plano4.svg",  "image/svg+xml",  function(xml) {
                    seating(xml);
                });
            } else {
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

function loadExpositores() {
    $.ajax({
        type : 'GET',
        url  : API_URL + 'expositor/all/' + user_id + '/' + api_key,
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status === 1) {
                response.expositores.forEach(function (expositor) {
                    $('#expositores').append($('<option>', {
                        value: expositor.id,
                        text: expositor.nombre
                    }));
                });
            } else {
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

function seating(xml) {
    var svg = d3.select(xml);
    var seats = svg.select("#layer3");
    var i = 1;

    $.ajax({
        url: API_URL + 'mapa/expositores/' + user_id + '/' + api_key,
        type: 'GET',
        beforeSend: function () {
            $("#wait").show();
        },
        success: function (response) {
            stands = response.stands;
            seats.selectAll("rect").each(function() {
                var r = d3.select(this);

                if (r.attr('height') == 27.9 || ((r.attr('height') == 28.2) && (r.attr('width') == 27.9) ) ) {
                    var locked = false;

                    if (stands[i-1].available == 0) {
                        locked = true;
                    }

                    r.attr("id", 'estante_' + i);
                    data.push({
                        id: r.attr("id"),
                        title: r.attr("id"),
                        reserved: false,
                        locked: locked,
                        color: stands[i-1].color
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
        },
        complete: function () {
            $("#wait").hide();
        }
    });
}

function handleResevations(data) {

    data.forEach(function(value, index) {
        var seat = d3.select("#" + value.id);
        seat.datum(value);

        if (value.locked === false) {
            seat.on("click", function (d) {
                reserve(seat, d, data);
            });
        } else {
            seat.style("fill", value.color);

            seat.on("click", function (d) {
                deleteReservation(value.id);
            });
        }
        seat.append("title")
            .text(function(d) {
                return d.title;
            });

    });
}

function reserve(o, data, allData) {
    data.reserved = !data.reserved;
    o.style("fill", seatColor(data, o));
}

function deleteReservation(estantes) {
    $.ajax({
        type : 'POST',
        url  : API_URL + 'mapa/expositores/remove/reservation',
        data: {
            user_id: user_id,
            api_key: api_key,
            estantes: estantes
        },
        beforeSend: function () {
            $("#wait").show();
        },
        success :  function(response) {
            if (response.status == 1) {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-success"> &nbsp; ' + response.mensaje + '</div>');
                });

                location.reload();
            } else {
                $("#error").fadeIn(1000, function() {
                    $("#error").html('<div class="alert alert-danger"> &nbsp; ' + response.mensaje + '</div>');
                });
            }
        },
        error: function(response) {
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

function seatColor(data, node) {
    if (!data.origColor) {
        data.origColor = node.style("fill");
    }
    node.style("fill");
    console.log(data.id);
    $("#" + data.id).addClass("reserved");
    return data.reserved ? color : data.origColor;
}

function type(d) {
    //d.x = +d.x; // coerce to number
    //d.y = +d.y;
    d.reserved = d.reserved == 'R';
    return d;
}