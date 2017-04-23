<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class MapaExpositores extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('mapa_expositores', function (Blueprint $table) {
            $table->increments('id');
            /*
             * 1 = Esta libre
             * 0 = Esta ocupado
             */
            $table->tinyInteger('available')->default(1);
            $table->string('coords');
            $table->unsignedBigInteger('expositor_id')->nullable();
            $table->string('color')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::drop('mapa_expositores');
    }
}
