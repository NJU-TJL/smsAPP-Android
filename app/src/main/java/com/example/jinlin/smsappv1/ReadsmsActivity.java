package com.example.jinlin.smsappv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

public class ReadsmsActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{


    private RadioGroup rg;
    int keyType=1;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //checkedId表示被选中的那个RadioButton的id
        switch(checkedId){
            case R.id.radio1:
                keyType=1;
                break;
            case R.id.radio2:
                keyType=2;
                break;
            case R.id.radio3:
                keyType=3;
                break;
            case R.id.radio4:
                keyType=4;
                break;

            case R.id.radio6:
                keyType=6;
                break;
            case R.id.radio7:
                keyType=7;
                break;
            case R.id.radio8:
                keyType=8;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readsms);

        //第一步：初始化控件（找到需要操作的控件）
        rg = (RadioGroup) findViewById(R.id.radioGroup1);
        //第二步：实现RadioGroup的监听事件
        rg.setOnCheckedChangeListener(this);


        //使显示返回按键
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("短信分类");
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



    public void goShow(View v){
        //Verion 1.0: 读取全部
//        Intent intentGo = new Intent(ReadsmsActivity.this, ShowsmsActivity.class);
//        if(rootFile == null ){
//            intentGo.putExtra("filepath", (String)null);
//        }else{
//            intentGo.putExtra("filepath", rootFile.getAbsolutePath());
//        }
//        startActivity(intentGo);//启动另一个activity

        //V 2.0
        Intent intentGo = new Intent(ReadsmsActivity.this, ShowsmsActivity.class);
        //intentGo.putExtra("filepath",resRootPath+newestFilename);
        intentGo.putExtra("type", keyType);
        startActivity(intentGo);//启动另一个activity

    }



}
