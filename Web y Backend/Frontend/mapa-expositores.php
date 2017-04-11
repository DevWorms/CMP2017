<?php require dirname(__FILE__) . '/sesion/validar.php'; ?>
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

    <style type="text/css">
        .st0{fill:none;stroke:#000000;stroke-miterlimit:10;}
        .st1{fill:none;stroke:#C2C1C2;stroke-width:1.5845;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;}
        .st2{fill:none;stroke:#575754;stroke-width:1.5845;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;}
        .st3{fill:none;stroke:#000000;stroke-width:8.529500e-02;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;}
        .st4{fill:none;stroke:#9ECADE;stroke-width:8.529500e-02;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;}
        .st5{fill:none;stroke:#A1A0A5;stroke-width:8.529500e-02;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;}
        .st6{font-family:'MyriadPro-Regular';}
        .st7{font-size:23.693px;}
        .st8{clip-path:url(#SVGID_2_);fill:#27241F;}
        .st9{fill:#00366C;}
        .st10{fill:#27241F;}
        .st11{clip-path:url(#SVGID_4_);fill:#27241F;}
        .st12{clip-path:url(#SVGID_6_);fill:url(#SVGID_7_);}
        .st13{clip-path:url(#SVGID_9_);fill:url(#SVGID_10_);}
        .st14{clip-path:url(#SVGID_12_);fill:url(#SVGID_13_);}
        .st15{clip-path:url(#SVGID_15_);fill:url(#SVGID_16_);}
        .st16{clip-path:url(#SVGID_18_);fill:url(#SVGID_19_);}
        .st17{clip-path:url(#SVGID_21_);fill:url(#SVGID_22_);}
        .st18{fill:none;stroke:#ECC200;stroke-width:2.063;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;}
        .st19{fill:none;stroke:#ECC200;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;}
        .st20{fill:#344A75;}
        .st21{fill:#344A75;stroke:#000000;stroke-miterlimit:10;}
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

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.php">DASHBOARD <b style="color: #52e7ff;">CMP 2017</b></a>
            </div>

            <!-- Top Menu -->
            <ul class="nav navbar-right top-nav">
                
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-bell"></i> <b class="caret"></b></a>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i>&nbsp;&nbsp;Administrador <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="#"><i class="fa fa-fw fa-user"></i> Perfil</a>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-fw fa-gear"></i> Configuración</a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#"><i class="fa fa-fw fa-power-off"></i> Cerrar Sesión</a>
                        </li>
                    </ul>
                </li>
            </ul>
           
            <!-- Sidebar -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav side-nav">
                    <li>
                        <a href="programa.php">Programa</a>
                    </li>
                    <li>
                        <a href="expositores.php">Expositores</a>
                    </li>
                    <li>
                        <a href="acompanantes.php">Acompañantes</a>
                    </li>
                    <li>
                        <a href="sociales-deportivos.php">Sociales y Deportivos</a>
                    </li>
                    <li>
                        <a href="patrocinadores.php">Patrocinadores</a>
                    </li>
                    <li class="active">
                        <a href="mapa.php">Mapa</a>
                    </li>
                    <li>
                        <a href="transportacion.php">Transportación</a>
                    </li>
                    <li>
                        <a href="puebla.php">Puebla</a>
                    </li>
                    <li>
                        <a href="banner.php">Banners</a>
                    </li>
                    <li>
                        <a href="encuestas.php">Encuestas</a>
                    </li>
                    <li>
                        <a href="categorias.php">Categorías</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </nav>

        <!-- Contenido -->
        <div id="page-wrapper">

            <div class="container-fluid">
                
                <div class="row page-header">
                   <div class="col-xs-12 col-md-offset-1 col-md-4">
                        <h3 style="color: #10375e;">Mapa</h3>
                    </div> 
                </div>
                <!-- Fin Fila -->

                <div class="row">
                    <div class="col-xs-12 col-md-offset-1 col-md-4">
                        <h4>Mapa del recinto</h4>
                    </div>
                </div>
                <br><br>
                <!-- Fin Fila -->
    
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <img class="map" src="img/plano.png" usemap="#expositores">
                        <map id="expositores" name="expositores">
                            <?php
                                $x = 144;
                                $y = 172;
                                $espacio = 40;

                                //for ($j = 0; $j < 14; $j++) {
                                    for ($i = 0; $i < 4; $i++) {
                                        echo '<area shape="rect" alt="" title="" coords="' . $x . ',542,' . $y . ',568" href="#' . ($i + 1) . '" target="" />';
                                        $x += 29;
                                        $y += 29;
                                    }
                                    //$x += 0;
                                //}
                            ?>
                        </map>
                        <br>
                    </div>
                </div>
                <!-- Fin Fila -->
            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
    
    <!-- Scripts -->
    <script src="js/bootstrap.min.js"></script>

</body>

</html>
