/**
 * Created by chemasmas on 19/03/17.
 */
baseUrl = "http://localhost:1111"
authDir = "/sesion/iniciar.php"



/* Selector */
$("[id^=guardar]").click( //Checar el evento
    function (){
        event.preventDefault();
        ruta  = $(this).attr("ruta");
        console.log(ruta);

        titulo = $("#titulo"+ruta).val();
        archivo = $("#archivo"+ruta)[0].files[0] ;
        desc = $("#desc"+ruta).val();
        key = $(this).attr("key");
        usr = $(this).attr("user");
        formData = new FormData($("#ruta"+ruta)[0]);
        formData.append("user_id",usr);
        formData.append("api_key",key);
        formData.append("titulo",titulo);
        formData.append("descripcion",desc);
        formData.append("archivo",archivo);
        formData.append("user_asignado",usr); //Lo asigna a si mismo

        console.log(formData);

        /*
        console.log(titulo);
        console.log(archivo);
        console.log(desc);
        console.log(key);
        console.log(usr);
        */
        $.ajax({
                type: 'POST',
                url: baseUrl+"/api/ruta/create",
                crossDomain: true,
                processData: false,  // Important!
                contentType: false,
                enctype: 'multipart/form-data',

            /*
            data: {
                user_id:usr,
                api_key:key,
                titulo:titulo,
                descripcion: desc,
                archivo: archivo
            },
            */
                data:formData,
                success: function(response){
                    console.log(response);
                    //response = JSON.parse(response);
                    /*if (response.status == 1) {
                        //enviarDatos(response);
                        //setTimeout(' window.location.href = "'+ baseUrl + "/programa.php" + '";', 1000);
                    }
                    else {
                        //CAso de error
                        console.log("eror");
                        console.log(response);
                    }*/
                }
            }
        );
    }
);