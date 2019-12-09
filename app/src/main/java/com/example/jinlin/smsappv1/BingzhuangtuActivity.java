package com.example.jinlin.smsappv1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class BingzhuangtuActivity extends AppCompatActivity {
    public PieChart mChart;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingzhuangtu);
        Intent myi=getIntent();
        path=myi.getStringExtra("filepath");
        mChart = (PieChart) findViewById(R.id.pie_chart);
        PieData mPieData = getPieData(7, 100);
        showChart(mChart, mPieData);
        Toast.makeText(this, "饼图中数字表示百分比", Toast.LENGTH_LONG).show();

        //点击事件

       // mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
         //   @Override

         //   public void onValueSelected(Entry e, int i, Highlight highlight) {
          //      if (e==null)return;


                //此时e.getY()等于数据 由此判断点击了哪一个扇区
               // switch ( e.getXIndex()){
                 //   case 0:{
                 //       Intent intentToFeed = new Intent(BingzhuangtuActivity.this,Selected_ZhuzhuangtuActivity.class);
                //        startActivity(intentToFeed);
                    //    break;}
                    /*
                    case 0:{
                        Toast.makeText(getBaseContext(), "...0...", Toast.LENGTH_LONG).show();
                        break;}
                    case 1:{
                        Toast.makeText(getBaseContext(), "...1...", Toast.LENGTH_LONG).show();
                        break;}
                    case 2:{
                        Toast.makeText(getBaseContext(), "...2...", Toast.LENGTH_LONG).show();
                        break;}
                    case 3:{
                        Toast.makeText(getBaseContext(), "...3...", Toast.LENGTH_LONG).show();
                        break;}
                    case 4:{
                        Toast.makeText(getBaseContext(), "...4...", Toast.LENGTH_LONG).show();
                        break;}
                    case 5:{
                        Toast.makeText(getBaseContext(), "...5...", Toast.LENGTH_LONG).show();
                        break;}
                    case 6:{
                        Toast.makeText(getBaseContext(), "...6...", Toast.LENGTH_LONG).show();
                        break;}
                    case 7:{
                        Toast.makeText(getBaseContext(), "...7...", Toast.LENGTH_LONG).show();
                        break;}
                        */
                   // default:
                    //    Toast.makeText(getBaseContext(), "...-1...", Toast.LENGTH_LONG).show();
                    //    break;
                //}
        //    }

        //    @Override
       //     public void onNothingSelected() {

        //    }
      //  });
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆
        pieChart.setDescription(" ");
        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setCenterTextSize(20);
        pieChart.setDescriptionTextSize(20);
        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转
        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);
        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);
//      mChart.setOnAnimationListener(this);
        pieChart.setCenterText("短信种类分布");  //饼状图中间的文字
//        pieChart.animateY(3000);
        //设置数据
        pieChart.setData(pieData);
        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图

        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);//设置距离饼图的距离，防止与饼图重合
        mLegend.setYEntrySpace(5f);


        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        int index_line = 0;

        //   从指定文件中读取、处理数据

        File file = new File(path);
        InputStreamReader read = null;
        BufferedReader reader = null;
        try {
            read = new InputStreamReader(new FileInputStream(file),"utf-8");
            reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                if(index_line<count)//前count行为数据名称
                    xValues.add(line);
                else {
                    //最后后count行为相应数据的数量
                    float quarterly = Float.parseFloat(line);//将string转化为float
                    yValues.add(new Entry(quarterly, 0));
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


/*
        for (int i = 0; i < count; i++) {
            xValues.add("Quarterly" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
        }
*/


        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */

/*
        float quarterly1 = 14;
        float quarterly2 = 14;
        float quarterly3 = 34;
        float quarterly4 = 38;

        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));
        yValues.add(new Entry(quarterly4, 3));
*/
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        ArrayList<Integer> colors = new ArrayList<Integer>();
        // 饼图颜色
        for(int j = 0;j<count;j++) {
            int temp = j%8;
            if(temp == 0)  colors.add(Color.rgb(205, 205, 205));
            else if(temp == 1)  colors.add(Color.rgb(114, 188, 223));
            else if(temp == 2) colors.add(Color.rgb(255, 123, 124));
            //增加
            else if(temp == 3)  colors.add(Color.rgb(0, 255, 225));//天蓝
            else if(temp == 4) colors.add(Color.rgb(46, 139, 87));//墨绿
            else if(temp == 5)  colors.add(Color.rgb(106, 90, 205));//淡紫
            else if(temp == 6) colors.add(Color.rgb(255, 130, 71));//橘红
            else if(temp == 7)  colors.add(Color.rgb(255, 222, 173));//粉白
            else if(temp == 8) colors.add(Color.rgb(238, 106, 167));//粉红

            else colors.add(Color.rgb(57, 135, 200));
        }
        pieDataSet.setColors(colors);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    public void send_selected_zhuzhuangtu(View view) {
        // Do something in response to button
        //DEBUG:
        //Toast.makeText(this, MyConst.rootPath+MyConst.Second_WG, Toast.LENGTH_SHORT).show();
        Intent intentToFeed = new Intent(BingzhuangtuActivity.this,Selected_ZhuzhuangtuActivity.class);
        startActivity(intentToFeed);
    }
    //       Intent intentToFeed = new Intent(BingzhuangtuActivity.this,Selected_ZhuzhuangtuActivity.class);
    //        startActivity(intentToFeed);
}
