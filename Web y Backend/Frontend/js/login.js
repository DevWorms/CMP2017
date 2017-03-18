/**
 * Created by chemasmas on 17/03/17.
 */

//baseUrl = "http://cmp.devworms.com"
baseUrl = "http://localhost:2222"

$("#login").click(
    function (){
        console.log("ok");

        usuario = $("#id_usuario").val();
        pass = $("#contrasena").val();

        $.ajax(
            {
                type: 'POST',
                url: baseUrl+"/user/login",
                crossDomain: true,
                data: {
                    user:usuario,
                    password:pass
                },
                succes: function(response){
                    response = JSON.parse(response);
                    if (response.status == 1) {
                        //Manejo de sesion?
                        setTimeout(' window.location.href = "'+ baseUrl + "/programa.php" + '";', 1000);
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
