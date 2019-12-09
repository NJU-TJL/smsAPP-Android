package com.example.jinlin.smsappv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChartdisplayActivity extends AppCompatActivity {
    String destFilePath;
    String bingzhuangtuFile;
    String zhexiantuFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartdisplay);

        //使显示返回按键
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("短信统计");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //文件
        destFilePath = SPUtils.get(this,"Results_FilePath","-1").toString();
        if(destFilePath.equals("-1")){
            Toast.makeText(getBaseContext(), "初次使用，请先在“短信分类”中点击“读取短信”按钮", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        bingzhuangtuFile=getFilesDir().getAbsolutePath()+"/bingzhuangtuFile.txt";
        zhexiantuFile=getFilesDir().getAbsolutePath()+"/zhexiantuFile.txt";

        StatisticsSMS myCaler = new StatisticsSMS(destFilePath);
        myCaler.category(bingzhuangtuFile);
        myCaler.hours(zhexiantuFile);

        int privateSMSnum = (Integer)SPUtils.get(this,"privateSMSnum",0);
        float income_num= (float) SPUtils.get(this,"income_num",(float)0.0);
        float pay_num= (float) SPUtils.get(this,"pay_num",(float)0.0);
        //关于短信数据，需要显示的文本信息
        String infoStatistics="短信总条数："+myCaler.mySMSs.size()+"条\n";
        infoStatistics+="（其中，私人号码短信："+privateSMSnum+"条）\n";
        infoStatistics+="统计到的支入支出情况如下：（大致）\n";
        infoStatistics+="支入："+income_num+"元\n";
        infoStatistics+="支出："+pay_num+"元\n";

        TextView textView4 = (TextView)findViewById(R.id.textView4);
        textView4.setText(infoStatistics);

        //DEBUG
        //textView4.setText(MyDebugger.showFile2Str(bingzhuangtuFile));
        //Toast.makeText(getBaseContext(), "...统计结束√...\n短信总条数："+myCaler.mySMSs.size(), Toast.LENGTH_LONG).show();
//        MyDebugger.showFile(bingzhuangtuFile);
//        MyDebugger.showFile(zhexiantuFile);

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

    /** Called when the user taps the Send button */
    public void send_bingzhuangtu(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, BingzhuangtuActivity.class);
        intent.putExtra("filepath",bingzhuangtuFile);
        startActivity(intent);
    }
    public void send_zhexiantu(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ZhexiantuActivity.class);
        intent.putExtra("filepath",zhexiantuFile);
        startActivity(intent);
    }

    public void send_ciyunstar(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, CiyunStar.class);
        startActivity(intent);
    }

}
