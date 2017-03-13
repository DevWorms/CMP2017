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

/*
 * Usuarios
 */
$app->post('/api/user/signup', 'UserController@create');
$app->patch('/api/user/edit', 'UserController@update');
$app->get('/api/user/profile/{id}/{token}', 'UserController@select');
$app->delete('/api/user/delete/{id}', 'UserController@delete');
$app->post('/api/user/login', 'UserController@login');

/*
 * Programas
 */
$app->post('/api/programa/create', 'ProgramaController@create');
$app->get('/api/programa/all/{user_id}/{api_key}', 'ProgramaController@getAll');

/*
 * Categorias
 */
$app->post('/api/categoria/create', 'CategoriaController@create');
$app->get('/api/categoria/all/{user_id}/{api_key}', 'CategoriaController@getAll');