<?php

use Faker\Factory;
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
        $faker = Factory::create();

        try {
            \App\User::create([
                'name' => "Administrador",
                'last_name' => "",
                'type' => 0,
                'association' => 0,
                'email' => "contacto@congreso.digital",
                'password' => "$2y$10\$jyblYgb2MUdLD6JOCKCzu.6oVqhaVFl.HmLwTCeuaNDHo.UJr6Czy",
                'api_token' => 0
            ]);

            \App\User::create([
                'name' => "Valentin",
                'last_name' => "Granados",
                'type' => 1,
                'association' => 1,
                'email' => "vale@devworms.com",
                'password' => "$2y$10\$jyblYgb2MUdLD6JOCKCzu.6oVqhaVFl.HmLwTCeuaNDHo.UJr6Czy",
                'api_token' => 1
            ]);

            \App\User::create([
                'name' => "Salvador",
                'last_name' => "Munguia",
                'type' => 2,
                'association' => "",
                'email' => "smunguia@devworms.com",
                'password' => "$2y$10\$jyblYgb2MUdLD6JOCKCzu.6oVqhaVFl.HmLwTCeuaNDHo.UJr6Czy",
                'api_token' => 2
            ]);

            \App\User::create([
                'name' => "Richard",
                'last_name' => "VelRo",
                'type' => 3,
                'association' => 3,
                'email' => "richard@devworms.com",
                'password' => "$2y$10\$jyblYgb2MUdLD6JOCKCzu.6oVqhaVFl.HmLwTCeuaNDHo.UJr6Czy",
                'api_token' => 3
            ]);

            /*
             * Imágenes
             */
            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen1.png",
                'size' => 1000000
            ]);

            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen2.png",
                'size' => 1000000
            ]);

            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen3.png",
                'size' => 1000000
            ]);

            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen4.png",
                'size' => 1000000
            ]);

            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen5.png",
                'size' => 1000000
            ]);

            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen6.png",
                'size' => 1000000
            ]);

            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen7.png",
                'size' => 1000000
            ]);

            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen8.png",
                'size' => 1000000
            ]);

            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen9.png",
                'size' => 1000000
            ]);

            \App\File::create([
                'user_id' => 1,
                'url' => $faker->imageUrl(),
                'nombre' => "imagen10.png",
                'size' => 1000000
            ]);

            /*
             * El único PDF que voy a subir xD
             */
            \App\File::create([
                'user_id' => 1,
                'url' => "http://cmp.devworms.com/files/expositores/MapaRecinto.pdf",
                'nombre' => "MapaRecinto.pdf",
                'size' => 2000000
            ]);

            /*
             * Banners
             */
            for ($i = 0; $i < 5; $i++) {
                \App\File::create([
                    'user_id' => 1,
                    'url' => $faker->imageUrl(),
                    'nombre' => "imagen2" . $i . ".png",
                    'size' => 1000000,
                    'is_banner' => 1
                ]);
            }

            /*
             * Categorías
             */
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
                'nombre' => "Comidas / Conferencias",
            ]);

            \App\Categoria::create([
                'user_id' => 1,
                'nombre' => "E-Póster",
            ]);

            \App\Categoria::create([
                'user_id' => 1,
                'nombre' => "Cursos Pre-Congreso",
            ]);

            /*
             * Seeder Programa
             */
            \App\Programa::create([
                'nombre' => "Evento 1",
                'categoria_id' => 1,
                'lugar' => "Lugar 1",
                'recomendaciones' => "Te recomiendo una recomendación",
                'latitude' => 19.5032747,
                'longitude' => -99.1324364,
                'fecha' => "2017-06-05",
                'hora_inicio' => "10:00:00",
                'foto_id' => 1
            ]);

            \App\Programa::create([
                'nombre' => "Evento 2",
                'categoria_id' => 1,
                'lugar' => "Lugar 2",
                'recomendaciones' => "Te recomiendo una recomendación",
                'latitude' => 19.5032747,
                'longitude' => -99.1324364,
                'fecha' => "2017-06-05",
                'hora_inicio' => "10:30:00",
                'foto_id' => 2
            ]);

            \App\Programa::create([
                'nombre' => "Evento 3",
                'categoria_id' => 2,
                'lugar' => "Lugar 3",
                'recomendaciones' => "Te recomiendo una recomendación",
                'latitude' => 19.5032747,
                'longitude' => -99.1324364,
                'fecha' => "2017-06-06",
                'hora_inicio' => "11:00:00",
                'foto_id' => 3
            ]);

            \App\Programa::create([
                'nombre' => "Evento 4",
                'categoria_id' => 3,
                'lugar' => "Lugar 4",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-06",
                'hora_inicio' => "11:30:00",
                'foto_id' => 4
            ]);

            \App\Programa::create([
                'nombre' => "Evento 5",
                'categoria_id' => 4,
                'lugar' => "Lugar 5",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-07",
                'hora_inicio' => "12:00:00",
                'foto_id' => 5
            ]);

            \App\Programa::create([
                'nombre' => "Evento 6",
                'categoria_id' => 5,
                'lugar' => "Lugar 6",
                'recomendaciones' => "Te recomiendo una recomendación",
                'latitude' => 19.5032747,
                'longitude' => -99.1324364,
                'fecha' => "2017-06-07",
                'hora_inicio' => "12:30:00",
                'foto_id' => 6
            ]);

            \App\Programa::create([
                'nombre' => "Evento 7",
                'categoria_id' => 2,
                'lugar' => "Lugar 7",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-08",
                'hora_inicio' => "13:00:00",
                'foto_id' => 7
            ]);

            \App\Programa::create([
                'nombre' => "Evento 8",
                'categoria_id' => 3,
                'lugar' => "Lugar 7",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-08",
                'hora_inicio' => "13:30:00",
                'foto_id' => 8
            ]);

            \App\Programa::create([
                'nombre' => "Evento 9",
                'categoria_id' => 5,
                'lugar' => "Lugar 9",
                'recomendaciones' => "Te recomiendo una recomendación",
                'latitude' => 19.5032747,
                'longitude' => -99.1324364,
                'fecha' => "2017-06-09",
                'hora_inicio' => "14:00:00",
                'foto_id' => 9
            ]);

            \App\Programa::create([
                'nombre' => "Evento 10",
                'categoria_id' => 4,
                'lugar' => "Lugar 10",
                'recomendaciones' => "Te recomiendo una recomendación",
                'latitude' => 19.5032747,
                'longitude' => -99.1324364,
                'fecha' => "2017-06-09",
                'hora_inicio' => "14:30:00",
                'foto_id' => 10
            ]);

            /*
             * Eventos de Acompañantes
             */
            \App\Programa::create([
                'nombre' => "Evento de acompañantes 1",
                'lugar' => "Lugar 10",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-05",
                'hora_inicio' => "15:00:00",
                'type' => 2,
                'foto_id' => 1
            ]);

            \App\Programa::create([
                'nombre' => "Evento de acompañantes 2",
                'lugar' => "Lugar 11",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-05",
                'hora_inicio' => "15:30:00",
                'type' => 2,
                'foto_id' => 2
            ]);

            \App\Programa::create([
                'nombre' => "Evento de acompañantes 3",
                'lugar' => "Lugar 12",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-06",
                'hora_inicio' => "16:00:00",
                'type' => 2,
                'foto_id' => 3
            ]);

            \App\Programa::create([
                'nombre' => "Evento de acompañantes 4",
                'lugar' => "Lugar 13",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-06",
                'hora_inicio' => "16:30:00",
                'type' => 2,
                'foto_id' => 4
            ]);

            \App\Programa::create([
                'nombre' => "Evento de acompañantes 5",
                'lugar' => "Lugar 14",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-07",
                'hora_inicio' => "17:00:00",
                'type' => 2,
                'foto_id' => 5
            ]);

            \App\Programa::create([
                'nombre' => "Evento de acompañantes 6",
                'lugar' => "Lugar 15",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-07",
                'hora_inicio' => "17:30:00",
                'type' => 2,
                'foto_id' => 6
            ]);

            \App\Programa::create([
                'nombre' => "Evento de acompañantes 7",
                'lugar' => "Lugar 16",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-08",
                'hora_inicio' => "18:00:00",
                'type' => 2,
                'foto_id' => 7
            ]);

            \App\Programa::create([
                'nombre' => "Evento de acompañantes 8",
                'lugar' => "Lugar 17",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-08",
                'hora_inicio' => "18:30:00",
                'type' => 2,
                'foto_id' => 8
            ]);

            \App\Programa::create([
                'nombre' => "Evento de acompañantes 9",
                'lugar' => "Lugar 18",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-09",
                'hora_inicio' => "19:00:00",
                'type' => 2,
                'foto_id' => 9
            ]);

            \App\Programa::create([
                'nombre' => "Evento de acompañantes 10",
                'lugar' => "Lugar 19",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-09",
                'hora_inicio' => "19:30:00",
                'type' => 2,
                'foto_id' => 10
            ]);

            /*
             * Eventos Sociales y Deportivos
             */
            \App\Programa::create([
                'nombre' => "Evento social-deportivo 1",
                'lugar' => "Lugar 20",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-05",
                'hora_inicio' => "10:00:00",
                'type' => 3,
                'foto_id' => 1
            ]);

            \App\Programa::create([
                'nombre' => "Evento social-deportivo 2",
                'lugar' => "Lugar 21",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-05",
                'hora_inicio' => "11:00:00",
                'type' => 3,
                'foto_id' => 2
            ]);

            \App\Programa::create([
                'nombre' => "Evento social-deportivo 3",
                'lugar' => "Lugar 22",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-06",
                'hora_inicio' => "12:00:00",
                'type' => 3,
                'foto_id' => 3
            ]);

            \App\Programa::create([
                'nombre' => "Evento social-deportivo 4",
                'lugar' => "Lugar 23",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-06",
                'hora_inicio' => "13:00:00",
                'type' => 3,
                'foto_id' => 4
            ]);

            \App\Programa::create([
                'nombre' => "Evento social-deportivo 5",
                'lugar' => "Lugar 24",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-07",
                'hora_inicio' => "14:00:00",
                'type' => 3,
                'foto_id' => 5
            ]);

            \App\Programa::create([
                'nombre' => "Evento social-deportivo 6",
                'lugar' => "Lugar 25",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-07",
                'hora_inicio' => "15:00:00",
                'type' => 3,
                'foto_id' => 6
            ]);

            \App\Programa::create([
                'nombre' => "Evento social-deportivo 7",
                'lugar' => "Lugar 26",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-08",
                'hora_inicio' => "16:00:00",
                'type' => 3,
                'foto_id' => 7
            ]);

            \App\Programa::create([
                'nombre' => "Evento social-deportivo 8",
                'lugar' => "Lugar 27",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-08",
                'hora_inicio' => "17:00:00",
                'type' => 3,
                'foto_id' => 8
            ]);

            \App\Programa::create([
                'nombre' => "Evento social-deportivo 9",
                'lugar' => "Lugar 28",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-09",
                'hora_inicio' => "18:00:00",
                'type' => 3,
                'foto_id' => 9
            ]);

            \App\Programa::create([
                'nombre' => "Evento social-deportivo 10",
                'lugar' => "Lugar 29",
                'recomendaciones' => "Te recomiendo una recomendación",
                'fecha' => "2017-06-09",
                'hora_inicio' => "19:00:00",
                'type' => 3,
                'foto_id' => 10
            ]);

            /*
             * Expositores
             */
            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 1 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 1,
                'pdf_file' => 11,
                'logo_file' => 1
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 2 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 2,
                'pdf_file' => 11,
                'logo_file' => 2
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 3 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 3,
                'pdf_file' => 11,
                'logo_file' => 3
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 4 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 4,
                'pdf_file' => 11,
                'logo_file' => 4
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 5 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 5,
                'pdf_file' => 11,
                'logo_file' => 5
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 6 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 6,
                'pdf_file' => 11,
                'logo_file' => 6
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 7 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 7,
                'pdf_file' => 11,
                'logo_file' => 7
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 8 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 8,
                'pdf_file' => 11,
                'logo_file' => 8
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 9 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 9,
                'pdf_file' => 11,
                'logo_file' => 9
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Expositor 10 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'stand' => 10,
                'pdf_file' => 11,
                'logo_file' => 10
            ]);

            /*
             * Patrocinadores
             */

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 1 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 1,
                'stand' => 1,
                'is_expositor' => 0
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 2 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 2,
                'stand' => 1,
                'is_expositor' => 0
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 3 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 3,
                'stand' => 3,
                'is_expositor' => 0
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 4 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 4,
                'stand' => 3,
                'is_expositor' => 0
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 5 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 5,
                'stand' => 7,
                'is_expositor' => 0
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 6 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 6,
                'stand' => 7,
                'is_expositor' => 0
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 7 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 7,
                'stand' => 17,
                'is_expositor' => 0
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 8 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 8,
                'stand' => 17,
                'is_expositor' => 0
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 9 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 9,
                'stand' => 10,
                'is_expositor' => 0
            ]);

            \App\Expositor::create([
                'user_id' => 1,
                'url' => $faker->url,
                'nombre' => "Patrocinador 10 " . $faker->company,
                'email' => $faker->email,
                'telefono' => $faker->phoneNumber,
                'acerca' => $faker->paragraph,
                'tipo' => "Op",
                'pdf_file' => 11,
                'logo_file' => 10,
                'stand' => 10,
                'is_expositor' => 0
            ]);

            for ($i = 0; $i < 5; $i++) {
                /*
                 * Agrega 5 eventos randoms a la agenda de todos
                 */
                \App\MisEventos::create([
                    'user_id' => 1,
                    'evento_id' => rand(1, 30)
                ]);

                \App\MisEventos::create([
                    'user_id' => 2,
                    'evento_id' => rand(1, 30)
                ]);

                \App\MisEventos::create([
                    'user_id' => 3,
                    'evento_id' => rand(1, 30)
                ]);

                \App\MisEventos::create([
                    'user_id' => 4,
                    'evento_id' => rand(1, 30)
                ]);

                /*
                 * 5 Expositores favoritos para cada user
                 */
                \App\MisExpositores::create([
                    'user_id' => 1,
                    'expositor_id' => rand(1, 20)
                ]);

                \App\MisExpositores::create([
                    'user_id' => 2,
                    'expositor_id' => rand(1, 20)
                ]);

                \App\MisExpositores::create([
                    'user_id' => 3,
                    'expositor_id' => rand(1, 20)
                ]);

                \App\MisExpositores::create([
                    'user_id' => 4,
                    'expositor_id' => rand(1, 20)
                ]);

                /*
                 * Notificaciones de los usuarios
                 */
                \App\Notificacion::create([
                    'user_id' => 1,
                    'notificacion' => "Esto es una notificación " . $i,
                    'leido' => rand(0, 1)
                ]);

                \App\Notificacion::create([
                    'user_id' => 2,
                    'notificacion' => "Esto es una notificación " . $i,
                    'leido' => rand(0, 1)
                ]);

                \App\Notificacion::create([
                    'user_id' => 3,
                    'notificacion' => "Esto es una notificación " . $i,
                    'leido' => rand(0, 1)
                ]);

                \App\Notificacion::create([
                    'user_id' => 4,
                    'notificacion' => "Esto es una notificación " . $i,
                    'leido' => rand(0, 1)
                ]);

                /*
                 * Notificaciones de los usuarios
                 */
                \App\Notificacion::create([
                    'user_id' => 1,
                    'notificacion' => "Esta es la notificación " . $i,
                    'leido' => rand(0, 1)
                ]);

                \App\Notificacion::create([
                    'user_id' => 2,
                    'notificacion' => "Esta es la notificación " . $i,
                    'leido' => rand(0, 1)
                ]);

                \App\Notificacion::create([
                    'user_id' => 3,
                    'notificacion' => "Esta es la notificación " . $i,
                    'leido' => rand(0, 1)
                ]);

                \App\Notificacion::create([
                    'user_id' => 4,
                    'notificacion' => "Esta es la notificación " . $i,
                    'leido' => rand(0, 1)
                ]);
            }

            for ($i = 1; $i < 7; $i++) {
                \App\PueblaSitios::create([
                    'user_id' => 1,
                    'titulo' => $faker->city,
                    'maps_link' => $faker->url,
                    'descripcion' => $faker->paragraph,
                    'imagen_id' => $i,
                    'url' => $faker->url
                ]);

                \App\PueblaTelefonos::create([
                    'user_id' => 1,
                    'titulo' => $faker->company,
                    'imagen_id' => 1,
                    'telefono' => $faker->phoneNumber
                ]);
            }

            \App\Ruta::create([
                'user_id' => 1,
                'pdf_file' => 11,
                'titulo' => "Info de tu ruta 1",
                'descripcion' => "Al fondo a la derecha"
            ]);

            \App\Ruta::create([
                'user_id' => 1,
                'pdf_file' => 11,
                'titulo' => "Info de tu ruta 2",
                'descripcion' => "Al fondo a la derecha"
            ]);

            \App\Ruta::create([
                'user_id' => 1,
                'pdf_file' => 11,
                'titulo' => "Info de tu ruta 3",
                'descripcion' => "Al fondo a la derecha"
            ]);

            \App\PueblaMapa::create([
                'user_id' => 1,
                'file_id' => 11
            ]);

            // Estantes - mapa expositores
            // Primera fila
            $y = 542;
            $id = 1;
            for ($k = 0; $k < 2; $k++) {
                $x = 144;
                $espacio = 39;

                for ($j = 0; $j < 3; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 29;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 605;
                $espacio = 40;
                for ($j = 3; $j < 6; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1064;
                $espacio = 40;
                for ($j = 6; $j < 9; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1526;
                $espacio = 40;
                for ($j = 9; $j < 13; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 2138;
                $espacio = 40;
                for ($j = 13; $j < 15; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }
                $y += 26;
            }

            // Fila 1.5
            $y = 594;
            $x = 2288;
            for ($i = 0; $i < 4; $i++) {
                \App\MapaExpositores::create([
                    'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                ]);
                $x += 28;
                $id++;
            }

            // Segunda fila
            $y = 636;
            for ($k = 0; $k < 2; $k++) {
                $x = 144;
                $espacio = 39;

                for ($j = 0; $j < 3; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 29;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 605;
                $espacio = 40;
                for ($j = 3; $j < 6; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1064;
                $espacio = 40;
                for ($j = 6; $j < 9; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1526;
                $espacio = 40;
                for ($j = 9; $j < 13; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 2138;
                $espacio = 40;
                for ($j = 13; $j < 14; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }
                $y += 26;
            }

            $y = 626;
            $x = 2288;
            for ($i = 0; $i < 4; $i++) {
                \App\MapaExpositores::create([
                    'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                ]);
                $x += 28;
                $id++;
            }

            $y = 652;
            $x = 2288;
            for ($i = 0; $i < 4; $i++) {
                \App\MapaExpositores::create([
                    'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                ]);
                $x += 28;
                $id++;
            }

            // Tercera fila
            $y = 734;
            for ($k = 0; $k < 2; $k++) {
                $x = 144;
                $espacio = 39;

                for ($j = 0; $j < 3; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 29;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 605;
                $espacio = 40;
                for ($j = 3; $j < 6; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1064;
                $espacio = 40;
                for ($j = 6; $j < 9; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1526;
                $espacio = 40;
                for ($j = 9; $j < 13; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 2138;
                $espacio = 40;
                for ($j = 13; $j < 15; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }
                $y += 26;
            }

            // cuarta Fila
            $y = 830;
            for ($k = 0; $k < 4; $k++) {
                $x = 173;
                $espacio = 39;

                for ($i = 0; $i < 2; $i++) {
                    \App\MapaExpositores::create([
                        'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                    ]);
                    $x += 29;
                    $id++;
                }
                $x += 70;

                for ($j = 3; $j < 6; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 605;
                $espacio = 40;
                for ($j = 3; $j < 6; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1064;
                $espacio = 40;
                for ($j = 6; $j < 9; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1526;
                $espacio = 40;
                for ($j = 9; $j < 13; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 2138;
                $espacio = 40;
                for ($j = 13; $j < 15; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }
                $y += 30;
            }

            // Quinta Fila
            $y = 1005;
            for ($k = 0; $k < 4; $k++) {
                $x = 173;
                $espacio = 39;

                for ($i = 0; $i < 2; $i++) {
                    \App\MapaExpositores::create([
                        'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                    ]);
                    $x += 29;
                    $id++;
                }
                $x += 70;

                for ($j = 3; $j < 6; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 605;
                $espacio = 40;
                for ($j = 3; $j < 6; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1064;
                $espacio = 40;
                for ($j = 6; $j < 9; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1526;
                $espacio = 40;
                for ($j = 9; $j < 13; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 2138;
                $espacio = 40;
                for ($j = 13; $j < 15; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }
                $y += 30;
            }

            // Primera fila
            $y = 1159;
            for ($k = 0; $k < 2; $k++) {
                $x = 144;
                $espacio = 39;

                for ($j = 0; $j < 3; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 29;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 605;
                $espacio = 40;
                for ($j = 3; $j < 6; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1064;
                $espacio = 40;
                for ($j = 6; $j < 9; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1526;
                $espacio = 40;
                for ($j = 9; $j < 13; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }
                $y += 26;
            }

            // Primera fila
            $y = 1258;
            for ($k = 0; $k < 2; $k++) {
                $x = 144;
                $espacio = 39;

                for ($j = 0; $j < 3; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 29;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 605;
                $espacio = 40;
                for ($j = 3; $j < 6; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1064;
                $espacio = 40;
                for ($j = 6; $j < 9; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $x = 1526;
                $espacio = 40;
                for ($j = 9; $j < 12; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 28;
                        $id++;
                    }
                    $x += $espacio;
                }

                $y += 26;
            }

            $y = 1218;
            for ($k = 0; $k < 2; $k++) {
                $x = 1982;
                $espacio = 39;

                for ($j = 0; $j < 1; $j++) {
                    for ($i = 0; $i < 4; $i++) {
                        \App\MapaExpositores::create([
                            'coords' => $x . ',' . $y . ',' . ($x + 28) . ',' . ($y+26)
                        ]);
                        $x += 29;
                        $id++;
                    }
                    $x += $espacio;
                }
                $y += 28;
            }

            $x = -1850;
            for ($i = 1; $i < 113; $i++) {
                $x -= 600;
                for ($j = 1; $j < 9; $j++) {
                    \App\MapaExpositores::where('id', $i)->update([
                        'coords' => $x . ',-454'
                    ]);
                }
            }
        } catch (Exception $ex) {
            echo $ex->getMessage();
        }
    }
}
