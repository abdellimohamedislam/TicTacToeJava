package com.example.myapplication;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.content.Context;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String ActivePlayer="";
    String p1="";
    String p2="";
    String PlayerXname="";
    String PlayerOname="";
    int[][] board = new int[3][3]; // 0 = empty, 1 = X, 2 = O
    boolean gameOver = false;
    public void giveRole(String p1, String p2) {
        Random rand = new Random();
        String startingPlayerName;
        String startingSymbol;

        // 1. Randomly assign who is X and who is O
        if (rand.nextBoolean()) {
            PlayerXname = p1;
            PlayerOname = p2;
        } else {
            PlayerXname = p2;
            PlayerOname = p1;
        }

        // 2. Randomly decide which *symbol* starts
        if (rand.nextBoolean()) {
            ActivePlayer = "x";
            startingPlayerName = PlayerXname;
            startingSymbol = "X";
        } else {
            ActivePlayer = "o";
            startingPlayerName = PlayerOname;
            startingSymbol = "O";
        }

        // 3. Set the starting text
        ((TextView) findViewById(R.id.textView3)).setText(startingPlayerName + "'s turn (" + startingSymbol + ")");
        ((TextView) findViewById(R.id.textView3)).setTextColor(Color.RED); // Set a default start color
    }
    public void tapPlayer(View v){
        if (gameOver) return; // no more moves if game ended

        Button b = (Button) v;
        String tag = v.getTag().toString(); // example "00"
        int row = Character.getNumericValue(tag.charAt(0));
        int col = Character.getNumericValue(tag.charAt(1));

        if (board[row][col] != 0) return; // already played
        if(ActivePlayer.equals("x")){
            ((Button)v).setText("x");
            board[row][col]=1;
            ActivePlayer="o";
            ((TextView) findViewById(R.id.textView3)).setText(PlayerOname+"'s TURN 0");
            ((TextView) findViewById(R.id.textView3)).setTextColor(Color.GRAY);
            b.setBackgroundColor(Color.GRAY);

        } else if (ActivePlayer.equals("o")) {
            ((Button) v).setText("o");
            board[row][col]=2;
            ActivePlayer="x";
            ((TextView) findViewById(R.id.textView3)).setText(PlayerXname+"'s TURN X");
            ((TextView) findViewById(R.id.textView3)).setTextColor(Color.WHITE);
            b.setBackgroundColor(Color.WHITE);
        }
        ((Button) v).setEnabled(false);
        checkWinner(v);
    }
    public void checkWinner(View v){
        int winner =0;
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                winner = board[i][0];
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i])
                winner = board[0][i];
        }

        // Check diagonals
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            winner = board[0][0];
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            winner = board[0][2];

        if (winner != 0) {
            gameOver = true;
            String winnerName = (winner == 1) ? PlayerXname : PlayerOname;
            String winnerText = winnerName + " wins!";
            ((TextView) findViewById(R.id.textView3)).setText(winnerText);
            Toast.makeText(this, winnerText, Toast.LENGTH_SHORT).show();
            vibrateOnEnd();
            new AlertDialog.Builder(this).setTitle("Game Over").setMessage(winnerText).setCancelable(true).setPositiveButton("Play Again",((dialog, which) -> reset(null))).show();

        } else if (isBoardFull()) {
            gameOver = true;
            ((TextView) findViewById(R.id.textView3)).setText("It's a draw!");
            Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show();
            vibrateOnEnd();
            new AlertDialog.Builder(this)
                    .setTitle("Draw 🤝")
                    .setMessage("Nobody wins this round.")
                    .setCancelable(false)
                    .setPositiveButton("Play Again", (dialog, which) -> {
                        reset(null);
                    })
                    .show();
        }

    }
    public void vibrateOnEnd(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500); // deprecated but works on old phones
            }
        }
    }
    public void getPlayerNames(){

    }
    private boolean isBoardFull() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) return false;
            }
        }
        return true;
    }
    public void reset(View v) {
        GridLayout grid = findViewById(R.id.gridlayout);

        if (grid == null) {
            Toast.makeText(this, "GridLayout not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        int childCount = grid.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = grid.getChildAt(i);
            if (child instanceof Button) {
                Button b = (Button) child;
                b.setText("X/O");       // clear text
                b.setEnabled(true);  // enable again
                b.setBackgroundColor(Color.RED);
            }
        }
        ((TextView) findViewById(R.id.textView3)).setTextColor(Color.RED);

        // Reset internal logic
        board = new int[3][3];
        gameOver = false;
       // giveRole();
        ((TextView)findViewById(R.id.textView3)).setText("New Game Started");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("home");
        Intent i= getIntent();
        p1=i.getStringExtra("player1");
        p2=i.getStringExtra("player2");
        giveRole(p1,p2);



    }
}