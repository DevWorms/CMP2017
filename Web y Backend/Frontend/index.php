<!DOCTYPE html>
<html lang="">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="CMP 2017 - ADMIN">
    <meta name="author" content="DevWorms">

    <title>CMP 2017 - ¡Bienvenido!</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- CSS -->
    <link href="css/login.css" rel="stylesheet">
    <link href="css/animate.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
</head>

<body>
   <div class="container">
		<div class="title-container">
			<h2 class="animated zoomIn" style="letter-spacing: 2px;"><b>Inicio de Sesión</b></h2>
		</div>
		<div class="login-container">
            <div id="error"></div>
            <div class="form-box" align="center">
                <p>Usuario</p>
                <input name="id_usuario" id="id_usuario" type="text" style="color: black">
                <br><br>
                <p>Contraseña</p>
                <input type="password" name="contrasena" id="contrasena" style="color: black">
                <br><br>
                <button class="btn app-boton btn-block btn-lg animated pulse" id="login">Ingresar</button>
            </div>
        </div>
	</div>
    
    <!-- Scripts -->
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script src="js/login.js"></script>
</body>
</html>
