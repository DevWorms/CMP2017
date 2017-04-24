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

    <script src="js/jquery.js"></script>
    <style>
        strong {
            font-size: 22px;
        }
    </style>
</head>

<body>
<div id="page-wrapper">
    <div class="container-fluid">
        <div id="error">
            <div class="alert alert-danger"> &nbsp; No se encontró el expositor</div>
        </div>
        <div class="row page-header">
            <div class="col-md-2"></div>
            <div class="col-md-8">
                <div class="col-md-12">
                    <div id="logo">
                        <img src="img/logo.png" width="200px">
                    </div>
                </div>
                <div class="col-md-12">
                    <br>
                </div>
                <div class="col-md-12">
                    <div id="nombre"><p><strong>Nombre</strong>: </p></div>
                </div>
                <div class="col-md-12">
                    <hr>
                </div>
                <div class="col-md-12">
                    <div id="contacto"><p><strong>Contacto</strong>: </p></div>
                </div>
                <div class="col-md-12">
                    <div id="acerca"><p><strong>Acerca de</strong>: </p></div>
                </div>
                <div class="col-md-12">
                    <hr>
                </div>
                <div class="col-md-12">
                    <div id="presentacion"><button type="button" id="btn_presentacion" class="btn btn-primary btn-lg" style="background-color: #2b3f9c"><img src="img/ic_picture_as_pdf_white_24dp_1x.png"> &nbsp;&nbsp;&nbsp;Presentación de la empresa</button></div>
                </div>
            </div>
            <div class="col-md-2"></div>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.12.1/jquery-ui.js"></script>
<script type="text/javascript" src="js/moment-with-locales.js"></script>
<script type="text/javascript" src="js/jquery-ui-timepicker-addon.js"></script>
<script src="js/js.php"></script>
<script src="js/lodash.js"></script>
<script src="js/expositor.js"></script>
</body>
</html>
