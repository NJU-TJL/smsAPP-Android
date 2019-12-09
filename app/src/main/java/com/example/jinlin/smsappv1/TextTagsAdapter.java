package com.example.jinlin.smsappv1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moxun.tagcloudlib.view.TagsAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TextTagsAdapter extends TagsAdapter {
    private List<String> dataSet = new ArrayList<>();

    String path = MyConst.rootPath+MyConst.wordCloud;

    public TextTagsAdapter(@NonNull String... data) {
        dataSet.clear();
        Collections.addAll(dataSet, data);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public View getView(final Context context, final int position, ViewGroup parent) {

        //从文件中读取数据
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示词语名称
        List<Integer> yValues = new ArrayList<Integer>();  //yVals用来表示词语的频率

        int index_line = 0;
        int count =0;
        //   从指定文件中读取、处理数据

        File file = new File(path);
        InputStreamReader read = null;
        BufferedReader reader = null;
        try {
            read = new InputStreamReader(new FileInputStream(file),"utf-8");
            reader = new BufferedReader(read);
            String line;

            while ((line = reader.readLine()) != null) {
                if(index_line == 0){
                    count = Integer.parseInt(line);//读取数据总数
                    index_line++;
                    continue;
                }
                if(index_line<=count)//前count行为数据名称
                    xValues.add(line);
                else {
                    //最后后count行为相应数据的数量
                    int quarterly = Integer.parseInt(line);//将string转化为float
                    yValues.add(quarterly);
                }
                index_line++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        Log.d("Tag","1："+count);
        TextView tv = new TextView(context);
        //TODO 决定标签 - Size
        //int SizePx=55-position*15/10;
        int SizePx=45;
        String firstLine="";
        String secondLine="";
        if(position<count){
            Log.d("Tag","2："+ position);
            firstLine=xValues.get(position);
            secondLine="("+ yValues.get(position)+"次)";
            final String topNumber=xValues.get(position);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyConst.showToastCenter(context,"TOP "+(position+1)+"\n“"+topNumber+"”", Toast.LENGTH_SHORT);
                }
            });
        }
        int WordLength=Math.max(MyConst.strLength(firstLine),MyConst.strLength(secondLine));
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams( WordLength*SizePx,  SizePx*30/10);
        tv.setText(firstLine+"\n"+secondLine);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizePx);
        tv.setLayoutParams(lp);
        return tv;
/*
//原数据处理
        String[] name = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        Random rand = new Random();
        int randNum = rand.nextInt(9);

        TextView tv = new TextView(context);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(100, 100);
        tv.setLayoutParams(lp);
        tv.setText("No." + name[randNum]);
        tv.setGravity(Gravity.CENTER);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "Tag " + position + " clicked.");
            }
        });
        return tv;
        */
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return position % 7;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {
        ((TextView) view).setTextColor(themeColor);
    }

}
