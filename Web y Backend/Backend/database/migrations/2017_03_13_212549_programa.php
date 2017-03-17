<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Programa extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('eventos', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedBigInteger('categoria_id')->nullable();
            $table->unsignedBigInteger('user_id');
            $table->unsignedBigInteger('foto_id')->nullable();
            $table->string('nombre');
            $table->string('lugar', 90);
            $table->text('recomendaciones');
            $table->string('latitude', 40)->nullable();
            $table->string('longitude', 40)->nullable();
            $table->date('fecha');
            $table->time('hora_inicio')->nullable();
            $table->time('hora_fin')->nullable();
            /*
             * 1 = Programa (Evento)
             * 2 = Eventos acompañantes
             * 3 = Eventos sociales y deportivos
             */
            $table->tinyInteger('type')->default(1);
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
        Schema::drop('eventos');
    }
}
