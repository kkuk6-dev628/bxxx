package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
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
public class PostCategoryActivity extends AppCompatActivity implements View.OnClickListener{
    private static String TAG = PostCategoryActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    private String postItem;
    View vi_lyt_cate03_parent, vi_lyt_cate04_parent, vi_lyt_cate05_parent, vi_lyt_cate06_parent, vi_lyt_cate07_parent, vi_lyt_cate10_parent, vi_lyt_cate12_parent;
    ImageView iv_img_cate03, iv_img_cate04, iv_img_cate05, iv_img_cate06, iv_img_cate07, iv_img_cate10, iv_img_cate12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_category);
        Intent intent = getIntent();
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        pActivity = (AnimatedActivity) PostCategoryActivity.this.getParent();
        CommonUtils.customActionBar(mContext, this, true, "请选择发布信息分类");
        postItem = (String) intent.getSerializableExtra("PostItem");

        int[] clickListenerIds = {R.id.rlt_cate00, R.id.rlt_cate00a, R.id.rlt_cate01,R.id.rlt_cate01a,
                R.id.rlt_cate02, R.id.rlt_cate03, R.id.rlt_cate03a, R.id.rlt_cate03b,
                R.id.rlt_cate04, R.id.rlt_cate05, R.id.rlt_cate05a, R.id.rlt_cate06,
                R.id.rlt_cate06a, R.id.rlt_cate06b, R.id.rlt_cate06c, R.id.rlt_cate07,
                R.id.rlt_cate07a, R.id.rlt_cate08, R.id.rlt_cate09,R.id.rlt_cate10,
                R.id.rlt_cate10a, R.id.rlt_cate11, R.id.rlt_cate12, R.id.rlt_cate12a,
                R.id.rlt_cate13, R.id.rlt_cate14, R.id.rlt_cate15,
                R.id.rlt_cate03_parent, R.id.rlt_cate04_parent, R.id.rlt_cate05_parent,R.id.rlt_cate06_parent,
                R.id.rlt_cate07_parent,R.id.rlt_cate10_parent, R.id.rlt_cate12_parent
        };
        for (int i = 0; i < clickListenerIds.length; i++){
            findViewById(clickListenerIds[i]).setOnClickListener(this);
        }
        vi_lyt_cate03_parent = findViewById(R.id.lyt_cate03_parent);
        vi_lyt_cate04_parent = findViewById(R.id.lyt_cate04_parent);
        vi_lyt_cate05_parent = findViewById(R.id.lyt_cate05_parent);
        vi_lyt_cate06_parent = findViewById(R.id.lyt_cate06_parent);
        vi_lyt_cate07_parent = findViewById(R.id.lyt_cate07_parent);
        vi_lyt_cate10_parent = findViewById(R.id.lyt_cate10_parent);
        vi_lyt_cate12_parent = findViewById(R.id.lyt_cate12_parent);
        iv_img_cate03 = (ImageView) findViewById(R.id.img_cate03);
        iv_img_cate04 = (ImageView) findViewById(R.id.img_cate04);
        iv_img_cate05 = (ImageView) findViewById(R.id.img_cate05);
        iv_img_cate06 = (ImageView) findViewById(R.id.img_cate06);
        iv_img_cate07 = (ImageView) findViewById(R.id.img_cate07);
        iv_img_cate10 = (ImageView) findViewById(R.id.img_cate10);
        iv_img_cate12 = (ImageView) findViewById(R.id.img_cate12);

        vi_lyt_cate03_parent.setVisibility(View.GONE);
        vi_lyt_cate04_parent.setVisibility(View.GONE);
        vi_lyt_cate05_parent.setVisibility(View.GONE);
        vi_lyt_cate06_parent.setVisibility(View.GONE);
        vi_lyt_cate07_parent.setVisibility(View.GONE);
        vi_lyt_cate10_parent.setVisibility(View.GONE);
        vi_lyt_cate12_parent.setVisibility(View.GONE);
        if(postItem!=null) {
            switch (postItem) {
                case "4":
                    vi_lyt_cate03_parent.setVisibility(View.VISIBLE);
                    vi_lyt_cate04_parent.setVisibility(View.GONE);
                    vi_lyt_cate05_parent.setVisibility(View.GONE);
                    vi_lyt_cate06_parent.setVisibility(View.GONE);
                    vi_lyt_cate07_parent.setVisibility(View.GONE);
                    vi_lyt_cate10_parent.setVisibility(View.GONE);
                    vi_lyt_cate12_parent.setVisibility(View.GONE);
                    iv_img_cate03.setImageResource(R.drawable.navi_down_);
                    iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
                    break;
                case "5":
                    vi_lyt_cate03_parent.setVisibility(View.GONE);
                    vi_lyt_cate04_parent.setVisibility(View.VISIBLE);
                    vi_lyt_cate05_parent.setVisibility(View.GONE);
                    vi_lyt_cate06_parent.setVisibility(View.GONE);
                    vi_lyt_cate07_parent.setVisibility(View.GONE);
                    vi_lyt_cate10_parent.setVisibility(View.GONE);
                    vi_lyt_cate12_parent.setVisibility(View.GONE);
                    iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate04.setImageResource(R.drawable.navi_down_);
                    iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
                    break;
                case "6":
                    vi_lyt_cate03_parent.setVisibility(View.GONE);
                    vi_lyt_cate04_parent.setVisibility(View.GONE);
                    vi_lyt_cate05_parent.setVisibility(View.VISIBLE);
                    vi_lyt_cate06_parent.setVisibility(View.GONE);
                    vi_lyt_cate07_parent.setVisibility(View.GONE);
                    vi_lyt_cate10_parent.setVisibility(View.GONE);
                    vi_lyt_cate12_parent.setVisibility(View.GONE);
                    iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate05.setImageResource(R.drawable.navi_down_);
                    iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
                    break;
                case "7":
                    vi_lyt_cate03_parent.setVisibility(View.GONE);
                    vi_lyt_cate04_parent.setVisibility(View.GONE);
                    vi_lyt_cate05_parent.setVisibility(View.GONE);
                    vi_lyt_cate06_parent.setVisibility(View.VISIBLE);
                    vi_lyt_cate07_parent.setVisibility(View.GONE);
                    vi_lyt_cate10_parent.setVisibility(View.GONE);
                    vi_lyt_cate12_parent.setVisibility(View.GONE);
                    iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate06.setImageResource(R.drawable.navi_down_);
                    iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
                    break;
                case "8":
                    //搜索二手买卖
                    vi_lyt_cate03_parent.setVisibility(View.GONE);
                    vi_lyt_cate04_parent.setVisibility(View.GONE);
                    vi_lyt_cate05_parent.setVisibility(View.GONE);
                    vi_lyt_cate06_parent.setVisibility(View.GONE);
                    vi_lyt_cate07_parent.setVisibility(View.VISIBLE);
                    vi_lyt_cate10_parent.setVisibility(View.GONE);
                    vi_lyt_cate12_parent.setVisibility(View.GONE);
                    iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate07.setImageResource(R.drawable.navi_down_);
                    iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
                    break;
                case "11":
                    //搜索婚姻交友
                    vi_lyt_cate03_parent.setVisibility(View.GONE);
                    vi_lyt_cate04_parent.setVisibility(View.GONE);
                    vi_lyt_cate05_parent.setVisibility(View.GONE);
                    vi_lyt_cate06_parent.setVisibility(View.GONE);
                    vi_lyt_cate07_parent.setVisibility(View.GONE);
                    vi_lyt_cate10_parent.setVisibility(View.VISIBLE);
                    vi_lyt_cate12_parent.setVisibility(View.GONE);
                    iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate10.setImageResource(R.drawable.navi_down_);
                    iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
                    break;
                case "13":
                    //搜索求购号码
                    vi_lyt_cate03_parent.setVisibility(View.GONE);
                    vi_lyt_cate04_parent.setVisibility(View.GONE);
                    vi_lyt_cate05_parent.setVisibility(View.GONE);
                    vi_lyt_cate06_parent.setVisibility(View.GONE);
                    vi_lyt_cate07_parent.setVisibility(View.GONE);
                    vi_lyt_cate10_parent.setVisibility(View.GONE);
                    vi_lyt_cate12_parent.setVisibility(View.VISIBLE);
                    iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
                    iv_img_cate12.setImageResource(R.drawable.navi_down_);
                    break;
                default:

                    break;
            }
        }
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rlt_cate03_parent) { //旅游专栏
            vi_lyt_cate03_parent.setVisibility(View.VISIBLE);
            vi_lyt_cate04_parent.setVisibility(View.GONE);
            vi_lyt_cate05_parent.setVisibility(View.GONE);
            vi_lyt_cate06_parent.setVisibility(View.GONE);
            vi_lyt_cate07_parent.setVisibility(View.GONE);
            vi_lyt_cate10_parent.setVisibility(View.GONE);
            vi_lyt_cate12_parent.setVisibility(View.GONE);
            iv_img_cate03.setImageResource(R.drawable.navi_down_);
            iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
        }
        else if (id == R.id.rlt_cate04_parent){ //旅游专栏
            vi_lyt_cate03_parent.setVisibility(View.GONE);
            vi_lyt_cate04_parent.setVisibility(View.VISIBLE);
            vi_lyt_cate05_parent.setVisibility(View.GONE);
            vi_lyt_cate06_parent.setVisibility(View.GONE);
            vi_lyt_cate07_parent.setVisibility(View.GONE);
            vi_lyt_cate10_parent.setVisibility(View.GONE);
            vi_lyt_cate12_parent.setVisibility(View.GONE);
            iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate04.setImageResource(R.drawable.navi_down_);
            iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
        }
        else if (id == R.id.rlt_cate05_parent){//旅游专栏
            vi_lyt_cate03_parent.setVisibility(View.GONE);
            vi_lyt_cate04_parent.setVisibility(View.GONE);
            vi_lyt_cate05_parent.setVisibility(View.VISIBLE);
            vi_lyt_cate06_parent.setVisibility(View.GONE);
            vi_lyt_cate07_parent.setVisibility(View.GONE);
            vi_lyt_cate10_parent.setVisibility(View.GONE);
            vi_lyt_cate12_parent.setVisibility(View.GONE);
            iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate05.setImageResource(R.drawable.navi_down_);
            iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
        }
        else if (id == R.id.rlt_cate06_parent){//旅游专栏
            vi_lyt_cate03_parent.setVisibility(View.GONE);
            vi_lyt_cate04_parent.setVisibility(View.GONE);
            vi_lyt_cate05_parent.setVisibility(View.GONE);
            vi_lyt_cate06_parent.setVisibility(View.VISIBLE);
            vi_lyt_cate07_parent.setVisibility(View.GONE);
            vi_lyt_cate10_parent.setVisibility(View.GONE);
            vi_lyt_cate12_parent.setVisibility(View.GONE);
            iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate06.setImageResource(R.drawable.navi_down_);
            iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
        }
        else if (id == R.id.rlt_cate07_parent){//旅游专栏
            vi_lyt_cate03_parent.setVisibility(View.GONE);
            vi_lyt_cate04_parent.setVisibility(View.GONE);
            vi_lyt_cate05_parent.setVisibility(View.GONE);
            vi_lyt_cate06_parent.setVisibility(View.GONE);
            vi_lyt_cate07_parent.setVisibility(View.VISIBLE);
            vi_lyt_cate10_parent.setVisibility(View.GONE);
            vi_lyt_cate12_parent.setVisibility(View.GONE);
            iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate07.setImageResource(R.drawable.navi_down_);
            iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
        }
        else if (id == R.id.rlt_cate10_parent){//旅游专栏
            vi_lyt_cate03_parent.setVisibility(View.GONE);
            vi_lyt_cate04_parent.setVisibility(View.GONE);
            vi_lyt_cate05_parent.setVisibility(View.GONE);
            vi_lyt_cate06_parent.setVisibility(View.GONE);
            vi_lyt_cate07_parent.setVisibility(View.GONE);
            vi_lyt_cate10_parent.setVisibility(View.VISIBLE);
            vi_lyt_cate12_parent.setVisibility(View.GONE);
            iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate10.setImageResource(R.drawable.navi_down_);
            iv_img_cate12.setImageResource(R.drawable.category_right_arrow);
        }
        else if (id == R.id.rlt_cate12_parent){//旅游专栏
            vi_lyt_cate03_parent.setVisibility(View.GONE);
            vi_lyt_cate04_parent.setVisibility(View.GONE);
            vi_lyt_cate05_parent.setVisibility(View.GONE);
            vi_lyt_cate06_parent.setVisibility(View.GONE);
            vi_lyt_cate07_parent.setVisibility(View.GONE);
            vi_lyt_cate10_parent.setVisibility(View.GONE);
            vi_lyt_cate12_parent.setVisibility(View.VISIBLE);
            iv_img_cate03.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate04.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate05.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate06.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate07.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate10.setImageResource(R.drawable.category_right_arrow);
            iv_img_cate12.setImageResource(R.drawable.navi_down_);
        }
        else {
            String fid = "";
            int sortid = -1;
            Intent intent = new Intent(PostCategoryActivity.this, PostActivity.class);
            if (id == R.id.rlt_cate00) {//房屋出售
                fid = "2";
                sortid = 1;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "1");
            }

            else if (id == R.id.rlt_cate00a) {//房屋求购
                fid = "2";
                sortid = 56;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "1a");
            }
            else if (id == R.id.rlt_cate01) { //房屋出租
                fid = "2";
                sortid = 4;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "2");
            }
            else if (id == R.id.rlt_cate02) { //店铺出兑
                fid = "93";
                sortid = 33;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "3");
            }
            else if (id == R.id.rlt_cate03) {//招兼职
                fid = "38";
                sortid = 7;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "4");
            }
            else if (id == R.id.rlt_cate03a) {//求兼职
                fid = "38";
                sortid = 28;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "4a");
            }
            else if (id == R.id.rlt_cate03b) {//招聘全职
                fid = "38";
                sortid = 53;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "4b");
            }
            else if (id == R.id.rlt_cate04) {//求职简历
                fid = "38";
                sortid = 8;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "5");
            }
            else if (id == R.id.rlt_cate05) { //便民服务
                fid = "42";
                sortid = 13;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "6");
            }
            else if (id == R.id.rlt_cate05a) { //便民求助
                fid = "42";
                sortid = 34;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "6a");
            }
            else if (id == R.id.rlt_cate06) {//车辆交易 - 车辆出租
                fid = "39";
                sortid = 58;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "7");
            }
             else if (id == R.id.rlt_cate06a) { //车辆交易 - 车辆求租
                fid = "39";
                sortid = 57;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "7a");
            }
            else if (id == R.id.rlt_cate06b) {//车辆交易 - 车辆出售
                fid = "39";
                sortid = 2;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "7b");
            }
            else if (id == R.id.rlt_cate06c) { //车辆交易 - 车辆求购
                fid = "39";
                sortid = 30;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "7c");
            }
            else if (id == R.id.rlt_cate07) { //二手买卖 -物品出售
                fid = "40";
                sortid = 3;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "8");
            }
            else if (id == R.id.rlt_cate07a) { //二手买卖 -物品求购
                fid = "40";
                sortid = 29;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "8a");
            }
            else if (id == R.id.rlt_cate08) { //打折促销
                fid = "107";
                sortid = 54;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "9");
            }
            else if (id == R.id.rlt_cate09) {//招商加盟
                fid = "44";
                sortid = 60;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "10");
            }
            else if (id == R.id.rlt_cate10) { //婚姻交友
                fid = "48";
                sortid = 15;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "11");
            }
            else if (id == R.id.rlt_cate10a) {//婚姻交友
                fid = "48";
                sortid = 16;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "11a");
            }
            else if (id == R.id.rlt_cate11) {//教育培训
                fid = "74";
                sortid = 21;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "12");
            }
            else if (id == R.id.rlt_cate12) {//求购号码 - 电话号码
                fid = "94";
                sortid = 40;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "13");
            }
            else if (id == R.id.rlt_cate12a) { //出售号码 - 电话号码
                fid = "94";
                sortid = 41;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "13a");
            }
            else if (id == R.id.rlt_cate13) {//出国资讯
                fid = "83";
                sortid = 24;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "14");
            }
            else if (id == R.id.rlt_cate14) {//宠物天地
                fid = "92";
                sortid = 32;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "15");
            }
            else if (id == R.id.rlt_cate15) {//旅游专栏
                fid = "50";
                sortid = 61;
                intent.putExtra("fid", fid);
                intent.putExtra("sortid", Integer.toString(sortid));
                intent.putExtra("PostItem", "16");
            }
            else
                intent = null;
            if (intent != null) {
                final Intent finalIntent = intent;
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("uid", CommonUtils.userInfo.getUserID());
                params.put("fid", fid);
                params.put("sortid", Integer.toString(sortid));
                String url = "api/post/limit";
                // 전화번호가 join되였는가를 먼저 검사하고 아니면 post할수 없다.
                if(CommonUtils.userInfo.getUserJoinMobile() != null && !CommonUtils.userInfo.getUserJoinMobile().equals("")) {
                    NetRetrofit.getInstance().post(url, params, new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> resp) {
                            try {
                                JSONObject response = resp.body();
                                if (response.getInt("ret") == 1) {
                                    PostCategoryActivity.this.startActivityForResult(finalIntent, CommonUtils.REQUEST_CODE_ANOTHER);
                                } else {
                                    CommonUtils.showAlertDialog(mContext, "您在本版块的发布数量已达到上限", null);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(mContext, res.getString(R.string.error_db), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            //progressDialog.dismiss();
                            Toast.makeText(mContext, res.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    CommonUtils.showAlertDialog(mContext, "您必须转到“我的/账号安全”页面并拨打电话号码", null);
                }


            }
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //overridePendingTransition(R.anim.pop_in, R.anim.pop_out);
        //this.getParent().getParent().onBackPressed();
        Intent intent = new Intent(PostCategoryActivity.this, HomeActivity.class);
        pActivity.startChildActivity("home", intent);
        TabHostActivity.tabWidget.setCurrentTab(0);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(PostCategoryActivity.this, HomeActivity.class);
            pActivity.startChildActivity("home", intent);
            TabHostActivity.tabWidget.setCurrentTab(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
