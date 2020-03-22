package com.example.placement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import models.company;
import models.student;

public class Login extends Activity {
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth mFirebaseAuth;
    EditText userId, password;
    Button loginButton;
    AppCompatRadioButton stud, comp;
    TextView dontHaveAnAccount;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    public void back_to_main(View view) {
        Intent gotoMainPage = new Intent(this, MainActivity.class);
        startActivity(gotoMainPage);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        userId = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        stud = findViewById(R.id.student_selector_2);
        comp = findViewById(R.id.company_selector_2);
        dontHaveAnAccount = findViewById(R.id.dont_have_an_account);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Toast.makeText(Login.this, "You are Logged in !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, home.class));
                    finish();
                }
                else{
                    Toast.makeText(Login.this, "Please fill Correct Details", Toast.LENGTH_SHORT).show();
                }
            }
        };
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = userId.getText().toString();
                String pwd = password.getText().toString();
                if(username.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Login.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(username.isEmpty()){
                    userId.setError("Please enter Username");
                    userId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter a Password");
                    password.requestFocus();
                }
                else if(pwd.length()<6){
                    password.setError("Password should be at least 6 characters long");
                    password.requestFocus();
                }
                else{
                    mFirebaseAuth.signInWithEmailAndPassword(username,pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(! task.isSuccessful()){
                                Toast.makeText(Login.this,"Login Error, Please try Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(stud.isChecked()){
                                    if(checkUsernamePresence(username, false)){
                                        Toast.makeText(Login.this, "Logged in as Student !", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this, home.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(Login.this, "Username Not in Students !", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                } else if(comp.isChecked()){
                                    if(checkUsernamePresence(username, true)){
                                        Toast.makeText(Login.this, "Logged in as Student !", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this, home.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(Login.this, "Username Not in Students !", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                }else{
                                    Toast.makeText(Login.this, "Please Check in your Type !", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                }
                            }
                        }
                    });
                }
            }
        });
        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Signin.class));
                finish();
            }
        });

        stud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stud.isChecked()){
                    comp.setChecked(false);
                }
            }
        });

        comp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(comp.isChecked()){
                    stud.setChecked(false);
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton googleLogin = findViewById(R.id.google_login_button);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.google_login_button:
                        googleLogIn();
                        break;
                }
            }
        });
    }

    private boolean checkUsernamePresence(String username, boolean a){
        final boolean[] ans = {false};
        if(!a){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Students");
            ref.child("email").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        ans[0] = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Companies");
            ref.child("email").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        ans[0] = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return ans[0];
    }

    private void googleLogIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            startActivity(new Intent(Login.this,  home.class));
            finish();
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
        } else {

        }
    }
}