package com.example.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.socialmediagamer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText textInputUserName, textInputUserMail, textInputUserPassword, textInputUserConfirmPassword;
    Button btnRegister;
    FirebaseAuth auth;
    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textInputUserName = findViewById(R.id.textInputUser);
        textInputUserMail = findViewById(R.id.textInputMail);
        textInputUserPassword = findViewById(R.id.textInputPassword);
        textInputUserConfirmPassword = findViewById(R.id.textInputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        // Así instanciamos los objetos
        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
    }

    private void register(){
        String userName = textInputUserName.getText().toString();
        String mail = textInputUserMail.getText().toString();
        String pass = textInputUserPassword.getText().toString();
        String confirmPassword = textInputUserConfirmPassword.getText().toString();

        if(!userName.isEmpty() && !mail.isEmpty() && !pass.isEmpty() && !confirmPassword.isEmpty() ){

            if(isEmailValid(mail)){
                if(pass.equals(confirmPassword)){
                    if(pass.length() > 6){
                        createUser(userName, mail, pass);
                    }
                    else{
                        Toast.makeText(this, "Password need 6 characters", Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Type info", Toast.LENGTH_LONG).show();
        }
    }

    // Método para crear el usuario
    private void createUser(final String userName, final String mail, String password){
        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String, Object> map = new HashMap<>();
                    map.put("mail", mail);
                    map.put("name", userName);
                    String userId = auth.getCurrentUser().getUid();
                    fireStore.collection("Users").document(userId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "User saved in database", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}