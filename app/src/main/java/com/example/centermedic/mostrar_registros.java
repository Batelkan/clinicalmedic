package com.example.centermedic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewDebug;

import com.example.centermedic.adapters.citasadapter;
import com.example.centermedic.models.citas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mostrar_registros extends AppCompatActivity {

    private citasadapter madapter;
    private RecyclerView mreciclerview;
    private ArrayList<citas> marray = new ArrayList<>();
    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_registros);

       /* mreciclerview = (RecyclerView)findViewById(R.id.my_recycler_view);
        mreciclerview.setLayoutManager(new LinearLayoutManager(this));*/
        mdatabase= FirebaseDatabase.getInstance().getReference();
        getdatos();

    }
    private void getdatos()
    {
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        String uuidUser = Auth.getCurrentUser().getUid();
        Query query = mdatabase.child("Citas").orderByChild("uuid").equalTo(uuidUser);

       query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds :snapshot.getChildren()) {
                        String Area = ds.child("Area").getValue().toString();
                        String Medico = ds.child("Medico").getValue().toString();
                        String Hospital = ds.child("Hospital").getValue().toString();
                        String Horario = ds.child("Horario").getValue().toString();
                        String Fecha = ds.child("Fecha").getValue().toString();
                        String uuid = ds.child("uuid").getValue().toString();
                        marray.add(new citas(Area, Medico, Hospital, Fecha, Horario, uuid));
                        Log.d("num encontrados", Integer.toString(marray.size()));
                    }
                iniciarLista();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void iniciarLista(){
        citasadapter adaptador = new citasadapter(marray);
        RecyclerView rv = (RecyclerView)findViewById(R.id.my_recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm= new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(adaptador);
    }
}
