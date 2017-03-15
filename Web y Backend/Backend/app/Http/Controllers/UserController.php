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
use Illuminate\Validation\Rule;
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
            $type = $request->get('type');
            $association = $request->get('association');
            $email = $request->get('email');
            $password = Hash::make($request->get('password'));
            $api_token = md5(str_random(20));
            $clave = $request->get('clave');

            $validator = Validator::make($request->all(), [
                'name' => 'required',
                'last_name' => 'required',
                'type' => 'required',
                'association' => 'required',
                'email' => 'required|email|unique:users',
                'clave' => 'required',
                'password' => 'required|min:5'
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
                return response()->json($res, 201);
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

    /**
     * Actualizar perfil
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function update(Request $request) {
        try {
            $id = $request->get('id');
            $name = $request->get('name');
            $email = $request->get('email');
            $phone = $request->get('phone');
            $api_token = $request->get('api_token');

            $validator = Validator::make($request->all(), [
                'id' => 'required|numeric',
                'name' => 'required',
                'api_token' => 'required',
                'email' => [
                    'required',
                    'email',
                    Rule::unique('users')->ignore($id)
                ],
                'phone' => [
                    'required',
                    'min:8',
                    'max:20',
                    Rule::unique('users')->ignore($id)
                ]
            ]);

            if ($validator->fails()) {
                $errors = $validator->errors();
                $res['status'] = 0;
                $res['mensaje'] = $errors->first();
                return response()->json($res, 400);
            } else {
                $user = User::where(['id' => $id, 'api_token' => $api_token])->firstOrFail();

                // Valida el password
                if ($request->has('password')) {
                    $validatePasswords = Validator::make($request->all(), [
                        'password' => 'required|min:5|confirmed',
                        'password_confirmation' => 'required|min:5'
                    ]);
                    if ($validatePasswords->fail()) {
                        $errors = $validatePasswords->errors();

                        $res['status'] = 0;
                        $res['mensaje'] = $errors->first();
                        return response()->json($res, 400);
                    } else {
                        $user->password = Hash::make($request->get('password'));
                    }
                }

                $user->name = $name;
                $user->email = $email;
                $user->phone = $phone;
                $user->save();

                $res['status'] = 1;
                $res['mensaje'] = "Tu perfil se actualizo correctamente";
                return response()->json($res, 201);
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario no encontrado";
            return response()->json($res, 404);
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
            return response()->json($res, 404);
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

            $res['status'] = 1;
            $res['mensaje'] = "Usuario encontrado";
            $res['user'] = $user;
            return response()->json($res, 200);
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario no encontrado";
            return response()->json($res, 404);
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
                    return response()->json($res, 200);
                } else {
                    $res['status'] = 0;
                    $res['mensaje'] = "Usuario o contraseña incorrecto";
                    return response()->json($res, 404);
                }
            }
        } catch (ModelNotFoundException $ex) {
            $res['status'] = 0;
            $res['mensaje'] = "Usuario o contraseña incorrecto";
            return response()->json($res, 404);
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
}