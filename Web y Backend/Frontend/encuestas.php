<?php require dirname(__FILE__) . '/sesion/validar.php'; ?>
<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="CMP 2017 - ADMIN">
    <meta name="author" content="DevWorms">

    <title>CMP 2017 - Programa</title>

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
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-bell"></i> <b
                            class="caret"></b></a>
            </li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i>&nbsp;&nbsp;Administrador
                    <b class="caret"></b></a>
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
                <li>
                    <a href="usuarios.php">Usuarios</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </nav>

    <!-- Contenido -->
    <div id="page-wrapper">
        <div class="container-fluid">
            <div id="error"></div>
            <div class="modalLoading" id="wait"></div>
            <div class="row page-header">
                <div class="col-xs-12 col-md-6">
                    <form action="">
                        <div class="form-group row">
                            <div class="col-md-10">


                            </div>
                        </div>
                    </form>
                </div>

                <div class="col-xs-12 col-md-6" align="right">
                    <a href="agregar-encuestas.php" class="btn basico" id="btn_crearEvento"><i
                                class="fa fa-plus-circle"></i> &nbsp;Agregar Encuesta</a>
                </div>
            </div>
            <!-- Fin Fila -->

            <br>
            <div class="row">
                <div class="col-md-10 col-md-offset-1">
                    <table class="table table-striped" id="tbl_eventos">
                        <thead>
                        <tr>
                            <th>Encuesta</th>
                            <th align="center">Ver</th>
                            <th align="center">Editar</th>
                            <th align="center">Eliminar</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- Fin Fila -->

            <br>
            <div class="row">
                <div class="col-xs-3" style="margin-top: 30px;">
                    <div id="page-counter"></div>
                </div>
                <div class="col-xs-6" align="center" style="margin-top: 0px;">
                    <ul class="pagination" id="pagination">
                    </ul>
                </div>
                <div class="col-xs-3" style="margin-top: 30px;">
                    <p><a href="#" onclick="showAll()">Ver todos</a></p>
                </div>
            </div>
            <!-- Fin Fila -->

        </div>
        <!-- /.container-fluid -->

    </div>
    <!-- /#page-wrapper -->

</div>
<div id="modalsExpositores"></div>

<script type="text/template" id="modal_detalle_expositor">
    <div id="DetalleExpositor-${id}" class="modal" role="dialog">
        <div class="modal-dialog modal-lg">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title" style="text-align:center;">Encuesta: ${id}</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="col-sm-12" align="center">
                                <br>
                                <ul class="nav nav-tabs">
                                    <li class="active"><a data-toggle="tab" href="#detalles-${id}"><b>Detalles</b></a></li>
                                    <li><a data-toggle="tab" href="#usuariosRespondidos-${id}"><b>Usuarios que han respondido</b></a></li>
                                    <li><a data-toggle="tab" href="#usuariosFaltantes-${id}"><b>Usuarios faltantes</b></a></li>
                                </ul>
                                <div class="tab-content">
                                    <div id="detalles-${id}" class="tab-pane fade in active">
                                        <div class="col-md-12">
                                            <br>
                                        </div>
                                        <div class="col-md-12">
                                            <a href="${filesm.url}" target="_blank"><img src="${filesm.url}" title="${filesm.nombre}" style="width: 100%"></a>
                                        </div>
                                        <div class="col-md-12">
                                            <br>
                                        </div>
                                        <div class="col-md-12">
                                            <p><strong>Usuarios que han respondido</strong>: ${respondidos} de ${totales}</p>
                                            <p><strong>Calificación general</strong>: ${media}</p>
                                        </div>
                                        <div class="col-md-12">
                                            <br>
                                        </div>
                                        <div class="col-md-12">
                                            <p><strong>Pregunta 1</strong>: ${preguntas[0].pregunta}</p>
                                            <p><strong>Calificación promedio</strong>: ${media1}</p>
                                        </div>
                                        <div class="col-md-12">
                                            <br>
                                        </div>
                                        <div class="col-md-12">
                                            <p><strong>Pregunta 2</strong>: ${preguntas[1].pregunta}</p>
                                            <p><strong>Calificación promedio</strong>: ${media2}</p>
                                        </div>
                                        <div class="col-md-12">
                                            <br>
                                        </div>
                                        <div class="col-md-12">
                                            <p><strong>Pregunta 3</strong>: ${preguntas[2].pregunta}</p>
                                            <p><strong>Calificación promedio</strong>: ${media3}</p>
                                        </div>
                                    </div>
                                    <div id="usuariosRespondidos-${id}" class="tab-pane fade">
                                        <div class="col-md-12">
                                            <br>
                                        </div>
                                        <p><strong>Usuarios que han respondido</strong>:</p>
                                        <table class="table table-striped" id="tbl_users_respondidos_${id}">
                                            <thead>
                                            <tr>
                                                <th>Usuario</th>
                                                <th>Cal. Pregunta 1</th>
                                                <th>Cal. Pregunta 2</th>
                                                <th>Cal. Pregunta 3</th>
                                                <th>Cal. General</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div id="usuariosFaltantes-${id}" class="tab-pane fade">
                                        <div class="col-md-12">
                                            <br>
                                        </div>
                                        <p><strong>Usuarios que aún no han respondido</strong>:</p>
                                        <table class="table table-striped" id="tbl_users_faltantes_${id}">
                                            <thead>
                                            <tr>
                                                <th>Usuario</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="form-group" align="right">
                        <div class="col-md-12">
                            <div class="col-md-8"></div>
                            <div class="col-md-4">
                                <!--<button class="btn btn-primary btn-block" type="submit" name="up_button" onclick="event.preventDefault();" id="up_button">Guardar</button>-->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>
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

<script>
    loadExpostitores(init_url);
</script>

</body>

</html>
