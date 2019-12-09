package com.example.jinlin.smsappv1;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowsmsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    File smsFile;
    ArrayList<MySMS> mySMSs;
    String smsBodyForCopy;
    int keyType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showsms);

        mySMSs=new ArrayList<MySMS>();
        Intent myi=getIntent();
        //String filePath=myi.getStringExtra("filepath");
        keyType = myi.getIntExtra("type",-1);

        String filePath=SPUtils.get(this,"Results_FilePath","-1").toString();

        //使显示返回按键
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("短信列表 - " + MyConst.whatType(keyType));
        actionBar.setDisplayHomeAsUpEnabled(true);

        //TODO
        if(filePath.equals("-1")){
            Toast.makeText(this, "初次使用，请先”读取短信“", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }else{
            makeMySMSlist(filePath);
        }

        List<Map<String,?>> listItems=new ArrayList<Map<String,?>>();
        for(MySMS temp:mySMSs){
            Map<String,String> tmpMap= new HashMap<String ,String>();
            //TODO:格式
//            tmpMap.put("body",temp.body);
//            tmpMap.put("time","时间："+temp.time);
//            tmpMap.put("number","来自："+temp.number);
            tmpMap.put("body",temp.body);
            tmpMap.put("time",temp.time);
            tmpMap.put("number",temp.number);
            listItems.add(tmpMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.smsitem_layout,
                new String[]{"body","time","number"},
                new int[]{R.id.smsbody,R.id.smstime,R.id.smsnumber});
        ListView list=(ListView)findViewById(R.id.smslist);
        list.setOnItemClickListener(this);
        list.setAdapter(simpleAdapter);
    }

    void makeMySMSlist(String filepath){
        smsFile=new File(filepath);
        //生成List
        try {
            FileReader fr = new FileReader(smsFile);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            int state=0;
            MySMS tempSMS=new MySMS();
            while ((str = bf.readLine()) != null) {
                if(str.equals("!@#$%^&") && state==0){
                    state=1;
                }
                else if(str.equals("!@#$%^&") && state==1) {
                    state=2;
                }
                else if(state==1){
                    tempSMS.body+=(str+"\n");
                }
                else if(state==2){
                    tempSMS.number=str;
                    state=3;
                }
                else if(state==3){
                    tempSMS.time=str;
                    state=4;
                }
                else if(state==4){
                    if(!str.equals("0")&&!str.equals("null")) {
                        tempSMS.known = true;
                    }else{
                        tempSMS.known = false;
                    }
                    state=5;
                }
                else if(state==5){
                    int tempType=Integer.parseInt(str);
                    if(tempType == keyType || ((tempType==2||tempType==5)&&(keyType==2||keyType==5))){
                        mySMSs.add(tempSMS);
                    }else{
                        tempSMS=null;
                    }
                    tempSMS=new MySMS();
                    state=0;
                }
                else;
            }
            bf.close();
            fr.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    //activity类中的方法 ActionBar 显示返回箭头
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MySMS tempSMS=mySMSs.get(i);
            smsBodyForCopy =tempSMS.body;
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("短信内容");
            StringBuilder sb=new StringBuilder();
        sb.append(tempSMS.body);
        sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
        sb.append("来自："+tempSMS.number+"\n");
        sb.append("时间："+tempSMS.time+"\n");
        dialog.setMessage(sb);
        dialog.setNegativeButton("取消",null).setPositiveButton("复制",new DialogInterface.OnClickListener(){
                @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("smsbody", smsBodyForCopy);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(ShowsmsActivity.this, "短信内容已复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}

class MySMS{
    String body; //短信内容体
    String time; //时间
    String number; //发送方电话号码
    boolean known; //是否是已知号码（此项貌似一致为0/null，实际没什么用，先留在这）
    int type=-1; //表示短信所属的类别（1-8个类，-1表示未分类）
    List<String> words;
    MySMS(){body="";} //body在读取时需要多次拼接(+=)，不初始化的话，开头会多出“null”四个字符
}

