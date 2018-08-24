package cn.reservation.app.baixingxinwen.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;

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
            Intent intent = new Intent(PostCategoryActivity.this, PostActivity.class);
            if (id == R.id.rlt_cate00)//房屋出售
                intent.putExtra("PostItem", "1");
            else if (id == R.id.rlt_cate00a)//房屋求购
                intent.putExtra("PostItem", "1a");
            else if (id == R.id.rlt_cate01) //房屋出租
                intent.putExtra("PostItem", "2");
            else if (id == R.id.rlt_cate02) //店铺出兑
                intent.putExtra("PostItem", "3");
            else if (id == R.id.rlt_cate03) //招兼职
                intent.putExtra("PostItem", "4");
            else if (id == R.id.rlt_cate03a)//求兼职
                intent.putExtra("PostItem", "4a");
            else if (id == R.id.rlt_cate03b) //招聘全职
                intent.putExtra("PostItem", "4b");
            else if (id == R.id.rlt_cate04) //求职简历
                intent.putExtra("PostItem", "5");
            else if (id == R.id.rlt_cate05) //便民服务
                intent.putExtra("PostItem", "6");
            else if (id == R.id.rlt_cate05a) //便民求助
                intent.putExtra("PostItem", "6a");
            else if (id == R.id.rlt_cate06) //车辆交易 - 车辆出租
                intent.putExtra("PostItem", "7");
             else if (id == R.id.rlt_cate06a) //车辆交易 - 车辆求租
                intent.putExtra("PostItem", "7a");
            else if (id == R.id.rlt_cate06b)//车辆交易 - 车辆出售
                intent.putExtra("PostItem", "7b");
            else if (id == R.id.rlt_cate06c) //车辆交易 - 车辆求购
                intent.putExtra("PostItem", "7c");
            else if (id == R.id.rlt_cate07) //二手买卖 -物品出售
                intent.putExtra("PostItem", "8");
            else if (id == R.id.rlt_cate07a) //二手买卖 -物品求购
                intent.putExtra("PostItem", "8a");
            else if (id == R.id.rlt_cate08) //打折促销
                intent.putExtra("PostItem", "9");
            else if (id == R.id.rlt_cate09) //招商加盟
                intent.putExtra("PostItem", "10");
            else if (id == R.id.rlt_cate10) //婚姻交友
                intent.putExtra("PostItem", "11");
            else if (id == R.id.rlt_cate10a) //婚姻交友
                intent.putExtra("PostItem", "11a");
            else if (id == R.id.rlt_cate11) //教育培训
                intent.putExtra("PostItem", "12");
            else if (id == R.id.rlt_cate12) //求购号码 - 电话号码
                intent.putExtra("PostItem", "13");
            else if (id == R.id.rlt_cate12a) //出售号码 - 电话号码
                intent.putExtra("PostItem", "13a");
            else if (id == R.id.rlt_cate13) //出国资讯
                intent.putExtra("PostItem", "14");
            else if (id == R.id.rlt_cate14) //宠物天地
                intent.putExtra("PostItem", "15");
            else if (id == R.id.rlt_cate15) //旅游专栏
                intent.putExtra("PostItem", "16");
            else
                intent = null;
            if (intent != null)
                PostCategoryActivity.this.startActivityForResult(intent, CommonUtils.REQUEST_CODE_ANOTHER);
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
