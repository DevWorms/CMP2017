<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Users extends Migration {
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up() {
        Schema::create('users', function (Blueprint $table) {
            $table->increments('id');
            $table->string('name', 90);
            $table->string('last_name', 90);
            /*
             * Tipos de usuario
             *
             * 1 - Congresista
             * 2 - Expositor
             * 3 - Estudiante
             * 4 - Acompañante
             */
            $table->tinyInteger('type')->nullable();
            /*
             * Asociación
             *
             * 1 - AIPM
             * 2 - CIPM
             * 3 - AMGE
             * 4 - AMGP
             * 5 - SPE / México
             */
            $table->tinyInteger('association')->nullable();
            $table->string('email');
            $table->string('password');
            $table->string('clave')->nullable();
            $table->string('api_token', 32);
            $table->string('oauth_token')->nullable();
            $table->string('tmp_token')->nullable();
            $table->dateTime('expires')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down() {
        Schema::drop('users');
    }
}
