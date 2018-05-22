package com.example.aman.androidquiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aman on 06-04-2018.
 */

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 15000;

    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_MILLIS_LEFT =" keyMillisLeft";
    private static final String KEY_ANSWERED ="keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewCountDown;

    private CountDownTimer countDownTimer;
    private long timeLeftinMillis;

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;
    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;
    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;

    private long backpressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = (TextView) findViewById(R.id.text_view_question);
        textViewCountDown=(TextView)findViewById(R.id.text_view_countdown);
        textViewScore = (TextView) findViewById(R.id.text_view_score);
        textViewQuestionCount = (TextView) findViewById(R.id.text_view_question_count);
        rbGroup = (RadioGroup) findViewById(R.id.radio_group);
        rb1 = (RadioButton) findViewById(R.id.radio_button1);
        rb2 = (RadioButton) findViewById(R.id.radio_button2);
        rb3 = (RadioButton) findViewById(R.id.radio_button3);
        buttonConfirmNext = (Button) findViewById(R.id.button_confirm_next);

        textColorDefaultRb=rb1.getTextColors();
        textColorDefaultCd=textViewCountDown.getTextColors();

        QuizdbHelper dbHelper = new QuizdbHelper(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked())
                    {
                        checkAnswer();

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please select an answer",Toast.LENGTH_SHORT).show();
                    }
                }
                else{showNextQuestion();}
            }
        });
    }
    private void showNextQuestion(){
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if(questionCounter < questionCountTotal)
        {
            currentQuestion=questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            textViewQuestionCount.setText("Question: "+ questionCounter+"/"+questionCountTotal);
            answered=false;
            buttonConfirmNext.setText("confirm");

            timeLeftinMillis=COUNTDOWN_IN_MILLIS;
            startCountDown();
        }
        else{
            finishQuiz();
        }


    }
    private void startCountDown(){
        countDownTimer=new CountDownTimer(timeLeftinMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftinMillis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftinMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }
    private void updateCountDownText(){
        int minutes =(int)(timeLeftinMillis / 1000) / 60;
        int seconds =(int)(timeLeftinMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);

        if(timeLeftinMillis<10000) {
            textViewCountDown.setTextColor(Color.RED);
        }else{
            textViewCountDown.setTextColor(textColorDefaultCd);
        }}

    private void checkAnswer(){
        answered=true;
        countDownTimer.cancel();

        RadioButton rbSelected = (RadioButton) findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected)+1;

        if(answerNr==currentQuestion.getAnswerNr()){
            score++;
            textViewScore.setText("Score: "+score);
        }
        showSolution();
    }

    private void showSolution(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch(currentQuestion.getAnswerNr()){

            case 1:
                rb1.setTextColor(Color.GREEN);
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                break;
        }
        if(questionCounter < questionCountTotal){
            buttonConfirmNext.setText("Next");
        }
        else{
            buttonConfirmNext.setText("Finish");
        }
    }
    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK,resultIntent);
        finish();
    }
    @Override

    public void onBackPressed(){
        if(backpressedTime + 2000>System.currentTimeMillis())
            finishQuiz();
        else{
            Toast.makeText(getApplicationContext(),"press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backpressedTime = System.currentTimeMillis();
    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

}