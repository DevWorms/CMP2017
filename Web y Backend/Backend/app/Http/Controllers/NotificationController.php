<?php

namespace App\Http\Controllers;

use App\File;
use App\Notificacion;
use App\User;
use Carbon\Carbon;
use Exception;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class NotificationController extends Controller {
    protected $destinationPath = "./files/";

    public function save(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $notificacion = $request->get('notificacion');
            $leido = $request->get('leido');
            if (!$leido) {
                $leido = 0;
            }

            $notification = Notificacion::create([
                'user_id' => $user_id,
                'notificacion' => $notificacion,
                'leido' => $leido
            ]);

            $response['estado'] = 1;
            $response['mensaje'] = "success";
            $response['notificacion'] = $notification;
            return response()->json($response, 200);
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (Exception $ex) {
            $response['estado'] = 0;
            $response['mensaje'] = $ex->getMessage();
            return response()->json($response, 500);
        }
    }

    public function getAll($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $notificaciones = Notificacion::where('user_id', $user_id)->get();

            $response['estado'] = 1;
            $response['mensaje'] = "success";
            $response['notificaciones'] = $notificaciones;
            return response()->json($response, 200);
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (Exception $ex) {
            $response['estado'] = 0;
            $response['mensaje'] = $ex->getMessage();
            return response()->json($response, 500);
        }
    }

    public function unread($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $notificaciones = Notificacion::where([
                'user_id' => $user_id,
                'leido' => '0'
            ])->get();

            $response['estado'] = 1;
            $response['mensaje'] = "success";
            $response['notificaciones'] = $notificaciones;
            return response()->json($response, 200);
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (Exception $ex) {
            $response['estado'] = 0;
            $response['mensaje'] = $ex->getMessage();
            return response()->json($response, 500);
        }
    }

    public function markAsRead($user_id, $api_key, $notificacion_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $notificacion = Notificacion::where([
                'id' => $notificacion_id,
                'user_id' => $user_id
            ])->first();

            if ($notificacion) {
                $notificacion->leido = 1;
                $notificacion->save();

                $response['estado'] = 1;
                $response['mensaje'] = "La notificación se marco como leída";
                return response()->json($response, 200);
            } else {
                $response['estado'] = 0;
                $response['mensaje'] = "No se encontro la notificación";
                return response()->json($response, 200);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (Exception $ex) {
            $response['estado'] = 0;
            $response['mensaje'] = $ex->getMessage();
            return response()->json($response, 500);
        }
    }
}
