<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It is a breeze. Simply tell Lumen the URIs it should respond to
| and give it the Closure to call when that URI is requested.
|
*/

$app->get('/', function () use ($app) {
    return "CMP WebService";
});

$app->get('/api/docs', function () use ($app) {
    return view('swagger');
});

$app->post('/api/user/signup', 'UserController@create');
$app->patch('/api/user/edit', 'UserController@update');
$app->get('/api/user/profile/{id}/{token}', 'UserController@select');
$app->delete('/api/user/delete/{id}', 'UserController@delete');
$app->post('/api/user/login', 'UserController@login');