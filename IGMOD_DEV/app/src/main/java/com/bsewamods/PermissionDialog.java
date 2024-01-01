package com.bsewamods;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class PermissionDialog extends Activity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(FULLSCREEN_MODE_REQUEST_ENTER);
        try {
            Activity.class.getMethod("requestPermissions", String[].class, Integer.TYPE).invoke(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 682394);
        } catch (Exception e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==682394) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                Brian.showToast(this, "Error! Please enable app permissions so you can create and read backups", Toast.LENGTH_SHORT);
            }
            finish();
        }
    }
}
