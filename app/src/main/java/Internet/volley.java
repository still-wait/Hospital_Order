package Internet;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.KeshiBean;
import bean.Userbean;
import login.LoginActivity;

/**
 * Created by Y-GH on 2017/1/3.
 */

public class volley {

    public static String url="http://192.168.1.106";
    private static String httpurl1 = url+":8080/xiaoyu/keshi";
    private static ProgressDialog dialog;


    /**
     * 获取科室列表
     * @param context
     */
    public static List<KeshiBean> onGetkeshiData(final Context context) {
        final List<KeshiBean> list = new ArrayList<KeshiBean>();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", "tom");
        final int[] flag = {0};
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
                                    list.add(bean);
                                    Log.e("-----从服务器取得------", "----"+json.getString("keshiName"));
                                }
                                flag[0] =1;
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
        if(flag[0] >0){
            return list;
        }else{
            return null;
        }
    }

//    /**
//     *
//     *
//     * @param username
//     * @param matchid
//     * @param context
//     */
//    public void onJoinMatch(final String username, String matchid, final Context context) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("username", username);
//        params.put("matchid", matchid);
//
//        /**
//         * loading...
//         */
//
//        if (dialog == null) {
//            dialog = new ProgressDialog(context);
//        }
//        dialog.setMessage("加入中...");
//        dialog.setCancelable(false);
//        dialog.show();
//        /**
//         * 访问网络
//         */
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
//        Request<JSONObject> request = new NormalPostRequest(httpurl1,
//                ew com.android.volley.Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        String result = null;
//                        try {
//                            result = response.getString("result");
//                            if (result.equals("success")) {
//
//                                if (dialog != null) {
//                                    dialog.cancel();
//                                    dialog = null;
//                                }
////                                position.setText(response.getString("poision"));
//                                Toast.makeText(context, "加入成功", Toast.LENGTH_SHORT).show();
////                                userTransfer = response;
//                            } else {
//
//                                if (dialog != null) {
//                                    dialog.cancel();
//                                    dialog = null;
//                                }
//                                Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
////                            Toast.makeText(Old_LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
////                JUtils.ToastLong(getResources().getString(R.string.other_server_error));
//                Toast.makeText(context, "连接服务器错误", Toast.LENGTH_SHORT).show();
//
//                if (dialog != null) {
//                    dialog.cancel();
//                    dialog = null;
//                }
//            }
//        }, params);
//        requestQueue.add(request);
//    }





}
