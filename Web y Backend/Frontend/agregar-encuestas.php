<?php require dirname(__FILE__) . '/sesion/validar.php'; ?>
<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="CMP 2017 - ADMIN">
    <meta name="author" content="DevWorms">

    <title>CMP 2017 - Encuestas</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- CSS -->
    <link href="css/main.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
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
                    <li>
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
                    <li class="active">
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
                <div id="error"></div>
                <input type="hidden" id="id" value="">
                <form enctype="multipart/form-data" method="POST" id="crearEvento" name="crearEvento">
                    <div class="row page-header">
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                            <h4 style="color: #10375e;">Encuestas</h4>
                        </div>
                    </div>
                    <!-- Fin Fila -->
                    
                    <div class="row">
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                            <h4><b>Crear encuesta</b></h4>
                        </div>
                    </div>
                    <br>
                    <!-- Fin Fila -->

                    <div class="row">
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                            <div class="form-group">
                                <label for="archivo_sm">Imagen Chica</label>
                                <br>
                                <input type="file" name="archivo_sm" id="archivo_sm" class="form-control" required>
                            </div>
                            <br>
                            <div id="file_sm"></div>
                            <br>
                        </div>

                        <div class="col-xs-12 col-md-5">
                            <div class="form-group">
                                <label for="archivo_xl">Imagen Grande</label>
                                <br>
                                <input type="file" name="archivo_xl" id="archivo_xl" class="form-control" required>
                            </div>
                            <br>
                            <div id="file_xl"></div>
                            <br>
                        </div>
                    </div>
                    <br>
                    <!-- Fin Fila -->

                    <div class="row">
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                            <div class="form-group">
                                <label for="">Pregunta 1</label>
                                    <br>
                                <input type="text" name="pregunta[]" id="pregunta1" class="form-control">
                            </div>
                            <br>
                        </div>

                        <div class="col-xs-12 col-md-5">
                            <div class="form-group">
                                <label for="">Pregunta 2</label>
                                    <br>
                                <input type="text" name="pregunta[]" id="pregunta2" class="form-control">
                            </div>
                        </div>
                    </div>
                    <!-- Fin Fila -->

                    <div class="row">
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                            <div class="form-group">
                                <label for="">Pregunta 3</label>
                                    <br>
                                <input type="text" name="pregunta[]" id="pregunta3" class="form-control">
                            </div>
                            <br>
                        </div>
                    </div>
                    <!-- Fin Fila -->
                    
                    <div class="row">
                        <col-xs-12 class="col-xs-offset-2 col-xs-8 col-md-2 col-md-offset-1">
                            <button type="submit" class="btn basico btn-block" name="btn_crearEvento" id="btn_crearEvento">Crear</button>
                        </col-xs-12>
                    </div>
                </form>

            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->
    
    <!-- Scripts -->
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.12.1/jquery-ui.js"></script>
    <script type="text/javascript" src="js/moment-with-locales.js"></script>
    <script type="text/javascript" src="js/jquery-ui-timepicker-addon.js"></script>
    <script src="js/js.php"></script>
    <script src="js/lodash.js"></script>
    <script src="js/encuesta.js"></script>
</body>

</html>
