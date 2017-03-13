<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 02.02.17
 * Time: 12:29
 */

namespace App\Http\Controllers;

use App\Categoria;
use App\Programa;
use App\User;
use Carbon\Carbon;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Symfony\Component\Debug\Exception\FatalThrowableError;

class ProgramaController extends Controller {
    /**
     * UserController constructor.
     */
    public function __construct() {
    }

    private function messages() {
        return [
            'required' => 'Ingresa un valor para :attribute.'
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

            $categoria_id = $request->get('categoria_id');
            $lugar = $request->get('lugar');
            $recomendaciones = $request->get('recomendaciones');
            $latitude = $request->get('latitude');
            $longitude = $request->get('longitude');
            $fecha = Carbon::parse($request->get('fecha'));

            $validator = Validator::make($request->all(), [
                'categoria_id' => 'required',
                'lugar' => 'required',
                'recomendaciones' => 'required',
                'fecha' => 'required'
            ], $this->messages());

            if ($validator->fails()) {
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $id_cat = Categoria::where('id', $categoria_id)->first();
                if ($id_cat) {
                    $programa = Programa::create([
                        'categoria_id' => $categoria_id,
                        'lugar' => $lugar,
                        'recomendaciones' => $recomendaciones,
                        'latitude' => $latitude,
                        'longitude' => $longitude,
                        'fecha' => $fecha
                    ]);

                    $res['status'] = 1;
                    $res['mensaje'] = "Evento creado correctamente";
                    $res['evento'] = $programa;
                    return response()->json($res, 201);
                } else {
                    $res['status'] = 0;
                    $res['mensaje'] = "Categoría invalida";
                    return response()->json($res, 400);
                }
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
            $programas = Programa::orderBy('id', 'desc')->take(10)->get();

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['programas'] = $programas;
            return response()->json($res, 200);
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
}