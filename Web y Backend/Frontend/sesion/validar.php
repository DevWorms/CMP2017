<?php
/**
 * Created by PhpStorm.
 * User: chemasmas
 * Date: 19/03/17
 * Time: 03:53 PM
 */
session_start();

if(!isset( $_SESSION["user_id"]))
{
    header('Location: /');
    exit;
}