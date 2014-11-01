package com.dappervision.android.wearscript.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.crittercism.app.CrittercismConfig;
import com.dappervision.android.wearscript.BuildConfig;
import com.dappervision.android.wearscript.MobileWearScriptInfo;
import com.dappervision.wearscript.Utils;
import com.dappervision.wearscript.launcher.WearScriptInfo;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MobileMainActivity extends ActionBarActivity implements ScriptListFragment.Callbacks {
    private static final String TAG = "MainActivity";
    private static final boolean DBG = false;
    private static final String GISTS_PATH = "gists/";
    private static final String WEARSCRIPT_PATH = Utils.dataPath() + GISTS_PATH;
    private boolean launchScriptList = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());
        String gist = Utils.getPackageGist(this);
        if (DBG) Log.d(TAG, "Gist id is " + gist);
        if (gist != null) {
            if (DBG) Log.d(TAG, "Found package name gist id " + gist);
            byte[] manifestData = Utils.LoadData(GISTS_PATH + gist, "manifest.json");
            if (manifestData != null) {
                JSONObject manifest = (JSONObject) JSONValue.parse(new String(manifestData));
                if (manifest != null && manifest.containsKey("name")) {
                    String filePath = WEARSCRIPT_PATH + gist + "/" + "glass.html";
                    WearScriptInfo wsInfo = buildInfoForGist(manifest, filePath);
                    Intent intent = wsInfo.getIntent();
                    int flags = intent.getFlags() | Intent.FLAG_ACTIVITY_SINGLE_TOP;
                    intent.setFlags(flags);
                    startActivity(intent);
                    launchScriptList = false;
                } else {
                    if (DBG) Log.d(TAG, "Didn't find gist name in manifest.");
                }
            } else {
                // Load from apk assets
                if (DBG) Log.d(TAG, "Loading script from apk assets");
                String filePath = "/android_asset/" + gist + "/" + "glass.html";
                WearScriptInfo wsInfo = buildInfoForApkAsset(null);
                Intent intent = wsInfo.getIntent();
                int flags = intent.getFlags() | Intent.FLAG_ACTIVITY_SINGLE_TOP;
                intent.setFlags(flags);
                startActivity(intent);
                launchScriptList = false;
            }
        }

        if(!BuildConfig.DEBUG) {
            CrittercismConfig config = new CrittercismConfig();
            config.setLogcatReportingEnabled(true);
            Crittercism.initialize(getApplicationContext(), "544090b4466eda745b000005", config);
        }
    }

    protected WearScriptInfo buildInfoForApkAsset(String filePath) {
        return buildInfoForPath("APK Script", filePath);
    }

    protected WearScriptInfo buildInfoForGist(JSONObject manifest, String filePath) {
        return buildInfoForPath((String) manifest.get("name"), filePath);
    }

    protected int getLayoutResId() {
        return com.dappervision.wearscript.R.layout.activity_fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DBG) Log.d(TAG, "LifeCycle: onResume");
        if (launchScriptList) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = createFragment();
            manager.beginTransaction()
                    .replace(com.dappervision.wearscript.R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    public void onScriptSelected(WearScriptInfo scriptInfo) {
        startActivity(scriptInfo.getIntent());
    }

    protected Fragment createFragment() {
        return ScriptListFragment.newInstance();
    }

    protected WearScriptInfo buildInfoForPath(String name, String filePath) {
        return new MobileWearScriptInfo(this, name, filePath);
    }
}
