package com.example.centermedic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class credits extends AppCompatActivity {
    /*ImageButton ubicacion;
    ImageButton correo;
    ImageButton telefono;

    //root.findViewById(R.id.edtarea)
    ubicacion=(ImageButton)findViewById(R.id.imgbtnmail);
    correo=(ImageButton)findViewById(R.id.imgbtnmail);
    telefono=(ImageButton)findViewById(R.id.imgbtnphone);*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }

    public void iralocacion(View v)
    {
        Uri gmmIntentUri = Uri.parse("geo:20.9722498,-89.623406");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        v.getContext().startActivity(mapIntent);
    }

    public void enviarcorreo(View v)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"centermedic@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "que deseas saber?...");
        i.putExtra(Intent.EXTRA_TEXT   , "Escribe tus comentario...");
        try {
            startActivity(Intent.createChooser(i, "Enviar correo..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No tienes clientes de correo instalados.", Toast.LENGTH_SHORT).show();
        }
    }

    public void llamar(View v){
        Intent intentoLlamada = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel","9995121018",null));
        v.getContext().startActivity(intentoLlamada);
    }

}