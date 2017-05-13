package ui.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hospital2.BaseActivity;
import com.example.hospital2.R;
import com.githang.statusbar.StatusBarCompat;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Internet.NormalPostRequest2;
import bean.DoctorBean;
import bean.OrdinaryBean;
import bean.RepertBean;
import ui.adapter.OrdinaryAdapter;
import ui.adapter.RepertAdapter;

import static Internet.volley.url;


/**
 * Created by Y-GH on 2016/6/13.
 */
public class gh_ordinary extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private List<OrdinaryBean> List = new ArrayList<OrdinaryBean>();
    private ListView listview;
    private OrdinaryAdapter ordinaryAdapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private static ProgressDialog dialog;
    private static String httpurl1 = url+":8080/xiaoyu/ordinarylist";
    private String keshi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gh_ordinary);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("普通挂号");
        listview = (ListView) findViewById(R.id.ordinary_list_view);
        ordinaryAdapter = new OrdinaryAdapter(gh_ordinary.this,
                R.layout.guahao_ordinary_item,List);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout1);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                List.clear();
                onGetkeshiData(gh_ordinary.this,2);
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrdinaryBean bean = List.get(position);
                Intent intent = new Intent(gh_ordinary.this, SetOrderActivity.class);
                intent.putExtra("keshi",bean.getKeshi_name());
                intent.putExtra("category","普通号");
                intent.putExtra("time",bean.getTime());
                intent.putExtra("sort",bean.getKeshihaoyue());
                intent.putExtra("repert","暂无信息");
                startActivity(intent);
//                Toast.makeText(gh_ordinary.this,bean.getKeshi_name(),Toast.LENGTH_LONG).show();
            }
        });
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        keshi = bundle.getString("keshi");
        /**
         * loading...
         */

        if (dialog == null) {
            dialog = new ProgressDialog(gh_ordinary.this);
        }
        dialog.setMessage("加载中...");
        dialog.setCancelable(false);
        dialog.show();
        onGetkeshiData(gh_ordinary.this,1);
    }

    @Override
    public void onClick(View v) {

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
                    listview.setAdapter(ordinaryAdapter);
                    break;
                case 2:
                    listview.setAdapter(ordinaryAdapter);
                    swipyRefreshLayout.setRefreshing(false);
                    break;
            }
        };
    };

    /**
     * 获取科室列表
     * @param context
     */
    private void onGetkeshiData(final Context context, final int FLAG) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("keshiname", keshi);

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
                                    OrdinaryBean bean = new OrdinaryBean(json.getString("keshiname"),
                                            null,json.getString("keshihaoyue"),
                                            json.getString("keshizhuangtai"),null,
                                            json.getString("keshiriqi"));
                                    List.add(bean);
                                    Log.e("-----从服务器取得------", "----"+json.getString("keshiname"));
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

}
