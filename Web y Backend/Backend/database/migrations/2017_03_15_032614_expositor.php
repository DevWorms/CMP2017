<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Expositor extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('expositores', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedBigInteger('user_id');
            $table->unsignedInteger('pdf_file')->nullable();
            $table->unsignedInteger('logo_file')->nullable();
            $table->integer('stand')->nullable();
            $table->string('tipo')->nullable();
            $table->string('nombre');
            $table->string('email');
            $table->string('url');
            $table->text('maps_url')->nullable();
            $table->string('telefono');
            $table->text('acerca');
            $table->tinyInteger('is_expositor')->default(1);
            $table->string('latitude')->nullable();
            $table->string('longitude')->nullable();
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
        Schema::drop('expositores');
    }
}
