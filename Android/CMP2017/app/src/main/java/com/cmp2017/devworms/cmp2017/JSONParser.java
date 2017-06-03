package com.cmp2017.devworms.cmp2017;

import android.util.Log;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.loopj.android.http.AsyncHttpClient.log;

public class JSONParser {


    static InputStream is = null;
    static JSONObject jObj = null;
    static String json,json2;
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
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(300, TimeUnit.SECONDS)
                        .writeTimeout(300, TimeUnit.SECONDS)
                        .readTimeout(300, TimeUnit.SECONDS)
                        .build();

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
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(300, TimeUnit.SECONDS)
                        .writeTimeout(300, TimeUnit.SECONDS)
                        .readTimeout(300, TimeUnit.SECONDS)
                        .build();

                Log.d("URL : ", "> " + url);
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("cache-control", "no-cache")
                        .build();

                respuesta = client.newCall(request).execute();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        try {

            if("OK".equals(respuesta.message().toString()) || "Created".equals(respuesta.message().toString())){
                json = respuesta.body().string().toString();

            }else{
                json = "error "+ respuesta.body().string().toString();
            }
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return JSON String

            return json;


    }
}