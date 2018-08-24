package cn.reservation.app.baixingxinwen.api;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.WindowManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import java.io.File;

public class WXAPI {
	public static IWXAPI api = null;
	public static Activity instance;
	public static boolean isLogin = false;

	public static int loginRes = -1;
	public static String loginToken = null;
	public static Handler callBackHandler;
	public static void Init(Activity context){
		WXAPI.instance = context;
		api = WXAPIFactory.createWXAPI(context, APIManager.WC_APP_ID, true);
        api.registerApp(APIManager.WC_APP_ID);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	private static String buildTransaction(final String type) {
	    return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	public static void Login(){
		isLogin = true;
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "carjob_wx_login";
		api.sendReq(req);
		//instance.finish();
	}
	
	public static void Share(String url,String title,String desc){
		try{
			isLogin = false;
			WXWebpageObject webpage = new WXWebpageObject();
			webpage.webpageUrl = url;
			WXMediaMessage msg = new WXMediaMessage(webpage);
			boolean wechat = true;
			if (title.startsWith("wechat")){
				wechat = true;
				title = title.substring(7);
			} else {
				wechat = false;
				title = title.substring(12);
			}
			msg.title = title;
			msg.description = desc;
			//msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
			
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("webpage");
			req.message = msg;
			if (wechat)
				req.scene = /*isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : */SendMessageToWX.Req.WXSceneSession;
			else
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
			api.sendReq(req);
			//instance.finish();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
