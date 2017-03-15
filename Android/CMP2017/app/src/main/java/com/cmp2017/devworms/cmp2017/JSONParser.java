package com.cmp2017.devworms.cmp2017;

import android.util.Log;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JSONParser {


    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    Response respuesta;
    // constructor
    public JSONParser() {

    }


    // function get json from url
    // by making HTTP POST or GET mehtod
    public String makeHttpRequest(String url, String method,String bd,String bd2) {
        Log.d("metho : ", "> " + method);
        // Making HTTP request
        try {

            // check for request method
            if (method == "POST") {
                // request method is POST
                // defaultHttpClient
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType,bd );
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Log.d("REQUEST : ", "> " + request.body());
                respuesta = client.newCall(request).execute();




            } else if (method == "GET") {

                // request method is GET
                OkHttpClient client = new OkHttpClient();
                Log.d("URL : ", "> " + url);
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("Apikey", bd)
                        .build();

                respuesta = client.newCall(request).execute();
            } else if(method == "GETF") {
                OkHttpClient client = new OkHttpClient();
                Log.d("URL : ", "> " + url);
                Log.d("bd : ", "> " + bd2);
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("Apikey", bd)
                        .addHeader("Folio", bd2)
                        .build();

                respuesta = client.newCall(request).execute();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Log.d("message: ", "> " + respuesta.body().string().toString());
            if("OK".equals(respuesta.message().toString())){
                json = respuesta.body().string().toString();
            }else{
                json = "error"  ;
            }
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return JSON String
        return json;

    }
}