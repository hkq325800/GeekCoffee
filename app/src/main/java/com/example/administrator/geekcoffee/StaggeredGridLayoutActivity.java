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
    private static final int MenuHuashi = 0;
    private static final int MenuDanpin = 1;
    private static final int MenuTea = 2;
    private static final int MenuYinLiao = 3;
    private static final int MenuLingShi = 4;
    private static final int MenuXiaoChi = 5;
    private static final int MenuPiGuo = 6;
    private static final int MenuXiaRi = 7;
    private int backCount = 0;
    private int position = 0;
    private int i = -1;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private RecyclerView mRecyclerView;
    private List<String> mDatas4Huashi = new ArrayList<String>();
    private List<String> mDatas4Danpin = new ArrayList<String>();
    private List<String> mDatas4Tea = new ArrayList<String>();
    private List<String> mDatas4YinLiao = new ArrayList<String>();
    private List<String> mDatas4LingShi = new ArrayList<String>();
    private List<String> mDatas4XiaoChi = new ArrayList<String>();
    private List<String> mDatas4PiGuo = new ArrayList<String>();
    private List<String> mDatas4XiaRi = new ArrayList<String>();
    private List<AVObject> mResult;
    private List<Integer> mPosition4Huashi = new ArrayList<Integer>();
    private List<Integer> mPosition4Danpin = new ArrayList<Integer>();
    private List<Integer> mPosition4Tea = new ArrayList<Integer>();
    private List<Integer> mPosition4YinLiao = new ArrayList<Integer>();
    private List<Integer> mPosition4LingShi = new ArrayList<Integer>();
    private List<Integer> mPosition4XiaoChi = new ArrayList<Integer>();
    private List<Integer> mPosition4PiGuo = new ArrayList<Integer>();
    private List<Integer> mPosition4XiaRi = new ArrayList<Integer>();
    private StaggeredHomeAdapter mStaggeredHomeAdapter;
    private StaggeredHomeAdapter[] mAdapter = new StaggeredHomeAdapter[Config.Amount];
    private boolean[] isGetAda = new boolean[Config.Amount];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recyclerview);
        setupAVOSCloud();
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
    }

    private void init() {//初始化数据
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);
        new CountDownTimer(800 * 16, 800) {
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
                    case 7:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 8:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 9:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 10:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 11:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.gray_btn_bg_color));
                        break;
                    case 12:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 13:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.error_stroke_color));
                        break;
                    default:
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
                    Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
                    mResult = avObjects;
                    getAda(MenuHuashi);
                    mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
                    onSectionAttached(position + 1);
                    pDialog.dismiss();
                } else {
                    Log.d("失败", "查询错误: " + e.getMessage());
                }
            }
        });
    }

    private void getAda(int menu) {//获取需要的Ada
        switch (menu) {
            case MenuHuashi:
                for (int i = 0; i < mResult.size(); i++) {
                    int type = mResult.get(i).getInt("type");
                    if (type == 0 || type == 1 || type == 2) {
                        mDatas4Huashi.add(mResult.get(i).getString("name"));
                        mPosition4Huashi.add(i);
                    }
                }
                mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4Huashi, mResult, mPosition4Huashi);
                break;
            case MenuDanpin:
                for (int i = 0; i < mResult.size(); i++) {
                    if (mResult.get(i).getInt("type") == 3) {
                        mDatas4Danpin.add(mResult.get(i).getString("name"));
                        mPosition4Danpin.add(i);
                    }
                }
                mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4Danpin, mResult, mPosition4Danpin);
                break;
            case MenuTea:
                for (int i = 0; i < mResult.size(); i++) {
                    if (mResult.get(i).getInt("type") == 4 || mResult.get(i).getInt("type") == 5) {
                        mDatas4Tea.add(mResult.get(i).getString("name"));
                        mPosition4Tea.add(i);
                    }
                }
                mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4Tea, mResult, mPosition4Tea);
                break;
            case MenuYinLiao:
                for (int i = 0; i < mResult.size(); i++) {
                    if (mResult.get(i).getInt("type") == 6) {
                        mDatas4YinLiao.add(mResult.get(i).getString("name"));
                        mPosition4YinLiao.add(i);
                    }
                }
                mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4YinLiao, mResult, mPosition4YinLiao);
                break;
            case MenuLingShi:
                for (int i = 0; i < mResult.size(); i++) {
                    if (mResult.get(i).getInt("type") == 7) {
                        mDatas4LingShi.add(mResult.get(i).getString("name"));
                        mPosition4LingShi.add(i);
                    }
                }
                mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4LingShi, mResult, mPosition4LingShi);
                break;
            case MenuXiaoChi:
                for (int i = 0; i < mResult.size(); i++) {
                    if (mResult.get(i).getInt("type") == 8) {
                        mDatas4XiaoChi.add(mResult.get(i).getString("name"));
                        mPosition4XiaoChi.add(i);
                    }
                }
                mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4XiaoChi, mResult, mPosition4XiaoChi);
                break;
            case MenuPiGuo:
                for (int i = 0; i < mResult.size(); i++) {
                    if (mResult.get(i).getInt("type") == 9) {
                        mDatas4PiGuo.add(mResult.get(i).getString("name"));
                        mPosition4PiGuo.add(i);
                    }
                }
                mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4PiGuo, mResult, mPosition4PiGuo);
                break;
            case MenuXiaRi:
                for (int i = 0; i < mResult.size(); i++) {
                    if (mResult.get(i).getInt("type") == 10) {
                        mDatas4XiaRi.add(mResult.get(i).getString("name"));
                        mPosition4XiaRi.add(i);
                    }
                }
                mStaggeredHomeAdapter = new StaggeredHomeAdapter(StaggeredGridLayoutActivity.this, mDatas4XiaRi, mResult, mPosition4XiaRi);
                break;
            default:
                break;
        }
        mAdapter[menu] = mStaggeredHomeAdapter;
        isGetAda[menu] = true;
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
        String[] nameArr = {"Espresso", "美式咖啡", "拿铁", "卡布奇诺", "摩卡", "焦糖玛奇朵", "康宝兰", "法国牛奶咖啡",
                "玛罗奇诺咖啡", "奥地利凯撒混合", "爱尔兰咖啡", "摩卡冰杰伯", "波多尔去年的桂花", "皇家咖啡", "德国Eiskafee",
                "雪国之秋", "南国之恋",
                "黄金曼特宁", "极品巴西",
                "龙井奶茶", "普洱奶茶", "铁观音奶茶", "毛峰奶茶", "祁门红奶茶", "碧螺春奶茶", "杭白菊奶茶", "玫瑰奶茶",
                "蜜桃奶茶", "蓝莓奶茶", "柠檬红茶", "西湖龙井", "安溪铁观音", "洞庭碧螺春", "黄山毛峰", "安徽祁门红茶",
                "杭白菊花茶", "云南顶级滇红", "云南普洱", "玫瑰花茶", "蓝莓物语", "蜜恋时光",
                "热牛奶", "香蕉牛奶", "浓情热可可", "冰水", "橘子汁", "芒果汁",
                "奶香爆米花", "精装散装", "酱烤翅尖/翅根", "酱烤鸭脖/小鸡腿", "酱烤鸭", "皇家奶香蓝罐曲奇",
                "华夫饼", "芝士蛋糕", "Cup Cake", "牛奶饼干", "巧克力曲奇", "比萨",
                "百威", "Hoegaarden", "喜力", "1664", "干果拼盘", "什锦水果拼盘",
                "雪国之秋", "南国之恋", "德国Eiskafee", "摩卡冰杰伯", "香蕉船", "冰摩卡", "冰拿铁", "冰卡布奇诺", "冰焦糖玛奇朵"};
        int[] typeArr = {0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1,
                3, 3,
                4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                6, 6, 6, 6, 6, 6,
                7, 7, 7, 7, 7, 7,
                8, 8, 8, 8, 8, 8,
                9, 9, 9, 9, 9, 9,
                10, 10, 10, 10, 10, 10, 10, 10, 10};
        int[] priceArr = {15, 20, 35, 35, 38, 38, 35, 35, 35, 45, 45, 45, 45, 38, 38, 45, 45,
                50, 50,
                20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45,
                20, 25, 25, 10, 20, 20,
                15, 15, 15, 15, 15, 20,
                35, 20, 7, 25, 25, 38,
                35, 40, 35, 45, 35, 45,
                45, 45, 38, 45, 25, 41, 38, 38, 41};
        boolean[] isDeleted = {false, false, false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false,
                false, false,
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false,
                false, false, false, false, false, false,
                false, false, false, false, false, false,
                false, false, false, false, false, false,
                false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false};
        for (int i = 0; i < nameArr.length; i++) {
            //int price = (int) (Math.random() * 10 + 5);
            AVObject Menu = new AVObject("Menu");
            Menu.put("isDeleted", isDeleted[i]);
            Menu.put("name", nameArr[i]);
            Menu.put("price", priceArr[i]);
            Menu.put("type", typeArr[i]);
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

    private void setupAVOSCloud() {
        AVOSCloud.initialize(getApplication(),
                Config.APP_ID, Config.APP_KEY);
        return;
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
                position = MenuHuashi;
                setAda(MenuHuashi);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                position = MenuDanpin;
                if (mDatas4Danpin.size() == 0 || mPosition4Danpin.size() == 0) {
                    getAda(MenuDanpin);
                }
                setAda(MenuDanpin);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                position = MenuTea;
                if (mDatas4Tea.size() == 0 || mPosition4Tea.size() == 0) {
                    getAda(MenuTea);
                }
                setAda(MenuTea);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                position = MenuYinLiao;
                if (mDatas4YinLiao.size() == 0 || mPosition4YinLiao.size() == 0) {
                    getAda(MenuYinLiao);
                }
                setAda(MenuYinLiao);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                position = MenuLingShi;
                if (mDatas4LingShi.size() == 0 || mPosition4LingShi.size() == 0) {
                    getAda(MenuLingShi);
                }
                setAda(MenuLingShi);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                position = MenuXiaoChi;
                if (mDatas4XiaoChi.size() == 0 || mPosition4XiaoChi.size() == 0) {
                    getAda(MenuXiaoChi);
                }
                setAda(MenuXiaoChi);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                position = MenuPiGuo;
                if (mDatas4PiGuo.size() == 0 || mPosition4PiGuo.size() == 0) {
                    getAda(MenuPiGuo);
                }
                setAda(MenuPiGuo);
                break;
            case 8:
                mTitle = getString(R.string.title_section8);
                position = MenuXiaRi;
                if (mDatas4XiaRi.size() == 0 || mPosition4XiaRi.size() == 0) {
                    getAda(MenuXiaRi);
                }
                setAda(MenuXiaRi);
                break;
            default:
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
                int sum = 0;
                ArrayList<String> temp = new ArrayList<String>();
                for (int i = 0; i < Config.Amount; i++) {
                    if (isGetAda[i] == true) {
                        mAdapter[i].setmAmount();
                        sum += mAdapter[i].getAmount();
                        temp.addAll(mAdapter[i].getResult());
                    }
                }
                if (sum == 0) {
                    return true;
                }
                Intent intent = new Intent(this, ResultActivity.class);
                /*temp = mAdapter[MenuHuashi].getResult();
                temp.addAll(mAdapter[MenuDanpin].getResult());*/
                intent.putStringArrayListExtra("result", temp);
                intent.putExtra("sum", sum);
                startActivity(intent);
                return true;
            case R.id.id_action_reload:
                for(int i=0;i<Config.Amount;i++){
                    if(isGetAda[i]==true) {
                        mAdapter[i].removeAll();
                        mAdapter[i].removeAllResult();
                    }
                }
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
