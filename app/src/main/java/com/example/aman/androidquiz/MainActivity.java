package com.example.aman.androidquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends ActionBarActivity {

    Button signup,login;
    EditText luid,lpass;
    SharedPreferences pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup = (Button) findViewById(R.id.signup);
        login=(Button)findViewById(R.id.button_login);
        luid=(EditText)findViewById(R.id.editText_login_uid);
        lpass=(EditText)findViewById(R.id.editText_login_pass);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            Toast.makeText(getApplicationContext(), "logout clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.setting) {
            Toast.makeText(getApplicationContext(), "setting clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }
    public void button_signup_click(View view){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    public void button_login_click(View view)
    {
        String user_id = luid.getText().toString();
        String password = lpass.getText().toString();

        new ExecuteTask().execute(user_id,password);
    }

    class ExecuteTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params){
            String res=PostData(params);
            return res;
        }

        @Override
        protected void onPostExecute(String result){
            if(result.trim().equals("success")){
                Intent intent=new Intent(getApplicationContext(),homepage.class);
                pr=getSharedPreferences("test",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=pr.edit();
                editor.putString("uid",luid.getText().toString());
                editor.commit();
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String PostData(String[] valuse) {
        String s = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.43.59/android/login.php");

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("user_id", valuse[0]));
            list.add(new BasicNameValuePair("password", valuse[1]));

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
}
