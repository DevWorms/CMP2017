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
    /**
     * @var string
     */
    public $destinationPath = "./files/";

    /**
     * @var string
     */
    public $url_server = "http://files.cmp.devworms.com";

    /**
     * @return array
     */
    private function messages() {
        return [
            'required' => 'Ingresa un valor para :attribute.',
            'max' => "Descipción máximo 300 caracteres"
        ];
    }

    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
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

                    $this->createUpdate(10);

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

    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
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

                    $this->createUpdate(9);
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

    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
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

    /**
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getSitios($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $sitios = PueblaSitios::all();

            foreach ($sitios as $sitio) {
                $sitio = $this->returnSitio($sitio);
            }

            $this->markUpdate($user_id, 10);

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

    /**
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getTelefonos($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $sitios = PueblaTelefonos::all();

            foreach ($sitios as $sitio) {
                $sitio = $this->returnSitio($sitio);
            }

            $this->markUpdate($user_id, 9);

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

    /**
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
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

    /**
     * @return bool
     */
    private function validateSitios() {
        $sitios = PueblaSitios::all();
        if ($sitios->count() < 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return bool
     */
    private function validateTelefonos() {
        $sitios = PueblaTelefonos::all();
        if ($sitios->count() < 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param $sitio
     * @return mixed
     */
    public function returnSitio($sitio) {
        if ($sitio->imagen_id) {
            $sitio->imagen;
            unset($sitio->imagen['is_banner']);
        } else {
            $sitio->imagen = [];
        }

        unset($sitio['imagen_id']);
        return $sitio;
    }

    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function updateSitio(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $titulo = $request->get('titulo');
            $mapa = $request->get('maps_link');
            $descripcion = $request->get('descripcion');
            $url_sitio = $request->get('url');
            $id = $request->get('id');

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

            /*
             * Valida los datos obligatorios
             */
            $validator = Validator::make($request->all(), [
                'id' => 'required|numeric',
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
                $sitio = PueblaSitios::where('id', $id)->first();
                if ($sitio) {
                    if ($sitio->imagen_id) {
                        $logo_id = $sitio->imagen_id;
                    } else {
                        $logo_id = null;
                    }
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

                    $sitio->titulo = $titulo;
                    $sitio->maps_link = $mapa;
                    $sitio->descripcion = $descripcion;
                    $sitio->imagen_id = $logo_id;
                    $sitio->url = $url_sitio;
                    $sitio->save();

                    $this->createUpdate(10);

                    $sitio = $this->returnSitio($sitio);

                    $res['status'] = 1;
                    $res['mensaje'] = "El sitio se actualizó correctamente";
                    $res['sitio'] = $sitio;
                    return response()->json($res, 200);
                } else {
                    $response['estado'] = 0;
                    $response['mensaje'] = "No se encontro el sitio: " . $id;

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

    /**
     * @param $user_id
     * @param $api_key
     * @param $sitio
     * @return \Illuminate\Http\JsonResponse
     */
    public function deleteSitio($user_id, $api_key, $sitio) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $programa = PueblaSitios::where('id', $sitio)->first();
            if ($programa) {
                $programa->delete();
                $this->createUpdate(10);

                $res['status'] = 1;
                $res['mensaje'] = "El sitio se eliminó correctamente.";
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el sitio: " . $sitio;
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
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function updateTelefono(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $titulo = $request->get('titulo');
            $telefono = $request->get('telefono');
            $id = $request->get('id');

            $logo = $request->file('imagen');

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
                $tel = PueblaTelefonos::where('id', $id)->first();
                if ($tel) {
                    if ($tel->imagen_id) {
                        $logo_id = $tel->imagen_id;
                    } else {
                        $logo_id = null;
                    }
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

                    $tel->titulo = $titulo;
                    $tel->imagen_id = $logo_id;
                    $tel->telefono = $telefono;
                    $tel->save();

                    $this->createUpdate(9);
                    $tel = $this->returnSitio($tel);

                    $res['status'] = 1;
                    $res['mensaje'] = "El teléfono se creó correctamente";
                    $res['telefono'] = $tel;
                    return response()->json($res, 200);
                } else {
                    $response['estado'] = 0;
                    $response['mensaje'] = "No se encontro el teléfono: " . $id;

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

    /**
     * @param $user_id
     * @param $api_key
     * @param $telefono
     * @return \Illuminate\Http\JsonResponse
     */
    public function deleteTelefono($user_id, $api_key, $telefono) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $programa = PueblaTelefonos::where('id', $telefono)->first();
            if ($programa) {
                $programa->delete();
                $this->createUpdate(9);

                $res['status'] = 1;
                $res['mensaje'] = "El telefono se eliminó correctamente.";
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el telefono: " . $telefono;
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
     * @param $user_id
     * @param $api_key
     * @param $mapa
     * @return \Illuminate\Http\JsonResponse
     */
    public function deleteMapa($user_id, $api_key, $mapa) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $programa = PueblaMapa::where('id', $mapa)->first();
            if ($programa) {
                $programa->delete();

                $res['status'] = 1;
                $res['mensaje'] = "El mapa se eliminó correctamente.";
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el mapa: " . $mapa;
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

    public function paginateSitios($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = PueblaSitios::orderBy('id', 'DESC')->paginate(5);

            foreach ($expositores as $expositor) {
                $expositor = $this->returnSitio($expositor);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['sitios'] = $expositores;
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

    public function paginateTelefonos($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = PueblaTelefonos::orderBy('id', 'DESC')->paginate(5);

            foreach ($expositores as $expositor) {
                $expositor = $this->returnSitio($expositor);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['telefonos'] = $expositores;
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

    public function detailSitio($user_id, $api_key, $id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = PueblaSitios::where('id', $id)->first();
            if ($expositores) {
                $expositores = $this->returnSitio($expositores);

                $res['status'] = 1;
                $res['mensaje'] = "success";
                $res['sitio'] = $expositores;
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el sitio";
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

    public function detailTelefono($user_id, $api_key, $id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = PueblaTelefonos::where('id', $id)->first();
            if ($expositores) {
                $expositores = $this->returnSitio($expositores);

                $res['status'] = 1;
                $res['mensaje'] = "success";
                $res['telefono'] = $expositores;
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el sitio";
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

    public function createUpdate($modulo) {
        $up = new UpdatesController();
        $up->createUpdate($modulo);
    }

    public function markUpdate($user_id, $modulo) {
        $up = new UpdatesController();
        $up->markUpdateAsRead($user_id, $modulo);
    }
}