package com.example.jinlin.smsappv1;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import static java.lang.Character.isLetter;

public final class MyConst {
    public static final int LJ = 1; //垃圾短信
    public static final int RJ = 2; //软件通知短信
    public static final int WG = 3; //网购短信
    public static final int YZM = 4;    //验证码短信
    public static final int YDYYS = 5;  //移动运营商短信
    public static final int ZP = 6; //诈骗短信
    public static final int ZRZC = 7;   //支入支出短信
    public static final int QT = 8; //其他短信
    public static String rootPath="";
    public static String wordCloud="wordCloud.txt";
    public static int counter=0;

    //2019/9/1
    public static String Second_WG = "zhuzhuangtuFile_2.txt";

//    //输入类别名称，返回类别对应的int值  2019.08.24：从未使用，目前已过时
//    public static int getSMStpye(String typename){
//        int type=-1;
//        if(typename.equals("垃圾短信"))
//            type=LJ;
//        else if(typename.equals("软件通知短信"))
//            type=RJ;
//        else if(typename.equals("网购短信"))
//            type=WG;
//        else if(typename.equals("验证码短信"))
//            type=YZM;
//        else if(typename.equals("移动运营商短信"))
//            type=YDYYS;
//        else if(typename.equals("诈骗短信"))
//            type=ZP;
//        else if(typename.equals("支入支出短信"))
//            type=ZRZC;
//        else if(typename.equals("其他短信"))
//            type=QT;
//        else;
//        return type;
//    }

    //输入int值，返回对应的类别名
    public static String whatType(int type){
        String typename="未知的短信类型";
        if(type==LJ)
            typename = "垃圾短信";
        else if(type==RJ || type==YDYYS)
            typename = "服务通知类短信";
        else if(type==WG)
            typename = "网购短信";
        else if(type==YZM)
            typename = "验证码短信";
        else if(type==ZP)
            typename = "诈骗短信";
        else if(type==ZRZC)
            typename = "支入支出短信";
        else if(type==QT)
            typename = "其他短信";
        else;
        return typename;
    }

    //非英文长度2，英文长度1
    public static int strLength(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    public static void showToastCenter(Context context, String toastStr, int LENGTH) {
        Toast toast = Toast.makeText(context.getApplicationContext(), toastStr, LENGTH);
        int tvToastId = Resources.getSystem().getIdentifier("message", "id", "android");
        TextView tvToast = ((TextView) toast.getView().findViewById(tvToastId));
        if(tvToast != null){
            tvToast.setGravity(Gravity.CENTER);
        }
        toast.show();
    }
    //将词语换成对应的一个String，以split分割
    public static String wordsList2String(List<String> ls,String split){
        StringBuilder res= new StringBuilder();
        int i;
        for(i=0;i<ls.size()-1;i++) {
            res.append(ls.get(i));
            res.append(split);
        }
        res.append(ls.get(i));
        return res.toString();
    }
    //字符串的简单加密：将所有字符换成'*'
    public static String change2str(String str)
    {
        String strNew= "";
//        try {
//            strNew = new String(str.getBytes("UTF-8"), "8859_1");
//        } catch (UnsupportedEncodingException e) {
//            Log.d("FILE","CATCH");
//            e.printStackTrace();
//        }
        byte b[] = new byte[str.length()];
        Arrays.fill(b, (byte) '*');
        strNew=new String(b);
        return strNew;
    }
}
