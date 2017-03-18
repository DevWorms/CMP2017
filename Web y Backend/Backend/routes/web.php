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
$app->post('/api/programa/search', ['middleware' => 'cors', 'uses' => 'ProgramaController@getFiltro']);
$app->get('/api/programa/detail/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'ProgramaController@getPrograma']);

/*
 * Categorias
 */
$app->post('/api/categoria/create', ['middleware' => 'cors', 'uses' => 'CategoriaController@create']);
$app->get('/api/categoria/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'CategoriaController@getAll']);

/*
 * Expositores
 */
$app->post('/api/expositor/create', ['middleware' => 'cors', 'uses' => 'ExpositorController@create']);
$app->get('/api/expositor/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getAll']);
$app->get('/api/expositor/order/name/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getByName']);
$app->get('/api/expositor/order/stand/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getByStand']);
$app->get('/api/expositor/detail/{user_id}/{api_key}/{expositor_id}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getExpositor']);

$app->get('/api/expositor/addfavorito/{user_id}/{api_key}/{expositor_id}', ['middleware' => 'cors', 'uses' => 'ExpositorController@addFavorito']);
$app->get('/api/expositor/misfavoritos/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getMyExpositores']);

/*
 * Eventos de Acompañantes
 */
$app->post('/api/acompanantes/create', ['middleware' => 'cors', 'uses' => 'AcompanantesController@create']);
$app->get('/api/acompanantes/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'AcompanantesController@getAll']);
$app->post('/api/acompanantes/search', ['middleware' => 'cors', 'uses' => 'AcompanantesController@getFiltro']);
$app->get('/api/acompanantes/detail/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'AcompanantesController@getPrograma']);

/*
 * Eventos Sociales y Deportivos
 */
$app->post('/api/deportivos/create', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@create']);
$app->get('/api/deportivos/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@getAll']);
$app->post('/api/deportivos/search', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@getFiltro']);
$app->get('/api/deportivos/detail/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@getPrograma']);

/*
 * Patrocinadores
 */
$app->post('/api/patrocinador/create', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@create']);
$app->get('/api/patrocinador/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@getAll']);
$app->get('/api/patrocinador/order/name/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@getByName']);
$app->get('/api/patrocinador/detail/{user_id}/{api_key}/{expositor_id}', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@getPatrocinador']);

/*
 * Archivo
 */
$app->post('/api/fileupload',['middleware' => 'cors', 'uses' => 'FileController@upload']);