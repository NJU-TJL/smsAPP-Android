package com.example.jinlin.smsappv1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.moxun.tagcloudlib.view.TagCloudView;


public class CiyunStar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciyustar);

        MyConst.counter=0;

        TagCloudView tagCloudView = (TagCloudView) findViewById(R.id.tag_cloud);
        tagCloudView.setBackgroundColor(Color.WHITE);

        TextTagsAdapter tagsAdapter = new TextTagsAdapter(new String[20]);
        tagCloudView.setAdapter(tagsAdapter);
    }

}
