<?php
/**
 * Created by PhpStorm.
 * User: rk521
 * Date: 18.03.17
 * Time: 13:43
 */

session_start();
//echo 'var API_URL = "http://cmp.devworms.com/api/";';
echo 'var API_URL = "http://localhost:8001/api/";';
echo 'var user_id = "' . $_SESSION["user_id"] . '";';
echo 'var api_key = "' . $_SESSION["api_key"] . '";';
echo "
    $(document).on('ready', function () {
        $('#hora_inicio, #hora_fin').timepicker({
            onSelect: function(dateText, inst)
                {
                    var today = moment().add(1, 'd').format('YYYY-MM-DD');
                    var time = moment().format('HH:ma:ss');
                    if ($(\"#fecha2\").val() == today) {
                        $('#hora').timepicker('option', 'minTime', time);
                    } else {
                        $('#hora').timepicker('option', 'minTime', null);
                    }
                }
            });
        });
";