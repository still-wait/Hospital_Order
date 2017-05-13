package ui.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hospital2.BaseActivity;
import com.example.hospital2.MainActivity;
import com.example.hospital2.R;
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
import bean.KeshiBean;
import ui.adapter.KeshiAdapter;

import static Internet.volley.url;


/**
 * Created by Y-GH on 2016/6/13.
 */
public class Keshi_select_repert extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private List<KeshiBean> List = new ArrayList<KeshiBean>();
    private ListView listview;
    private KeshiAdapter Adapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private static ProgressDialog dialog;
    private static String httpurl1 = url+":8080/xiaoyu/keshi";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keshi_view);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("选择科室(专家)");
        listview = (ListView) findViewById(R.id.keshi_list_view);
        Adapter = new KeshiAdapter(Keshi_select_repert.this,
                R.layout.keshi_list_item,List);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                List.clear();
                onGetkeshiData(Keshi_select_repert.this,2);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                KeshiBean keshibean = List.get(position);
                Intent intent = new Intent(Keshi_select_repert.this, gh_repert.class);
                intent.putExtra("keshi",keshibean.getName());
                startActivity(intent);
//                Toast.makeText(Keshi_select_repert.this,keshibean.getName(),Toast.LENGTH_LONG).show();
            }
        });

        initData();
    }

    private void initData() {
        /**
         * loading...
         */

        if (dialog == null) {
            dialog = new ProgressDialog(Keshi_select_repert.this);
        }
        dialog.setMessage("加载中...");
        dialog.setCancelable(false);
        dialog.show();
        onGetkeshiData(Keshi_select_repert.this,1);
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
                    listview.setAdapter(Adapter);
                    break;
                case 2:
                    listview.setAdapter(Adapter);
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
        params.put("userid", "tom");
        /**
         * loading...
         */

        if (dialog == null) {
            dialog = new ProgressDialog(context);
        }
        dialog.setMessage("加载中...");
        dialog.setCancelable(false);
        dialog.show();
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


}
