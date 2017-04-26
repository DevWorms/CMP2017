<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 13.03.17
 * Time: 15:24
 */

namespace App;

use Illuminate\Database\Eloquent\Model;

class Expositor extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'expositores';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'id', 'user_id', 'url', 'nombre', 'pdf_file', 'logo_file', 'stand', 'tipo', 'email', 'telefono', 'acerca',
        'latitude', 'longitude', 'is_expositor', 'maps_url'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = ['updated_at'];

    public function pdf() {
        return $this->hasOne('App\File', 'id', 'pdf_file');
    }

    public function logo() {
        return $this->hasOne('App\File', 'id', 'logo_file');
    }

    public function estantes() {
        return $this->hasMany('App\MapaExpositores', 'expositor_id', 'id');
    }
}