package cn.reservation.app.baixingxinwen.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.reservation.app.baixingxinwen.R;
import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.utils.AnimatedActivity;
import cn.reservation.app.baixingxinwen.utils.CommonUtils;

@SuppressWarnings("deprecation")
public class PrivacyActivity extends AppCompatActivity implements DialogInterface.OnCancelListener{
    private static String TAG = PrivacyActivity.class.getSimpleName();

    private Context mContext;
    private Resources res;
    public AnimatedActivity pActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        mContext = TabHostActivity.TabHostStack;
        res = getResources();
        ((TextView)findViewById(R.id.txt_privacy_desc)).setText(Html.fromHtml("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;欢迎光临百姓信息网网站。百姓信息网致力于为您提供最优质、最便捷的服务。在访问百姓信息网的同时，也请您仔细阅读我们的协议条款。您需要同意该条款才能注册成为我们的用户。一经注册，将视为接受并遵守该条款的所有约定。</p>\n" +
                "<p> 1．用户应按照百姓信息网的注册、登录程序和相应规则进行注册、登录，注册信息应真实可靠，信息内容如有变动应及时更新。</p>\n" +
                "\n" +
                "<p>2．用户应在适当的栏目或地区发布信息，所发布信息内容必须真实可靠，不得违反百姓信息网对发布信息的禁止性规定。用户对其自行发表、上传或传送的内容负全部责任。</p>\n" +
                "\n" +
                "<p>3．遵守中华人民共和国相关法律法规，包括但不限于《中华人民共和国计算机信息系统安全保护条例》、《计算机软件保护条例》、《最高人民法院关于审理涉及计算机网络著作权纠纷案件适用法律若干问题的解释(法释[2004]1号)》、《互联网电子公告服务管理规定》、《互联网新闻信息服务管理规定》、《互联网著作权行政保护办法》和《信息网络传播权保护条例》等有关计算机互联网规定和知识产权的法律和法规、实施办法。</p>\n" +
                "\n" +
                "<p>4．所有用户不得在百姓信息网任何版块发布、转载、传送含有下列内容之一的信息，否则百姓信息网有权自行处理并不通知用户：</p>\n" +
                "<p>(1)违反宪法确定的基本原则的； (2)危害国家安全，泄漏国家机密，颠覆国家政权，破坏国家统一的； (3)损害国家荣誉和利益的； (4)煽动民族仇恨、民族歧视，破坏民族团结的； (5)破坏国家宗教政策，宣扬邪教和封建迷信的； (6)散布淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的； (7)侮辱或者诽谤他人，侵害他人合法权益的； (8)含有法律、行政法规禁止的其他内容的。</p>\n" +
                "            "));
        CommonUtils.customActionBar(mContext, this, true, "百姓信息网服务协议");
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
    @Override
    public void onCancel(DialogInterface dialog) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PrivacyActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
