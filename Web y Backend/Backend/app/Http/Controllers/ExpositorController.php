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
     * @var string
     */
    public $destinationPath = "./files/expositores/";

    /**
     * @var string
     */
    public $url_server = "http://files.cmp.devworms.com";

    /**
     * ExpositorController constructor.
     */
    public function __construct() {
    }

    /**
     * Mensajes de error
     *
     * @return array
     */
    private function messages() {
        return [
            'required' => 'Ingresa un valor para :attribute.',
            'numeric' => "Ingresa un número de stand válido"
        ];
    }

    /**
     * Crea un nuevo Expositor
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
            $url_expositor = $request->get('url');
            if ((strtolower(substr($url_expositor, 0, 7)) == "http://") || (strtolower(substr($url_expositor, 0, 8)) == "https://")) {
                if (substr($url_expositor, -1) != "/") {
                    $url_expositor = $url_expositor . "/";
                }
            } else {
                if (substr($url_expositor, -1) == "/") {
                    $url_expositor = "http://" . $url_expositor;
                } else {
                    $url_expositor = "http://" . $url_expositor . "/";
                }
            }
            $telefono = $request->get('telefono');
            $acerca = $request->get('acerca');
            $latitude = $request->get('latitude');
            $longitude = $request->get('longitude');
            $stand = $request->get('stand');
            $maps_url = $request->get('maps_url');

            // Archivos del expositor
            $presentacion = $request->file('archivo_pdf');
            $pdf_id = null;
            $logo = $request->file('archivo_logo');
            $logo_id = null;

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
                            $uploadedFile = $request->file('archivo_logo')->move($path, uniqid() . "." . $logo->getClientOriginalExtension());
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

                // Sube el pdf (presentación) del expositor
                if ($presentacion) {
                    $rules = array('file' => 'required|mimes:pdf|max:10000000');
                    $validator = Validator::make(array('file' => $presentacion), $rules);

                    // Si el archivo tiene extensión valida
                    if ($validator->passes()) {
                        // Si el archivo es mayor a 10mb
                        if ($presentacion->getSize() > 10000000) {
                            $response['estado'] = 0;
                            $response['mensaje'] = "El archivo excede el límite de 10mb";
                            return response()->json($response, 400);
                        } else {
                            // Si va bien, lo mueve a la carpeta y guarda el registro
                            $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                            $uploadedFile = $request->file('archivo_pdf')->move($path, uniqid() . "." . $presentacion->getClientOriginalExtension());
                            $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                            $file = File::create([
                                'user_id' => $user_id,
                                'url' => $url,
                                'nombre' => $presentacion->getClientOriginalName(),
                                'size' => $presentacion->getClientSize()
                            ]);

                            $pdf_id = $file->id;
                        }
                    } else {
                        $response['estado'] = 0;
                        $response['mensaje'] = "Error, tipo de PDF invalido";

                        return response()->json($response, 400);
                    }
                }

                $expositor = Expositor::create([
                    'user_id' => $user_id,
                    'url' => $url_expositor,
                    'nombre' => $nombre,
                    'email' => $email,
                    'telefono' => $telefono,
                    'acerca' => $acerca,
                    'latitude' => $latitude,
                    'longitude' => $longitude,
                    'stand' => $stand,
                    'pdf_file' => $pdf_id,
                    'logo_file' => $logo_id,
                    'maps_url' => $maps_url
                ]);

                $expositor = $this->returnExpositor($expositor);
                $this->createUpdate();

                $res['status'] = 1;
                $res['mensaje'] = "Expositor creado correctamente";
                $res['expositor'] = $expositor;
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
     * Devuelve todos los expositores
     *
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getAll($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Expositor::orderBy('id', 'DESC')->where('is_expositor', 1)->get();

            foreach ($expositores as $expositor) {
                $expositor = $this->returnExpositor($expositor);
            }

            $this->markUpdate($user_id);

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

    /**
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function paginate($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Expositor::orderBy('id', 'DESC')->where('is_expositor', 1)->paginate(5);
            foreach ($expositores as $expositor) {
                $expositor = $this->returnExpositor($expositor);
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

    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function search(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            $search = $request->get('search');

            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Expositor::where('nombre', 'LIKE', '%'. $search .'%')->where('is_expositor', 1)->get();
            foreach ($expositores as $expositor) {
                $expositor = $this->returnExpositor($expositor);
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

    /**
     * Devuelve todos los expositores en orden alfabético
     *
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getByName($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Expositor::where('is_expositor', 1)->orderBy('nombre', 'asc')->get();

            foreach ($expositores as $expositor) {
                $expositor = $this->returnExpositor($expositor);
            }

            $this->markUpdate($user_id);

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

    /**
     * Devuelve todos los expositores en orden de stand
     *
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getByStand($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Expositor::where('is_expositor', 1)->orderBy('stand', 'asc')->get();

            foreach ($expositores as $expositor) {
                $expositor = $this->returnExpositor($expositor);
            }

            $this->markUpdate($user_id);

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

    /**
     * Devuelve mis expositores favoritos
     *
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getMyExpositores($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $mis_expositores = MisExpositores::where('user_id', $user_id)->get();
            if ($mis_expositores) {
                $expositores = Expositor::whereIn('id', $mis_expositores->pluck('expositor_id'))->get();
            } else {
                $expositores = [];
            }

            foreach ($expositores as $expositor) {
                $expositor = $this->returnExpositor($expositor);
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

    /**
     * Devuelve el detalle de un expositor
     *
     * @param $user_id
     * @param $api_key
     * @param $expositor_id
     * @return \Illuminate\Http\JsonResponse
     */
    public function getExpositor($user_id, $api_key, $expositor_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositor = Expositor::where('id', $expositor_id)->first();
            if ($expositor) {
                $expositor = $this->returnExpositor($expositor);

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

    /**
     * @param $expositor
     * @return mixed
     */
    public function returnExpositor($expositor) {
        if ($expositor->pdf_file) {
            $expositor->pdf;
            unset($expositor->pdf['is_banner']);
        } else {
            $expositor->pdf = [];
        }

        if ($expositor->logo_file) {
            $expositor->logo;
        } else {
            $expositor->logo = [];
        }

        unset($expositor['pdf_file']);
        unset($expositor['logo_file']);
        unset($expositor['is_expositor']);
        return $expositor;
    }

    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $nombre = $request->get('nombre');
            $email = $request->get('email');
            $url_expositor = $request->get('url');
            if ((strtolower(substr($url_expositor, 0, 7)) == "http://") || (strtolower(substr($url_expositor, 0, 8)) == "https://")) {
                if (substr($url_expositor, -1) != "/") {
                    $url_expositor = $url_expositor . "/";
                }
            } else {
                if (substr($url_expositor, -1) == "/") {
                    $url_expositor = "http://" . $url_expositor;
                } else {
                    $url_expositor = "http://" . $url_expositor . "/";
                }
            }
            $telefono = $request->get('telefono');
            $acerca = $request->get('acerca');
            $latitude = $request->get('latitude');
            $longitude = $request->get('longitude');
            $stand = $request->get('stand');
            $id = $request->get('id');
            $maps_url = $request->get('maps_url');

            // Archivos del expositor
            $presentacion = $request->file('archivo_pdf');
            $logo = $request->file('archivo_logo');

            $validator = Validator::make($request->all(), [
                'id' => 'required|numeric',
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
                $expositor = Expositor::where('id', $id)->first();
                if ($expositor->pdf_file) {
                    $pdf_id = $expositor->pdf_file;
                } else {
                    $pdf_id = null;
                }

                if ($expositor->logo_file) {
                    $logo_id = $expositor->logo_file;
                } else {
                    $logo_id = null;
                }

                if ($expositor) {
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
                                $uploadedFile = $request->file('archivo_logo')->move($path, uniqid() . "." . $logo->getClientOriginalExtension());
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

                    // Sube el pdf (presentación) del expositor
                    if ($presentacion) {
                        $rules = array('file' => 'required|mimes:pdf|max:10000000');
                        $validator = Validator::make(array('file' => $presentacion), $rules);

                        // Si el archivo tiene extensión valida
                        if ($validator->passes()) {
                            // Si el archivo es mayor a 10mb
                            if ($presentacion->getSize() > 10000000) {
                                $response['estado'] = 0;
                                $response['mensaje'] = "El archivo excede el límite de 10mb";
                                return response()->json($response, 400);
                            } else {
                                // Si va bien, lo mueve a la carpeta y guarda el registro
                                $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                                $uploadedFile = $request->file('archivo_pdf')->move($path, uniqid() . "." . $presentacion->getClientOriginalExtension());
                                $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                                $file = File::create([
                                    'user_id' => $user_id,
                                    'url' => $url,
                                    'nombre' => $presentacion->getClientOriginalName(),
                                    'size' => $presentacion->getClientSize()
                                ]);

                                $pdf_id = $file->id;
                            }
                        } else {
                            $response['estado'] = 0;
                            $response['mensaje'] = "Error, tipo de PDF invalido";

                            return response()->json($response, 400);
                        }
                    }

                    if ($maps_url) {
                        $expositor->maps_url = $maps_url;
                    }

                    $expositor->url = $url_expositor;
                    $expositor->nombre = $nombre;
                    $expositor->email = $email;
                    $expositor->telefono = $telefono;
                    $expositor->acerca = $acerca;
                    $expositor->latitude = $latitude;
                    $expositor->longitude = $longitude;
                    $expositor->stand = $stand;
                    $expositor->pdf_file = $pdf_id;
                    $expositor->logo_file = $logo_id;
                    $expositor->save();

                    $expositor = $this->returnExpositor($expositor);
                    $this->createUpdate();

                    $res['status'] = 1;
                    $res['mensaje'] = "Expositor actualizado correctamente";
                    $res['expositor'] = $expositor;
                    return response()->json($res, 200);
                } else {
                    $response['estado'] = 0;
                    $response['mensaje'] = "No se encontró el expositor: " . $id;

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
     * @param $expositor
     * @return \Illuminate\Http\JsonResponse
     */
    public function delete($user_id, $api_key, $expositor) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $programa = Expositor::where('id', $expositor)->first();
            if ($programa) {
                $programa->delete();
                $this->createUpdate();

                $res['status'] = 1;
                $res['mensaje'] = "El expositor se eliminó correctamente.";
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el expositor: " . $expositor;
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
            $expositor = Expositor::where('id', $expositor_id)->first();

            if ($expositor) {
                $valida = MisExpositores::where(['user_id' => $user_id, 'expositor_id' => $expositor_id])->first();

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

    public function createUpdate() {
        $up = new UpdatesController();
        $up->createUpdate(4);
    }

    public function markUpdate($user_id) {
        $up = new UpdatesController();
        $up->markUpdateAsRead($user_id, 4);
    }
}