package com.alibaba.alipay.pay;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.alipay.pay.ThirdPartPayImpl;

public class PayService extends Service {

    private static final String TAG = "TAG-PayService";
    private ThirdPartPayImpl thirdPartPay;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onBind: Action ---> " + action);
        if ("com.alibaba.alipay.THIRD_PART_PAY".equals(action)) {
            Log.d(TAG, "onBind: ");
            thirdPartPay = new ThirdPartPayImpl(this);
            return thirdPartPay;
        }
        return new PayAction();
    }

    public class PayAction extends Binder {

        //支付的方法
        public void pay(float money) {
            Log.d(TAG, "pay: " + money);
            if (thirdPartPay != null) {
                thirdPartPay.paySuccess(money);
            }
        }

        //用户取消支付
        public void onUserCancel() {
            Log.d(TAG, "onUserCancel:");
            if (thirdPartPay != null) {
                thirdPartPay.payFailed(1, "用户取消支付");
            }
        }
    }

}
