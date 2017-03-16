<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 02.02.17
 * Time: 12:29
 */

namespace App\Http\Controllers;

use App\Categoria;
use App\Expositor;
use App\File;
use App\MisExpositores;
use App\Programa;
use App\User;
use Carbon\Carbon;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Symfony\Component\Debug\Exception\FatalThrowableError;

class ExpositorController extends Controller {
    /**
     * UserController constructor.
     */
    public function __construct() {
    }

    private function messages() {
        return [
            'required' => 'Ingresa un valor para :attribute.',
            'numeric' => "Ingresa un número de stand válido"
        ];
    }

    /**
     * Crea un nuevo Evento
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function create(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $nombre = $request->get('nombre');
            $email = $request->get('email');
            $url = $request->get('url');
            $telefono = $request->get('telefono');
            $acerca = $request->get('acerca');
            $latitude = $request->get('latitude');
            $longitude = $request->get('longitude');
            $stand = $request->get('stand');

            $validator = Validator::make($request->all(), [
                'nombre' => 'required',
                'email' => 'required',
                'telefono' => 'required',
                'acerca' => 'required',
                'stand' => 'required|numeric'
            ], $this->messages());

            if ($validator->fails()) {
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $expositor = Expositor::create([
                    'user_id' => $user_id,
                    'url' => $url,
                    'nombre' => $nombre,
                    'email' => $email,
                    'telefono' => $telefono,
                    'acerca' => $acerca,
                    'latitude' => $latitude,
                    'longitude' => $longitude,
                    'stand' => $stand
                ]);

                $res['status'] = 1;
                $res['mensaje'] = "Expositor creado correctamente";
                $res['expositor'] = $expositor;
                return response()->json($res, 201);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (FatalThrowableError $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    public function getAll($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositor = Expositor::all();

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['expositor'] = $expositor;
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

    public function getByName($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Expositor::orderBy('nombre', 'asc')->get();

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['expositores'] = $expositores;
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

    public function getByStand($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Expositor::orderBy('stand', 'asc')->get();

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['expositores'] = $expositores;
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

    public function getMyExpositores($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $mis_expositores = MisExpositores::where('user_id', $user_id)->get();
            if ($mis_expositores) {
                $expositores = Expositor::whereIn('id', $mis_expositores->pluck('expositor_id'))->get();
            } else {
                $expositores = [];
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['expositores'] = $expositores;
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

    public function getExpositor($user_id, $api_key, $expositor_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositor = Expositor::where('id', $expositor_id)->first();
            if ($expositor) {
                $res['status'] = 1;
                $res['mensaje'] = "success";
                $res['expositor'] = $expositor;
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el Expositor";
                return response()->json($res, 400);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (FatalThrowableError $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    public function addFavorito($user_id, $api_key, $expositor_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositor = Expositor::where('id', $expositor_id)->first();
            if ($expositor) {
                $valida = MisExpositores::where(['user_id' => $user_id, 'expositor_id' => $expositor_id])->first();

                if (!$valida) {
                    MisExpositores::create([
                        'user_id' => $user_id,
                        'expositor_id' => $expositor_id
                    ]);

                    $res['status'] = 1;
                    $res['mensaje'] = "Se agrego " . $expositor->nombre . " a tus favoritos";
                    return response()->json($res, 200);
                } else {
                    $res['status'] = 0;
                    $res['mensaje'] = "El Expositor ya se encuentra en tus favoritos";
                    return response()->json($res, 400);
                }
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el Expositor";
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
}