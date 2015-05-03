package com.example.administrator.geekcoffee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.geekcoffee.StaggeredHomeAdapter.OnItemClickLitener;

import java.util.ArrayList;
import java.util.List;

public class StaggeredGridLayoutActivity extends ActionBarActivity  implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
	private RecyclerView mRecyclerView;
	private List<String> mDatas;
    private List<AVObject> mResult;
	private StaggeredHomeAdapter mStaggeredHomeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recyclerview);
        /*Intent intent = getIntent();
        String str = intent.getStringExtra("extra_data");
        if(str!=""&&str!=null){
            Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
        }*/

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        setupAVOSCloud(false);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        //LeanSave();生成第一批数据

        initData();//init mDatas

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
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
		mStaggeredHomeAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int pos) {
                Toast.makeText(StaggeredGridLayoutActivity.this,
                        pos + " click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int pos) {
                Toast.makeText(StaggeredGridLayoutActivity.this,
                        pos + " long click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNumAddClick(View view, int pos, Consumption mCon) {
                //由于AlertDialog较难分离
                mCon.addmSum(pos);
                mCon.addmAmount();
            }

            @Override
            public void onNumCutClick(View view, int pos, Consumption mCon) {
                if (mCon.getmDetail()[pos][mCon.getmSum(pos) - 1] == 1) {
                    mCon.cutcoldNum(pos);
                } else if (mCon.getmDetail()[pos][mCon.getmSum(pos) - 1] == 2) {
                    mCon.cuthotNum(pos);
                }
                mCon.getmDetail()[pos][mCon.getmSum(pos) - 1] = 0;
                mCon.cutmAmount();
                mCon.cutmSum(pos);//减少定的个数
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

    private void setupAVOSCloud(boolean config) {
        if (!config) {
            AVOSCloud.initialize(getApplication(),
                    Config.APP_ID, Config.APP_KEY);
            //Toast.makeText(this,Config.APP_ID+Config.APP_KEY,Toast.LENGTH_SHORT).show();
            return;
        }
    }

    //DrawerLayout点击切换
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }
    //重置ActionBar
        public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
    //替换标题
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main_staggered, menu);
            //侧边栏
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.id_action_add:
                mStaggeredHomeAdapter.addData(mDatas.size());
                return true;
            case R.id.id_action_delete:
                //mStaggeredHomeAdapter.removeData(1);
                if(mStaggeredHomeAdapter.getAmount()==0){
                    return true;
                }
                Intent intent = new Intent(this , ResultActivity.class);
                intent.putStringArrayListExtra("result", mStaggeredHomeAdapter.getResult());
                intent.putExtra("sum",mStaggeredHomeAdapter.getSum());
                startActivity(intent);
                return true;
            case R.id.id_action_reload:
                int j=mDatas.size();
                for(int i=0;i<j;i++){
                    mStaggeredHomeAdapter.removeData(0);
                }
                LeanSave();
                initData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((StaggeredGridLayoutActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
