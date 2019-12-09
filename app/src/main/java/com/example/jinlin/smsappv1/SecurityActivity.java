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
import android.util.Log;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    File smsFile;
    ArrayList<MySMS> mySMSs;
    String smsBodyForCopy;
    MessageNode myMesNode;

    boolean passed=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        Log.d("SSSSSSSSS","B0");
        Intent myi=getIntent();
        Log.d("SSSSSSSSS","B1");
        passed= myi.getBooleanExtra("passed",false);
        Log.d("SSSSSSSSS","A0");
        mySMSs=new ArrayList<MySMS>();
        Log.d("SSSSSSSSS","A1");
        String filePath=SPUtils.get(this,"Results_FilePath","-1").toString();
        Log.d("SSSSSSSSS","A2");
        //使显示返回按键
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("敏感词短信");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //TODO
        if(filePath.equals("-1")){
            Log.d("SSSSSSSSS","L2");
            Toast.makeText(this, "初次使用，请先“读取短信”", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }else{
            Log.d("SSSSSSSSS","L3");
            myMesNode = new MessageNode();
            Log.d("SSSSSSSSS","L4");
            InputStream fileIn = getResources().openRawResource(R.raw.dangerous_words);
            Log.d("SSSSSSSSS","L5");
            myMesNode.MN_file_input(fileIn);
            Log.d("SSSSSSSSS","L6");
            makeMySMSlist(filePath);
            try {
                fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        //提示信息
        if(passed && mySMSs.size()==0){
            Toast.makeText(this, "...所有短信均不含敏感词...", Toast.LENGTH_LONG).show();
        }
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
            String addStr;
            String tempSMSbody="";
            while ((str = bf.readLine()) != null) {
                if(passed){
                    addStr=str;
                }else{
                    addStr=MyConst.change2str(str);
                }
                if(str.equals("!@#$%^&") && state==0){
                    state=1;
                }
                else if(str.equals("!@#$%^&") && state==1) {
                    state=2;
                }
                else if(state==1){
                    tempSMSbody=str;
                    tempSMS.body+=(addStr+"\n");
                }
                else if(state==2){
                    tempSMS.number=addStr;
                    state=3;
                }
                else if(state==3){
                    tempSMS.time=addStr;
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
                    tempSMS.type=Integer.parseInt(str);
                    tempSMS.words=myMesNode.MN_match(tempSMSbody);
                    if(tempSMS.words==null || tempSMS.words.isEmpty()){
                        tempSMS=null;
                    }else{
                        mySMSs.add(tempSMS);
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
        String TpyeName=MyConst.whatType(tempSMS.type);
        String wordString=MyConst.wordsList2String(tempSMS.words,"、");
        if(passed){
            sb.append("所属类型："+TpyeName+"\n");
            sb.append("所含敏感词："+wordString+"\n");
        }else{
            sb.append("所属类型："+MyConst.change2str(TpyeName)+"\n");
            sb.append("所含敏感词："+MyConst.change2str(wordString)+"\n");
        }
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
                Toast.makeText(SecurityActivity.this, "短信内容已复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

}
