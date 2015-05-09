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
        /*btn_cut.setOnClickListener(new OnClickListener() {
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
        });*/
    }
}
