package com.example.jinlin.smsappv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText mypassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(!SPUtils.contains(this,"PASSWORD")){
            Toast.makeText(this,"请先于“设置”页面设置密码后使用此功能...",Toast.LENGTH_LONG).show();
            finish();
        }
        mypassword = (EditText)findViewById(R.id.mypassword);

        //使显示返回按键
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("设置");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    //左上角返回箭头结束当前Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goLogin(View v){
        String myWord = mypassword.getText().toString();
        String trueWord = (String)SPUtils.get(this,"PASSWORD","-1");
        boolean passed=false;
        if(myWord.equals(trueWord)){
            passed=true;
            Toast.makeText(this, "密码正确！", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "密码错误！显示内容已加密处理...", Toast.LENGTH_SHORT).show();
        }
        Intent intentToFeed = new Intent(this,SecurityActivity.class);
        intentToFeed.putExtra("passed",passed);
        startActivity(intentToFeed);
    }

}
