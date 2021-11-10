package com.example.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.socialmediagamer.R;
import com.example.socialmediagamer.models.User;
import com.example.socialmediagamer.providers.AuthProvider;
import com.example.socialmediagamer.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText textInputUserName, textInputUserMail, textInputUserPassword, textInputUserConfirmPassword;
    Button btnRegister;
    AuthProvider mAuthProvider;
    UserProvider mUsersProvider;
    AlertDialog mDialog;

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
        mAuthProvider = new AuthProvider();
        mUsersProvider = new UserProvider();
        mDialog = new SpotsDialog.Builder().setContext(this).setMessage(R.string.wait).build();

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
        mDialog.show();
        mAuthProvider.register(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mDialog.dismiss();
                    String userId = mAuthProvider.getUid();

                    User user = new User();
                    user.setId(userId);
                    user.setEmail(mail);

                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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