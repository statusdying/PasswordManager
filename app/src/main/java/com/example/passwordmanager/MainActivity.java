package com.example.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button, authenticate_button;

    TextView login_text;

    Activity activity;
    MyDatabaseHelper myDB;
    ArrayList<String> _id, title, username, password, url;
    CustomAdapter customAdapter;

    private Executor executor;

    private androidx.biometric.BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    Boolean firstLogIn;

    boolean loggedOutDueInactivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);





        authenticate_button = findViewById(R.id.authenticate_button);
        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        login_text = findViewById(R.id.login_text);
        executor = ContextCompat.getMainExecutor(this);
        String loginState = "loggedOut";


        SharedViewModel viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        String loginState1 = viewModel.getMyVariable();

        if(getIntent().getStringExtra("loggedIn") != null)
        {
            loginState = getIntent().getStringExtra("loggedIn");
            //Toast.makeText(this, mySecondVariable, Toast.LENGTH_SHORT).show();
        }
        //String mySecondVariable = getIntent().getStringExtra("loggedIn");


        //Toast.makeText(this, myVariable+mySecondVariable, Toast.LENGTH_SHORT).show();
            if(loginState1 == "first_state" && loginState == "loggedOut"){
                loggedOut();
                //Toast.makeText(this, firstLogIn.toString(), Toast.LENGTH_SHORT).show();

            }else{loggedIn();}





        biometricPrompt = new androidx.biometric.BiometricPrompt(MainActivity.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(MainActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(MainActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                firstLogIn = true;
                viewModel.setMyVariable("second");
                loggedIn();



            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

            }
        });

        promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint or face")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                                .build();



        authenticate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
                //Intent intent = new Intent(MainActivity.this, FingerprintActivity.class);
                //startActivity(intent);
            }
        });



        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                //activity.startActivityForResult(intent,1);
                startActivity(intent);

            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        _id = new ArrayList<>();
        title = new ArrayList<>();
        username = new ArrayList<>();
        password = new ArrayList<>();
        url = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this,this,_id, title, username, password, url);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "OnActivityResult", Toast.LENGTH_SHORT).show();
        if(requestCode == 1){
            recreate();
        }

    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                _id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                username.add(cursor.getString(2));
                password.add(cursor.getString(3));
                url.add(cursor.getString(4));

            }

        }
    }

    void loggedIn(){
        add_button.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        authenticate_button.setVisibility(View.INVISIBLE);
        login_text.setVisibility(View.INVISIBLE);

    }

    void loggedOut(){
        recyclerView.setVisibility(View.INVISIBLE);
        add_button.setVisibility(View.INVISIBLE);
        authenticate_button.setVisibility(View.VISIBLE);
        login_text.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPause() {
        super.onPause();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1 * 60 * 1000); // 10 minutes in milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Use a Handler to post the action to the main thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Perform your action here on the main thread
                        // e.g., update UI, show a notification
                        loggedOutDueInactivity = true;
                        loggedOut();
                        //Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }



}