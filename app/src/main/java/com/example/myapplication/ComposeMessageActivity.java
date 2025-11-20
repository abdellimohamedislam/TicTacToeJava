package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ComposeMessageActivity extends AppCompatActivity {

    // 1. Declare your View variables
    EditText player1EditText;
    EditText player2EditText;
    Button submitButton;

    // REMOVED: Don't create the Intent here
    // Intent i = new Intent(this, MainActivity.class);

    // REMOVED: These methods were problematic
    // public void getPlayer1(View v){ ... }
    // public void getPlayer2(View v){ ... }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        player1EditText = findViewById(R.id.editTextText);
        player2EditText = findViewById(R.id.editTextText2);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                launchMainActivity();
            }
        });

    }

    // 4. Create a single method to start the activity
    private void launchMainActivity() {
        // Get the text from BOTH fields
        String player1 = player1EditText.getText().toString();
        String player2 = player2EditText.getText().toString();

        // Create the Intent HERE, inside the method
        // Use "ComposeMessageActivity.this" for the context
        Intent intent = new Intent(ComposeMessageActivity.this, MainActivity.class);

        // Put BOTH pieces of data into the Intent
        intent.putExtra("player1", player1);
        intent.putExtra("player2", player2);

        // 5. START the activity
        startActivity(intent);
    }
}