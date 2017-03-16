<?php

namespace App\Http\Controllers;

use App\File;
use App\User;
use Carbon\Carbon;
use Exception;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class FileController extends Controller {
    protected $destinationPath = "./files/";

    public function upload(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $file = $request->file('archivo');
            $rules = array('file' => 'required|mimes:jpeg,jpg,gif,png,pdf|max:10000000');
            $validator = Validator::make(array('file' => $file), $rules);

            if ($validator->passes()) {
                if ($file->getSize() > 10000000) {
                    $response['estado'] = 0;
                    $response['mensaje'] = "El archivo excede el límite de 10mb";
                    return response()->json($response, 401);
                } else {
                    $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                    $uploadedFile = $request->file('archivo')->move($path, uniqid() . "." . $file->getClientOriginalExtension());

                    File::create([
                        'user_id' => $user_id,
                        'url' => $uploadedFile->getPathname(),
                        'nombre' => $file->getClientOriginalName(),
                        'size' => $file->getClientSize()
                    ]);

                    $response['estado'] = 1;
                    $response['mensaje'] = "Exito";
                    $response['ruta'] = $uploadedFile->getPathname();
                    return response()->json($response, 200);
                }
            } else {
                $response['estado'] = 0;
                $response['mensaje'] = "Error, tipo de archivo invalido";

                return response()->json($response, 401);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales";
            return response()->json($res, 400);
        } catch (Exception $ex) {
            $response['estado'] = 0;
            $response['mensaje'] = $ex->getMessage();
            return response()->json($response, 500);
        }
    }
}
