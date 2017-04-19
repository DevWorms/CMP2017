package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetalleMapa extends Fragment

{
WebView wbPdf;
    String   urlMapaRecinto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_mapa, container, false);

        wbPdf = (WebView) view.findViewById(R.id.webMapa);
        WebSettings wbs=wbPdf.getSettings();
        wbs.setBuiltInZoomControls(true);
        wbs.setJavaScriptEnabled(true);
        new LoadSingleTrack().execute();



        return view;


    }

    class LoadSingleTrack extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting song json and parsing
         * */
        protected String doInBackground(String... args) {
            // Building Parameters

            String fromUrl = "http://cmp.devworms.com/api/mapa/recinto/1/0";


            JSONParser jsonParser = new JSONParser();

            String respuesta = jsonParser.makeHttpRequest(fromUrl, "GET", fromUrl, "");

            if (respuesta != "error") {
                try {
                    JSONObject json = new JSONObject(respuesta);

                    JSONObject  strMapa = new JSONObject(json.getString("mapa"));
                    urlMapaRecinto = strMapa.getString("url");




                }catch (JSONException jex){

                    jex.printStackTrace();
                }

            }else{

            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting song information

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {



                    wbPdf.setWebViewClient(new WebViewClient() {
                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                        }

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon)
                        {

                        }


                        @Override
                        public void onPageFinished(WebView view, String url) {
                            String webUrl = wbPdf.getUrl();

                        }

                    });

                    wbPdf.loadUrl("http://docs.google.com/gview?embedded=true&url="+urlMapaRecinto);



                }
            });

        }

    }


}
