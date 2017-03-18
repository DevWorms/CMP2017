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

        \App\Programa::create([
            'nombre' => "Evento 1",
            'categoria_id' => 1,
            'lugar' => "Lugar 1",
            'recomendaciones' => "Te recomiendo una recomendación",
            'latitude' => 19.5032747,
            'longitude' => -99.1324364,
            'fecha' => "2017-03-20"
        ]);

        \App\Programa::create([
            'nombre' => "Evento 2",
            'categoria_id' => 1,
            'lugar' => "Lugar 2",
            'recomendaciones' => "Te recomiendo una recomendación",
            'latitude' => 19.5032747,
            'longitude' => -99.1324364,
            'fecha' => "2017-03-20"
        ]);

        \App\Programa::create([
            'nombre' => "Evento 3",
            'categoria_id' => 2,
            'lugar' => "Lugar 3",
            'recomendaciones' => "Te recomiendo una recomendación",
            'latitude' => 19.5032747,
            'longitude' => -99.1324364,
            'fecha' => "2017-03-19"
        ]);

        \App\Programa::create([
            'nombre' => "Evento 4",
            'categoria_id' => 3,
            'lugar' => "Lugar 4",
            'recomendaciones' => "Te recomiendo una recomendación",
            'fecha' => "2017-03-18"
        ]);

        \App\Programa::create([
            'nombre' => "Evento 5",
            'categoria_id' => 4,
            'lugar' => "Lugar 5",
            'recomendaciones' => "Te recomiendo una recomendación",
            'fecha' => "2017-03-21"
        ]);

        \App\Programa::create([
            'nombre' => "Evento 6",
            'categoria_id' => 5,
            'lugar' => "Lugar 6",
            'recomendaciones' => "Te recomiendo una recomendación",
            'latitude' => 19.5032747,
            'longitude' => -99.1324364,
            'fecha' => "2017-03-22"
        ]);

        \App\Programa::create([
            'nombre' => "Evento 7",
            'categoria_id' => 6,
            'lugar' => "Lugar 7",
            'recomendaciones' => "Te recomiendo una recomendación",
            'fecha' => "2017-03-22"
        ]);

        \App\Programa::create([
            'nombre' => "Evento 8",
            'categoria_id' => 6,
            'lugar' => "Lugar 7",
            'recomendaciones' => "Te recomiendo una recomendación",
            'fecha' => "2017-03-23"
        ]);

        \App\Programa::create([
            'nombre' => "Evento 9",
            'categoria_id' => 5,
            'lugar' => "Lugar 9",
            'recomendaciones' => "Te recomiendo una recomendación",
            'latitude' => 19.5032747,
            'longitude' => -99.1324364,
            'fecha' => "2017-03-23"
        ]);

        \App\Programa::create([
            'nombre' => "Evento 10",
            'categoria_id' => 4,
            'lugar' => "Lugar 10",
            'recomendaciones' => "Te recomiendo una recomendación",
            'latitude' => 19.5032747,
            'longitude' => -99.1324364,
            'fecha' => "2017-03-24"
        ]);
    }
}
