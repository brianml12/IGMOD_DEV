package com.bsewamods;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class RestoreBackupDev extends Activity {

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == 1255 && resultCode == -1 && data.getData() != null) {
            Brian.importDevSettings(this, data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("*/*");
        this.startActivityForResult(intent, 1255);
    }
}
