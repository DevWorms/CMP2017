<?php

namespace App\Http\Controllers;

use App\File;
use App\User;
use Exception;
use Illuminate\Database\Eloquent\ModelNotFoundException;

class BannerController extends Controller {
    protected $destinationPath = "./files/";

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

    private function returnBanners($banners) {
        foreach ($banners as $banner) {
            unset($banner['is_banner']);
        }

        return $banners;
    }
}
