<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 13.03.17
 * Time: 15:24
 */

namespace App;

use Illuminate\Database\Eloquent\Model;

class Programa extends Model
{
    protected $table = "eventos";
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'id', 'categoria_id', 'foto_id', 'user_id', 'nombre', 'lugar', 'recomendaciones', 'latitude', 'longitude',
        'fecha', 'hora_inicio', 'hora_fin'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    public function categoria() {
        return $this->hasOne('App\Categoria', 'categoria_id', 'id');
    }

    public function foto() {
        return $this->hasOne('App\File', 'foto_id', 'id');
    }
}