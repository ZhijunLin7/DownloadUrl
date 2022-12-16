package com.example.downloadurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.downloadurl.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    String[] Permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pedirMultiplePermission()) {
                    coneccion();
                }
            }
        });
    }

    //Pedir todo los permisos necesarios si es necesario
    public boolean pedirMultiplePermission(){
            ArrayList<String> requests=new ArrayList<>();
            boolean permisoAutorizado=false;
            for (String permission :Permissions) {
                if (ContextCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_DENIED) {
                    requests.add(permission);
                }
            }
            if (!requests.isEmpty()) {
                ActivityCompat.requestPermissions(this,requests.toArray(new String[requests.size()]),1);
            }else{
                Toast.makeText(this,"Todo los permisos conseguido",Toast.LENGTH_SHORT).show();
                permisoAutorizado=true;
            }
            return permisoAutorizado;
    }
    public void coneccion(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageText().execute(binding.editTextUrl.getText().toString());
        } else {
            Toast.makeText(MainActivity.this,"Error de coneccion",Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadWebpageText extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            binding.textresult.setText(o.toString());
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                return downloadUrl(objects[0].toString());
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
    }
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;



        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            is = conn.getInputStream();

            String contentAsString = readIt(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");

        //Sacar char de InputStream y meter en un String
        String result = new String();
        int data = reader.read();
        while(data != -1){
            result+= String.valueOf((char)data);
            data = reader.read();
        }

        return result;
    }
}