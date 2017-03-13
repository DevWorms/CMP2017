<?php

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        \App\User::create([
            'name' => "Ricardo",
            'last_name' => "Osorio",
            'type' => "Visitante",
            'association' => "Asociación 1",
            'email' => "ricardo.0x7@gmail.com",
            'password' => "$2y$10\$jyblYgb2MUdLD6JOCKCzu.6oVqhaVFl.HmLwTCeuaNDHo.UJr6Czy", // "123456"
            'api_token' => 0
        ]);

        \App\User::create([
            'name' => "Valentin",
            'last_name' => "Granados",
            'type' => "Trabajador de Pemex",
            'association' => "Asociación 2",
            'email' => "vale@devworms.com",
            'password' => "$2y$10\$jyblYgb2MUdLD6JOCKCzu.6oVqhaVFl.HmLwTCeuaNDHo.UJr6Czy", // "123456"
            'api_token' => 1
        ]);

        \App\User::create([
            'name' => "Salvador",
            'last_name' => "Munguia",
            'type' => "Estudiante",
            'association' => "Asociación 3",
            'email' => "salva@devworms.com",
            'password' => "$2y$10\$jyblYgb2MUdLD6JOCKCzu.6oVqhaVFl.HmLwTCeuaNDHo.UJr6Czy", // "123456"
            'api_token' => 2
        ]);

        \App\User::create([
            'name' => "Richard",
            'last_name' => "VelRo",
            'type' => "Staff",
            'association' => "Asociación 4",
            'email' => "richard@devworms.com",
            'password' => "$2y$10\$jyblYgb2MUdLD6JOCKCzu.6oVqhaVFl.HmLwTCeuaNDHo.UJr6Czy", // "123456"
            'api_token' => 3
        ]);

        \App\Categoria::create([
            'user_id' => 1,
            'nombre' => "Sesiones Técnicas",
        ]);

        \App\Categoria::create([
            'user_id' => 1,
            'nombre' => "Plenarias",
        ]);

        \App\Categoria::create([
            'user_id' => 1,
            'nombre' => "Comidas",
        ]);

        \App\Categoria::create([
            'user_id' => 1,
            'nombre' => "Conferencias",
        ]);

        \App\Categoria::create([
            'user_id' => 1,
            'nombre' => "E-Póster",
        ]);

        \App\Categoria::create([
            'user_id' => 1,
            'nombre' => "Cursos Pre-Congreso",
        ]);
    }
}
