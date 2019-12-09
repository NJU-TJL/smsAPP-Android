package com.example.jinlin.smsappv1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatisticsSMS {
    File smsFile;
    public ArrayList<MySMS> mySMSs;
    public Double income_num;  //支入总数
    public Double pay_num;     //支出总数


    StatisticsSMS(String filePath){
        makeMySMSlist(filePath);
        Log.d("FILE","Size："+mySMSs.size());
    }

    public void category(String destFilePath){
        int num[]=new int[8];
        for(MySMS iSMS:mySMSs){
            num[iSMS.type-1]++;
        }

        StringBuilder sbForFile=new StringBuilder("");
        //sbForFile.append("8\n");
        sbForFile.append("垃圾短信("+num[0]+"条)\n");
        sbForFile.append("服务通知类短信("+(num[1]+num[4])+"条)\n");
        sbForFile.append("网购短信("+num[2]+"条)\n");
        sbForFile.append("验证码短信("+num[3]+"条)\n");
        //sbForFile.append("移动运营商短信("+num[4]+"条)\n");
        sbForFile.append("诈骗短信("+num[5]+"条)\n");
        sbForFile.append("支入支出短信("+num[6]+"条)\n");
        sbForFile.append("其他短信("+num[7]+"条)\n");
        for(int i=0;i<8;i++){
            if(i==4) continue;
            if(i==1) {
                sbForFile.append((num[1]+num[4])).append("\n");
            }else{
                sbForFile.append(num[i]).append("\n");
            }
        }

        File destFile=new File(destFilePath);
        if(!destFile.exists()){
            try {
                destFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Writer out = new FileWriter(destFile);
            out.write(sbForFile.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("统计结果写入文件失败！- "+destFile.getAbsolutePath());
        }
    }

    public void hours(String destFilePath){
        int num[]=new int[24];
        for(MySMS iSMS:mySMSs){
            int keyHour=0;

            //从String类型的时间 获取 小时 字段
            Date date1=null;
            SimpleDateFormat simdate1=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
            try {
                date1=simdate1.parse(iSMS.time);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("TIME","时间提取错误");
            }
            SimpleDateFormat simdate2=new SimpleDateFormat("HH"); //24小时制
            String theHour=simdate2.format(date1);
            keyHour = Integer.parseInt(theHour);

            num[keyHour]++;
        }

        StringBuilder sbForFile=new StringBuilder("");
        //sbForFile.append("24\n");
        for(int i=0;i<24;i++){
            sbForFile.append(i).append("-").append(i + 1).append("时\n");
        }
        for(int i=0;i<24;i++){
            sbForFile.append(num[i]).append("\n");
        }

        //通用结构：结果写入文件
        File destFile=new File(destFilePath);
        if(!destFile.exists()){
            try {
                destFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Writer out = new FileWriter(destFile);
            out.write(sbForFile.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("统计结果写入文件失败！- "+destFile.getAbsolutePath());
        }
    }

    void makeMySMSlist(String filepath){
        mySMSs=new ArrayList<MySMS>();
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
                    tempSMS.type=tempType;
                    mySMSs.add(tempSMS);
                    tempSMS=new MySMS();
                    state=0;
                }
                else;
            }
            bf.close();
            fr.close();
        }catch (IOException e) {
            Log.d("FILE","IOEXCEPTION\n"+filepath);
            e.printStackTrace();
        }
    }

}
