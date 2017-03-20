/**
 * Created by chemasmas on 17/03/17.
 */

//baseUrl = "http://cmp.devworms.com"
baseUrl = "http://localhost:1111"
authDir = "/sesion/iniciar.php"

$("#login").click(
    function (){
        usuario = $("#id_usuario").val();
        pass = $("#contrasena").val();
        //console.log(usuario);
        $.ajax(
            {
                type: 'POST',
                url: baseUrl+"/api/user/login",
                crossDomain: true,
                data: {
                    user:usuario,
                    password:pass
                },
                success: function(response){
                    //console.log(response);
                    //response = JSON.parse(response);
                    if (response.status == 1) {
                        enviarDatos(response);
                        //setTimeout(' window.location.href = "'+ baseUrl + "/programa.php" + '";', 1000);
                    }
                    else {
                        //CAso de error
                        console.log("eror");
                        console.log(response);
                    }
                }
            }
        );
    }
);

function enviarDatos(datos)
{
    $.ajax({
        type: 'POST',
        url: authDir,
        crossDomain: true,
        data: {
            user:datos,
        },
        success:function(response){
            response = JSON.parse(response);
            //console.log(response);

            setTimeout(' window.location.href = "'+ response.url + '";', 1000);
        }
    });
}