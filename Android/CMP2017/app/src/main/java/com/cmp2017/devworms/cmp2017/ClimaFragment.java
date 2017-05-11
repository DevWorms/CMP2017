package com.cmp2017.devworms.cmp2017;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import static com.loopj.android.http.AsyncHttpClient.log;

public class ClimaFragment extends Fragment

{
    private ProgressDialog pDialog;
    String url, tipo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clima, container, false);
        WebView web = (WebView) view.findViewById(R.id.webViewClima);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Cargando mapa de expositores...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();

            }
        });
        url =  getArguments().getString("url");
        tipo = getArguments().getString("tipo");
        log.d("url presentacion",url);

        if (tipo.equals("2")){
            web.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+url);
        }else if (tipo.equals("3")){

            web.loadUrl(url);
        } else {

            web.loadUrl("https://es-us.noticias.yahoo.com/clima");
        }
        return view;


    }
}
