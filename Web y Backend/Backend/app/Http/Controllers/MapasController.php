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
use App\MapaExpositores;
use App\MapaHasExpositores;
use App\User;
use Carbon\Carbon;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Mockery\Exception;
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

    public function getStands($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $stands = MapaExpositores::all();

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['stands'] = $stands;
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

    public function reservar(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $stands = $request->get('estantes');
            $expositor_id = $request->get('expositor_id');

            $expositor = Expositor::where('id', $expositor_id)->first();
            if ($expositor) {
                $coma = substr($stands, -1);
                if ($coma == ",") {
                    $stands = substr($stands, 0, -1);
                }
                $estantes = explode(",", $stands);

                $color = $this->rand_color();
                $estantes_ids = [];
                foreach ($estantes as $estante) {
                    $id = substr($estante, 8);
                    array_push($estantes_ids, $id);

                    MapaExpositores::where('id', $id)->update([
                        'available' => 0,
                        'expositor_id' => $expositor->id,
                        'color' => $color
                    ]);
                }

                $res['status'] = 1;
                $res['mensaje'] = "Los cambios se guardaron correctamente";
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontró el expositor: " . $expositor_id;
                return response()->json($res, 400);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage() . $ex->getLine() . $ex->getFile();
            return response()->json($res, 500);
        }
    }

    function rand_color() {
        return '#' . str_pad(dechex(mt_rand(0, 0xFFFFFF)), 6, '0', STR_PAD_LEFT);
    }

    public function getPublicStands() {
        try {
            $stands = MapaExpositores::with('expositor')->get();

            foreach ($stands as $stand) {

                if ($stand->expositor) {
                    $stand->expo = $stand->expositor->nombre;
                    unset($stand["expositor"]);
                } else {
                    $stand->expositor = null;
                }
                unset($stand["created_at"]);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['stands'] = $stands;
            return response()->json($res, 200);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    public function getPublicStandsV2() {
        try {
            $expositores = Expositor::select('id', 'nombre', 'email', 'url', 'telefono', 'acerca', 'logo_file', 'pdf_file')
                ->whereHas('estantes')
                ->with('estantes', 'logo', 'pdf')->get();

            foreach ($expositores as $expositor) {
                $expositor = $this->returnPublicExpositor($expositor);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['expositores'] = $expositores;
            return response()->json($res, 200);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    public function loadExpositorLocation($id) {
        try {
            $expositor = Expositor::where('id', $id)
                ->select('id', 'nombre', 'email', 'url', 'telefono', 'acerca', 'logo_file', 'pdf_file')
                ->with('estantes', 'logo', 'pdf')
                ->firstOrFail();

            if ($expositor->estantes->count() > 0) {
                $expositor = $this->returnPublicExpositor($expositor);

                $res['status'] = 1;
                $res['mensaje'] = "success";
                $res['expositor'] = $expositor;
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se ha asignado ningún estante al expositor: " . $expositor->nombre;
                return response()->json($res, 200);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "No se encontró el Expositor: " . $id;
            return response()->json($res, 400);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    function returnPublicExpositor($expositor) {
        unset($expositor["logo_file"]);
        //unset($expositor["id"]);
        unset($expositor["logo"]["is_banner"]);
        unset($expositor["logo"]["user_id"]);
        unset($expositor["logo"]["id"]);
        unset($expositor["logo"]["created_at"]);

        $estantes = collect($expositor->estantes)->groupBy('coords');
        //unset($expositor["estantes"]);

        $flag = 0;
        $coords = null;
        foreach ($estantes as $estante) {
            if ($estante->count() > $flag) {
                $flag = $estante->count();
                $coords = $estante[0]->coords;
            }
        }
        $expositor["coords"] = $coords;

        if ($expositor->pdf_file) {
            $expositor->pdf;
            unset($expositor->pdf['is_banner']);
        } else {
            $expositor->pdf = [];
        }

        unset($expositor['pdf_file']);

        return $expositor;
    }
}