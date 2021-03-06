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
use App\Programa;
use App\User;
use Carbon\Carbon;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Symfony\Component\Debug\Exception\FatalThrowableError;

class SocioDeportivosController extends Controller {

    /**
     * instancia de ProgramaController para la funcion returnPrograma
     *
     * @var ProgramaController
     */
    private $tool;

    /**
     * UserController constructor.
     */
    public function __construct() {
        $this->tool = new ProgramaController();
    }

    /**
     * Mensajes de error
     *
     * @return array
     */
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
            if (!$categoria_id) {
                $categoria_id = null;
            }
            $nombre = $request->get('nombre');
            $lugar = $request->get('lugar');
            $recomendaciones = $request->get('recomendaciones');
            $latitude = $request->get('latitude');
            $longitude = $request->get('longitude');
            $fecha = Carbon::parse($request->get('fecha'))->toDateString();
            $maps_url = $request->get('maps_url');

            // Si tiene hora de inicio, la valida, si no es null
            if ($request->get('hora_inicio')) {
                $hora_inicio = Carbon::parse($request->get('hora_inicio'))->toTimeString();
            } else {
                $hora_inicio = null;
            }

            // Si tiene hora de fin, la valida, si no es null
            if ($request->get('hora_fin')) {
                $hora_fin = Carbon::parse($request->get('hora_fin'))->toTimeString();
            } else {
                $hora_fin = null;
            }
            $file_id = null;

            /*
             * Valida los datos obligatorios
             */
            $validator = Validator::make($request->all(), [
                'nombre' => 'required',
                'lugar' => 'required',
                'recomendaciones' => 'required',
                'fecha' => 'required'
            ], $this->messages());

