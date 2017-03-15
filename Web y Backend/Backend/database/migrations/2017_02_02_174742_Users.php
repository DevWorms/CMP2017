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
            $table->string('type', 30);
            $table->string('association', 40);
            $table->string('email');
            $table->string('password');
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
