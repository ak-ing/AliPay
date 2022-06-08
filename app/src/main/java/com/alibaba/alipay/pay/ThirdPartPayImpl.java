package com.alibaba.alipay.pay;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.alibaba.alipay.Constants;
import com.alibaba.alipay.PayActivity;
import com.alibaba.alipay.ThirdPartPayAction;
import com.alibaba.alipay.ThirdPartPayResult;


public class ThirdPartPayImpl extends ThirdPartPayAction.Stub {

    private static final String TAG = "TAG-ThirdPartPayImpl";
    private ThirdPartPayResult mCallback;
    private Context mContext;

    public ThirdPartPayImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void requestPay(String orderInfo, float payMoney, ThirdPartPayResult callBack) throws RemoteException {
        Log.d(TAG, "requestPay: ");
        this.mCallback = callBack;

        //第三方应用发起支付，打开一个支付页面
        Intent intent = new Intent(mContext, PayActivity.class);
        intent.putExtra(Constants.KEY_BILL_INFO, orderInfo);
        intent.putExtra(Constants.KEY_PAY_MONEY, payMoney);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 支付成功
     */
    public void paySuccess(float money) {
        if (mCallback != null) {
            try {
                Log.d(TAG, "paySuccess: ");
                mCallback.onPaySuccess(money);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 支付失败
     */
    public void payFailed(int errorCode, String msg) {
        if (mCallback != null) {
            try {
                mCallback.onPayFailed(errorCode, msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
