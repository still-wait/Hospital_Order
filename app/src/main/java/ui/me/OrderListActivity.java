package ui.me;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import bean.OrderBean;
import bean.OrdinaryBean;
import ui.adapter.OrderAdapter;
import ui.adapter.OrdinaryAdapter;
import ui.main.SetOrderActivity;

import static Internet.volley.url;


/**
 * Created by Y-GH on 2016/6/13.
 */
public class OrderListActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private List<OrderBean> List = new ArrayList<OrderBean>();
    private ListView listview;
    private OrderAdapter Adapter;
    private SwipyRefreshLayout swipyRefreshLayout;
    private static ProgressDialog dialog;
    private SharedPreferences pref;
    private static String httpurl1 = url+":8080/xiaoyu/orderlist";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("我的订单");
        pref = getSharedPreferences("user", MODE_PRIVATE);
        listview = (ListView) findViewById(R.id.order_list_view);
        Adapter = new OrderAdapter(OrderListActivity.this,
                R.layout.order_list_item,List);

        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                List.clear();
                onGetkeshiData(OrderListActivity.this,2);
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderBean bean = List.get(position);
                Intent intent = new Intent(OrderListActivity.this, GetOrderActivity.class);
                intent.putExtra("orderid",bean.getOrderid());
                intent.putExtra("userid",bean.getUserid());
                intent.putExtra("category",bean.getCategory());
                intent.putExtra("time",bean.getTime());
                intent.putExtra("idcard",bean.getIdcard());
                intent.putExtra("price",bean.getPrice());
                intent.putExtra("sort",bean.getSort());
                intent.putExtra("repert",bean.getRepert());
                intent.putExtra("keshi",bean.getKeshi());
                intent.putExtra("paystatus",bean.getPaystatus());
                startActivity(intent);
//                Toast.makeText(gh_ordinary.this,bean.getKeshi_name(),Toast.LENGTH_LONG).show();
            }
        });
        initData();
    }

    private void initData() {
        /**
         * loading...
         */

        if (dialog == null) {
            dialog = new ProgressDialog(OrderListActivity.this);
        }
        dialog.setMessage("加载中...");
        dialog.setCancelable(false);
        dialog.show();
        onGetkeshiData(OrderListActivity.this,1);
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
     * 获取订单列表
     * @param context
     */
    private void onGetkeshiData(final Context context, final int FLAG) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid",pref.getString("userid",null) );

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
                                    OrderBean bean = new OrderBean(json.getString("orderid"),json.getString("userid"),
                                            json.getString("category"),json.getString("time"),
                                            json.getString("idcard"),json.getString("price"),
                                            json.getString("sort"),json.getString("repert"),json.getString("keshi"),json.getString("paystatus"));
                                    List.add(bean);
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
