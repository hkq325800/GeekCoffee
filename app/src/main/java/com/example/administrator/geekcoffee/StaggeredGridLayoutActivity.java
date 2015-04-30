package com.example.administrator.geekcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.geekcoffee.StaggeredHomeAdapter.OnItemClickLitener;

import java.util.ArrayList;
import java.util.List;

public class StaggeredGridLayoutActivity extends ActionBarActivity
{

	private RecyclerView mRecyclerView;
	private List<String> mDatas;
    private List<AVObject> mResult;
	private StaggeredHomeAdapter mStaggeredHomeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recyclerview);

        setupAVOSCloud(false);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        //LeanSave();生成第一批数据

        initData();//init mDatas






	}

    private void LeanSave() {
        String[] nameArr = {"cafe","cake","strawberry","melon","lemon","coke","beer","wine","black forest","puff"};
        Boolean[] typeArr = {true,false,true,true,true,true,true,true,false,false};
        for(int i=0;i<10;i++){
            String name=nameArr[i];
            int price = (int) (Math.random()*10+5);
            boolean type = typeArr[i];
            AVObject Menu = new AVObject("Menu");
            Menu.put("name", name);
            Menu.put("price", price);
            Menu.put("type", type);
            try {
                Menu.save();//saveInBackground()后台保存
            } catch (AVException e) {
                Log.e("avosave", e.getMessage()); //捕获的异常信息
            }
        }

    }

    private void initEvent()
	{
		mStaggeredHomeAdapter.setOnItemClickLitener(new OnItemClickLitener()
		{
			@Override
			public void onItemClick(View view, int position)
			{
				Toast.makeText(StaggeredGridLayoutActivity.this,
						position + " click", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onItemLongClick(View view, int position)
			{
				Toast.makeText(StaggeredGridLayoutActivity.this,
						position + " long click", Toast.LENGTH_SHORT).show();
			}

            @Override
            public void onNumAddClick(View view, int position) {

            }

            @Override
            public void onNumCutClick(View view, int position) {

            }
        });
	}

	protected void initData()
	{
		mDatas = new ArrayList<String>();
        AVQuery<AVObject> query = new AVQuery<AVObject>("Menu");
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    //Toast.makeText(StaggeredGridLayoutActivity.this,"查询到" + avObjects.size() + " 条符合条件的数据",Toast.LENGTH_SHORT).show();
                    Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
                    for(int i=0;i<avObjects.size();i++){
                        mDatas.add(avObjects.get(i).getString("name"));
                    }
                    mResult=avObjects;
                    mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
                    mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas,mResult);
                    mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                            StaggeredGridLayoutManager.VERTICAL));
                    mRecyclerView.setAdapter(mStaggeredHomeAdapter);
                    // 设置item动画
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    initEvent();

                } else {
                    Log.d("失败", "查询错误: " + e.getMessage());
                }
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_staggered, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
            case R.id.id_action_add:
                mStaggeredHomeAdapter.addData(mDatas.size());
                break;
            case R.id.id_action_delete:
                //mStaggeredHomeAdapter.removeData(1);
                Intent intent = new Intent(this , ResultActivity.class);
                intent.putStringArrayListExtra("extra_data", mStaggeredHomeAdapter.getResult());
                startActivity(intent);
                break;
            case R.id.id_action_reload:
                int j=mDatas.size();
                for(int i=0;i<j;i++){
                    mStaggeredHomeAdapter.removeData(0);
                }
                LeanSave();
                initData();
                break;
		}
		return true;
	}

    private void setupAVOSCloud(boolean config) {
        if (!config) {
            AVOSCloud.initialize(getApplication(),
                    Config.APP_ID, Config.APP_KEY);
            //Toast.makeText(this,Config.APP_ID+Config.APP_KEY,Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
