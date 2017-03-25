<?php
session_start();

if ((!isset( $_SESSION["user_id"])) || (hash("sha256", $_SESSION["user_id"]) != $_SESSION["key"])) {
    header('Location: /');
    exit;
}