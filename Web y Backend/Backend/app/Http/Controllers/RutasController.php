<?php

namespace App\Http\Controllers;

use App\File;
use App\MisEventos;
use App\Programa;
use App\Ruta;
use App\User;
use Carbon\Carbon;
use Exception;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class RutasController extends Controller {
    public $destinationPath = "./files/";
    public $url_server = "http://files.cmp.devworms.com";

    private function messages() {
        return [
            'required' => 'Ingresa un valor para :attribute.',
            'max' => 'La descripción debe ser máximo 100 caracteres'
        ];
    }

    public function create(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');

            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $titulo = $request->get('titulo');
            $descripcion = $request->get('descripcion');
            $user_asignado = $request->get('user_asignado');
            $file = $request->file('archivo');
            $file_id = null;

            /*
             * Valida los datos obligatorios
             */
            $validator = Validator::make($request->all(), [
                'titulo' => 'required',
                'descripcion' => 'required|max:100',
                'user_asignado' => 'required'
            ], $this->messages());

            if ($validator->fails()) {
                //Si los datos no estan completos, devuelve error
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                // Valida el usuario asignado
                $user_id_asignado = User::where(['id' => $user_asignado])->first();
                if ($user_id_asignado) {
                    if ($this->validarRutas($user_id_asignado)) {
                        // Si se adjunta un archivo, se sube
                        if ($file) {
                            $rules = array('file' => 'required|mimes:jpeg,jpg,gif,png,pdf|max:10000000');
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

                                    $file_id = File::create([
                                        'user_id' => $user_id,
                                        'url' => $url,
                                        'nombre' => $file->getClientOriginalName(),
                                        'size' => $file->getClientSize()
                                    ]);

                                    $file_id = $file_id->id;
                                }
                            } else {
                                $response['estado'] = 0;
                                $response['mensaje'] = "Error, tipo de archivo invalido";

                                return response()->json($response, 400);
                            }
                        }

                        // Crea el programa
                        $ruta = Ruta::create([
                            'user_id' => $user_id,
                            'pdf_file' => $file_id,
                            'titulo' => $titulo,
                            'descripcion' => $descripcion,
                            'user_id_asignado' => $user_id_asignado->id
                        ]);

                        // Devuelve el programa
                        $ruta = $this->returnRuta($ruta);

                        $res['status'] = 1;
                        $res['mensaje'] = "Ruta creada correctamente";
                        $res['ruta'] = $ruta;
                        return response()->json($res, 200);
                    } else {
                        $response['estado'] = 0;
                        $response['mensaje'] = "Error, el usuario " . $user_id_asignado->name . " " . $user_id_asignado->last_name . " ya tiene 10 rutas asignadas";

                        return response()->json($response, 400);
                    }
                } else {
                    $response['estado'] = 0;
                    $response['mensaje'] = "Error, no se encontro al usuario";

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

    public function getAll($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $rutas = Ruta::where('user_id_asignado', $user_id)->get();

            foreach ($rutas as $ruta) {
                $ruta = $this->returnRuta($ruta);
            }

            $res['status'] = 1;
            $res['mensaje'] = "Ruta creada correctamente";
            $res['rutas'] = $rutas;
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

    public function getRuta($user_id, $api_key, $ruta_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $ruta = Ruta::where('id', $ruta_id)->first();
            if ($ruta) {
                $res['status'] = 1;
                $res['mensaje'] = "success";
                $res['ruta'] = $this->returnRuta($ruta);
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro la ruta";
                $res['ruta'] = [];
                return response()->json($res, 200);
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

    // Valida el número de rutas de un usuario
    public function validarRutas($user) {
        $rutas = Ruta::where('user_id_asignado', $user->id)->get();

        if ($rutas->count() < 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Agrega la categoría y foto del evento
     *
     * @param $programa
     * @return mixed
     */
    public function returnRuta($ruta) {
        if ($ruta->pdf_file) {
            $ruta->pdf;
        } else {
            $ruta->pdf = [];
        }

        unset($ruta['pdf_file']);
        unset($ruta['user_id']);
        unset($ruta['user_id_asignado']);
        return $ruta;
    }

}
