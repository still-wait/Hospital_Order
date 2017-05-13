package ui.main;

import android.app.ProgressDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Internet.NormalPostRequest;
import login.ResignActivity;

import static Internet.volley.url;

/**
 * Created by Y-GH on 2017/5/12.
 */

public class SetOrderActivity extends BaseActivity  {
    private EditText userid,idcard;
    private TextView keshi,category,repert_info,time,sort,price;
    private Button commit;
    private static ProgressDialog dialog;
    private SharedPreferences pref;
    private static final String MSG_LOGIN_SUCCESS = "提交成功";
    private String httpurl1 = url+":8080/xiaoyu/setorder";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setorder);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        keshi.setText(bundle.getString("keshi"));
        category.setText(bundle.getString("category"));
        time.setText(bundle.getString("time"));
        sort.setText(bundle.getString("sort"));
        repert_info.setText(bundle.getString("repert"));
        if(bundle.getString("category").equals("普通号")){
            price.setText("5元");
        }else {
            price.setText("15元");
        }


        pref = getSharedPreferences("user", MODE_PRIVATE);
        userid.setText(pref.getString("username",null));
        idcard.setText(pref.getString("idcard",null));
    }

    private void initView() {
        getSupportActionBar().setTitle("提交订单");
        userid = (EditText) findViewById(R.id.msg_username);
        idcard = (EditText) findViewById(R.id.msg_idcard);
        keshi = (TextView) findViewById(R.id.keshi);
        category = (TextView) findViewById(R.id.category);
        repert_info = (TextView) findViewById(R.id.repert_info);
        time = (TextView) findViewById(R.id.time);
        sort = (TextView) findViewById(R.id.sort);
        price = (TextView) findViewById(R.id.price);
        commit = (Button) findViewById(R.id.btn_order);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId = pref.getString("userid",null);
                final String Category = category.getText().toString();
                final String Time = time.getText().toString();
                final String Idcard = idcard.getText().toString();
                final String Price = price.getText().toString();
                final String Sort = sort.getText().toString();
                final String repert = repert_info.getText().toString();
                final String Keshi = keshi.getText().toString();
                /**
                 * loading...
                 */

                if (dialog == null) {
                    dialog = new ProgressDialog(SetOrderActivity.this);
                }
                dialog.setMessage("提交中...");
                dialog.setCancelable(false);
                dialog.show();
                onSetOrder(userId,Category,Time,Idcard,Price,Sort,repert,Keshi);
            }
        });


    }


    private void onSetOrder(final String userid, String category, String time, String idcard, String price, String sort, String repert,String Keshi) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userid);
        params.put("category", category);
        params.put("time", time);
        params.put("idcard", idcard);
        params.put("price", price);
        params.put("sort", (Integer.parseInt(sort)-1)+"");
        params.put("repert", repert);
        params.put("keshi", Keshi);
        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        String json = new Gson().toJson(response);
                        Log.e("注册成功", response.toString() );
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
                                Toast.makeText(SetOrderActivity.this,"内部错误", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SetOrderActivity.this, "连接服务器错误", Toast.LENGTH_SHORT).show();

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

            finish();
        }

    }
}
