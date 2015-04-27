package com.example.administrator.geekcoffee;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/4/26 0026.
 */
public class CrossButtonLayout extends RelativeLayout{
    public CrossButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_cross,this);
        final Button btn_cut = (Button) findViewById(R.id.btn_cut);
        final Button btn_add = (Button) findViewById(R.id.btn_add);
        /*final Button cold = (Button) findViewById(R.id.cold);
        final Button hot = (Button) findViewById(R.id.hot);*/
        //TextView tv = (TextView) findViewById(R.id.cross_text);
        btn_cut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),btn_cut.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        btn_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),btn_add.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        /*cold.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        hot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
}
