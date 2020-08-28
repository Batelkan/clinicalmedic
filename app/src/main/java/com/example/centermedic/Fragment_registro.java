package com.example.centermedic;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.centermedic.models.citas;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_registro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_registro extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mdatabase;
    private List<String> listHospitales = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    PowerSpinnerView pwspinner;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //variables
    EditText area;
    EditText medico;
    EditText hospital;
    EditText horario;
    EditText fecha;
    Button registrar_cita;
    DatabaseReference mrootreference;
    String[] hopitales ={"Clinica Santa Maria", "Clinica Merida", "Clinica Pensiones", "Centro de Especialidades Medicas", "Star Medica"};
    String Hospital = "";
    public Fragment_registro() {



    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_registro.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_registro newInstance(String param1, String param2) {
        Fragment_registro fragment = new Fragment_registro();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mdatabase= FirebaseDatabase.getInstance().getReference();

    }

    private void getdatosHospitales()
    {
        Query query = mdatabase.child("Hospitales").orderByChild("nombre");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds :snapshot.getChildren()) {
                    listHospitales.add(ds.child("nombre").getValue().toString());
                }

                pwspinner.setItems(listHospitales);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());

        //progressDialog.setMessage("Un momento porfavor...");
        //progressDialog.show();

        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_registro, container, false);

        mrootreference = FirebaseDatabase.getInstance().getReference();

        getdatosHospitales();
       // Spinner spinner = (Spinner) root.findViewById(R.id.sphospital);
        pwspinner = (PowerSpinnerView) root.findViewById(R.id.sphospital);
        //dataAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_item,listHospitales);
       /* hintAdapter = new HintAdapter<String>(this.getContext(),android.R.layout.simple_spinner_item,"Seleccione un hospital",listHospitales);
        hintAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(hintAdapter);*/

        final TextView Area = root.findViewById(R.id.edtarea);
        final TextView Medico = root.findViewById(R.id.edtMedico);
        final TextView Horario = root.findViewById(R.id.edthorario);
        final EditText Fecha = root.findViewById(R.id.edtfecha);
        final Button registrar= root.findViewById(R.id.btnregistrar_cita);

/*
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item =(String) parent.getItemAtPosition(position);
                Hospital = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/

        //Metodo Boton para Guardados en firebase
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String area = Area.getText().toString();
                String medico = Medico.getText().toString();
                String horario = Horario.getText().toString();
                String fecha = Fecha.getText().toString();

                FirebaseAuth Auth = FirebaseAuth.getInstance();
                String uuidUser = Auth.getCurrentUser().getUid();

                Map<String, String> registrocitas = new HashMap<>();
                registrocitas.put("Area", area);
                registrocitas.put("Hospital", Hospital);
                registrocitas.put("Medico", medico);
                registrocitas.put("Horario", horario);
                registrocitas.put("Fecha", fecha);
                registrocitas.put("uuid",uuidUser);

                mrootreference.child("Citas").push().setValue(registrocitas);

                Area.setText("");
                Medico.setText("");
                Horario.setText("");
                Fecha.setText("");
            }
        });


        return root;


    }
}
