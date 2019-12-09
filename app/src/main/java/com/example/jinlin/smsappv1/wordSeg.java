package com.example.jinlin.smsappv1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class wordSeg {
    private static String format="gbk";
    /*
     * 用于获知TXT文件的编码格式
     * */
    public static void resolveFormat(InputStream inputStream) throws Exception {
        byte[] head=new byte[3];
        inputStream.read(head);
        format="gbk";
        if(head[0]==-1&&head[1]==-2)
            format="UTF-16";
        else if(head[0]==-2&&head[1]==-1)
            format="Unicode";
        else if((head[0]==-17&&head[1]==-69&&head[2]==-65)||(head[0]==-27&&head[1]==-80&&head[2]==-118))
            format="UTF-8";
    }

    /*
     * 用来读取任意编写格式的TXT文件并将内容转为String返回
     * */
    public static String readFile(InputStream filein,String sep) {/*第一个为文件地址，第二个为每一行之间的分隔符，因为
    读取过程中会自动忽略换行符所以要想区分每一行需要添加换行符*/
        String fileContent = "";
        try
        {
            //resolveFormat(filein);
            Log.d("Security","I3");
            InputStreamReader read = new InputStreamReader(filein,"UTF-8");
            Log.d("Security","I4");
            BufferedReader reader=new BufferedReader(read);
            Log.d("Security","I5");
            String line;
            while ((line = reader.readLine()) != null)
            {
                fileContent += line;
                fileContent+=sep;
            }
            //fileContent=SplitS(fileContent,regex);
            read.close();
            Log.d("Security","I6");

        } catch (Exception e)
        {
            Log.d("Security","I7");
            e.printStackTrace();
        }
        return fileContent;
    }

}
