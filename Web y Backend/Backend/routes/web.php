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

/*
 * Usuarios
 */
$app->post('/api/user/signup', ['middleware' => 'cors', 'uses' => 'UserController@create']);
$app->patch('/api/user/edit', ['middleware' => 'cors', 'uses' => 'UserController@update']);
$app->get('/api/user/profile/{id}/{token}', ['middleware' => 'cors', 'uses' => 'UserController@select']);
$app->delete('/api/user/delete/{id}', ['middleware' => 'cors', 'uses' => 'UserController@delete']);
$app->post('/api/user/login', ['middleware' => 'cors', 'uses' => 'UserController@login']);

/*
 * Programas
 */
$app->post('/api/programa/create', ['middleware' => 'cors', 'uses' => 'ProgramaController@create']);
$app->get('/api/programa/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ProgramaController@getAll']);

/*
 * Categorias
 */
$app->post('/api/categoria/create', ['middleware' => 'cors', 'uses' => 'CategoriaController@create']);
$app->get('/api/categoria/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'CategoriaController@getAll']);