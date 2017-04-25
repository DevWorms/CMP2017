<?php

namespace App\Http\Controllers;

use App\Encuesta;
use App\File;
use App\Pregunta;
use App\Respuesta;
use App\User;
use App\UserHasEncuestas;
use Carbon\Carbon;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class EncuestasController extends Controller {
    /**
     * @var string
     */
    public $destinationPath = "./files/";

    /**
     * @var string
     */
    public $url_server = "http://files.cmp.devworms.com";

    private function messages() {
        return [
            'required' => 'Ingresa un valor para :attribute.'
        ];
    }

    public function create(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');

            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $preguntas[] = $request->get('pregunta');
            $filesm = $request->file('archivo_sm');
            $filexl = $request->file('archivo_xl');

            if (count($preguntas[0]) < 4 && count($preguntas[0]) > 0) {
                $validator = Validator::make($request->all(), [
                    'archivo_sm' => 'required|mimes:jpeg,jpg,gif,png,pdf|max:10000000',
                    'archivo_xl' => 'required|mimes:jpeg,jpg,gif,png,pdf|max:10000000'
                ], $this->messages());

                // Si el archivo tiene extensión valida
                if ($validator->passes()) {
                    // Si el archivo es mayor a 10mb
                    if ($filesm->getSize() > 10000000) {
                        $response['estado'] = 0;
                        $response['mensaje'] = "El archivo excede el límite de 10mb";
                        return response()->json($response, 400);
                    } else {
                        // Si va bien, lo mueve a la carpeta y guarda el registro
                        $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                        $uploadedFile = $request->file('archivo_sm')->move($path, uniqid() . "." . $filesm->getClientOriginalExtension());
                        $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                        $filesm_id = File::create([
                            'user_id' => $user_id,
                            'url' => $url,
                            'nombre' => $filesm->getClientOriginalName(),
                            'size' => $filesm->getClientSize()
                        ]);
                    }

                    if ($filexl->getSize() > 10000000) {
                        $response['estado'] = 0;
                        $response['mensaje'] = "El archivo excede el límite de 10mb";
                        return response()->json($response, 400);
                    } else {
                        // Si va bien, lo mueve a la carpeta y guarda el registro
                        $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                        $uploadedFile = $request->file('archivo_xl')->move($path, uniqid() . "." . $filexl->getClientOriginalExtension());
                        $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                        $filexl_id = File::create([
                            'user_id' => $user_id,
                            'url' => $url,
                            'nombre' => $filexl->getClientOriginalName(),
                            'size' => $filexl->getClientSize()
                        ]);
                    }

                    $encuesta = Encuesta::create([
                        'file_sm' => $filesm_id->id,
                        'file_xl' => $filexl_id->id
                    ]);

                    foreach ($preguntas[0] as $pregunta) {
                        Pregunta::create([
                            'pregunta' => $pregunta,
                            'encuesta_id' => $encuesta->id
                        ]);
                    }

                    $users = User::all();
                    foreach ($users as $user) {
                        UserHasEncuestas::create([
                            'encuesta_id' => $encuesta->id,
                            'user_id' => $user->id
                        ]);
                    }

                    $this->createUpdate();

                    $response['estado'] = 1;
                    $response['mensaje'] = "Encuesta creada correctamente";
                    return response()->json($response, 200);
                } else {
                    $errors = $validator->errors();
                    $response['estado'] = 0;
                    $response['mensaje'] = $errors->first();

                    return response()->json($response, 400);
                }
            } else {
                $response['estado'] = 0;
                if (count($preguntas[0]) < 1) {
                    $response['mensaje'] = "Ingresa al menos 1 pregunta";
                } else {
                    $response['mensaje'] = "La encuesta debe contener máximo 3 preguntas";
                }
                return response()->json($response, 400);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Error de credenciales" . $ex->getMessage();
            return response()->json($res, 400);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    public function getAll($user_id, $api_key) {
        try {
            $user = User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $encs = UserHasEncuestas::where(['user_id' => $user->id, 'resuelta' => 0])->get();
            $encuestas = Encuesta::select('id', 'file_sm')->whereIn('id', $encs->pluck('encuesta_id'))->get();

            foreach ($encuestas as $encuesta) {
                $encuesta = $this->returnEncuesta($encuesta);
            }

            $this->markUpdate($user_id);

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['encuestas'] = $encuestas;
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

    public function getEncuesta($user_id, $api_key, $encuesta_id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $encuesta = Encuesta::where('id', $encuesta_id)->first();
            if ($encuesta) {
                $res['status'] = 1;
                $res['mensaje'] = "success";
                $res['encuesta'] = $this->returnEncuesta($encuesta, 1);
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro la encuesta";
                $res['encuesta'] = [];
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

    public function returnEncuesta($encuesta, $preguntas = null) {
        if ($preguntas == 1) {
            $preguntas = $encuesta->preguntas;
            foreach ($preguntas as $pregunta) {
                unset($pregunta['encuesta_id']);
                unset($pregunta['updated_at']);
            }
        }

        $encuesta->filesm;

        if ($encuesta->file_xl) {
            $encuesta->filexl;
            unset($encuesta->filexl['user_id']);
            unset($encuesta->filexl['is_banner']);
            unset($encuesta['file_xl']);
        }

        unset($encuesta->filesm['user_id']);
        unset($encuesta->filesm['is_banner']);
        unset($encuesta['file_sm']);
        return $encuesta;
    }

    public function update(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');

            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $encuesta_id = $request->get('encuesta_id');
            $preguntas[] = $request->get('pregunta');
            $filesm = $request->file('archivo_sm');
            $filexl = $request->file('archivo_xl');

            $encuesta = Encuesta::where('id', $encuesta_id)->first();
            if ($encuesta) {
                if (count($preguntas[0]) < 4 && count($preguntas[0]) > 0) {
                    $validator = Validator::make($request->all(), [
                        'archivo_sm' => 'mimes:jpeg,jpg,gif,png,pdf|max:10000000',
                        'archivo_xl' => 'mimes:jpeg,jpg,gif,png,pdf|max:10000000'
                    ], $this->messages());

                    // Si el archivo tiene extensión valida
                    if ($validator->passes()) {
                        if ($filesm) {
                            // Si el archivo es mayor a 10mb
                            if ($filesm->getSize() > 10000000) {
                                $response['estado'] = 0;
                                $response['mensaje'] = "El archivo excede el límite de 10mb";
                                return response()->json($response, 400);
                            } else {
                                // Si va bien, lo mueve a la carpeta y guarda el registro
                                $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                                $uploadedFile = $request->file('archivo_sm')->move($path, uniqid() . "." . $filesm->getClientOriginalExtension());
                                $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                                $filesm_id = File::create([
                                    'user_id' => $user_id,
                                    'url' => $url,
                                    'nombre' => $filesm->getClientOriginalName(),
                                    'size' => $filesm->getClientSize()
                                ]);

                                $encuesta->file_sm = $filesm_id->id;
                            }
                        }

                        if ($filexl) {
                            if ($filexl->getSize() > 10000000) {
                                $response['estado'] = 0;
                                $response['mensaje'] = "El archivo excede el límite de 10mb";
                                return response()->json($response, 400);
                            } else {
                                // Si va bien, lo mueve a la carpeta y guarda el registro
                                $path = $this->destinationPath . Carbon::now()->year . "/" . Carbon::now()->month . "/";
                                $uploadedFile = $request->file('archivo_xl')->move($path, uniqid() . "." . $filexl->getClientOriginalExtension());
                                $url = $this->url_server . substr($uploadedFile->getPathname(), 7);

                                $filexl_id = File::create([
                                    'user_id' => $user_id,
                                    'url' => $url,
                                    'nombre' => $filexl->getClientOriginalName(),
                                    'size' => $filexl->getClientSize()
                                ]);

                                $encuesta->file_xl = $filexl_id->id;
                            }
                        }

                        $encuesta->save();

                        Pregunta::where('encuesta_id', $encuesta_id)->delete();
                        foreach ($preguntas[0] as $pregunta) {
                            Pregunta::create([
                                'pregunta' => $pregunta,
                                'encuesta_id' => $encuesta_id
                            ]);
                        }

                        $this->createUpdate();

                        $response['estado'] = 1;
                        $response['mensaje'] = "Encuesta actualizada correctamente";
                        return response()->json($response, 200);
                    } else {
                        $errors = $validator->errors();
                        $response['estado'] = 0;
                        $response['mensaje'] = $errors->first();

                        return response()->json($response, 400);
                    }
                } else {
                    $response['estado'] = 0;
                    if (count($preguntas[0]) < 1) {
                        $response['mensaje'] = "Ingresa al menos 1 pregunta";
                    } else {
                        $response['mensaje'] = "La encuesta debe contener máximo 3 preguntas";
                    }
                    return response()->json($response, 400);
                }
            } else {
                $response['estado'] = 0;
                $response['mensaje'] = "No se encontró la encuesta: " . $encuesta_id;

                return response()->json($response, 400);
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

    public function response(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            $encuesta_id = $request->get('encuesta_id');

            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

            $encuesta = Encuesta::where('id', $encuesta_id)->first();
            if ($encuesta) {
                $preguntas = $encuesta->preguntas;

                $count = 1;
                foreach ($preguntas as $pregunta) {
                    Respuesta::updateOrCreate(
                        [
                            'user_id' => $user_id,
                            'pregunta_id' => $pregunta->id
                        ],
                        [
                            'respuesta' => $request->get('respuesta_' . $count)
                        ]
                    );
                    $count++;
                }

                UserHasEncuestas::where(['user_id' => $user_id, 'encuesta_id' => $encuesta->id])->update([
                    'resuelta' => 1
                ]);

                $res['status'] = 1;
                $res['mensaje'] = "Tus respuestas se guardaron correctamente";
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontró la encuesta: " . $encuesta_id;
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
     * @param $expositor
     * @return \Illuminate\Http\JsonResponse
     */
    public function delete($user_id, $api_key, $id) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositor = Encuesta::where('id', $id)->first();
            if ($expositor) {
                foreach ($expositor->usersHas as $relation) {
                    $relation->delete();
                }

                foreach ($expositor->preguntas as $relation) {
                    $relation->delete();
                }

                $expositor->delete();

                $res['status'] = 1;
                $res['mensaje'] = "La encuesta se eliminó correctamente.";
                return response()->json($res, 200);
            } else {
                $res['status'] = 0;
                $res['mensaje'] = "No se encontro la encuesta: " . $id;
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

    public function paginate($user_id, $api_key) {
        try {
            User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
            $expositores = Encuesta::orderBy('id', 'DESC')->paginate(5);
            foreach ($expositores as $expositor) {
                $expositor = $this->returnEncuesta($expositor);
            }

            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['encuestas'] = $expositores;
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

    public function createUpdate() {
        $up = new UpdatesController();
        $up->createUpdate(8);
    }

    public function markUpdate($user_id) {
        $up = new UpdatesController();
        $up->markUpdateAsRead($user_id, 8);
    }
}
