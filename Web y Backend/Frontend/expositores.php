<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="CMP 2017 - ADMIN">
    <meta name="author" content="DevWorms">

    <title>CMP 2017 - Expositores</title>

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
                <a class="navbar-brand" href="index.html">DASHBOARD CMP 2017</a>
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
                
                <div class="row page-header">
                    <div class="col-xs-12 col-md-6">
                        <form action=""> 
                          <div class="form-group row"> 
                            <div class="col-md-10"> 
                              
                              <form name="" action="" method="post" class="form-inline" role="form">
                                <div class="input-group">
                                  <div class="input-group-btn">
                                      <button class="btn btn-primary" type="button"><i class="glyphicon glyphicon-search"></i></button>
                                  </div>
                                  <input type="text" class="form-control" placeholder="Buscar ..." name="" id="">
                                </div>
                              </form>
                              
                            </div> 
                          </div>  
                        </form> 
                    </div>

                    <div class="col-xs-12 col-md-6" align="right">
                        <a href="agregar-expositor.php" class="btn btn-default"><i class="fa fa-plus-circle"></i> Agregar Expositor</a>   
                    </div>
                </div>
                <!-- Fin Fila -->

                <br>
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <table class="table table-striped">
                            <thead>
                              <tr>
                                <th>Nombre</th>
                                <th></th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr>
                                <td>British Pretoleoum</td>
                                <td align="right"> <a href="" class="btn btn-primary">Ver</a> </td>
                              </tr>
                              <tr>
                                <td>Pemex</td>
                                <td align="right"> <a href="" class="btn btn-primary">Ver</a> </td>
                              </tr>
                              <tr>
                                <td>Shell</td>
                                <td align="right"> <a href="" class="btn btn-primary">Ver</a> </td>
                              </tr>
                              <tr>
                                <td>Industria Mexicana de...</td>
                                <td align="right"> <a href="" class="btn btn-primary">Ver</a> </td>
                              </tr>
                              <tr>
                                <td>BCM Nacional</td>
                                <td align="right"> <a href="" class="btn btn-primary">Ver</a> </td>
                              </tr>
                              <tr>
                                <td>Petrobras</td>
                                <td align="right"> <a href="" class="btn btn-primary">Ver</a> </td>
                              </tr>
                            </tbody>
                        </table>
                    </div>    
                </div>
                <!-- Fin Fila -->
                
                <br>
                <div class="row">
                    <div class="col-xs-6">
                        <p>Pag. 1</p>
                    </div>
                    <div class="col-xs-6" align="right">
                        <p>Ver todos</p>
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
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>

</body>

</html>
