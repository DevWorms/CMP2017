<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 13.03.17
 * Time: 15:24
 */

namespace App;

use Illuminate\Database\Eloquent\Model;

class MapaExpositores extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'mapa_expositores';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'id', 'available', 'coords', 'expositor_id', 'color'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = ['updated_at'];

    public function expositor() {
        return $this->hasOne('App\Expositor', 'id', 'expositor_id');
    }
}