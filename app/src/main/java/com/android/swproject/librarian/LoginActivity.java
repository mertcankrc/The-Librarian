package com.android.swproject.librarian;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.swproject.librarian.Models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActiviy {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private TextView title;
    private EditText email,password;
    private Button signIn,signUp,bGoole;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar

        this.setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        title =  findViewById(R.id.title);
        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        signIn =  findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        bGoole =  findViewById(R.id.google);

        email.setWidth(title.getWidth());
        password.setWidth(title.getWidth());

        Typeface typeface = ResourcesCompat.getFont(this,R.font.american_typewriter);
        title.setTypeface(typeface);
        email.setTypeface(typeface);
        password.setTypeface(typeface);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        bGoole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });


    }

    @Override
    public void onStart(){
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            onAuthSuccess(mAuth.getCurrentUser());
        }

    }


    private void signInGoogle() {
        showProgressDialog();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleSignIn(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                hideProgressDialog();
            }
        }
    }


    private  void googleSignIn(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            hideProgressDialog();

                            onAuthSuccess(mAuth.getCurrentUser());
                        } else {

                        }
                    }
                });
    }

    private void signIn(){
        Log.d(TAG, "signIn");
        if(!validateForm()){
            return;
        }
        showProgressDialog();

        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG,"signIn:onComplete:" + task.isSuccessful());
                hideProgressDialog();

                if(task.isSuccessful()){
                    onAuthSuccess(task.getResult().getUser());
                }else{
                    Toast.makeText(LoginActivity.this,getApplicationContext().getResources().getString(R.string.signin_fail),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void signUp(){
        Log.d(TAG,"signup");
        if(!validateForm()){
            return;
        }

        showProgressDialog();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG,"signUp:complate");
                hideProgressDialog();

                if(task.isSuccessful()){
                    onAuthSuccess(task.getResult().getUser());
                }else{
                    Toast.makeText(LoginActivity.this,getApplicationContext().getResources().getString(R.string.signup_fail),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean validateForm(){
        boolean result = true;

        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError(this.getApplicationContext().getResources().getString(R.string.required_error));
            return false;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError(this.getApplicationContext().getResources().getString(R.string.required_error));
            return false;
        }

        return result;
    }

    private void onAuthSuccess(FirebaseUser user){
        String username =  usernameFromEmail(user.getEmail());

        writeNewUser(user.getUid(), username, user.getEmail());
        startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
        finish();

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        UserModel user = new UserModel(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}
