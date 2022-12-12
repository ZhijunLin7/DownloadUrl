package com.example.downloadurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.downloadurl.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;

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
                    Toast.makeText(MainActivity.this,"Descargando",Toast.LENGTH_SHORT).show();

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
        
    }
}