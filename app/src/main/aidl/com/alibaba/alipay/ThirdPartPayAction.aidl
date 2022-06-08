// ThirdPartPayAction.aidl
package com.alibaba.alipay;

// Declare any non-default types here with import statements
import com.alibaba.alipay.ThirdPartPayResult;

interface ThirdPartPayAction {
    /**
     * 发起支付
     */
     void requestPay(String orderInfo,float payMoney,ThirdPartPayResult callBack);

}