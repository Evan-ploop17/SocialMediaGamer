package com.example.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediagamer.R;
import com.example.socialmediagamer.models.User;
import com.example.socialmediagamer.providers.AuthProvider;
import com.example.socialmediagamer.providers.UserProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;



public class MainActivity extends AppCompatActivity {

    TextView mTextViewRegister;
    TextInputEditText mTextinputEmail, mTextinputPassword;
    Button btnLogin;
    AuthProvider mAuthProvider;
    private GoogleSignInClient mGoogleSignInClient;
    private final int REQUEST_CODE_GOOGLE = 9001;
    SignInButton btnLoginGoogle;
    UserProvider mUsersProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewRegister = findViewById(R.id.textViewRegister);
        mTextinputEmail = findViewById(R.id.textInputMail);
        mTextinputPassword = findViewById(R.id.textInputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("150216068780-29ie1qofvbs97khpce6tipemouhr6jun.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // inicializamos al instancia
        mAuthProvider = new AuthProvider();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mUsersProvider = new UserProvider();
        mDialog = new SpotsDialog.Builder().setContext(this).setMessage(R.string.wait).build();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this , RegisterActivity.class);
                 startActivity(intent);
            }
        });
    }

    // Con este método estará esperando que el usuario selecciona la cuenta de google con l que se registrará
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }

    // Espera hasta que el usuario escoja la cuenta
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("ERROR", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ERROR", "Google sign in failed", e);
            }
        }
    }
    // Desde acá si ejecuta la autenticación
    private void firebaseAuthWithGoogle(String idToken) {
        mDialog.show();
        mAuthProvider.googleLogin(idToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = mAuthProvider.getUid();
                            checkUserExist(id);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ERROR", "signInWithCredential:success");
                        }
                        else {
                            mDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w("ERROR", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void checkUserExist(final String id) {
        // vamos a la coleccion, luego al documento con el id especifico, get() consulta una sola vez, verificamos que haya sido exitoso
        mUsersProvider.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    mDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else{
                    // Si la persona no esta guardada en la base de datos entra acá
                    String mail = mAuthProvider.getEmail();
                    User user = new User();
                    user.setEmail(mail);
                    user.setId(id);
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this, CompleteProfileActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MainActivity.this,"Data unsaved", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    // Método pra entrar solamente con correo y contraseña
    private void login(){
        String mail = mTextinputEmail.getText().toString();
        String pass = mTextinputPassword.getText().toString();

        if(mail.isEmpty() && pass.isEmpty()){
            Toast.makeText(MainActivity.this, "Fill fields", Toast.LENGTH_LONG).show();
        }else{
            mDialog.show();
            mAuthProvider.login(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mDialog.dismiss();
                    if(task.isSuccessful()){
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "X Wrong X", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}