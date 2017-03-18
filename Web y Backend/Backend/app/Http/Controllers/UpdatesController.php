<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 18.03.17
 * Time: 09:02
 */

namespace App\Http\Controllers;

use App\Update;
use App\User;
use App\UserUpdate;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;

class UpdatesController extends Controller
{
    /**
     * UpdatesController constructor.
     */
    public function __construct() {
    }

    public function checkUpdates($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $updates = UserUpdate::where(['user_id' => $user_id, 'status' => 1])->get();
            if ($updates->count() > 0) {
                $ups = Update::whereIn('id', $updates->pluck('update_id'))
                    ->select('id')
                    ->groupby('modulo')
                    ->orderBy('id')
                    ->get();

                $res['status'] = 1;
                $res['mensaje'] = "success";
                $res['actualizaciones'] = $ups;
                return response()->json($res, 200);
            } else {
                $res['status'] = 2;
                $res['mensaje'] = "No tienes actualizaciones disponibles";
                $res['actualizaciones'] = [];
                return response()->json($res, 200);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario no encontrado";
            return response()->json($res, 400);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    public function getUpdate($user_id, $api_key, $update_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            // Busca la actualización solicitada
            $up = Update::where('id', $update_id)->first();

            // Selecciona la última actualización disponible
            $modulo = Update::where('modulo', $up->modulo)
                ->orderBy('updated_at', 'DESC')
                ->first();

            // TODO devolver la actulalización

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['actualizacion'] = $modulo;
            return response()->json($res, 200);
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario no encontrado";
            return response()->json($res, 400);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }
}