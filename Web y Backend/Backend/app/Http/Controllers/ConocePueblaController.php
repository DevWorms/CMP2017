<?php
/**
 * Created by PhpStorm.
 * User: rk521
 * Date: 19.03.17
 * Time: 18:49
 */

namespace App\Http\Controllers;

use App\File;
use App\PueblaMapa;
use App\PueblaSitios;
use App\PueblaTelefonos;
use App\User;
use Carbon\Carbon;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class ConocePueblaController extends Controller {
    public $destinationPath = "./files/";
    public $url_server = "http://files.cmp.devworms.com";

    private function messages() {
        return [
            'required' => 'Ingresa un valor para :attribute.',
            'max' => "Descipción máximo 300 caracteres"
        ];
    }

    public function createSitio(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $titulo = $request->get('titulo');
            $mapa = $request->get('maps_link');
            $descripcion = $request->get('descripcion');
            $url_sitio = $request->get('url');
            if ((strtolower(substr($url_sitio, 0, 7)) == "http://") || (strtolower(substr($url_sitio, 0, 8)) == "https://")) {
                if (substr($url_sitio, -1) != "/") {
                    $url_sitio = $url_sitio . "/";
                }
            } else {
                if (substr($url_sitio, -1) == "/") {
                    $url_sitio = "http://" . $url_sitio;
                } else {
                    $url_sitio = "http://" . $url_sitio . "/";
                }
            }

            $logo = $request->file('imagen');
            $logo_id = null;

            /*
             * Valida los datos obligatorios
             */
            $validator = Validator::make($request->all(), [
                'titulo' => 'required',
                'maps_link' => 'required',
                'descripcion' => 'required|max:300',
                'url' => 'required'
            ], $this->messages());

            if ($validator->fails()) {
                //Si los datos no estan completos, devuelve error
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                if ($this->validateSitios()) {
                    // Sube el logo del expositor
                    if ($logo) {
                        $rules = array('file' => 'required|mimes:jpeg,jpg,gif,png|max:10000000');
                        $validator = Validator::make(array('file' => $logo), $rules);

                        // Si el archivo tiene extensión valida
                        if ($validator->passes()) {
                            // Si el archivo es mayor a 10mb
                            if ($logo->getSize() > 10000000) {
                                $response['estado'] = 0;
                                $response['mensaje'] = "El archivo excede el límite de 10mb";
                                return response()->json($response, 400);
                            } else {
                                // Si va bien, lo mueve a la carpeta y guarda el registro
                                $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                                $uploadedFile = $request->file('imagen')->move($path, uniqid() . "." . $logo->getClientOriginalExtension());
                                $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                                $file = File::create([
                                    'user_id' => $user_id,
                                    'url' => $url,
                                    'nombre' => $logo->getClientOriginalName(),
                                    'size' => $logo->getClientSize()
                                ]);

                                $logo_id = $file->id;
                            }
                        } else {
                            $response['estado'] = 0;
                            $response['mensaje'] = "Error, tipo de archivo invalido";

                            return response()->json($response, 400);
                        }
                    }

                    $sitio = PueblaSitios::create([
                        'user_id' => $user_id,
                        'titulo' => $titulo,
                        'maps_link' => $mapa,
                        'descripcion' => $descripcion,
                        'imagen_id' => $logo_id,
                        'url' => $url_sitio
                    ]);

                    $sitio = $this->returnSitio($sitio);

                    $res['status'] = 1;
                    $res['mensaje'] = "El sitio se creó correctamente";
                    $res['sitio'] = $sitio;
                    return response()->json($res, 200);
                } else {
                    $response['estado'] = 0;
                    $response['mensaje'] = "No se puede crear más de 10 sitios de interés";

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

    public function createTelefono(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $titulo = $request->get('titulo');
            $telefono = $request->get('telefono');

            $logo = $request->file('imagen');
            $logo_id = null;

            /*
             * Valida los datos obligatorios
             */
            $validator = Validator::make($request->all(), [
                'titulo' => 'required',
                'telefono' => 'required'
            ], $this->messages());

            if ($validator->fails()) {
                //Si los datos no estan completos, devuelve error
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                if ($this->validateTelefonos()) {
                    // Sube el logo del expositor
                    if ($logo) {
                        $rules = array('file' => 'required|mimes:jpeg,jpg,gif,png|max:10000000');
                        $validator = Validator::make(array('file' => $logo), $rules);

                        // Si el archivo tiene extensión valida
                        if ($validator->passes()) {
                            // Si el archivo es mayor a 10mb
                            if ($logo->getSize() > 10000000) {
                                $response['estado'] = 0;
                                $response['mensaje'] = "El archivo excede el límite de 10mb";
                                return response()->json($response, 400);
                            } else {
                                // Si va bien, lo mueve a la carpeta y guarda el registro
                                $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                                $uploadedFile = $request->file('imagen')->move($path, uniqid() . "." . $logo->getClientOriginalExtension());
                                $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                                $file = File::create([
                                    'user_id' => $user_id,
                                    'url' => $url,
                                    'nombre' => $logo->getClientOriginalName(),
                                    'size' => $logo->getClientSize()
                                ]);

                                $logo_id = $file->id;
                            }
                        } else {
                            $response['estado'] = 0;
                            $response['mensaje'] = "Error, tipo de archivo invalido";

                            return response()->json($response, 400);
                        }
                    }

                    $tel = PueblaTelefonos::create([
                        'user_id' => $user_id,
                        'titulo' => $titulo,
                        'imagen_id' => $logo_id,
                        'telefono' => $telefono
                    ]);

                    $tel = $this->returnSitio($tel);

                    $res['status'] = 1;
                    $res['mensaje'] = "El teléfono se creó correctamente";
                    $res['telefono'] = $tel;
                    return response()->json($res, 200);
                } else {
                    $response['estado'] = 0;
                    $response['mensaje'] = "No se puede crear más de 10 números telefónicos";

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

    public function uploadMapa(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $mapa = $request->file('mapa');
            $mapa_id = null;

            /*
             * Valida los datos obligatorios
             */
            $validator = Validator::make($request->all(), [
                'mapa' => 'required'
            ], $this->messages());

            if ($validator->fails()) {
                //Si los datos no estan completos, devuelve error
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $rules = array('file' => 'required|mimes:jpeg,jpg,gif,png,pdf|max:10000000');
                $validator = Validator::make(array('file' => $mapa), $rules);

                // Si el archivo tiene extensión valida
                if ($validator->passes()) {
                    // Si el archivo es mayor a 10mb
                    if ($mapa->getSize() > 10000000) {
                        $response['estado'] = 0;
                        $response['mensaje'] = "El archivo excede el límite de 10mb";
                        return response()->json($response, 400);
                    } else {
                        // Si va bien, lo mueve a la carpeta y guarda el registro
                        $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                        $uploadedFile = $request->file('mapa')->move($path, uniqid() . "." . $mapa->getClientOriginalExtension());
                        $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                        $file = File::create([
                            'user_id' => $user_id,
                            'url' => $url,
                            'nombre' => $mapa->getClientOriginalName(),
                            'size' => $mapa->getClientSize()
                        ]);

                        $mapa_id = $file->id;
                    }
                } else {
                    $response['estado'] = 0;
                    $response['mensaje'] = "Error, tipo de archivo invalido";

                    return response()->json($response, 400);
                }

                $mapa = PueblaMapa::first();
                $mapa->file_id = $mapa_id;
                $mapa->save();

                $res['status'] = 1;
                $res['mensaje'] = "El mapa se subio correctamente";
                $res['mapa'] = $mapa->mapa;
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

    public function getSitios($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $sitios = PueblaSitios::all();

            foreach ($sitios as $sitio) {
                $sitio = $this->returnSitio($sitio);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['sitios'] = $sitios;
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

    public function getTelefonos($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $sitios = PueblaTelefonos::all();

            foreach ($sitios as $sitio) {
                $sitio = $this->returnSitio($sitio);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['telefonos'] = $sitios;
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

    public function getMapa($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $mapa = PueblaMapa::first();

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['mapa'] = $mapa->mapa;
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

    private function validateSitios() {
        $sitios = PueblaSitios::all();
        if ($sitios->count() < 10) {
            return true;
        } else {
            return false;
        }
    }

    private function validateTelefonos() {
        $sitios = PueblaTelefonos::all();
        if ($sitios->count() < 10) {
            return true;
        } else {
            return false;
        }
    }

    public function returnSitio($sitio) {
        if ($sitio->imagen_id) {
            $sitio->imagen;
        } else {
            $sitio->imagen = [];
        }

        unset($sitio['imagen_id']);
        return $sitio;
    }
}