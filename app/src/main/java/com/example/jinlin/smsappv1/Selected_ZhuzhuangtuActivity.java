package com.example.jinlin.smsappv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Selected_ZhuzhuangtuActivity extends AppCompatActivity {
    String zhuzhuangtuFile_0;//垃圾短信
    String zhuzhuangtuFile_1;//服务通知短信
    String zhuzhuangtuFile_2;//网购短信
    String zhuzhuangtuFile_3;//验证码短信
    String zhuzhuangtuFile_4;//诈骗短信
    String zhuzhuangtuFile_5;//支入支出短信
    String zhuzhuangtuFile_6;//其他短信


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_zhuzhuangtu);

        //填写相应路径
        zhuzhuangtuFile_0=getFilesDir().getAbsolutePath()+"/zhuzhuangtuFile_0.txt";
        zhuzhuangtuFile_1=getFilesDir().getAbsolutePath()+"/zhuzhuangtuFile_1.txt";

        //2019.09.28 TJL
        zhuzhuangtuFile_2=getFilesDir().getAbsolutePath()+"/zhuzhuangtuFile_2.txt";

        //zhuzhuangtuFile_2=MyConst.rootPath+MyConst.Second_WG;
        Log.d("TJLFILE","TJLFILE"+zhuzhuangtuFile_2);
        zhuzhuangtuFile_3=getFilesDir().getAbsolutePath()+"/zhuzhuangtuFile_3.txt";
        zhuzhuangtuFile_4=getFilesDir().getAbsolutePath()+"/zhuzhuangtuFile_4.txt";
        zhuzhuangtuFile_5=getFilesDir().getAbsolutePath()+"/zhuzhuangtuFile_5.txt";
        zhuzhuangtuFile_6=getFilesDir().getAbsolutePath()+"/zhuzhuangtuFile_6.txt";

        Button btn_back=(Button) findViewById(R.id.button6);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Do something in response to button
                Intent intent = new Intent(Selected_ZhuzhuangtuActivity.this, ZhuzhuangtuActivity.class);
                //在这里填入相应的文件路径
                intent.putExtra("filepath",zhuzhuangtuFile_2);
                startActivity(intent);
            }
        });


    }

    /** Called when the user taps the Send button */
    public void send_zhuzhuangtu_0(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ZhuzhuangtuActivity.class);
        //在这里填入相应的文件路径
        intent.putExtra("filepath",zhuzhuangtuFile_0);
        startActivity(intent);
    }
    public void send_zhuzhuangtu_1(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ZhuzhuangtuActivity.class);
        //在这里填入相应的文件路径
        intent.putExtra("filepath",zhuzhuangtuFile_1);
        startActivity(intent);
    }
    public void send_zhuzhuangtu_2(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ZhuzhuangtuActivity.class);
        //在这里填入相应的文件路径
        intent.putExtra("filepath",zhuzhuangtuFile_2);
        startActivity(intent);
    }

    public void send_zhuzhuangtu_3(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ZhuzhuangtuActivity.class);
        //在这里填入相应的文件路径
        intent.putExtra("filepath",zhuzhuangtuFile_3);
        startActivity(intent);
    }
    public void send_zhuzhuangtu_4(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ZhuzhuangtuActivity.class);
        //在这里填入相应的文件路径
        intent.putExtra("filepath",zhuzhuangtuFile_4);
        startActivity(intent);
    }
    public void send_zhuzhuangtu_5(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ZhuzhuangtuActivity.class);
        //在这里填入相应的文件路径
        intent.putExtra("filepath",zhuzhuangtuFile_5);
        startActivity(intent);
    }
    public void send_zhuzhuangtu_6(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ZhuzhuangtuActivity.class);
        //在这里填入相应的文件路径
        intent.putExtra("filepath",zhuzhuangtuFile_6);
        startActivity(intent);
    }
    }
