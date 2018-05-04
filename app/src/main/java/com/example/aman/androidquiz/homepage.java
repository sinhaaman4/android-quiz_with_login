package com.example.aman.androidquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Aman on 01-05-2018.
 */

public class homepage extends AppCompatActivity{
    private static final int REQUEST_CODE_QUIZ =1;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HiGHSCORE = "keyHighscore";

    private TextView textViewHighscore;
    TextView textViewUserId;

    private int highscore;

    Button buttonStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        SharedPreferences sp = getSharedPreferences("test", Context.MODE_PRIVATE);
        String uid = sp.getString("uid","");

        textViewHighscore=(TextView)findViewById(R.id.text_view_highscore);
        textViewUserId=(TextView)findViewById(R.id.textViewUid);
        textViewUserId.setText("Welcome " + uid);
        loadHighscore();

        buttonStartQuiz=(Button)findViewById(R.id.button_start_quiz);

    }

    public void start_quiz(View v)
    {
        Intent intent = new Intent(homepage.this,QuizActivity.class);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==REQUEST_CODE_QUIZ){
            if(resultCode==RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
                if(score>highscore){
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadHighscore(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highscore=prefs.getInt(KEY_HiGHSCORE,0);
        textViewHighscore.setText("Your Highscore: "+highscore);
    }
    private void updateHighscore(int  highscoreNew){
        highscore=highscoreNew;
        textViewHighscore.setText("Your Highscore: "+highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putInt(KEY_HiGHSCORE,highscore);
        editor.apply();
    }
}
