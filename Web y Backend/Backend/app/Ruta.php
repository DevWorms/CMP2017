<?php
/**
 * Created by PhpStorm.
 * User: @HackeaMesta
 * Date: 13.03.17
 * Time: 15:24
 */

namespace App;

use Illuminate\Database\Eloquent\Model;

class Ruta extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'rutas';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'id', 'user_id', 'user_id_asignado', 'pdf_file', 'titulo', 'descripcion'
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
}