package com.example.centermedic;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    //valriables para la fecha
    private static final String CERO = "0";
    private static final String BARRA = "/";
    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    //variables
    EditText area;
    EditText medico;
    EditText hospital;
    EditText horario;
    EditText fecha;
    Button registrar_cita;
    DatabaseReference mrootreference;
    String Hospital = "";

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
        try {
           String  tempH =  getActivity().getIntent().getStringExtra("referenceMapTitle");
            Hospital = tempH.isEmpty() ? "" :tempH;
        }catch (Exception ex){}
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
                if(!Hospital.isEmpty())
                {
                    listHospitales.indexOf(Hospital);
                    pwspinner.selectItemByIndex(listHospitales.indexOf(Hospital));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(this.getContext());

        //progressDialog.setMessage("Un momento porfavor...");
        //progressDialog.show();

        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_registro, container, false);
        mrootreference = FirebaseDatabase.getInstance().getReference();
        getdatosHospitales();

        pwspinner = (PowerSpinnerView) root.findViewById(R.id.sphospital);

        final TextView Area = root.findViewById(R.id.edtarea);
        final TextView Medico = root.findViewById(R.id.edtMedico);
        final TextView Horario = root.findViewById(R.id.edthorario);
        final EditText Fecha = root.findViewById(R.id.edtfecha);
        final Button registrar= root.findViewById(R.id.btnregistrar_cita);
        pwspinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int position, String item) {
                Hospital = item;
            }
        });
        Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha(Fecha);
            }
        });
        Horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               obtenerHora(Horario);
            }
        });


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
                final Map<String, String>  registrocitas = new HashMap<>();
                registrocitas.put("Area", area);
                registrocitas.put("Hospital", Hospital);
                registrocitas.put("Medico", medico);
                registrocitas.put("Horario", horario);
                registrocitas.put("Fecha", fecha);
                registrocitas.put("uuid",uuidUser);
                //Consulta Ubicacion del hospital
                Query queryMap = mdatabase.child("Hospitales").orderByChild("nombre").equalTo(Hospital);
                queryMap.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds :snapshot.getChildren()) {
                            registrocitas.put("Longitud", ds.child("longitud").getValue().toString());
                            registrocitas.put("Latitud",ds.child("latitud").getValue().toString());
                        }
                        mrootreference.child("Citas").push().setValue(registrocitas);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                Toast toast =  Toast.makeText(view.getContext(),"Cita registrada con exito",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Area.setText("");
                Medico.setText("");
                Horario.setText("");
                Fecha.setText("");
                pwspinner.clearSelectedItem();
                startActivity(new Intent(Fragment_registro.this.getContext(), menu.class));
            }
        });
        return root;
    }

    private void obtenerFecha(final EditText fec){
        DatePickerDialog recogerFecha = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                fec.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        recogerFecha.show();
    }

    private void obtenerHora(final TextView hrs){
        TimePickerDialog recogerHora = new TimePickerDialog(this.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                hrs.setText(horaFormateada + ":" + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);
        recogerHora.show();
    }
}
