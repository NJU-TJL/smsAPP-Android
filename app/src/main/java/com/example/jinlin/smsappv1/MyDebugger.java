package com.example.jinlin.smsappv1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//调试辅助类
public class MyDebugger {
    //按行输出指定文件的全部内容
    public static void showFile(String destFilePath){
        File destFile=new File(destFilePath);
        if(!destFile.exists()){
            Log.d("FILE","文件"+destFilePath+"不存在！");
            return;
        }
        FileReader fr = null;
        try {
            fr = new FileReader(destFile);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            while ((str = bf.readLine()) != null) {
                Log.d("FILE",str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String showFile2Str(String destFilePath){
        File destFile=new File(destFilePath);
        if(!destFile.exists()){
            Log.d("FILE","文件"+destFilePath+"不存在！");
            return null;
        }
        FileReader fr = null;
        StringBuilder sbfile=new StringBuilder("");
        try {
            fr = new FileReader(destFile);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            while ((str = bf.readLine()) != null) {
                sbfile.append(str+"\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sbfile.toString();
    }
}
