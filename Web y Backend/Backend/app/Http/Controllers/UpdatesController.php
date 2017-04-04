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

    /*
     * 1 = Eventos de acompañantes
     * 2 = Banners
     * 3 = Categorías
     * 4 = Expositores
     * 5 = Patrocinadores
     * 6 = Programas
     * 7 = Rutas / Transportación
     * 8 = Eventos Sociales y Deportivos
     * 9 = Conoce Puebla, teléfonos
     * 10 = Conoce Puebla, sitios de interés
     *
     * Funcionales:
     * 1 = Eventos de acompañantes
     * 4 = Expositores
     * 5 = Patrocinadores
     * 6 = Programas
     * 7 = Rutas / Transportación
     * 8 = Eventos Sociales y Deportivos
     * 9 = Conoce Puebla, teléfonos
     * 10 = Conoce Puebla, sitios de interés
     */

    public function checkUpdates($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $updates = UserUpdate::where(['user_id' => $user_id, 'status' => 0])->get();
            if ($updates->count() > 0) {
                $ups = Update::whereIn('id', $updates->pluck('update_id'))
                    ->select('modulo')
                    ->groupby('modulo') // Agrupa solo por modulo
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

    public function createUpdate($modulo_id) {
        try {
            $users = User::all();

            $update = Update::create([
                'modulo' => $modulo_id
            ]);

            foreach ($users as $user) {
                UserUpdate::create([
                    'update_id' => $update->id,
                    'user_id' => $user->id,
                    'status' => 0
                ]);
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

    public function markUpdateAsRead($user_id, $modulo_id) {
        try {
            $modulos = Update::where('modulo', $modulo_id)->get();
            if ($modulos->count() > 0) {
                foreach ($modulos as $modulo) {
                    $updates = UserUpdate::where(['user_id' => $user_id, 'update_id' => $modulo->id])->get();
                    if ($updates->count() > 0) {
                        foreach ($updates as $update) {
                            $update->delete();
                        }
                    }
                }
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
}