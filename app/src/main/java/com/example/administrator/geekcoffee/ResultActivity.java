package com.example.administrator.geekcoffee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.geekcoffee.sweet.SweetAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/4/26 0026.
 */

public class ResultActivity extends Activity {
    private TextView tv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        final List<String> result = intent.getStringArrayListExtra("result");
        final int sum = intent.getIntExtra("sum", 0);
        String str = "";
        tv = (TextView) findViewById(R.id.tv_result);
        btn = (Button) findViewById(R.id.btn_complete);
        if (result.size() > 0) {
            for (int i = 0; i < result.size() - 1; i++) {
                str += result.get(i) + "\n";
            }
            str += result.get(result.size() - 1);
        } else if (result.size() == 0) {

        }
        tv.setText(str);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInLeanCloud(result, sum);
            }
        });
        System.gc();
    }

    private void saveInLeanCloud(List<String> result, final int sum) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(ResultActivity.this, SweetAlertDialog.PROGRESS_TYPE)
            .setTitleText("Loading");
        Date now = new Date();
        /*SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        String d1 = df1.format( now );*/
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        final int d2 = Integer.parseInt(df2.format(now));
        String list = "";
        final AVQuery query = new AVQuery("List");
        pDialog.show();
        pDialog.setCancelable(true);
        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                query.cancel();
            }
        });
        for (int i = 0; i < result.size(); i++) {
            list += result.get(i) + "/";
        }
        final String finalList = list;
        query.whereContains("longID", String.valueOf(d2));
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
                    String id = String.valueOf((d2 * 100 + avObjects.size() + 1));
                    AVObject List = new AVObject("List");
                    List.put("longID", id);
                    List.put("list", finalList);
                    List.put("sum", sum);
                    List.saveInBackground();/*(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            //pDialog.dismiss();
                        }
                    });*///saveInBackground()后台保存
                    Toast.makeText(ResultActivity.this, "已提交", Toast.LENGTH_SHORT).show();
                    redirect();
                } else {
                    Log.d("失败", "查询错误: " + e.getMessage());
                }
            }
        });
    }

    private void redirect() {
        Intent intent = new Intent(ResultActivity.this, StaggeredGridLayoutActivity.class);
        //intent.putExtra("extra_data", "complete");
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
