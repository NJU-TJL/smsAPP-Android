package com.example.jinlin.smsappv1;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    EditText oldpassword;
    EditText newpassword;
    CheckBox checkBox1;
    CheckBox checkBox2;
    LinearLayout linearLayout1;

    boolean firstUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        oldpassword=(EditText) findViewById(R.id.oldpassword);
        newpassword=(EditText) findViewById(R.id.newpassword);
        checkBox1 = (CheckBox) findViewById(R.id.CheckBox1);
        checkBox2 = (CheckBox) findViewById(R.id.CheckBox2);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if( isChecked){
                    //如果选中，显示密码
                    oldpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    oldpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if( isChecked){
                    //如果选中，显示密码
                    newpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    newpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        firstUse=false;
        if(!SPUtils.contains(this,"PASSWORD")){
            linearLayout1.setVisibility(View.GONE); // 隐藏
            firstUse=true;
        }

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

    public void setPassword(View v){
        if(firstUse){
            String newWord=newpassword.getText().toString();
            SPUtils.put(this,"PASSWORD",newWord);
            Toast.makeText(this,"密码设置成功！",Toast.LENGTH_LONG).show();
        }else{
            String oldWordTrue=(String)SPUtils.get(this,"PASSWORD","-1");
            String oldWord=oldpassword.getText().toString();
            if(oldWord.equals(oldWordTrue)){
                String newWord=newpassword.getText().toString();
                SPUtils.put(this,"PASSWORD",newWord);
                Toast.makeText(this,"新密码设置成功！",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"旧密码错误！",Toast.LENGTH_LONG).show();
            }
        }
    }

}
