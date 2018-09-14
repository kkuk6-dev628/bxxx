package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.NetRetrofit;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class SetNameActivity extends AppCompatActivity{
    private static String TAG = SetNameActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private EditText editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) SetNameActivity.this.getParent();

        CommonUtils.customActionBar(mContext, this, false, "");
        RelativeLayout rltBack = (RelativeLayout) findViewById(R.id.rlt_back);
        rltBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetNameActivity.this, MeActivity.class);
                pActivity.startChildActivity("home", intent);
            }
        });

        editTextName = (EditText) findViewById(R.id.edit_input_data7);

        ImageView img_delete = (ImageView) findViewById(R.id.img_delete);
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setText("");
            }
        });



        TextView txt_my_level_title = (TextView) findViewById(R.id.txt_save_name);
        txt_my_level_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editTextName.getText().toString();
                if(!name.equals("")){
                    final HashMap<String, Object> params = new HashMap<>();
                    final String url = "api/user/changeid";
                    params.put("uid", CommonUtils.userInfo.getUserID());
                    params.put("newname", name);
                    NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            try {
                                JSONObject responseBody = response.body();
                                if (responseBody != null && responseBody.getInt("code") == 1) {
                                    String message = responseBody.getString("message");
                                    Log.d("Change user name", message);

                                    SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                                    editor.putString("userName", name);
                                    editor.putString("changeid", "1");
                                    editor.apply();

                                    CommonUtils.userInfo.setUserName(name);
                                    CommonUtils.userInfo.setChangeid("1");

//                                    if(!isFinishing()){
//
//                                    }

                                    CommonUtils.showAlertDialog(mContext,
                                            message, new View.OnClickListener(){

                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent;
                                                    intent = new Intent(SetNameActivity.this, MeActivity.class);
                                                    pActivity.startChildActivity("about_activity", intent);
//                                                    finish();
                                                }
                                            });



                                }
                            } catch (JSONException ex) {

                            }
                        }
                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            //CommonUtils.dismissProgress(progressDialog);
                            Toast.makeText(mContext, "Add Channel Request failed", Toast.LENGTH_SHORT).show();
                        }

                    });
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(SetNameActivity.this, MeActivity.class);
        pActivity.startChildActivity("about_activity", intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("****event****" + event + "****" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent;
            intent = new Intent(SetNameActivity.this, MeActivity.class);
            pActivity.startChildActivity("about_activity", intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
