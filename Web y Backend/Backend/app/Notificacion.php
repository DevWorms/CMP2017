<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 13.03.17
 * Time: 15:24
 */

namespace App;

use Illuminate\Database\Eloquent\Model;

class Notificacion extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'notificaciones';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'id', 'user_id', 'notificacion', 'leido', 'asunto', 'saved_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = ['updated_at'];
}