package com.alibaba.alipay;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.alipay.pay.PayService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PayActivity extends AppCompatActivity {

    private TextView orderInfoTv;
    private TextView payMoneyTv;
    private EditText passwordInput;
    private Button payCommitBtn;

    private PayService.PayAction payAction;
    private Dialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        //绑定服务进行通讯
        doBindService();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            unbindService(mConnection);
            mConnection = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //用户取消支付
        if (payAction != null) {
            payAction.onUserCancel();
        }
    }

    /**
     * 绑定service
     */
    private void doBindService() {
        Intent intent = new Intent(this, PayService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            payAction = (PayService.PayAction) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            payAction = null;
        }
    };

    private void initView() {
        orderInfoTv = (TextView) findViewById(R.id.order_info_tv);
        payMoneyTv = (TextView) findViewById(R.id.pay_money_tv);
        passwordInput = (EditText) findViewById(R.id.password_input);
        payCommitBtn = (Button) findViewById(R.id.pay_commit_btn);
        loading = new MaterialAlertDialogBuilder(this).setView(new ProgressBar(this)).create();

        Intent intent = getIntent();
        String payInfo = intent.getStringExtra(Constants.KEY_BILL_INFO);
        float payMoney = intent.getFloatExtra(Constants.KEY_PAY_MONEY, 0);
        orderInfoTv.setText("支付信息：" + payInfo);
        payMoneyTv.setText("金额：" + payMoney + "元");
        //确认支付
        payCommitBtn.setOnClickListener(v -> {
            String pwd = passwordInput.getText().toString().trim();
            if (Constants.KEY_PASSWORD.equals(pwd) && payAction != null) {
                loading.show();
                new Handler().postDelayed(() -> {
                    if (loading.isShowing()) {
                        loading.dismiss();
                    }
                    payAction.pay(payMoney);
                    finish();
                }, 1000);
            } else {
                Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


}