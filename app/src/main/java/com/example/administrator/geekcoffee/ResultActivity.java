package com.example.administrator.geekcoffee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2015/4/26 0026.
 */

public class ResultActivity extends Activity{
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        final List<String> result = intent.getStringArrayListExtra("extra_data");
        String str="";
        tv= (TextView) findViewById(R.id.tv_result);
        if(result.size()>0){
            for(int i=0;i<result.size()-1;i++){
                str+=result.get(i)+"\n";
            }
            str+=result.get(result.size()-1);
        }else if(result.size()==0){

        }
        tv.setText(str);
    }
}
