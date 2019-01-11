package com.mcgrady.xpay.wxpay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mcgrady.xpay.PayAPI;
import com.mcgrady.xpay.PayReqAble;
import com.mcgrady.xpay.wxpay.WechatPayReq;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * <p>类说明</p>
 *
 * @author: mcgrady
 * @date: 2019/1/10
 */

public class WXPayEnterActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PayReqAble payReqAble = PayAPI.getInstance().getPayReqAble();
        if (payReqAble != null && payReqAble instanceof WechatPayReq) {
            ((WechatPayReq) payReqAble).handleIntent(getIntent(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        PayReqAble payReqAble = PayAPI.getInstance().getPayReqAble();
        if (payReqAble != null && payReqAble instanceof WechatPayReq) {
            ((WechatPayReq) payReqAble).handleIntent(getIntent(), this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.i("WeChatPayReq", "onReq===>>> baseReq.getType : " + baseReq.getType());
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.i("WeChatPayReq", "onResp===>>> resp.getType : "+ resp.getType());

        /**
         * 0    成功 展示成功页面
         * -1   错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
         * -2   用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
         */
        switch (resp.getType()) {
            case ConstantsAPI.COMMAND_PAY_BY_WX:
                PayReqAble payReqAble = PayAPI.getInstance().getPayReqAble();
                if (payReqAble != null && payReqAble instanceof WechatPayReq) {
                    ((WechatPayReq) payReqAble).onResp(resp.errCode);
                }
                break;
            default:
                break;
        }
    }
}
