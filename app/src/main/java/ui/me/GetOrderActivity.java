package ui.me;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hospital2.BaseActivity;
import com.example.hospital2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Internet.NormalPostRequest;
import Internet.NormalPostRequest2;
import bean.OrdinaryBean;
import login.ResignActivity;

import static Internet.volley.url;

/**
 * Created by Y-GH on 2017/5/12.
 */

public class GetOrderActivity extends BaseActivity  {
    private EditText userid,idcard;
    private TextView keshi,category,repert_info,time,sort,price,orderid;
    private Button pay,cancel;
    private static ProgressDialog dialog;
    private SharedPreferences pref;
    private static final String MSG_LOGIN_SUCCESS = "支付成功";
    private String httpurl1 = url+":8080/xiaoyu/orderpay";
    private String payStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getorder);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        pref = getSharedPreferences("user", MODE_PRIVATE);
        orderid.setText(bundle.getString("orderid"));
        userid.setText(pref.getString("username",null));
        idcard.setText(bundle.getString("idcard"));
        price.setText(bundle.getString("price"));
        repert_info.setText(bundle.getString("repert"));
        keshi.setText(bundle.getString("keshi"));
        category.setText(bundle.getString("category"));
        time.setText(bundle.getString("time"));
        sort.setText(bundle.getString("sort"));
        payStatus = bundle.getString("paystatus");
        if(payStatus.equals("1")){
            pay.setEnabled(false);
            pay.setText("已支付");
        }
    }

    private void initView() {
        getSupportActionBar().setTitle("订单详情");
        userid = (EditText) findViewById(R.id.msg_username);
        idcard = (EditText) findViewById(R.id.msg_idcard);
        orderid = (TextView) findViewById(R.id.orderid);
        keshi = (TextView) findViewById(R.id.keshi);
        category = (TextView) findViewById(R.id.category);
        repert_info = (TextView) findViewById(R.id.repert_info);
        time = (TextView) findViewById(R.id.time);
        sort = (TextView) findViewById(R.id.sort);
        price = (TextView) findViewById(R.id.price);
        pay = (Button) findViewById(R.id.btn_pay);
        cancel = (Button) findViewById(R.id.btn_canel);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * loading...
                 */

                if (dialog == null) {
                    dialog = new ProgressDialog(GetOrderActivity.this);
                }
                dialog.setMessage("支付中...");
                dialog.setCancelable(false);
                dialog.show();
                onSetOrder(GetOrderActivity.this);
            }
        });


    }


    /**
     * 支付
     * @param context
     */
    private void onSetOrder(final Context context) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("orderid", orderid.getText().toString());
        params.put("paystatus","1");
        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        String json = new Gson().toJson(response);
                        Log.e("成功", response.toString() );
                        try {
                            Log.e("====>>>>>",response.getString("status"));
                            if (response.getString("status").equals("success")) {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                showTip(MSG_LOGIN_SUCCESS);
                            } else {

                                if (dialog != null) {
                                    dialog.cancel();
                                    dialog = null;
                                }
                                Toast.makeText(context,"内部错误", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Old_LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        if (str == MSG_LOGIN_SUCCESS) {
            pay.setEnabled(false);
            pay.setText("已支付");
        }

    }
}
