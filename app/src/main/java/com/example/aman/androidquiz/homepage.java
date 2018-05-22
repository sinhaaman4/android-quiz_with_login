package com.example.aman.androidquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 01-05-2018.
 */

public class homepage extends AppCompatActivity{
    private static final int REQUEST_CODE_QUIZ =1;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HiGHSCORE = "keyHighscore";

    private TextView textViewHighscore;
    private TextView textViewMaxscore;
    TextView textViewUserId;

    private int highscore;

    private int db_score;

    private int temp;

    String user_name;

    Button buttonStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        SharedPreferences sp = getSharedPreferences("test", Context.MODE_PRIVATE);
        String uid = sp.getString("uid","");

        textViewHighscore=(TextView)findViewById(R.id.text_view_highscore);
        textViewUserId=(TextView)findViewById(R.id.textViewUid);
        textViewMaxscore=(TextView)findViewById(R.id.maxScore);
        textViewUserId.setText("Welcome " + uid);
        user_name = uid;
        loadHighscore();
        getScore();

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
                temp=score;
                updateHighscore(score);

                if(score>db_score)
                {
                    updateDb(score);
                    textViewMaxscore.setText("Your Highest Score :"+score);
                    db_score+=1;
                }
                else{
                    textViewMaxscore.setText("Your Highest Score :"+db_score);
                }
            }
        }
    }



    private void getScore(){
        new ExecuteTask().execute(user_name);
    }

    class ExecuteTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params){
            String res=PostData(params);
            return res;
        }

        @Override
        protected void onPostExecute(String result){

            String r = result;

                db_score=Integer.parseInt(r.trim());
                textViewMaxscore.setText("Your Highest Score :"+db_score);
        }
    }

    public String PostData(String[] valuse) {
        String s = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.43.59/android/dbp.php");

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("user_id", valuse[0]));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
        } catch (Exception exception) {
        }
        return s;
    }

    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
        }
        catch(Exception e)
        {}
        return return_text;

    }





    public void updateDb(int newscore){
        String newScore = Integer.toString(newscore);
        new ExecuteTask1().execute(user_name,newScore);

    }
    class ExecuteTask1 extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params){
            String res1=PostData1(params);
            return res1;
        }

        @Override
        protected void onPostExecute(String result1){

        }
    }

    public String PostData1(String[] valuse) {
        String s1 = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.43.59/android/updateDbHighScore.php");

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("user_id", valuse[0]));
            list.add(new BasicNameValuePair("newScore", valuse[1]));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse1 = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse1.getEntity();
            s1 = readResponse1(httpResponse1);
        } catch (Exception exception) {
        }
        return s1;
    }

    public String readResponse1(HttpResponse res) {
        InputStream is1=null;
        String return_text1="";
        try {
            is1 = res.getEntity().getContent();
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(is1));
            String line1 = "";
            StringBuffer sb1 = new StringBuffer();
            while ((line1 = bufferedReader1.readLine()) != null) {
                sb1.append(line1);
            }
            return_text1 = sb1.toString();
        }
        catch(Exception e)
        {}
        return return_text1;

    }





    private void loadHighscore(){

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highscore=0;
        textViewHighscore.setText("Your Score: "+highscore);
    }
    private void updateHighscore(int  highscoreNew){
        highscore=highscoreNew;
        temp=highscoreNew;
        textViewHighscore.setText("Your Score: "+highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putInt(KEY_HiGHSCORE,highscore);
        editor.apply();
    }
}