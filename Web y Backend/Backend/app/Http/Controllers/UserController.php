<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 02.02.17
 * Time: 12:29
 */

namespace App\Http\Controllers;

use App\User;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Symfony\Component\Debug\Exception\FatalThrowableError;

class UserController extends Controller {
    /**
     * UserController constructor.
     */
    public function __construct() {
    }

    private function messagesSignup() {
        return [
            'required' => 'Ingresa un valor para :attribute.',
            'min' => 'La contraseña debe ser mayor a 5 caracteres',
            'unique' => 'El email ya se encuentra registrado',
            'confirmed' => 'Las contraseñas no coinciden'
        ];
    }

    private function messagesLogin() {
        return [
            'required' => 'Ingresa un valor para :attribute.',
            'min' => 'La contraseña debe ser mayor a 5 caracteres'
        ];
    }

    /**
     * Registro de usuarios
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function create(Request $request) {
        try {
            $name = $request->get('name');
            $last_name = $request->get('last_name');
            $type = strtolower($request->get('type'));
            $association = strtolower($request->get('association'));
            $email = $request->get('email');
            $password = Hash::make($request->get('password'));
            $api_token = md5(str_random(20));
            $clave = $request->get('clave');

            $validator = Validator::make($request->all(), [
                'name' => 'required',
                'last_name' => 'required',
                'email' => 'required|email|unique:users',
                'password' => 'required|min:6'
                //'password' => 'required|min:5|confirmed',
                //'password_confirmation' => 'required|min:5'
            ], $this->messagesSignup());

            if ($validator->fails()) {
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $user = new User();
                $user->name = $name;
                $user->last_name = $last_name;
                $user->type = $type;
                $user->association = $association;
                $user->email = $email;
                $user->password = $password;
                $user->api_token = $api_token;
                $user->clave = $clave;
                $user->save();

                $res['status'] = 1;
                $res['mensaje'] = "Perfil creado correctamente";
                $res['api_key'] = $user->api_token;
                $res['user_id'] = $user->id;
                return response()->json($res, 200);
            }
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

    public function resetPassword(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            $password = Hash::make($request->get('password'));

            $validator = Validator::make($request->all(), [
                'password' => 'required|min:6'
            ], $this->messagesSignup());

            if ($validator->fails()) {
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $user = User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();
                $user->password = $password;
                $user->save();

                $msg = "Tu contraseña se actualizo correctamente";

                $headers = "From: contacto@congreso.digital" . "\r\n";
                $headers .= "Reply-To: contacto@congreso.digital" . "\r\n";
                $headers .= "X-Mailer: PHP/" . phpversion();
                $headers .= "Content-type: text/html; charset=utf-8\r\n";

                $msg2 = "
                    <div>
                        Recientemente haz cambiado tu contraseña desde la aplicación
                        <br><br>
                        Si no haz realizado este cambio ponte en contacto con nosotros: <a href='contacto@congreso.digital'>contacto@congreso.digital</a>
                    </div>
                ";
                mail($user->email, 'Haz cambiado tu contraseña', $msg2, $headers);

                $res['status'] = 1;
                $res['mensaje'] = $msg;
                return response()->json($res, 200);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario no encontrado";
            return response()->json($res, 400);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    /**
     * Actualizar perfil
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(Request $request) {
        try {
            $user_id = $request->get('user_id');
            $api_key = $request->get('api_key');
            $name = $request->get('name');
            $last_name = $request->get('last_name');
            $clave = $request->get('clave');

            $validator = Validator::make($request->all(), [
                'name' => 'required',
                'last_name' => 'required'
            ], $this->messagesLogin());

            if ($validator->fails()) {
                $errors = $validator->errors();
                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $user = User::where(['id' => $user_id, 'api_token' => $api_key])->firstOrFail();

                $user->name = $name;
                $user->last_name = $last_name;
                if ($clave) {
                    $user->clave = $clave;
                }
                $user->save();

                //$user->type = $this->parseType($user->type);
                //$user->association = $this->parseAssociation($user->association);

                $res['status'] = 1;
                $res['mensaje'] = "Tu perfil se actualizo correctamente";
                $res['user'] = $user;
                return response()->json($res, 200);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario no encontrado";
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
     * Eliminar perfil
     *
     * @param $id
     * @param $api_token
     * @return \Illuminate\Http\JsonResponse
     */
    public function delete($id, $api_token) {
        try {
            $user = User::where(['id' => $id, 'api_token' => $api_token])->firstOrFail();
            $user->delete = 30;
            $user->save();

            $res['status'] = 1;
            $res['mensaje'] = "Tu cuenta se eliminará en 30 días";
            return response()->json($res, 200);
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario no encontrado";
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
     * Devuelve el perfil del usuario
     *
     * @param $id
     * @param $api_token
     * @return \Illuminate\Http\JsonResponse
     */
    public function select($id, $api_token) {
        try {
            $user = User::where(['id' => $id, 'api_token' => $api_token])->firstOrFail();
            //$user->type = $this->parseType($user->type);
            //$user->association = $this->parseAssociation($user->association);

            $res['status'] = 1;
            $res['mensaje'] = "Usuario encontrado";
            $res['user'] = $user;
            return response()->json($res, 200);
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario no encontrado";
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
     * Valida el inicio de sesión
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request) {
        try {
            $validator = Validator::make($request->all(), [
                'user' => 'required',
                'password' => 'required|min:5'
            ], $this->messagesLogin());

            if ($validator->fails()) {
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $user = $request->get('user');
                $password = $request->get('password');
                $user = User::where('email', $user)
                    ->firstOrFail();

                if (Hash::check($password, $user->password)) {
                    $res['status'] = 1;
                    $res['mensaje'] = "Bienvenido " . $user->name . " " . $user->last_name;
                    $res['api_key'] = $user->api_token;
                    $res['user_id'] = $user->id;
                    return response()->json($res, 200);
                } else {
                    $res['status'] = 0;
                    $res['mensaje'] = "Usuario o contraseña incorrecto";
                    return response()->json($res, 400);
                }
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario o contraseña incorrecto";
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

    public function loginRoot(Request $request) {
        try {
            $validator = Validator::make($request->all(), [
                'user' => 'required',
                'password' => 'required|min:5'
            ], $this->messagesLogin());

            if ($validator->fails()) {
                $errors = $validator->errors();

                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $user = $request->get('user');
                $password = $request->get('password');
                $user = User::where(['email' => $user, 'id' => 1])->firstOrFail();

                if (Hash::check($password, $user->password)) {
                    $res['status'] = 1;
                    $res['mensaje'] = "Bienvenido " . $user->name . " " . $user->last_name;
                    $res['api_key'] = $user->api_token;
                    $res['user_id'] = $user->id;
                    $res['key'] = hash('sha256', $user->id);
                    return response()->json($res, 200);
                } else {
                    $res['status'] = 0;
                    $res['mensaje'] = "Usuario o contraseña incorrecto";
                    return response()->json($res, 400);
                }
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario o contraseña incorrecto";
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

    public function getTypes() {
        try {
            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['tipos'] = [
                1 => "Congresista",
                2 => "Expositor",
                3 => "Estudiante",
                4 => "Acompañante"
            ];
            return response()->json($res, 200);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    public function getAsociaciones() {
        try {
            $res['status'] = 1;
            $res['mensaje'] = "success";
            $res['asociaciones'] = [
                1 => "AIPM",
                2 => "CIPM",
                3 => "AMGE",
                4 => "AMGP",
                5 => "SPE / México"
            ];
            return response()->json($res, 200);
        } catch (\Exception $ex) {
            $res['status'] = 0;
            $res['mensaje'] = $ex->getMessage();
            return response()->json($res, 500);
        }
    }

    private function parseType($type) {
        switch ($type) {
            case 1:
                return "Congresista";
                break;
            case 2:
                return "Expositor";
                break;
            case 3:
                return "Estudiante";
                break;
            case 4:
                return "Acompañante";
                break;
            default:
                return "";
                break;
        }
    }

    private function parseAssociation($type) {
        switch ($type) {
            case 1:
                return "AIPM";
                break;
            case 2:
                return "CIPM";
                break;
            case 3:
                return "AMGE";
                break;
            case 4:
                return "AMGP";
                break;
            case 5:
                return  "SPE / México";
                break;
            default:
                return "";
                break;
        }
    }
}