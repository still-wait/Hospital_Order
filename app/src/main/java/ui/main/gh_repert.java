package ui.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import bean.DoctorBean;
import bean.KeshiBean;
import bean.OrdinaryBean;
import bean.RepertBean;
import ui.adapter.RepertAdapter;

import static Internet.volley.url;


/**
 * 专家挂号
 * Created by Y-GH on 2017/5/10.
 */
public class gh_repert extends BaseActivity implements View.OnClickListener {
    private List<DoctorBean> List = new ArrayList<DoctorBean>();
    private ListView listview;
    private RepertAdapter repertAdapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private static ProgressDialog dialog;
    private static String httpurl1 = url+":8080/xiaoyu/doctorlist";
    private String keshi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gh_repert);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("专家挂号");
        listview = (ListView) findViewById(R.id.repert_list_view);
        repertAdapter = new RepertAdapter(gh_repert.this,
                R.layout.guahao_repert_item,List);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                List.clear();
                onGetkeshiData(gh_repert.this,2);
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DoctorBean bean = List.get(position);
                Intent intent = new Intent(gh_repert.this, SetOrderActivity.class);
                intent.putExtra("keshi",keshi);
                intent.putExtra("category","专家号");
                intent.putExtra("time",bean.getTime());
                intent.putExtra("sort",bean.getYishenghaoyuan());
                intent.putExtra("repert",bean.getDoctor_name());
                startActivity(intent);
                Toast.makeText(gh_repert.this,bean.getDoctor_name(),Toast.LENGTH_LONG).show();
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
            dialog = new ProgressDialog(gh_repert.this);
        }
        dialog.setMessage("加载中...");
        dialog.setCancelable(false);
        dialog.show();
        onGetkeshiData(gh_repert.this,1);
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
                    listview.setAdapter(repertAdapter);
                    break;
                case 2:
                    listview.setAdapter(repertAdapter);
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
                                    DoctorBean bean = new DoctorBean(json.getString("keshiname"),
                                            json.getString("yishengname"),json.getString("yishengrank"),
                                            json.getString("yishengxinxi"),json.getString("yishengjieshao"),
                                            json.getString("yishengriqi"),json.getString("yishengzhuangtai"),
                                            json.getString("yishenghaoyuan"),null);
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
