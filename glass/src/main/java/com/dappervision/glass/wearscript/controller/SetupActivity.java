package com.dappervision.glass.wearscript.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dappervision.wearscript.Utils;
import com.dappervision.wearscript.events.ShutdownEvent;

public class SetupActivity extends Activity {
    private static final String TAG = "SetupActivity";
    private static final int REQUEST_SCAN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getEventBus().post(new ShutdownEvent());

        if(isZXingInstalled()) {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            startActivityForResult(intent, REQUEST_SCAN);
        } else {
            Toast.makeText(this, "This requires the ZXing scanner", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "QR: Got activity result: " + resultCode + " requestCode: " + requestCode);
        if (requestCode == REQUEST_SCAN) {
            String contents = null;
            if (resultCode == RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i(TAG, "QR: " + contents + " Format: " + format);
                Utils.SaveData(contents.getBytes(), "", false, "qr.txt");
            }
            finish();
        }
    }

    private boolean isZXingInstalled() {
        try {
            getPackageManager().getApplicationInfo("com.google.zxing.client.android", 0);
            return true;
        }
        catch(PackageManager.NameNotFoundException e){
            return false;
        }
    }
}
