package com.example.jinlin.smsappv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
//import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class ZhuzhuangtuActivity extends AppCompatActivity {
    //显示的图表
    public BarChart barChart;
    //保存数据的实体（下面定义了两组数据集合）
    public ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
   // public ArrayList<BarEntry> entries1 = new ArrayList<>();
    //数据的集合（每组数据都需要一个数据集合存放数据实体和该组的样式）
    public BarDataSet dataset;
    //public BarDataSet dataSet1;
    //表格下方的文字
    public ArrayList<String> labels = new ArrayList<String>();

    //读取文件的路径
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuzhuangtu);

        Intent myi=getIntent();
        path=myi.getStringExtra("filepath");

        barChart = (BarChart) findViewById(R.id.bar_chart);

        //读取文件数据
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个条形上的名称
        ArrayList<Float> yValues = new ArrayList<Float>();  //yVals用来表示每个条形的实际数据

        int index_line = 0;
        int count = 0;
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
                    count = Integer.parseInt(line);//读取第一行的数据总量
                    index_line++;
                    continue;
                }
                if(index_line<count+1)//前count行为数据名称
                    xValues.add(line);
                else {
                    //最后后count行为相应数据的数量
                    float quarterly = Float.parseFloat(line);//将string转化为float
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


        int xIndex = 0;
        while(xIndex<count) {
            entries.add(new BarEntry(yValues.get(xIndex), xIndex));  //设置条形图数值
            labels.add(xValues.get(xIndex));//设置条形图数值名称
            xIndex++;
        }

        dataset = new BarDataSet(entries, "图例");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataset);
     //   dataSets.add(dataSet1);
        BarData data = new BarData(labels, dataSets);
        barChart.setData(data);

        //   entries1.add(new BarEntry(4f, 0));
        //   entries1.add(new BarEntry(8f, 1));
        //   entries1.add(new BarEntry(6f, 2));
        //设置数据组的数据
        //  dataSet1 = new BarDataSet(entries1, "第二组数据");
        //设置数据组的样式（参数是显示颜色的数组）
        //     dataSet1.setColors(ColorTemplate.PASTEL_COLORS);
        //设置柱状图上方显示数据字体大小
        //    dataSet1.setValueTextSize(14);

        //设置单个点击事件
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                Toast.makeText(getApplicationContext(),entry.getVal()+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        //设置显示动画效果
        barChart.animateY(2000);
        //设置图标右下放显示文字
       // barChart.setDescription("MPandroidChart Test");

    }

}
