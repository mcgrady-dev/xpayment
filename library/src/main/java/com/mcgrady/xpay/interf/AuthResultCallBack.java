package com.mcgrady.xpay.interf;

/**
 * <p>授权结果回调</p>
 *
 * @author: mcgrady
 * @date: 2019/1/11
 */

public interface AuthResultCallBack {

    void onAuthSuccess(String result);

    void onAuthFailure(String result);

    void onAuthConfirm(String result);

    void onAuthCheck(String status);
}
