<?php
    //creo la nueva
    session_start();
    $datos = $_POST["user"];
    $res["url"] = "/";
    if($datos["status"] == 1)
    {
        $_SESSION["user_id"] = $datos["user_id"];
        $_SESSION["api_key"] = $datos["api_key"];
        $res["url"] = "/programa.php";
    }
    echo json_encode($res);

