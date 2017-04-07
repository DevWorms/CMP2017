<?php

namespace App\Http\Controllers;

use App\MisEventos;
use App\Programa;
use App\User;
use Illuminate\Database\Eloquent\ModelNotFoundException;

class MisEventosController extends Controller {

    /**
     * Devuelve mis expositores favoritos
     *
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getMyAgenda($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $mis_expositores = MisEventos::where('user_id', $user_id)->get();
            if ($mis_expositores) {
                $expositores = Programa::whereIn('id', $mis_expositores->pluck('evento_id'))->get();
            } else {
                $expositores = [];
            }

            foreach ($expositores as $expositor) {
                $expositor = $this->returnEvento($expositor);
            }

            $agenda = [
                "00:00" => $expositores->where('hora_inicio', '>=', "00:00:00")->where('hora_inicio', '<', "01:00:00")->values(),
                "01:00" => $expositores->where('hora_inicio', '>=', "01:00:00")->where('hora_inicio', '<', "02:00:00")->values(),
                "02:00" => $expositores->where('hora_inicio', '>=', "02:00:00")->where('hora_inicio', '<', "03:00:00")->values(),
                "03:00" => $expositores->where('hora_inicio', '>=', "03:00:00")->where('hora_inicio', '<', "04:00:00")->values(),
                "04:00" => $expositores->where('hora_inicio', '>=', "04:00:00")->where('hora_inicio', '<', "05:00:00")->values(),
                "05:00" => $expositores->where('hora_inicio', '>=', "05:00:00")->where('hora_inicio', '<', "06:00:00")->values(),
                "06:00" => $expositores->where('hora_inicio', '>=', "06:00:00")->where('hora_inicio', '<', "07:00:00")->values(),
                "07:00" => $expositores->where('hora_inicio', '>=', "07:00:00")->where('hora_inicio', '<', "08:00:00")->values(),
                "08:00" => $expositores->where('hora_inicio', '>=', "08:00:00")->where('hora_inicio', '<', "09:00:00")->values(),
                "09:00" => $expositores->where('hora_inicio', '>=', "09:00:00")->where('hora_inicio', '<', "10:00:00")->values(),
                "10:00" => $expositores->where('hora_inicio', '>=', "10:00:00")->where('hora_inicio', '<', "11:00:00")->values(),
                "11:00" => $expositores->where('hora_inicio', '>=', "11:00:00")->where('hora_inicio', '<', "12:00:00")->values(),
                "12:00" => $expositores->where('hora_inicio', '>=', "12:00:00")->where('hora_inicio', '<', "13:00:00")->values(),
                "13:00" => $expositores->where('hora_inicio', '>=', "13:00:00")->where('hora_inicio', '<', "14:00:00")->values(),
                "14:00" => $expositores->where('hora_inicio', '>=', "14:00:00")->where('hora_inicio', '<', "15:00:00")->values(),
                "15:00" => $expositores->where('hora_inicio', '>=', "15:00:00")->where('hora_inicio', '<', "16:00:00")->values(),
                "16:00" => $expositores->where('hora_inicio', '>=', "16:00:00")->where('hora_inicio', '<', "17:00:00")->values(),
                "17:00" => $expositores->where('hora_inicio', '>=', "17:00:00")->where('hora_inicio', '<', "18:00:00")->values(),
                "18:00" => $expositores->where('hora_inicio', '>=', "18:00:00")->where('hora_inicio', '<', "19:00:00")->values(),
                "19:00" => $expositores->where('hora_inicio', '>=', "19:00:00")->where('hora_inicio', '<', "20:00:00")->values(),
                "20:00" => $expositores->where('hora_inicio', '>=', "20:00:00")->where('hora_inicio', '<', "21:00:00")->values(),
                "21:00" => $expositores->where('hora_inicio', '>=', "21:00:00")->where('hora_inicio', '<', "22:00:00")->values(),
                "22:00" => $expositores->where('hora_inicio', '>=', "22:00:00")->where('hora_inicio', '<', "23:00:00")->values(),
                "23:00" => $expositores->where('hora_inicio', '>=', "23:00:00")->where('hora_inicio', '<=', "23:59:59")->values(),
                "all" => $expositores
            ];

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['agenda'] = $agenda;
            return response()->json($res, 200);
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    /**
     * Agrega un expositor a la lista de favoritos
     *
     * @param $user_id
     * @param $api_key
     * @param $expositor_id
     * @return \Illuminate\Http\JsonResponse
     */
    public function addFavorito($user_id, $api_key, $expositor_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositor = Programa::where('id', $expositor_id)->first();
            if ($expositor) {
                $valida = MisEventos::where(['user_id' => $user_id, 'evento_id' => $expositor_id])->first();

                if (!$valida) {
                    MisEventos::create([
                        'user_id' => $user_id,
                        'evento_id' => $expositor_id
                    ]);

                    $res['status'] = 1;
                    $res['mensaje'] = "Se agrego " . $expositor->nombre . " a tu agenda";
                    return response()->json($res, 200);
                } else {
                    $res['status'] = 0;
                    $res['mensaje'] = "El Evento ya se encuentra en tu agenda";
                    return response()->json($res, 400);
                }
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el Evento";
                return response()->json($res, 400);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    public function removeFavorito($user_id, $api_key, $expositor_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositor = Programa::where('id', $expositor_id)->first();
            if ($expositor) {
                $valida = MisEventos::where(['user_id' => $user_id, 'evento_id' => $expositor_id])->first();

                if ($valida) {
                    $valida->delete();
                }

                $res['status'] = 1;
                $res['mensaje'] = "Se removio " . $expositor->nombre . " de tu agenda";
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el Evento";
                return response()->json($res, 400);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    /**
     * Agrega la categoría y foto del evento
     *
     * @param $programa
     * @return mixed
     */
    public function returnEvento($programa) {
        if ($programa->foto_id) {
            $programa->foto;
            unset($programa->foto['is_banner']);
        } else {
            $programa->foto = [];
        }

        if ($programa->categoria_id) {
            $programa->categoria;
        }

        unset($programa['foto_id']);
        unset($programa['categoria_id']);
        return $programa;
    }

}
