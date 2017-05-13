package login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.hospital2.MainActivity;
import com.example.hospital2.R;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import Internet.NormalPostRequest;
import bean.Userbean;

import static Internet.volley.url;


/**
 * Created by Y-GH on 2017/5/9.
 */
public class LoginActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener {
    private EditText username, password;
    private ImageView mBtnClearUid, mBtnClearPsw;
    private ToggleButton mTgBtnShowPsw;
    private Button login;
    private TextView resign;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String httpurl1 = url+":8080/xiaoyu/login";

    private static final int FLAG_LOGIN_SUCCESS = 1;
    private static final String MSG_LOGIN_ERROR = "登录出错,请检查网络。";
    private static final String MSG_LOGIN_SUCCESS = "登录成功。";
    public static final String MSG_LOGIN_FAILED = "用户名或密码错误。";
    public static final String MSG_SERVER_ERROR = "请求服务器错误。";
    public static final String MSG_SERVER_TIMEOUT = "请求服务器超时。";
    public static final String MSG_RESPONCE_TIMEOUT = "服务器响应超时。";
    private static ProgressDialog dialog;
    private Userbean user = new Userbean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this,R.color.colorPrimaryDark,true);
        setContentView(R.layout.login);
        initView();
        initUID();
        setOnListener();
    }

    private void initView() {
        login = (Button) findViewById(R.id.btn_login);
        username = (EditText) findViewById(R.id.edit_uid);
        password = (EditText) findViewById(R.id.edit_psw);
        mBtnClearUid = (ImageView) findViewById(R.id.img_login_clear_uid);
        mBtnClearPsw = (ImageView) findViewById(R.id.img_login_clear_psw);
        mTgBtnShowPsw = (ToggleButton) findViewById(R.id.tgbtn_show_psw);
        resign = (TextView) findViewById(R.id.tv_quick_sign_up);

    }

    private void setOnListener() {

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String loginName = username.getText().toString();
                final String loginPassword = password.getText().toString();
                /**
                 * loading...
                 */

                if (dialog == null) {
                    dialog = new ProgressDialog(LoginActivity.this);
                }
                dialog.setMessage("登录中...");
                dialog.setCancelable(false);
                dialog.show();
                /**
                 * 访问网络
                 */
                onLoginDialogLogin(loginName, loginPassword);
//                showTip(MSG_LOGIN_SUCCESS);//服务器写好再注释掉

            }
        });



        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (username.getText().toString().length() > 0) {
                    mBtnClearUid.setVisibility(View.VISIBLE);
                    if (password.getText().toString().length() > 0 ) {
                        login.setEnabled(true);
                    } else {
                        login.setEnabled(false);
                    }
                } else {
                    login.setEnabled(false);
                    mBtnClearUid.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (password.getText().toString().length() > 0) {
                    mBtnClearPsw.setVisibility(View.VISIBLE);
                    if (username.getText().toString().length() > 0) {
                        login.setEnabled(true);
                    } else {
                        login.setEnabled(false);
                    }
                } else {
                    login.setEnabled(false);
                    mBtnClearPsw.setVisibility(View.INVISIBLE);
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



        resign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        ResignActivity.class);
                startActivity(intent);
            }
        });
        mBtnClearUid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearText(username);
            }
        });
        mBtnClearPsw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearText(password);
            }
        });

        mTgBtnShowPsw.setOnCheckedChangeListener(this);
    }

    /**
     * 初始化记住的用户名
     */

    private void initUID() {
        pref = getSharedPreferences("user", MODE_PRIVATE);
        String Username = pref.getString("username", "");
        username.setText(Username);
    }


    /**
     * 清空控件文本
     */
    private void clearText(EditText edit) {
        edit.setText("");
    }

    /**
     * 是否显示密码
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // 显示密码
            password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            // 隐藏密码
            password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        username.setText(pref.getString("userid",null));
    }

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        if (str == MSG_LOGIN_SUCCESS) {
            editor = pref.edit();
            editor.putString("userid", user.getUserId());
            editor.putString("username", user.getusername());
            editor.putString("sex", user.getsex());
            editor.putString("idcard", user.getidcard());
            editor.putBoolean("islogin", true);
            editor.commit();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }


    private void onLoginDialogLogin(final String username, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", username);
        params.put("password", password);
        /**
         * 访问网络
         */

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(httpurl1,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("登录成功", response.toString() );
                        if(response!=null){
                            try {
                                Log.e("登录成功", response.toString() );
                                user.setUserId(response.getString("userId"));
                                user.setsex(response.getString("sex"));
                                user.setusername(response.getString("username"));
                                user.setidcard(response.getString("idcard"));
                                    if (dialog   != null) {
                                        dialog.cancel();
                                        dialog = null;
                                    Log.e("登录成功", "  wwwww " );
                                    showTip(MSG_LOGIN_SUCCESS);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {

                            if (dialog != null) {
                                dialog.cancel();
                                dialog = null;
                            }
                            Toast.makeText(LoginActivity.this,"用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
                Toast.makeText(LoginActivity.this, "连接服务器错误", Toast.LENGTH_SHORT).show();

                if (dialog != null) {
                    dialog.cancel();
                    dialog = null;
                }
            }
        }, params);
        requestQueue.add(request);
    }


}
