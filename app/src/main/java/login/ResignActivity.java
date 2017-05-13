package login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hospital2.BaseActivity;
import com.example.hospital2.MainActivity;
import com.example.hospital2.R;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Internet.NormalPostRequest;
import Internet.NormalPostRequest3;

import static Internet.volley.url;


public class ResignActivity extends BaseActivity implements View.OnClickListener {
    private EditText userid, password, sure_password, username, idcard;
    RadioGroup mode;
    private String msex = null;
    private ImageView mBtnClearusername, mBtnClearrealname, mBtnClearpassword, mBtnClearqrpsw;
    private ImageView mBtnidcard;
    private Button resign;
    private static ProgressDialog dialog;
    private static final String MSG_LOGIN_SUCCESS = "注册成功。";
    private String httpurl1 = url+":8080/xiaoyu/resign";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resign_all);
        initView();
        setOnListener();


    }

    private void initView() {
        getSupportActionBar().setTitle("注册");

        //控件
        userid = (EditText) findViewById(R.id.msg_resign_username);
        password = (EditText) findViewById(R.id.msg_resign_password);
        sure_password = (EditText) findViewById(R.id.msg_resign_qrpsw);
        username = (EditText) findViewById(R.id.msg_resign_realname);
        idcard = (EditText) findViewById(R.id.msg_resign_cellphone1);
        mode = (RadioGroup) findViewById(R.id.mode);
        mBtnClearusername = (ImageView) findViewById(R.id.img_resign_username);
        mBtnClearpassword = (ImageView) findViewById(R.id.img_resign_password);
        mBtnClearqrpsw = (ImageView) findViewById(R.id.img_resign_qrpsw);
        mBtnClearrealname = (ImageView) findViewById(R.id.img_resign_realname);
        mBtnidcard = (ImageView) findViewById(R.id.img_resign_cellphone1);
        resign = (Button) findViewById(R.id.btn_resign);
        pref = getSharedPreferences("user", MODE_PRIVATE);
    }

    private void setOnListener() {
        //按钮监听
        resign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String loginName = userid.getText().toString();
                final String loginPassword = password.getText().toString();
                final String surePassword = sure_password.getText().toString();
                final String realName = username.getText().toString();
                final String Idcard = idcard.getText().toString();
                /**
                 * loading...
                 */

                if (dialog == null) {
                    dialog = new ProgressDialog(ResignActivity.this);
                }
                dialog.setMessage("注册中...");
                dialog.setCancelable(false);
                dialog.show();
                if(!loginPassword.equals(surePassword)){
                    Toast.makeText(ResignActivity.this,"密码前后不一致", Toast.LENGTH_SHORT).show();
                }else{
                    onresignLogin(loginName, loginPassword,msex,realName,Idcard);
                }


            }
        });

        mBtnClearusername.setOnClickListener(this);
        mBtnClearpassword.setOnClickListener(this);
        mBtnClearqrpsw.setOnClickListener(this);
        mBtnClearrealname.setOnClickListener(this);
        mBtnidcard.setOnClickListener(this);
        mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.man_select:
                        msex = "0";
                        break;
                    case R.id.women_select:
                        msex = "1";
                        break;
                }
            }
        });

        // 用户名 输入监听
        userid.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (userid.getText().toString().length() > 0) {
                    mBtnClearusername.setVisibility(View.VISIBLE);
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                    mBtnClearusername.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (password.getText().toString().length() > 0) {
                    mBtnClearpassword.setVisibility(View.VISIBLE);
                    findViewById(R.id.qrpsw_layout).setVisibility(View.VISIBLE);
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                    findViewById(R.id.qrpsw_layout).setVisibility(View.GONE);
                    mBtnClearpassword.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });

        sure_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (sure_password.getText().toString().length() > 0) {
                    mBtnClearqrpsw.setVisibility(View.VISIBLE);
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                    mBtnClearqrpsw.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });
        // 真是姓名输入监听
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (username.getText().toString().length() > 0) {
                    mBtnClearrealname.setVisibility(View.VISIBLE);
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                    mBtnClearrealname.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });

        idcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (idcard.getText().toString().length() > 0) {
                    mBtnidcard.setVisibility(View.VISIBLE);
                    initmsucess();
                } else {
                    resign.setEnabled(false);
                    mBtnidcard.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }
        });

    }

    /**
     * 按钮状态监听
     */
    private void initmsucess() {
        if (userid.getText().toString().length() > 0
                & password.getText().toString().length() > 0
                & sure_password.getText().toString().length() > 0
                & username.getText().toString().length() > 0
                & idcard.getText().toString().length() > 0){
            resign.setEnabled(true);
        } else {
            resign.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_resign_username:
                userid.setText("");
                break;
            case R.id.img_resign_password:
                password.setText("");
                break;
            case R.id.img_resign_qrpsw:
                sure_password.setText("");
                break;
            case R.id.img_resign_realname:
                username.setText("");
                break;
            case R.id.img_resign_cellphone1:
                idcard.setText("");
                break;

        }

    }

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        if (str == MSG_LOGIN_SUCCESS) {
            editor = pref.edit();
            editor.putString("userid", userid.getText().toString());
            editor.commit();
            finish();
        }

    }

    private void onresignLogin(final String userid, String password, String sex, String username, String idcard) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userid);
        params.put("password", password);
        params.put("username", username);
        params.put("sex", sex);
        params.put("idcard", idcard);
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
                                Toast.makeText(ResignActivity.this,"用户名已存在", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ResignActivity.this, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }
}
