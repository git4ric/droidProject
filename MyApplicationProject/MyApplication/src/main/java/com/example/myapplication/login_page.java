package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.SessionManager;

/**
 * Created by ripjain on 8/23/13.
 */
public class login_page extends Activity {

    // Email, password edittext
    EditText txtUsername, txtPassword;

    // login button
    Button btnLogin;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login_page);

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//               Switching to Register screen
                Intent i = new Intent(getApplicationContext(), registration_page.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        // Session Manager
        session = new SessionManager(getApplicationContext());

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.uname);
        txtPassword = (EditText) findViewById(R.id.password);
        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);
        startSession();
    }

    private void startSession(){

        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                // Check if username, password is filled
                if(username.trim().length() > 0 && password.trim().length() > 0){

                    if(username.equals("Ripul") && password.equals("pass")){


                        session.createLoginSession("MyApplication", "name");
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("MyApplication.username",username);
                        setResult(RESULT_OK,returnIntent);
                        finish();

                    }else{
                        // username / password doesn't match
                        alert.showAlertDialog(login_page.this, "Login failed", "Incorrect username/password", false);
                    }
                }else{
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(login_page.this, "Login failed", "Please enter username and password", false);
                }

            }
        });
    }
}
