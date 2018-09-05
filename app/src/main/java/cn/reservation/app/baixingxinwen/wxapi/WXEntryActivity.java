package cn.reservation.app.baixingxinwen.wxapi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.reservation.app.baixingxinwen.api.APIManager;
import cn.reservation.app.baixingxinwen.api.WXAPI;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI _api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.plugin_entry);
        _api = WXAPIFactory.createWXAPI(this, APIManager.WC_APP_ID, false);
        _api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        _api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        this.finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        String token = null;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if(resp.getType() == ConstantsAPI.COMMAND_SENDAUTH && WXAPI.isLogin){
                    final SendAuth.Resp authResp = (SendAuth.Resp)resp;
                    if(authResp != null && authResp.token != null){
                        token = authResp.token;
                    }
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = 2;//R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = 3;//R.string.errcode_deny;
                break;
            default:
                result = 4;//R.string.errcode_unknown;
                break;
        }
        Message msg = Message.obtain();// 和new Message（）；是一个意思
        Bundle data = new Bundle();
        data.putInt("loginRes", result);
        data.putString("loginToken", token);
        // 把数据保存到Message对象中
        msg.setData(data);
        if(WXAPI.callBackHandler != null){
            WXAPI.callBackHandler.sendMessage(msg);//传送登录结果
        }
        this.finish();
    }
}