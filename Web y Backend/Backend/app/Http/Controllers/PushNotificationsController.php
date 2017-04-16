<?php
/**
 * Created by PhpStorm.
 * User: rk521
 * Date: 15/04/17
 * Time: 10:25 PM
 */

namespace App\Http\Controllers;


use App\Notificacion;
use App\User;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Lab123\AwsSns\Messages\AwsSnsMessage;

class PushNotificationsController extends Controller {

    public function toAwsSnsTopic() {
        try {
            $a = new AwsSnsMessage();
            $a->message('Prueba Push notification android-backend')->topicArn('arn:aws:sns:us-east-1:291812403953:app/GCM/cmp_MOBILEHUB_41832316');

            print_r($a);
        } catch (\Exception $ex) {
            Log::info('Error: ' . $ex->getMessage());
        }
    }

    public function save(Request $request) {
        try {
            $pattern = '
/
\{              # { character
    (?:         # non-capturing group
        [^{}]   # anything that is not a { or }
        |       # OR
        (?R)    # recurses the entire pattern
    )*          # previous group zero or more times
\}              # } character
/x
';
            preg_match_all($pattern, $request, $matches);
            $body = json_decode($matches[0][0]);

            if (!empty($body->Message) && $body->Type == "Notification") {
                $dt = Carbon::parse($body->Timestamp);
                $users = User::all();
                foreach ($users as $user) {
                    Notificacion::create([
                        'user_id' => $user->id,
                        'asunto' => $body->Subject,
                        'notificacion' => $body->Message,
                        'saved_at' => $dt
                    ]);
                }
            }

        } catch (\Exception $ex) {
            Log::info('Error al almacenar notificación: ' . $ex->getMessage());
        }
    }
}