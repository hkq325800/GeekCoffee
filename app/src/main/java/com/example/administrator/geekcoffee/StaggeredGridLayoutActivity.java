package com.example.administrator.geekcoffee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.geekcoffee.StaggeredHomeAdapter.OnItemClickLitener;

import java.util.ArrayList;
import java.util.List;

public class StaggeredGridLayoutActivity extends ActionBarActivity
{

	private RecyclerView mRecyclerView;
	private List<String> mDatas;
	private StaggeredHomeAdapter mStaggeredHomeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recyclerview);

		initData();//init mDatas

		mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
		mStaggeredHomeAdapter = new StaggeredHomeAdapter(this, mDatas);

		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL));
		mRecyclerView.setAdapter(mStaggeredHomeAdapter);
		// 设置item动画
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		initEvent();

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

            /*@Override
            public void onColdClick(View view, int position) {

            }

            @Override
            public void onHotClick(View view, int position) {

            }*/

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
		for (int i = 'A'; i < 'Z'; i++)
		{
			mDatas.add("" + (char) i);
		}
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
		}
		return true;
	}

}
