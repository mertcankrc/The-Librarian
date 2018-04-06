package com.android.swproject.librarian;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.swproject.librarian.Models.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends Activity {

    private Button register;
    EditText name,surname,email,password,passwordAgain;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        register = (Button) findViewById(R.id.register);
        name = (EditText) findViewById(R.id.name) ;
        surname = (EditText) findViewById(R.id.surname) ;
        email = (EditText) findViewById(R.id.email) ;
        password = (EditText) findViewById(R.id.password) ;
        passwordAgain = (EditText) findViewById(R.id.password_again) ;

        //Initialize Firebase component
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("users");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(passwordAgain.getText().toString())) {
                    UserModel userModel = new UserModel(name.getText().toString(), surname.getText().toString(),
                            email.getText().toString(), password.getText().toString(), false);
                    mMessageDatabaseReference.push().setValue(userModel);
                    finish();
                }else{
                    //String degiştirilecek ve string xmle baglanacak
                    Toast.makeText(RegisterActivity.this,"Passwords are not matching!!",Toast.LENGTH_SHORT).show();
                }

            }
        });




    }
}
