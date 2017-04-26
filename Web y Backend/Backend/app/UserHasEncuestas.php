<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 13.03.17
 * Time: 15:24
 */

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserHasEncuestas extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'user_has_encuestas';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'id', 'encuesta_id', 'user_id', 'resuelta'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = ['updated_at'];

    public function encuesta() {
        return $this->hasOne('App\Expositor', 'id', 'expositor_id');
    }
}