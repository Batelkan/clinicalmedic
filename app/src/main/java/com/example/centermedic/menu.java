package com.example.centermedic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class menu extends AppCompatActivity {

    Button registrarcita;
    Button vercita;
    Button hospitales;
    Button acercade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        registrarcita=(Button)findViewById(R.id.btnregistrarcita);
        vercita=(Button)findViewById(R.id.btnvercitas);
        hospitales=(Button)findViewById(R.id.btnhospitales);
        acercade=(Button)findViewById(R.id.btnacercade);
    }
    public void registro(View v)
    {

        startActivity(new Intent(menu.this, Registrocita.class));
    }

    public void ver(View v)
    {
        startActivity(new Intent(menu.this, mostrar_registros.class));
    }

    public void mapa(View v)
    {
        startActivity(new Intent(menu.this, map.class));
    }
    public void acercade(View v)
    {

    }
}


