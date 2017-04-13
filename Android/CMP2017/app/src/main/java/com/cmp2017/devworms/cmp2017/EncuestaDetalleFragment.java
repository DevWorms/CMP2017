package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class EncuestaDetalleFragment  extends Fragment implements View.OnClickListener {

    String apiKey;
    String userId;
    int idEncuesta;
    String response;
    TextView preguntaUno;
    TextView preguntaDos;
    TextView preguntaTres;
    ImageView imagenEvento;

    String strPreguntaUno;
    String strPreguntaDos;
    String strPreguntaTres;
    Bitmap imagen;
    RatingBar ratingUno;
    RatingBar ratingDos;
    RatingBar ratingTres;
    Button btnRespuestas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encuesta_detalle, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences("prefe", Activity.MODE_PRIVATE);
        this.apiKey = sp.getString("APIkey", "");
        this.userId = sp.getString("IdUser", "");
        this.idEncuesta = getArguments().getInt("idEncuesta");
        this.preguntaUno = (TextView) view.findViewById(R.id.ePregunta1);
        this.preguntaDos = (TextView) view.findViewById(R.id.ePregunta2);
        this.preguntaTres = (TextView) view.findViewById(R.id.ePregunta3);
        this.imagenEvento = (ImageView) view.findViewById(R.id.imgDetaEncuesta);
        this.ratingUno = (RatingBar) view.findViewById(R.id.ratingPregunta1);
        this.ratingDos = (RatingBar) view.findViewById(R.id.ratingPregunta2);
        this.ratingTres = (RatingBar) view.findViewById(R.id.ratingPregunta3);

        this.ratingUno.setRating(0);
        this.ratingDos.setRating(0);
        this.ratingTres.setRating(0);

        this.btnRespuestas = (Button) view.findViewById(R.id.btnEnviarEncuesta);
        this.btnRespuestas.setOnClickListener(this);
        GetDetalleEncuesta deta = new GetDetalleEncuesta();
        deta.execute();

        return view;
    }

    @Override
    public void onClick(View v) {

        String calificacion = "Calificacion 1: " + this.ratingUno.getRating() + " Calificacion 2: " + this.ratingDos.getRating() + " Calificacion 3: " + this.ratingTres.getRating();
        Toast.makeText(getActivity(), calificacion, Toast.LENGTH_LONG).show();

    }

    class GetDetalleEncuesta extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String fromUrl = "http://cmp.devworms.com/api/encuesta/detail/"+userId+"/"+apiKey+"/"+idEncuesta;

            JSONParser jsonParser = new JSONParser();

            String respuesta = jsonParser.makeHttpRequest(fromUrl, "GET", fromUrl, "");

            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);

                    String strEncuesta = json.getString("encuesta");
                    JSONObject jEncuesta = new JSONObject(strEncuesta);

                    JSONArray jPreguntas = new JSONArray(jEncuesta.getString("preguntas"));

                    JSONObject jImagen = new JSONObject(jEncuesta.getString("filexl"));

                    imagen = getBitmapFromURL(jImagen.getString("url"));

                    strPreguntaUno = jPreguntas.getJSONObject(0).getString("pregunta");
                    strPreguntaDos = jPreguntas.getJSONObject(1).getString("pregunta");
                    strPreguntaTres = jPreguntas.getJSONObject(2).getString("pregunta");

                    response = "OK";

                }catch (JSONException jex){
                    response = "NO";
                    jex.printStackTrace();
                }catch (Exception ex){
                    response = "NO";
                    ex.printStackTrace();
                }

            }else{
                response = "NO";
            }

            return response;
        }

        protected void onPostExecute(String file_url) {
            if ( response.equals("OK") ) {
                fillEncuesta();
            }
        }
    }
    public void fillEncuesta(){

        preguntaUno.setText(strPreguntaUno);
        preguntaDos.setText(strPreguntaDos);
        preguntaTres.setText(strPreguntaTres);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imagenEvento.setImageBitmap(imagen);
                }
            });
    }
    public Bitmap getBitmapFromURL(String imageUrl) {
        Bitmap imagen = null;
        try{

            URL urlImage = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) urlImage.openConnection();
            conn.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

            imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);

        }catch (IOException iox){
            Log.e("Error imagen encuesta" , iox.getMessage());
        }
        return imagen;
    }
}
