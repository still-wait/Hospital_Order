package com.example.hospital2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Internet.NormalPostRequest2;
import bean.KeshiBean;
import login.LoginActivity;
import ui.adapter.KeshiAdapter;
import ui.loading.Loading;
import ui.main.DiscriptionDialog;
import ui.main.Keshi_select;
import ui.main.Keshi_select_repert;
import ui.main.SetOrderActivity;
import ui.main.gh_repert;
import ui.me.LogoutDialogFragment;
import Internet.volley;
import ui.me.OrderListActivity;

import static Internet.volley.url;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<KeshiBean> List = new ArrayList<KeshiBean>();
    private ListView listview;
    private KeshiAdapter Adapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private static ProgressDialog dialog;
    private static String httpurl1 = url+":8080/xiaoyu/keshifirst";
    private ViewPager mViewPaper;
    private List<ImageView> images;
    private int currentItem;
    //记录上一次点的位置
    private int oldPosition = 0;
    private List<View> dots;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e
    };
    private ViewPagerAdapter tupianadapter;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatButton();
        initView();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.main_keshi_list_view);
        Adapter = new KeshiAdapter(MainActivity.this,
                R.layout.keshi_list_item,List);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                List.clear();
                onGetkeshiData(MainActivity.this,2);

            }
        });
        /**
         *轮播
         */
        mViewPaper = (ViewPager) findViewById(R.id.vp);
        //显示的图片
        images = new ArrayList<ImageView>();
        for(int i = 0; i < imageIds.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<View>();
        dots.add(findViewById(R.id.dot_0));
        dots.add(findViewById(R.id.dot_1));
        dots.add(findViewById(R.id.dot_2));
        dots.add(findViewById(R.id.dot_3));
        dots.add(findViewById(R.id.dot_4));
        initPager();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                KeshiBean keshibean = List.get(position);
                showLoginDialog(keshibean.getDiscription());
//                Toast.makeText(Keshi_select.this,keshibean.getName(),Toast.LENGTH_LONG).show();
            }
        });

        initData();
    }

    private void initPager() {
        tupianadapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(tupianadapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageSelected(int position) {
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initData() {
        /**
         * loading...
         */

        if (dialog == null) {
            dialog = new ProgressDialog(MainActivity.this);
        }
        dialog.setMessage("加载中...");
        dialog.setCancelable(false);
        dialog.show();
        onGetkeshiData(MainActivity.this,1);
    }



    private Handler mHandler = new Handler() {
        @SuppressLint("LongLogTag")
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
                    if (dialog != null) {
                        dialog.cancel();
                        dialog = null;
                    }
                    Log.e("list长度=====》",List.size()+"");
                    listview.setAdapter(Adapter);
                    break;
                case 2:
                    listview.setAdapter(Adapter);
                    swipyRefreshLayout.setRefreshing(false);
                    break;
                case 3:
                    /**
                     * 接收子线程传递过来的数据
                     */
                    mViewPaper.setCurrentItem(currentItem);
                    break;
            }
        };
    };


    /**
     * 这是右下角“+”floatbutton事件
     */
    private void FloatButton() {
        final FloatingActionsMenu floatMenu  = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        com.getbase.floatingactionbutton.FloatingActionButton ordinary = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.ordinary);
        ordinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//普通挂号
                //逻辑这里添加
                Intent intent = new Intent(MainActivity.this, Keshi_select.class);
                startActivity(intent);
                floatMenu.toggle();
            }
        });
        com.getbase.floatingactionbutton.FloatingActionButton expert = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.repert);
        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//专家挂号
                //逻辑这里添加
                Intent intent = new Intent(MainActivity.this, Keshi_select_repert.class);
                startActivity(intent);
                floatMenu.toggle();
            }
        });
    }


    /**
     * 详情
     *
     * @param discription
     */
    private void showLoginDialog(String discription) {
        DiscriptionDialog loginDialog = new DiscriptionDialog();
        loginDialog.setDialogListener(dialogListener);
        loginDialog.show(getSupportFragmentManager(), discription);
    }
    DiscriptionDialog.LoginDialogListener dialogListener = new DiscriptionDialog.LoginDialogListener() {

        @Override
        public void getNameAddr(String username) {
        }



    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_guahao) {
            Intent intent = new Intent(MainActivity.this, OrderListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sort) {

        } else if (id == R.id.nav_mymessage) {

        } else if (id == R.id.nav_about) {

        }else if (id == R.id.nav_exit) {
            LogoutDialogFragment fragment = new LogoutDialogFragment();
            fragment.show(getFragmentManager(), null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 获取科室列表
     * @param context
     */
    private void onGetkeshiData(final Context context, final int FLAG) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", "tom");

        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        Request<JSONArray> request = new NormalPostRequest2(httpurl1,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("加载成功", response.toString() );
                        if(response!=null){
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject json = response.getJSONObject(i);
                                    KeshiBean bean = new KeshiBean(json.getString("keshiName"),json.getString("discription"),json.getString("mark"),null);
                                    List.add(bean);
                                    Log.e("-----从服务器取得------", "----"+json.getString("keshiName"));
                                }
                                mHandler.sendEmptyMessage(FLAG);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {

                            if (dialog != null) {
                                dialog.cancel();
                                dialog = null;
                            }
                            Toast.makeText(context,"内部错误", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(context, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }

    /**
     * 轮播效果实现
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub
//          super.destroyItem(container, position, object);
//          view.removeView(view.getChildAt(position));
//          view.removeViewAt(position);
            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }

    }

    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }


    /**
     * 图片轮播任务
     * @author liuyazhuang
     *
     */
    private class ViewPageTask implements Runnable{

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(3);
        }
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

}
