<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 02.02.17
 * Time: 12:29
 */

namespace App\Http\Controllers;

use App\Categoria;
use App\File;
use App\User;
use Carbon\Carbon;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Symfony\Component\Debug\Exception\FatalThrowableError;

class MapasController extends Controller {
    public $destinationPath = "./files/maps/";
    public $url_server = "http://files.cmp.devworms.com";

    /**
     * UserController constructor.
     */
    public function __construct() {
    }

    /**
     * @return array
     */
    private function messages() {
        return [
            'required' => 'Ingresa un valor para :attribute.'
        ];
    }

    public function createMapaRecinto(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $file = $request->file('archivo');

            $validator = Validator::make($request->all(), [
                'archivo' => 'required'
            ], $this->messages());

            if ($validator->fails()) {
                //Si los datos no estan completos, devuelve error
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $rules = array('file' => 'required|mimes:jpeg,jpg,gif,png|max:10000000');
                $validator = Validator::make(array('file' => $file), $rules);

                // Si el archivo tiene extensión valida
                if ($validator->passes()) {
                    // Si el archivo es mayor a 10mb
                    if ($file->getSize() > 10000000) {
                        $response['estado'] = 0;
                        $response['mensaje'] = "El archivo excede el límite de 10mb";
                        return response()->json($response, 400);
                    } else {
                        // Si va bien, lo mueve a la carpeta y guarda el registro
                        $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                        $uploadedFile = $request->file('archivo')->move($path, uniqid() . "." . $file->getClientOriginalExtension());
                        $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                        $file_ = File::updateOrCreate(
                            ['is_banner' => 2],
                            [
                                'user_id' => $user_id,
                                'url' => $url,
                                'nombre' => $file->getClientOriginalName(),
                                'size' => $file->getClientSize(),
                                'is_banner' => 2
                            ]
                        );

                        $response['estado'] = 1;
                        $response['mensaje'] = "El mapa se guardó correctamente";
                        $response['mapa'] = $file_;

                        return response()->json($response, 200);
                    }
                } else {
                    $response['estado'] = 0;
                    $response['mensaje'] = "Error, tipo de archivo invalido";

                    return response()->json($response, 400);
                }
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

    public function getMapaRecinto($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $mapa = File::where('is_banner', 2)->first();

            if ($mapa) {
                $response['estado'] = 1;
                $response['mensaje'] = "success";
                $response['mapa'] = $mapa;

                return response()->json($response, 200);
            } else {
                $response['estado'] = 1;
                $response['mensaje'] = "success";
                $response['mapa'] = [];

                return response()->json($response, 200);
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