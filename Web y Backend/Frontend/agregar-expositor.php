<?php require dirname(__FILE__) . '/sesion/validar.php'; ?>
<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="CMP 2017 - ADMIN">
    <meta name="author" content="DevWorms">

    <title>CMP 2017 - Agregar Expositor</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- CSS -->
    <link href="css/main.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="js/jquery-ui-1.12.1/jquery-ui.css">
    <link rel="stylesheet" href="js/jquery-ui-timepicker-addon.css">
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
                    <li class="active">
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
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </nav>

        <!-- Contenido -->
        <div id="page-wrapper">
            <div class="container-fluid">
                <div id="error"></div>
                <input type="hidden" id="id">
                <form enctype="multipart/form-data" method="POST" id="crearExpositor" name="crearExpositor">
                    <div class="row page-header">
                        <div class="col-xs-12 col-md-6">
                        </div>

                        <div class="col-xs-12 col-md-6" align="right">
                            <button type="submit" class="btn basico" id="btn_crearExpositor"><i class="fa fa-plus-circle"></i> &nbsp;Agregar Expositor</button>
                        </div>
                    </div>
                    <!-- Fin Fila -->

                    <div class="row">    
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                           <div class="form-group">
                                <label for="nombre">Nombre del Expositor</label>
                                    <br>
                                <input type="text" name="nombre" id="nombre" class="form-control" required>
                            </div> 
                        </div>
                        <div class="col-xs-12 col-md-5">
                            <div class="form-group">
                                <label for="email">Correo Electrónico</label>
                                    <br>
                                <input type="email" name="email" id="email" class="form-control" required>
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
                                <br>
                                <label for="telefono">Teléfono</label>
                                    <br>
                                <input type="number" name="telefono" id="telefono" class="form-control" required>

                               <br>
                               <label for="stand">Número de stand</label>
                               <br>
                               <input type="number" name="stand" id="stand" class="form-control" required>
                            </div> 
                        </div>
                        <div class="col-xs-12 col-md-5">
                            <div class="form-group">
                                <label for="acerca">Acerca del expositor</label>
                                    <br>
                                <textarea class="form-control" rows="5" id="acerca" name="acerca" required></textarea>
                            </div>
                        </div>                    
                    </div>
                    <br>
                    <!-- Fin Fila -->

                    <div class="row">    
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                           <div class="form-group">
                                <label for="archivo_pdf">Agregar archivo PDF</label>
                                    <br>
                                <input type="file" name="archivo_pdf" id="archivo_pdf" class="form-control">
                            </div>
                            <div id="file_pdf"></div>
                        </div>
                        <div class="col-xs-12 col-md-5">
                            <div class="form-group">
                                <label for="archivo_logo">Agregar imágen (logotipo)</label>
                                    <br>
                                <input type="file" name="archivo_logo" id="archivo_logo" class="form-control">
                            </div>
                            <div id="file_img"></div>
                        </div>                    
                    </div>
                    <br>
                    <!-- Fin Fila -->

                    <div class="row">    
                        <div class="col-xs-12 col-md-5 col-md-offset-1">
                           <div class="form-group">
                                <label for="">Agregar ubicación en el mapa</label>
                                    <br><br>
                                <button class="btn basico2">Examinar</button>
                            </div> 
                        </div>                
                    </div>
                    <br>
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
    <script type="text/javascript" src="js/jquery-ui-1.12.1/jquery-ui.js"></script>
    <script type="text/javascript" src="js/moment-with-locales.js"></script>
    <script type="text/javascript" src="js/jquery-ui-timepicker-addon.js"></script>
    <script src="js/js.php"></script>
    <script src="js/add-expositores.js"></script>
</body>

</html>
