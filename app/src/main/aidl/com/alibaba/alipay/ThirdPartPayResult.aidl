package com.alibaba.alipay;

interface ThirdPartPayResult {

    void onPaySuccess(float money);

    void onPayFailed(in int errorCode,String msg);

}