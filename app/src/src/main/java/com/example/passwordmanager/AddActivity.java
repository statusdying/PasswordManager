package com.example.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    EditText title_input, username_input, password_input, url_input;
    Button add_button, generate_password, reveal_button;

    String generatedPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.password_input);
        url_input = findViewById(R.id.url_input);
        add_button = findViewById(R.id.add_button);
        generate_password = findViewById(R.id.gen_password);
        reveal_button = findViewById(R.id.reveal_button);

        //title_input.setText("Helelo");
        generatedPassword = generatePassword(12);

        generate_password.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 generatedPassword = generatePassword(12);
                 password_input.setText(generatedPassword);
             }
        });

        reveal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password_input.getInputType()== InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    password_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    password_input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }

        });

        SharedViewModel viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        String myVariable = viewModel.getMyVariable();

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addCredentials(title_input.getText().toString().trim(),
                        username_input.getText().toString().trim(),
                        password_input.getText().toString().trim(),
                        url_input.getText().toString().trim());
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        //finish();
                intent.putExtra("loggedIn", "loggedIn");
                //viewModel.setMyVariable("third");
                startActivity(intent);
                //viewModel.setMyVariable("forth");
                recreate();
                //viewModel.setMyVariable("fifth");

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    String generatePassword(int length) {
        if (length < 4) {
            Toast.makeText(this, "Password length must be at least 4", Toast.LENGTH_SHORT).show();
        }

        // Define character sets
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String symbolChars = "!@#$%^&*()_-+={}[]|;:'\",.<>/?";

        // Create a list to store password characters
        List<Character> passwordChars = new ArrayList<>();

        // Ensure at least one of each character type
        passwordChars.add(getRandomChar(uppercaseChars));
        passwordChars.add(getRandomChar(lowercaseChars));
        passwordChars.add(getRandomChar(numberChars));
        passwordChars.add(getRandomChar(symbolChars));

        // Fill the rest of the password with random characters
        String allChars = uppercaseChars + lowercaseChars + numberChars + symbolChars;
        SecureRandom random = new SecureRandom();
        for (int i = 4; i < length; i++) {
            passwordChars.add(getRandomChar(allChars));
        }

        // Shuffle the characters to randomize the password
        Collections.shuffle(passwordChars);

        // Build the password string
        StringBuilder passwordBuilder = new StringBuilder();
        for (Character c : passwordChars) {
            passwordBuilder.append(c);
        }

        return passwordBuilder.toString();
    }

    char getRandomChar(String charSet) {
        SecureRandom random = new SecureRandom();
        int randomIndex = random.nextInt(charSet.length());
        return charSet.charAt(randomIndex);
    }
}