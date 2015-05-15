package com.example.administrator.geekcoffee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.view.KeyEvent;
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
import com.example.administrator.geekcoffee.sweet.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

public class StaggeredGridLayoutActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final int MenuDrink = 0;
    private static final int MenuCake = 1;
    private int backCount = 0;
    private int position = 0;
    private int i = -1;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private RecyclerView mRecyclerView;
    private List<String> mDatas4Drink;
    private List<String> mDatas4Cake;
    private List<AVObject> mResult;
    private List<Integer> mPosition4Drink;
    private List<Integer> mPosition4Cake;
    private StaggeredHomeAdapter mStaggeredHomeAdapter;
    private StaggeredHomeAdapter[] mAdapter = new StaggeredHomeAdapter[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recyclerview);
        setupAVOSCloud(false);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        init();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);

        //LeanSave();//生成第一批数据
    }

    private void init() {//初始化数据
        mDatas4Drink = new ArrayList<String>();
        mDatas4Cake = new ArrayList<String>();
        mPosition4Drink = new ArrayList<Integer>();
        mPosition4Cake = new ArrayList<Integer>();
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);
        new CountDownTimer(800 * 10, 800) {
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis、
                i++;
                switch (i) {
                    case 0:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.gray_btn_bg_color));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.error_stroke_color));
                        break;
                    case 8:
                        pDialog.setTitleText("Error!").setConfirmText("检查网络").changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        });
                        break;
                }
            }

            public void onFinish() {
                i = -1;
                /*pDialog.setTitleText("Success!")
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);*/
            }
        }.start();

        AVQuery<AVObject> query = new AVQuery<AVObject>("Menu");
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    pDialog.dismiss();
                    Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
                    mResult = avObjects;
                    getAda(MenuCake);
                    getAda(MenuDrink);
                    onSectionAttached(position + 1);
                } else {
                    Log.d("失败", "查询错误: " + e.getMessage());
                }
            }
        });
    }

    private void getAda(int menu) {//获取需要的Ada
        if (menu == MenuDrink) {
            for (int i = 0; i < mResult.size(); i++) {
                if (mResult.get(i).getInt("type") != 2) {
                    mDatas4Drink.add(mResult.get(i).getString("name"));
                    mPosition4Drink.add(i);
                }
            }
            mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4Drink, mResult, mPosition4Drink);
        } else if (menu == MenuCake) {
            for (int i = 0; i < mResult.size(); i++) {
                if (mResult.get(i).getInt("type") == 2) {
                    mDatas4Cake.add(mResult.get(i).getString("name"));
                    mPosition4Cake.add(i);
                }
            }
            mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4Cake, mResult, mPosition4Cake);
        }
        mAdapter[menu] = mStaggeredHomeAdapter;
        initEvent();
    }

    private void setAda(int menu) {//设定Ada
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter[menu]);
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void LeanSave() {
        String[] nameArr = {"cafe", "cake", "strawberry", "melon", "lemon", "coke", "beer", "wine", "black forest", "puff"};
        int[] typeArr = {0, 2, 1, 1, 1, 0, 0, 0, 2, 2};
        for (int i = 0; i < 10; i++) {
            String name = nameArr[i];
            int price = (int) (Math.random() * 10 + 5);
            int type = typeArr[i];
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

    private void initEvent() {
        mStaggeredHomeAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int pos, Consumption mCon) {
                /*mCon.addmSum(pos);
                mCon.addmAmount();*/
            }

            @Override
            public void onItemLongClick(View view, int pos) {
                Toast.makeText(StaggeredGridLayoutActivity.this,
                        pos + " long click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAVOSCloud(boolean config) {
        if (!config) {
            AVOSCloud.initialize(getApplication(),
                    Config.APP_ID, Config.APP_KEY);
            return;
        }
    }

    //DrawerLayout点击切换
    @Override
    public void onNavigationDrawerItemSelected(int position) {//newInstance
        // update the main content by replacing fragments
        if (position == this.position) {
            return;
        }
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
                mTitle = getString(R.string.title_section1);//默认已调用
                position = MenuDrink;
                //if(mAdapter[MenuDrink]!=null) {
                setAda(MenuDrink);
                //}
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                position = MenuCake;
                setAda(MenuCake);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main_staggered, menu);//三个按钮
            //侧边栏
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    //导航条，包括呼出菜单按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.id_action_add:
                *//*mStaggeredHomeAdapter.addData(mDatas.size());*//*
                return true;*/
            case R.id.id_action_delete:
                //mStaggeredHomeAdapter.removeData(1);
                mAdapter[0].setmAmount();
                mAdapter[1].setmAmount();
                if (mAdapter[0].getAmount() + mAdapter[1].getAmount() == 0) {
                    return true;
                }
                Intent intent = new Intent(this, ResultActivity.class);
                ArrayList<String> temp = null;
                temp = mAdapter[MenuDrink].getResult();
                temp.addAll(mAdapter[MenuCake].getResult());
                intent.putStringArrayListExtra("result", temp);
                intent.putExtra("sum", mAdapter[MenuDrink].getSum() + mAdapter[MenuCake].getSum());
                startActivity(intent);
                return true;
            case R.id.id_action_reload:
                /*int size1 = mDatas4Drink.size();
                int size2 = mDatas4Cake.size();
                for(int i = 0; i < size1; i++){
                    mAdapter[0].removeData(0);
                }
                for(int i = 0; i < size2; i++){
                    mAdapter[1].removeData(0);
                }*/
                mAdapter[0].removeAll();
                mAdapter[1].removeAll();
                mAdapter[0].removeAllResult();
                LeanSave();
                init();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                backCount++;
                if (backCount == 2) {
                    finish();
                } else {
                    Toast.makeText(this, "再点击一次退出应用程序", Toast.LENGTH_SHORT).show();
                    new CountDownTimer(800 * 4, 500) {
                        public void onTick(long millisUntilFinished) {
                            // you can change the progress bar color by ProgressHelper every 800 millis、
                        }

                        public void onFinish() {
                            backCount = 0;
                        }
                    }.start();
                }
                return false;
            }

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
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
        public void onAttach(Activity activity) {//onSectionAttached
            super.onAttach(activity);
            ((StaggeredGridLayoutActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));//获取菜单的number
        }
    }
}
