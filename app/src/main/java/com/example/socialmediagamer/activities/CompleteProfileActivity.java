package com.example.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.socialmediagamer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText textInputUserName;
    Button btnRegister;
    FirebaseAuth auth;
    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);


        // Así instanciamos los objetos
        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        textInputUserName = findViewById(R.id.textInputUser);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    } // On create

    private void register(){
        String userName = textInputUserName.getText().toString();
        if(!userName.isEmpty() ){
            update(userName);
        }else{
            Toast.makeText(this, "Type info", Toast.LENGTH_LONG).show();
        }
    }

    // Método para crear el usuario
    private void update(final String userName){
        Map<String, Object> map = new HashMap<>();
        map.put("name", userName);
        String userId = auth.getCurrentUser().getUid();
        // Update por get para que no sobreescriba los valores
        fireStore.collection("Users").document(userId).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}