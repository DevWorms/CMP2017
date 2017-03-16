<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="CMP 2017 - ADMIN">
    <meta name="author" content="DevWorms">

    <title>CMP 2017 - Agregar Evento</title>

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
                <a class="navbar-brand" href="index.php">DASHBOARD CMP 2017</a>
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
                    <li class="active">
                        <a href="programa.php">Programa</a>
                    </li>
                    <li>
                        <a href="expositores.php">Expositores</a>
                    </li>
                    <li>
                        <a href="acompañantes.php">Acompañantes</a>
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
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </nav>

        <!-- Contenido -->
        <div id="page-wrapper">

            <div class="container-fluid">
                
                <form action="" method="" id="" name="" role="">    
                    
                    <div class="row page-header">
                        <div class="col-xs-12 col-md-6">
                        </div>

                        <div class="col-xs-12 col-md-6" align="right">
                            <button type="submit" class="btn btn-default" name="" id=""><i class="fa fa-plus-circle"></i> Agregar Evento</button>   
                        </div>
                    </div>
                    <!-- Fin Fila -->

                    <div class="row">
                        <div class="col-md-6">
                            
                            <div class="form-group">
                                <label for="">Nombre del Evento</label>
                                    <br>
                                <input type="" name="" id="" class="form-control">
                            </div>
                            <br>

                            <div class="form-group">
                              <label for="sel1">Tipo de Evento</label>
                              <select class="form-control" id="sel1" placehodler="Selecciona">
                                <option disabled selected hidden>Selecciona</option>
                                <option>Sesiones Técnicas</option>
                                <option>Plenarias</option>
                                <option>Comidas Conferencias</option>
                                <option>E-Póster</option>
                                <option>Cursos Pre-Congreso</option>
                              </select>
                              <br>
                              <a href="categoria-evento.php" class="btn btn-default">Agregar categoría de evento</a>
                            </div>
                            <br>

                            <div class="form-group">
                                <label for="">Lugar</label>
                                    <br>
                                <input type="" name="" id="" class="form-control">
                            </div> 
                            <br>

                            <div class="form-group">
                                <label for="">Recomendaciones</label>
                                    <br>
                                <textarea class="form-control" rows="5" id=""></textarea>
                            </div>
                            <br>

                            <div class="form-group">
                                <label for="">Agregar ubicación en el mapa</label>
                                    <br><br>
                                <button class="btn btn-default">Examinar</button>
                            </div> 
                            <br>
                            
                        </div>
                        <div class="col-md-6"></div>
                    </div>
                    <!-- Fin Fila -->
                
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

</body>

</html>
