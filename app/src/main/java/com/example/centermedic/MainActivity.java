package com.example.centermedic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {


    //variables Login
    EditText Correo;
    EditText Contraseña;
    Button btnSesion;
    Button btnregistrarse;
    private String email;
    private String Password;
    FirebaseAuth Auth;
    private ProgressDialog progressDialog;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Conectamos vararibles
        btnSesion = (Button) findViewById(R.id.btnregistrarse);
        btnregistrarse = (Button) findViewById(R.id.btnsesion);
        Correo = (EditText) findViewById(R.id.edtemail);
        Contraseña = (EditText) findViewById(R.id.edtContraseña);
        Auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

    }

    public void loguearUsuario(View view) {
        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = Correo.getText().toString();
        String password = Contraseña.getText().toString();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Realizando validacion de datos en linea...");
        progressDialog.show();

        //loguear usuario
        Auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        startActivity(new Intent(MainActivity.this, menu.class));
                       /*
                        if (task.isSuccessful()) {
                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(),"Inicio de sesion exitoso",Toast.LENGTH_LONG).show();
                            Log.d("Succes", "signInWithEmail:success");
                            startActivity(new Intent(MainActivity.this, menu.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Usario invalido",Toast.LENGTH_LONG).show();
                        }*/
                    }
                });

    }

    //metodo para cambiar de activity y registrarse
    public void CambioRegistro(View v) {
        startActivity(new Intent(MainActivity.this, Registry.class));

    }
}


