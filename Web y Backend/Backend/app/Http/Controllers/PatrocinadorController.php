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

class PatrocinadorController extends Controller {
    public $destinationPath = "./files/expositores/";
    public $url_server = "http://cmp.devworms.com";

    /**
     * PatrocinadorController constructor.
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
     * Crea un nuevo Patrocinador
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
            $tipo = $request->get('tipo');

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
                'tipo' => 'required'
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
                            return response()->json($response, 401);
                        } else {
                            // Si va bien, lo mueve a la carpeta y guarda el registro
                            $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                            $uploadedFile = $request->file('archivo_logo')->move($path, uniqid() . "." . $logo->getClientOriginalExtension());
                            $url = $this->url_server . substr($uploadedFile->getPathname(), 1);

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

                        return response()->json($response, 401);
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
                            return response()->json($response, 401);
                        } else {
                            // Si va bien, lo mueve a la carpeta y guarda el registro
                            $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                            $uploadedFile = $request->file('archivo_pdf')->move($path, uniqid() . "." . $presentacion->getClientOriginalExtension());
                            $url = $this->url_server . substr($uploadedFile->getPathname(), 1);

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

                        return response()->json($response, 401);
                    }
                }

                $expositor = Expositor::create([
                    'user_id' => $user_id,
                    'url' => $url,
                    'nombre' => $nombre,
                    'email' => $email,
                    'telefono' => $telefono,
                    'acerca' => $acerca,
                    'latitude' => $latitude,
                    'longitude' => $longitude,
                    'tipo' => $tipo,
                    'pdf_file' => $pdf_id,
                    'logo_file' => $logo_id,
                    'is_expositor' => 0
                ]);

                $expositor = $this->returnPatrocinador($expositor);

                $res['status'] = 1;
                $res['mensaje'] = "Patrocinador creado correctamente";
                $res['patrocinador'] = $expositor;
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

    /**
     * Devuelve todos los Patrocinadores
     *
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getAll($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Expositor::where('is_expositor', 0)->get();

            foreach ($expositores as $expositor) {
                $expositor = $this->returnPatrocinador($expositor);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['patrocinadores'] = $expositores;
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
     * Devuelve todos los Patrocinadores en orden alfabético
     *
     * @param $user_id
     * @param $api_key
     * @return \Illuminate\Http\JsonResponse
     */
    public function getByName($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Expositor::where('is_expositor', 0)->orderBy('nombre', 'asc')->get();

            foreach ($expositores as $expositor) {
                $expositor = $this->returnPatrocinador($expositor);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['patrocinadores'] = $expositores;
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
    public function getPatrocinador($user_id, $api_key, $expositor_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositor = Expositor::where('id', $expositor_id)->first();
            if ($expositor) {
                $expositor = $this->returnPatrocinador($expositor);

                $res['status'] = 1;
                $res['mensaje'] = "success";
                $res['patrocinador'] = $expositor;
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
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    public function returnPatrocinador($expositor) {
        if ($expositor->pdf_file) {
            $expositor->pdf;
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
        unset($expositor['stand']);
        unset($expositor['is_expositor']);

        return $expositor;
    }
}