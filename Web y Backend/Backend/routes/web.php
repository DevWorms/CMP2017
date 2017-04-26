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
$app->post('/api/user/edit', ['middleware' => 'cors', 'uses' => 'UserController@update']);
$app->get('/api/user/profile/{id}/{token}', ['middleware' => 'cors', 'uses' => 'UserController@select']);
$app->delete('/api/user/delete/{id}', ['middleware' => 'cors', 'uses' => 'UserController@delete']);
$app->post('/api/user/login', ['middleware' => 'cors', 'uses' => 'UserController@login']);
$app->post('/api/user/login/root', ['middleware' => 'cors', 'uses' => 'UserController@loginRoot']);
$app->get('/api/user/types', ['middleware' => 'cors', 'uses' => 'UserController@getTypes']);
$app->get('/api/user/associations', ['middleware' => 'cors', 'uses' => 'UserController@getAsociaciones']);
$app->post('/api/user/resetpassword', ['middleware' => 'cors', 'uses' => 'UserController@resetPassword']);

/*
 * Programas
 */
$app->post('/api/programa/create', ['middleware' => 'cors', 'uses' => 'ProgramaController@create']);
$app->get('/api/programa/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ProgramaController@getAll']);
$app->post('/api/programa/search', ['middleware' => 'cors', 'uses' => 'ProgramaController@getFiltro']);
$app->get('/api/programa/detail/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'ProgramaController@getPrograma']);
$app->post('/api/programa/update', ['middleware' => 'cors', 'uses' => 'ProgramaController@update']);
$app->get('/api/programa/delete/{user_id}/{api_key}/{banner_id}', ['middleware' => 'cors', 'uses' => 'ProgramaController@delete']);

// Solo web (?)
$app->get('/api/programa/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ProgramaController@paginate']);

/*
 * Categorias
 */
$app->post('/api/categoria/create', ['middleware' => 'cors', 'uses' => 'CategoriaController@create']);
$app->get('/api/categoria/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'CategoriaController@getAll']);
$app->get('/api/categoria/complete/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'CategoriaController@getAllComplete']);
$app->post('/api/categoria/update', ['middleware' => 'cors', 'uses' => 'CategoriaController@update']);
$app->get('/api/categoria/delete/{user_id}/{api_key}/{banner_id}', ['middleware' => 'cors', 'uses' => 'CategoriaController@delete']);
$app->get('/api/categoria/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'CategoriaController@paginate']);
$app->get('/api/categoria/detail/{user_id}/{api_key}/{categoria_id}', ['middleware' => 'cors', 'uses' => 'CategoriaController@get']);

/*
 * Expositores
 */
$app->post('/api/expositor/create', ['middleware' => 'cors', 'uses' => 'ExpositorController@create']);
$app->get('/api/expositor/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getAll']);
$app->get('/api/expositor/order/name/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getByName']);
$app->get('/api/expositor/order/stand/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getByStand']);
$app->get('/api/expositor/detail/{user_id}/{api_key}/{expositor_id}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getExpositor']);
$app->post('/api/expositor/update', ['middleware' => 'cors', 'uses' => 'ExpositorController@update']);
$app->get('/api/expositor/delete/{user_id}/{api_key}/{banner_id}', ['middleware' => 'cors', 'uses' => 'ExpositorController@delete']);

// Solo web (?)
$app->get('/api/expositor/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ExpositorController@paginate']);
$app->post('/api/expositor/search', ['middleware' => 'cors', 'uses' => 'ExpositorController@search']);

$app->get('/api/expositor/addfavorito/{user_id}/{api_key}/{expositor_id}', ['middleware' => 'cors', 'uses' => 'ExpositorController@addFavorito']);
$app->get('/api/expositor/removefavorito/{user_id}/{api_key}/{expositor_id}', ['middleware' => 'cors', 'uses' => 'ExpositorController@removeFavorito']);
$app->get('/api/expositor/misfavoritos/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ExpositorController@getMyExpositores']);

