<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="CMP 2017 - ADMIN">
    <meta name="author" content="DevWorms">

    <title>CMP 2017 - Puebla, Sitios de Interes</title>

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
                    <li class="active">
                        <a href="puebla.php">Puebla</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </nav>

        <!-- Contenido -->
        <div id="page-wrapper">
            <div class="container-fluid">
                <div id="error"></div>
                <form enctype="multipart/form-data" method="POST" id="crearEvento" name="crearEvento">
                    <div class="row page-header">
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                            <h4 style="color: #10375e;">Puebla</h4>
                        </div>

                        <div class="col-xs-12 col-md-5" align="right">
                            <h4>Sitios de Interés</h4>   
                        </div>
                    </div>
                    <!-- Fin Fila -->
                    <!-- Fin Fila -->

                    <div class="row">
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                            <div class="form-group">
                                <label for="titulo">Titulo</label>
                                    <br>
                                <input type="text" name="titulo" id="titulo" class="form-control" required>
                            </div>
                            <br>
                        </div>

                        <div class="col-xs-12 col-md-5">
                            <div class="form-group">
                                <label for="maps_link">Google Maps Link</label>
                                    <br>
                                <input type="text" name="maps_link" id="maps_link" class="form-control" required>
                            </div>
                        </div>
                    </div>
                    <br>
                    <!-- Fin Fila -->

                    <div class="row">
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                            <div class="form-group">
                                <label for="descripcion">Descripción (300 caractéres)</label>
                                    <br>
                                <textarea class="form-control" rows="5" id="descripcion" name="descripcion" required></textarea>
                            </div> 
                        </div>

                        <div class="col-xs-12 col-md-6">
                            <div class="form-group">
                                <label for="imagen">Imágen (fotográfia)</label>
                                    <br><br>
                                <input type="file" id="imagen" name="imagen">
                            </div> 
                        </div>
                    </div>
                    <br>
                    <!-- Fin Fila -->

                    <div class="row">
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                            <div class="form-group">
                                <label for="url">URL</label>
                                    <br>
                                <input type="text" name="url" id="url" class="form-control" required>
                            </div>
                            <br>
                        </div>
                    </div>
                    <!-- Fin Fila -->
                    
                    <div class="row">
                        <col-xs-12 class="col-xs-offset-2 col-xs-8 col-md-2 col-md-offset-1">
                            <button type="submit" class="btn basico btn-lg btn-block" name="save" id="save">Guardar</button>
                        </col-xs-12>
                    </div>
                </form>

                <br><hr>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->
    
    <!-- Scripts -->
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>

    <script src="js/js.php"></script>
    <script src="js/lodash.js"></script>
    <script src="js/add-sitio.js"></script>
</body>

</html>