            if ($validator->fails()) {
                //Si los datos no estan completos, devuelve error
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $file = $request->file('archivo');
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
                            $path = $this->tool->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                            $uploadedFile = $request->file('archivo')->move($path, uniqid() . "." . $file->getClientOriginalExtension());
                            $url = $this->tool->url_server . substr($uploadedFile->getPathname(), 7);

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
                $programa = Programa::create([
                    'user_id' => $user_id,
                    'nombre' => $nombre,
                    'categoria_id' => $categoria_id,
                    'lugar' => $lugar,
                    'recomendaciones' => $recomendaciones,
                    'latitude' => $latitude,
                    'longitude' => $longitude,
                    'fecha' => $fecha,
                    'hora_inicio' => $hora_inicio,
                    'hora_fin' => $hora_fin,
                    'type' => 3,
                    'foto_id' => $file_id,
                    'maps_url' => $maps_url
                ]);

                // Devuelve el programa
                $programa = $this->tool->returnPrograma($programa);
                $this->createUpdate();

                $res['status'] = 1;
                $res['mensaje'] = "Evento creado correctamente";
                $res['evento'] = $programa;
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
     * Devuelve todos los Eventos
     *
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getAll($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $programas = Programa::where('type', 3)->orderBy('fecha', 'asc')->get();

            foreach ($programas as $programa) {
                $programa = $this->tool->returnPrograma($programa);
            }

            $this->markUpdate($user_id);

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['eventos'] = $programas;
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
    public function getFiltro(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            if ($request->get('fecha')) {
                $fecha = Carbon::parse($request->get('fecha'))->toDateString();
            } else {
                $fecha = null;
            }
            $tipo = $request->get('categoria_id');

            $programas = Programa::where('type', 3)->get();

            if ($fecha) {
                $programas = $programas->where('fecha', $fecha)->values();
            }

            if ($tipo) {
                $categoria = Categoria::where('id', $tipo)->first();
                if ($categoria) {
                    $programas = $programas->where('categoria_id', $tipo)->values();
                } else {
                    $res['status'] = 0;
                    $res['mensaje'] = "Categoría no encontrada";
                    return response()->json($res, 400);
                }
            }

            foreach ($programas as $programa) {
                $programa = $this->tool->returnPrograma($programa);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['eventos'] = $programas;
            return response()->json($res, 200);
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales " .$ex->getMessage();
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
     * @param $programa_id
     * @return \Illuminate\Http\JsonResponse
     */
    public function getPrograma($user_id, $api_key, $programa_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $programa = Programa::where('id', $programa_id)->first();
            if ($programa) {
                $programa = $this->tool->returnPrograma($programa);

                $res['status'] = 1;
                $res['mensaje'] = "success";
                $res['evento'] = $programa;
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

    public function paginate($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Programa::orderBy('id', 'DESC')->where('type', 3)->paginate(5);
            foreach ($expositores as $expositor) {
                $expositor = $this->tool->returnPrograma($expositor);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['eventos'] = $expositores;
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

    public function update(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $categoria_id = $request->get('categoria_id');
            if (!$categoria_id) {
                $categoria_id = null;
            }
            $nombre = $request->get('nombre');
            $lugar = $request->get('lugar');
            $recomendaciones = $request->get('recomendaciones');
            $latitude = $request->get('latitude');
            $longitude = $request->get('longitude');
            $fecha = Carbon::parse($request->get('fecha'))->toDateString();
            $id = $request->get('id');
            $maps_url = $request->get('maps_url');

            // Si tiene hora de inicio, la valida, si no es null
            if ($request->get('hora_inicio')) {
                $hora_inicio = Carbon::parse($request->get('hora_inicio'))->toTimeString();
            } else {
                $hora_inicio = null;
            }

            // Si tiene hora de fin, la valida, si no es null
            if ($request->get('hora_fin')) {
                $hora_fin = Carbon::parse($request->get('hora_fin'))->toTimeString();
            } else {
                $hora_fin = null;
            }

            /*
             * Valida los datos obligatorios
             */
            $validator = Validator::make($request->all(), [
                'nombre' => 'required',
                'lugar' => 'required',
                'recomendaciones' => 'required',
                'fecha' => 'required'
            ], $this->messages());

            if ($validator->fails()) {
                //Si los datos no estan completos, devuelve error
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $programa = Programa::where('id', $id)->first();
                if ($programa) {
                    if ($programa->foto_id) {
                        $file_id = $programa->foto_id;
                    } else {
                        $file_id = null;
                    }
                    $file = $request->file('archivo');
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
                                $path = $this->tool->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                                $uploadedFile = $request->file('archivo')->move($path, uniqid() . "." . $file->getClientOriginalExtension());
                                $url = $this->tool->url_server . substr($uploadedFile->getPathname(), 7);

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

                    if ($maps_url) {
                        $programa->maps_url = $maps_url;
                    }

                    // Crea el programa
                    $programa->nombre = $nombre;
                    $programa->categoria_id = $categoria_id;
                    $programa->lugar = $lugar;
                    $programa->recomendaciones = $recomendaciones;
                    $programa->latitude = $latitude;
                    $programa->longitude = $longitude;
                    $programa->fecha = $fecha;
                    $programa->hora_inicio = $hora_inicio;
                    $programa->hora_fin = $hora_fin;
                    $programa->type = 3;
                    $programa->foto_id = $file_id;
                    $programa->save();

                    // Devuelve el programa
                    $programa = $this->tool->returnPrograma($programa);
                    $this->createUpdate();

                    $res['status'] = 1;
                    $res['mensaje'] = "Evento actualizado correctamente";
                    $res['evento'] = $programa;
                    return response()->json($res, 200);
                } else {
                    $response['estado'] = 0;
                    $response['mensaje'] = "No se encontro el evento: " . $id;

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
    public function search(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            $search = $request->get('search');

            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Programa::where('nombre', 'LIKE', '%'. $search .'%')->where('type', 3)->get();
            foreach ($expositores as $expositor) {
                $expositor = $this->tool->returnPrograma($expositor);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['eventos'] = $expositores;
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
     * @param $expositor
     * @return \Illuminate\Http\JsonResponse
     */
    public function delete($user_id, $api_key, $id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositor = Programa::where('id', $id)->first();
            if ($expositor) {
                $expositor->delete();
                $this->createUpdate();

                $res['status'] = 1;
                $res['mensaje'] = "El evento se eliminó correctamente.";
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro el evento: " . $id;
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
        $up->createUpdate(8);
    }

    public function markUpdate($user_id) {
        $up = new UpdatesController();
        $up->markUpdateAsRead($user_id, 8);
    }
}