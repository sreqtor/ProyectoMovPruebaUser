package com.example.proyectomov;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectomov.Realm.UsuarioRealm;
import com.example.proyectomov.Realm.UsuarioRealmCRUD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class registro extends AppCompatActivity {

    private Context context;
    ImageButton back;
    private Button registro;
    private String userID;
    private EditText editTextUsuario, editTextCorreo, editTextContrasenia;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        context = this;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContrasenia = findViewById(R.id.editTextPass2);

        //Comparar pass2 con confirmarPass

        registro = findViewById(R.id.buttonInicio);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });

        back = findViewById(R.id.imageButtonBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MainActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void registerNewUser()
    {

        // Take the value of two edit texts in Strings
        String usuario, email, password;
        usuario = editTextUsuario.getText().toString();
        email = editTextCorreo.getText().toString();
        password = editTextContrasenia.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(usuario)) {
            Toast.makeText(getApplicationContext(),
                            "Introduce un usuario",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Introduce un mail",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Introduce una contraseña",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = db.collection("users").document(userID);

                            Map<String,Object> user=new HashMap<>();
                            user.put("Usuario", usuario);
                            user.put("Correo", email);
                            user.put("Contraseña", password);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("TAG", "onSuccess: Datos registrados "+userID);
                                }
                            });

                            Toast.makeText(getApplicationContext(),
                                            "Registro exitoso",
                                            Toast.LENGTH_LONG)
                                    .show();

                            UsuarioRealm usuarioRealm = new UsuarioRealm();
                            usuarioRealm.setUsuario(editTextUsuario.getText().toString());
                            usuarioRealm.setEmail(editTextCorreo.getText().toString());

                            UsuarioRealmCRUD.aniadirUsuario(usuarioRealm);

                            startActivity(new Intent(registro.this,
                                    MainActivity.class));

                        }
                        else {

                            // Registration failed
                            Toast.makeText(
                                            getApplicationContext(),
                                            "Registro fallido"
                                                    + " Prueba de nuevo más tarde",
                                            Toast.LENGTH_LONG)
                                    .show();


                        }
                    }
                });
    }
}