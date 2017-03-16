<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\UploadedFile;
use Illuminate\Support\Facades\Validator;

//use Symfony\Component\HttpFoundation\File\UploadedFile;



class FileController extends Controller
{
    protected $destinationPath = "./";


    public function upload(Request $request)
    {

        //dd($request->file('archivo'));
        try {
            $file = $request->file('archivo');
            //dd($request->files);

            $rules = array('file' => 'required|mimes:jpeg,jpg,gif,png,doc,docx,zip,odt,pdf,xls,xlsx,rar,rtf,txt|max:10000000'); //'required|mimes:png,gif,jpeg,txt,pdf,doc'
            $validator = Validator::make(array('file' => $file), $rules);
            //dd($file);

            if ($validator->passes()) {
                if ($file->getSize() > 10000000) {
                    $response['estado'] = 0;
                    $response['mensaje'] = "El archivo excede el límite de 10mb";
                    return response()->json($response, 401);
                } else {
                    $uploadedFile = $request->file('archivo')->move($this->destinationPath,$file->getClientOriginalName());
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
        } catch (Exception $ex) {
            $response['estado'] = 0;
            $response['mensaje'] = $ex->getMessage();
            return response()->json($response, 500);
        }
    }
}
