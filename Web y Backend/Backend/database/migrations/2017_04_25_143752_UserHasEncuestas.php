<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class UserHasEncuestas extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('user_has_encuestas', function (Blueprint $table) {
            $table->increments('id');
            $table->unsignedBigInteger('encuesta_id');
            $table->unsignedBigInteger('user_id');
            $table->tinyInteger('resuelta')->default(0);
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
        Schema::drop('user_has_encuestas');
    }
}
