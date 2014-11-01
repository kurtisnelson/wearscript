package com.dappervision.android.wearscript.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.dappervision.wearscript.Utils;
import com.dappervision.wearscript.events.ShutdownEvent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class SetupActivity extends FragmentActivity {
    private static final String TAG = "SetupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getEventBus().post(new ShutdownEvent());

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "QR: Got activity result: " + resultCode + " requestCode: " + requestCode);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            if(scanResult.getContents() != null) {
                Utils.SaveData(scanResult.getContents().getBytes(), "", false, "qr.txt");
            } else {
                Toast.makeText(this, "No code scanned", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}
