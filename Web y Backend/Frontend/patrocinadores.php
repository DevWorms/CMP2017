<?php require dirname(__FILE__) . '/sesion/validar.php'; ?>
<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="CMP 2017 - ADMIN">
    <meta name="author" content="DevWorms">

    <title>CMP 2017 - Patrocinadores</title>

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
                <li class="active">
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
                <li>
                    <a href="encuestas.php">Encuestas</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </nav>

    <!-- Contenido -->
    <div id="page-wrapper">
        <div class="container-fluid">
            <div id="error"></div>
            <div class="row page-header">
                <div class="col-xs-12 col-md-6">
                    <form action="">
                        <div class="form-group row">
                            <div class="col-md-10">

                                <form name="" action="" method="post" class="form-inline" role="form">
                                    <div class="input-group">
                                        <input type="text" class="form-control" placeholder="Buscar ..." name="q"
                                               id="q">
                                        <div class="input-group-btn">
                                            <button class="btn btn-default" type="button" name="search" id="search"><i
                                                        class="glyphicon glyphicon-search"></i></button>
                                        </div>
                                    </div>
                                </form>

                            </div>
                        </div>
                    </form>
                </div>

                <div class="col-xs-12 col-md-6" align="right">
                    <a href="agregar-patrocinador.php" class="btn basico"><i class="fa fa-plus-circle"></i> &nbsp;Agregar
                        Patrocinador</a>
                </div>
            </div>
            <!-- Fin Fila -->

            <br>
            <div class="row">
                <div class="col-md-10 col-md-offset-1">
                    <table class="table table-striped" id="tbl_patrocinadores">
                        <thead>
                        <tr>
                            <th>Nombre del Patrocinador</th>
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
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title" style="text-align:center;">Patrocinador: ${nombre}</h4>
                </div>
                <div class="modal-body">
                    <div class="col-md-2"></div>
                    <div class="col-md-8">
                        <div class="col-md-12">
                            <% if (logo.nombre) { %> <img src="${logo.url}" width="200px" height="200px"> <% } %>
                        </div>
                        <div class="col-md-12">
                            <br>
                        </div>
                        <div class="col-md-12">
                            <p><strong>Nombre</strong>: ${nombre}</p>
                        </div>
                        <div class="col-md-12">
                            <p><strong>Correo Electrónico</strong>: <a href="mailto:${email}">${email}</a></p>
                        </div>
                        <div class="col-md-12">
                            <p><strong>URL</strong>: <a target="_blank" href="${url}">${url}</a></p>
                        </div>
                        <div class="col-md-12">
                            <p><strong>Teléfono</strong>: ${telefono}</p>
                        </div>
                        <div class="col-md-12">
                            <p><strong>Tipo de patrocinador</strong>: ${tipo}</p>
                        </div>
                        <div class="col-md-12">
                            <p><strong>Número de stand</strong>: ${stand}</p>
                        </div>
                        <div class="col-md-12">
                            <p><strong>Acerca del expositor</strong>: ${acerca}</p>
                        </div>
                        <div class="col-md-12">
                            <p><strong>PDF</strong>: <% if (pdf.nombre) { %> <a href="${pdf.url}" target="_blank">${pdf.nombre}</a>
                                <% } %> </p>
                        </div>
                    </div>
                    <div class="col-md-2"></div>
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
<script src="js/patrocinadores.js"></script>
</body>

</html>
