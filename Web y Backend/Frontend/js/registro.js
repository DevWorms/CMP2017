/**
 * Created by chemasmas on 17/03/17.
 */


$("#login").click(
    function (){
        console.log("ok");


        pass = $("#contrasena").val();
        //TODO Nombres de los campos
        nombre = "";
        apellido = "";
        mail = "";
        clave ="";
        tipo = "";
        asociacion = ""


        $.ajax(
            {
                type: 'POST',
                url: baseUrl+"/user/signup",
                crossDomain: true,
                data: {
                    name:nombre,
                    last_name:apellido,
                    email:mail,
                    password:pass,
                    clave:clave,
                    type:tipo,
                    association: asociacion

                },
                succes: function(response){
                    response = JSON.parse(response);
                    if (response.status == 1) {
                        //Manejo de sesion?
                        setTimeout(' window.location.href = "'+ baseUrl + "/" + '";', 1000);
                    }
                    else {
                        //CAso de error
                        console.log(response)
                    }
                }
            }
        );
    }
);