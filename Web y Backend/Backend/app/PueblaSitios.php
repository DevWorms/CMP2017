<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 13.03.17
 * Time: 15:24
 */

namespace App;

use Illuminate\Database\Eloquent\Model;

class PueblaSitios extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'puebla_sitios';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'id', 'user_id', 'titulo', 'maps_link', 'descripcion', 'imagen_id', 'url'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = ['updated_at'];

    public function imagen() {
        return $this->hasOne('App\File', 'id', 'imagen_id');
    }
}