<?php //require dirname(__FILE__) . '/sesion/validar.php'; ?>
<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="CMP 2017 - ADMIN">
    <meta name="author" content="DevWorms">

    <title>CMP 2017 - Mapa Recinto</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- CSS -->
    <link href="css/main.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- Seat & d3 -->
    <link rel="stylesheet" type="text/css" href="css/seats.css" media="screen" />
    <script src="http://d3js.org/d3.v3.min.js"  charset="utf-8"></script>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <!--<link rel="stylesheet" href="http:/resources/demos/style.css">-->
    <style>
        .custom-combobox {
            position: relative;
            display: inline-block;
        }
        .custom-combobox-toggle {
            position: absolute;
            top: 0;
            bottom: 0;
            margin-left: -1px;
            padding: 0;
        }

        .expositores {
            position:absolute;
            left: 1000px;
            top: 120px;
            display:inline-block;
            z-index:10
        }

        #expositores {
            position:absolute;
            left: 1000px;
            top: 120px;
            display:inline-block;
            z-index:10
        }

        .expos {
            position:absolute;
            left: 840px;
            top: 120px;
            display:inline-block;
            z-index:10
        }

        .expos_btn {
            position:absolute;
            left: 1138px;
            top: 160px;
            display:inline-block;
            z-index:10
        }

        .background {
            fill: none;
            pointer-events: all;
        }

        .feature {
            fill: #ccc;
            cursor: pointer;
        }

        .feature.active {
            fill: orange;
        }

        .mesh {
            fill: none;
            stroke: #fff;
            stroke-linecap: round;
            stroke-linejoin: round;
        }
    </style>

    <script src="js/jquery.js"></script>
    <script src="js/jquery.maphilight.min.js"></script>
    <script type="text/javascript">
        $(function() {
            $('.map').maphilight();
        });
    </script>
</head>

<body>
    <div id="error"></div>
    <div class="modal" id="wait"></div>
    <div class="row">
        <div class="room" id="room"></div>
    </div>

    <!-- Scripts -->
    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.12.1/jquery-ui.js"></script>
    <script type="text/javascript" src="js/moment-with-locales.js"></script>
    <script type="text/javascript" src="js/jquery-ui-timepicker-addon.js"></script>
    <script src="js/js.php"></script>
    <script src="js/lodash.js"></script>
    <script src="js/public-map.js"></script>
    <script type="text/javascript" >
        d3.xml("img/plano4.svg",  "image/svg+xml",  function(xml) {
            seating(xml);
        });
    </script>
</body>
</html>
