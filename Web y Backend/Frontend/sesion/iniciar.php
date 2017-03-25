<?php
if (isset($_POST['id']) && isset($_POST['api_key'])) {
    session_start();
    $_SESSION["user_id"] = $_POST['id'];
    $_SESSION["api_key"] = $_POST['api_key'];
    $_SESSION["key"] = $_POST['key'];
    session_write_close();

    $res['estado'] = 1;
    $res["url"] = "/programa.php";
    echo json_encode($res);
} else {
    $res['estado'] = 0;
    $res["url"] = "/";
    $res['mensaje'] = "Ingresa un usuario y contraseña";

    echo json_encode($res);
}