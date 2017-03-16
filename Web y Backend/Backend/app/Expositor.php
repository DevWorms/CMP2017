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
        'id', 'user_id', 'url', 'nombre', 'pdf_file', 'logo_file', 'stand', 'email', 'telefono', 'acerca', 'latitude', 'longitude'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];
}