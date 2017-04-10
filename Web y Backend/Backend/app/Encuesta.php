<?php

namespace App;

use Illuminate\Auth\Authenticatable;
use Laravel\Lumen\Auth\Authorizable;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;

class Encuesta extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'encuestas';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'id', 'file_sm', 'file_xl'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = ['updated_at'];

    public function preguntas() {
        return $this->hasMany('App\Pregunta', 'encuesta_id', 'id');
    }

    public function filesm() {
        return $this->hasOne('App\File', 'id', 'file_sm');
    }

    public function filexl() {
        return $this->hasOne('App\File', 'id', 'file_xl');
    }
}
