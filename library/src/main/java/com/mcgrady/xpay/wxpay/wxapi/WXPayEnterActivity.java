package com.mcgrady.xpay.wxpay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mcgrady.xpay.PayAPI;
import com.mcgrady.xpay.PayReqAble;
import com.mcgrady.xpay.wxpay.WeChatPayReq;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * <p>微信支付结果回调</p>
 * app项目直接引用{@link com.mcgrady.xpay.wxpay.wxapi.WXPayEnterActivity}即可
 *
 * @author: mcgrady
 * @date: 2019/1/10
 */

public class WXPayEnterActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PayReqAble payReqAble = PayAPI.getInstance().getPayReqAble();
        if (payReqAble != null && payReqAble instanceof WeChatPayReq) {
            ((WeChatPayReq) payReqAble).handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        PayReqAble payReqAble = PayAPI.getInstance().getPayReqAble();
        if (payReqAble != null && payReqAble instanceof WeChatPayReq) {
            ((WeChatPayReq) payReqAble).handleIntent(getIntent(), this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.i("WeChatPayReq", "onReq===>>> baseReq.getType : " + baseReq.getType());
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.i("WeChatPayReq", "onResp===>>> resp.getType : "+ resp.getType());

        switch (resp.getType()) {
            case ConstantsAPI.COMMAND_PAY_BY_WX:
                PayReqAble payReqAble = PayAPI.getInstance().getPayReqAble();
                if (payReqAble != null && payReqAble instanceof WeChatPayReq) {
                    ((WeChatPayReq) payReqAble).onResp(resp.errCode);
                }

                finish();
                break;
            default:
                break;
        }
    }
}