/*
 * Eventos de Acompañantes
 */
$app->post('/api/acompanantes/create', ['middleware' => 'cors', 'uses' => 'AcompanantesController@create']);
$app->get('/api/acompanantes/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'AcompanantesController@getAll']);
$app->post('/api/acompanantes/search', ['middleware' => 'cors', 'uses' => 'AcompanantesController@getFiltro']);
$app->get('/api/acompanantes/detail/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'AcompanantesController@getPrograma']);
$app->post('/api/acompanantes/update', ['middleware' => 'cors', 'uses' => 'AcompanantesController@update']);
$app->get('/api/acompanantes/delete/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'AcompanantesController@delete']);

// Solo web (?)
$app->get('/api/acompanantes/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'AcompanantesController@paginate']);

/*
 * Eventos Sociales y Deportivos
 */
$app->post('/api/deportivos/create', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@create']);
$app->get('/api/deportivos/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@getAll']);
$app->post('/api/deportivos/search', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@getFiltro']);
$app->get('/api/deportivos/detail/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@getPrograma']);
$app->post('/api/deportivos/update', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@update']);
$app->get('/api/deportivos/delete/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@delete']);

// Solo web (?)
$app->get('/api/deportivos/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'SocioDeportivosController@paginate']);

/*
 * Patrocinadores
 */
$app->post('/api/patrocinador/create', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@create']);
$app->get('/api/patrocinador/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@getAll']);
$app->get('/api/patrocinador/order/name/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@getByName']);
$app->get('/api/patrocinador/order/stand/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@getByStand']);
$app->get('/api/patrocinador/detail/{user_id}/{api_key}/{expositor_id}', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@getPatrocinador']);
$app->post('/api/patrocinador/update', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@update']);
$app->get('/api/patrocinador/delete/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@delete']);

// Solo web (?)
$app->get('/api/patrocinador/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@paginate']);
$app->post('/api/patrocinador/search', ['middleware' => 'cors', 'uses' => 'PatrocinadorController@search']);

/*
 * Notificaciones push
 */
$app->post('/api/notificacion/save', ['middleware' => 'cors', 'uses' => 'NotificationController@save']);
$app->get('/api/notificacion/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'NotificationController@getAll']);
$app->get('/api/notificacion/unread/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'NotificationController@unread']);
$app->get('/api/notificacion/markasread/{user_id}/{api_key}/{notificacion_id}', ['middleware' => 'cors', 'uses' => 'NotificationController@markAsRead']);

/*
 * Mis eventos
 */
$app->get('/api/eventos/addfavorito/{user_id}/{api_key}/{evento_id}', ['middleware' => 'cors', 'uses' => 'MisEventosController@addFavorito']);
$app->get('/api/eventos/removefavorito/{user_id}/{api_key}/{evento_id}', ['middleware' => 'cors', 'uses' => 'MisEventosController@removeFavorito']);
$app->get('/api/eventos/miagenda/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'MisEventosController@getMyAgenda']);

/*
 * Actualizaciones
 */
$app->get('/api/updates/check/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'UpdatesController@checkUpdates']);

/*
 * Rutas
 */
$app->post('/api/ruta/create', ['middleware' => 'cors', 'uses' => 'RutasController@create']);
$app->get('/api/ruta/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'RutasController@getAll']);
$app->get('/api/ruta/detail/{user_id}/{api_key}/{ruta_id}', ['middleware' => 'cors', 'uses' => 'RutasController@getRuta']);
$app->post('/api/ruta/update', ['middleware' => 'cors', 'uses' => 'RutasController@update']);
$app->get('/api/ruta/delete/{user_id}/{api_key}/{programa_id}', ['middleware' => 'cors', 'uses' => 'RutasController@delete']);
$app->get('/api/ruta/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'RutasController@paginate']);

/*
 * Conoce puebla...
 */
$app->post('/api/puebla/sitio/create', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@createSitio']);
$app->post('/api/puebla/telefono/create', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@createTelefono']);
$app->post('/api/puebla/mapa/upload', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@uploadMapa']);
$app->get('/api/puebla/sitio/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@getSitios']);
$app->get('/api/puebla/telefono/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@getTelefonos']);
$app->get('/api/puebla/mapa/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@getMapa']);
$app->post('/api/puebla/sitio/update', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@updateSitio']);
$app->get('/api/puebla/sitio/delete/{user_id}/{api_key}/{banner_id}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@deleteSitio']);
$app->get('/api/puebla/sitio/detail/{user_id}/{api_key}/{banner_id}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@detailSitio']);
$app->get('/api/puebla/telefono/detail/{user_id}/{api_key}/{banner_id}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@detailTelefono']);
$app->post('/api/puebla/telefono/update', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@updateTelefono']);
$app->get('/api/puebla/telefono/delete/{user_id}/{api_key}/{banner_id}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@deleteTelefono']);
$app->get('/api/puebla/mapa/delete/{user_id}/{api_key}/{banner_id}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@deleteMapa']);

$app->get('/api/puebla/telefono/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@paginateTelefonos']);
$app->get('/api/puebla/sitio/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'ConocePueblaController@paginateSitios']);

/*
 * Banners
 */
$app->get('/api/banners/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'BannerController@getAll']);
$app->post('/api/banners/create', ['middleware' => 'cors', 'uses' => 'BannerController@create']);
$app->post('/api/banners/update', ['middleware' => 'cors', 'uses' => 'BannerController@update']);
$app->get('/api/banners/delete/{user_id}/{api_key}/{banner_id}', ['middleware' => 'cors', 'uses' => 'BannerController@delete']);
$app->get('/api/banners/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'BannerController@paginate']);

/*
 * Encuestas
 */
$app->post('/api/encuesta/create', ['middleware' => 'cors', 'uses' => 'EncuestasController@create']);
$app->post('/api/encuesta/update', ['middleware' => 'cors', 'uses' => 'EncuestasController@update']);
$app->post('/api/encuesta/response', ['middleware' => 'cors', 'uses' => 'EncuestasController@response']);
$app->get('/api/encuesta/all/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'EncuestasController@getAll']);
$app->get('/api/encuesta/detail/{user_id}/{api_key}/{encuesta_id}', ['middleware' => 'cors', 'uses' => 'EncuestasController@getEncuesta']);
$app->get('/api/encuesta/delete/{user_id}/{api_key}/{encuesta_id}', ['middleware' => 'cors', 'uses' => 'EncuestasController@delete']);
$app->get('/api/encuesta/paginate/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'EncuestasController@paginate']);

/*
 * Mapa recinto & mapa expositores
 */
$app->post('/api/mapa/recinto/upload', ['middleware' => 'cors', 'uses' => 'MapasController@createMapaRecinto']);
$app->get('/api/mapa/recinto/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'MapasController@getMapaRecinto']);
$app->get('/api/mapa/expositores/{user_id}/{api_key}', ['middleware' => 'cors', 'uses' => 'MapasController@getStands']);
$app->post('/api/mapa/expositores/reservar', ['middleware' => 'cors', 'uses' => 'MapasController@reservar']);
$app->post('/api/mapa/expositores/remove/reservation', ['middleware' => 'cors', 'uses' => 'MapasController@removeReservations']);
$app->get('/api/mapa/expositores/public', ['middleware' => 'cors', 'uses' => 'MapasController@getPublicStands']);
$app->get('/api/mapa/v2', ['middleware' => 'cors', 'uses' => 'MapasController@getPublicStandsV2']);
$app->get('/api/mapa/expositores/public/expositor/{expositor_id}', ['middleware' => 'cors', 'uses' => 'MapasController@loadExpositorLocation']);

/*
 * Push
 */
$app->get('/api/push/send', ['middleware' => 'cors', 'uses' => 'PushNotificationsController@toAwsSnsTopic']);
$app->post('/api/push/save', ['middleware' => 'cors', 'uses' => 'PushNotificationsController@save']);