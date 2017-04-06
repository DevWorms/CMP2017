package com.cmp2017.devworms.cmp2017;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PDFFragment extends Fragment

{
    WebView wbPdf;
    String url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdffragment, container, false);
        url =  getArguments().getString("url");
        wbPdf = (WebView) view.findViewById(R.id.wbPdf);
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

                    wbPdf.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);



                }
            });

        }

    }

}
