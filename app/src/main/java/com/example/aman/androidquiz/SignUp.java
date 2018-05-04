package com.example.aman.androidquiz;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

/**
 * Created by Aman on 24-03-2018.
 */

public class SignUp extends ActionBarActivity implements View.OnClickListener {
    EditText uid,pass,sq,ans;
    ProgressDialog progressDialogue;
    Button register_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        uid=(EditText)findViewById(R.id.editText_reg_uid);
        pass=(EditText)findViewById(R.id.editText_reg_pass);
        sq=(EditText)findViewById(R.id.editText_reg_sq);
        ans=(EditText)findViewById(R.id.editText_reg_ans);
        register_button=(Button)findViewById(R.id.register_button) ;

        register_button.setOnClickListener(this);
        

        progressDialogue = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        String user_id = uid.getText().toString();
        String password = pass.getText().toString();
        String security_question = sq.getText().toString();
        String answer = ans.getText().toString();

        new ExecuteTask().execute(user_id,password,security_question,answer);
    }

    class ExecuteTask extends AsyncTask<String,Integer,String>{
        @Override
       protected String doInBackground(String... params){
            String res=PostData(params);
            return res;
        }
        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            finish();
        }
    }

        public String PostData(String[] valuse) {
            String s = "";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://192.168.43.59/android/register.php");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("user_id", valuse[0]));
                list.add(new BasicNameValuePair("password", valuse[1]));
                list.add(new BasicNameValuePair("security_question", valuse[2]));
                list.add(new BasicNameValuePair("answer", valuse[3]));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
            } catch (Exception exception) {
            }
            return s;
        }

    public String readResponse(HttpResponse res){
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