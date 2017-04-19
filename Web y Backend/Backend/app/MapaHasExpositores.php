<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 13.03.17
 * Time: 15:24
 */

namespace App;

use Illuminate\Database\Eloquent\Model;

class MapaHasExpositores extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'mapa_has_expositor';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'id', 'estante_id', 'expositor_id'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = ['updated_at'];

    public function expositor() {
        return $this->belongsTo('App\Expositor', 'id', 'expositor_id');
    }

    public function estantes() {
        return $this->hasMany('App\MapaExpositores', 'id', 'estante_id');
    }
}