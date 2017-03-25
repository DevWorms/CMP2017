<?php

namespace App\Http\Controllers;

use App\File;
use App\User;
use Carbon\Carbon;
use Exception;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class BannerController extends Controller {
    public $destinationPath = "./files/";
    public $url_server = "http://files.cmp.devworms.com";

    public function getAll($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $banners = File::where('is_banner', 1)->get();
            $banners = $this->returnBanners($banners);

            $res['estadp'] = 1;
            $res['mensaje'] = "success";
            $res['banners'] = $banners;
            return response()->json($res, 200);
        } catch (ModelNotFoundException $ex) {
            $res['estado'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (Exception $ex) {
            $res['estado'] = 0;
            $res['mensaje'] = $ex->getMessage();

            return response()->json($res, 500);
        }
    }

    private function messages() {
        return [
            'required' => 'Ingresa un Banner'
        ];
    }

    public function create(Request $request) {
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
                // se adjunta un archivo, se sube
                if ($file) {
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

                            $banner = File::create([
                                'user_id' => $user_id,
                                'url' => $url,
                                'nombre' => $file->getClientOriginalName(),
                                'size' => $file->getClientSize(),
                                'is_banner' => 1
                            ]);

                            $res['status'] = 1;
                            $res['mensaje'] = "Banner creado correctamente";
                            $res['banner'] = $banner;
                            return response()->json($res, 200);
                        }
                    } else {
                        $response['estado'] = 0;
                        $response['mensaje'] = "Error, tipo de archivo invalido";

                        return response()->json($response, 400);
                    }
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

    private function returnBanners($banners) {
        foreach ($banners as $banner) {
            unset($banner['is_banner']);
        }

        return $banners;
    }
}
