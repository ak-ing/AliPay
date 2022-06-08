package com.alibaba.alipay;

interface ThirdPartPayResult {

    /**
     * 支付成功
     */
    void onPaySuccess(float money);

    /**
     * 支付失败
     */
    void onPayFailed(in int errorCode,String msg);

}