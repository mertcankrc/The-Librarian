package com.android.swproject.librarian;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    TextView title;
    EditText email,password;
    Button signIn,signUp,bGoole;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_login);

        title = (TextView) findViewById(R.id.title);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.sign_in);
        signUp = (Button) findViewById(R.id.sign_up);
        bGoole = (Button) findViewById(R.id.google);

        email.setWidth(title.getWidth());
        password.setWidth(title.getWidth());

        Typeface typeface = getResources().getFont(R.font.american_typewriter);
        title.setTypeface(typeface);
        email.setTypeface(typeface);
        password.setTypeface(typeface);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}
