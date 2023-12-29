package com.bsewamods;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import com.instagram.debug.devoptions.api.DeveloperOptionsLauncher;
import com.instagram.mainactivity.InstagramMainActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Brian implements View.OnLongClickListener {
    private final InstagramMainActivity mainActivity;

    public Brian(InstagramMainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static void onlongClickMenu(InstagramMainActivity activity, View v){
        Brian objBrian = new Brian(activity);
        v.setOnLongClickListener(objBrian);
    }

    static void showToast(Context ctx, String textoToast, int duracion){
        Toast.makeText(ctx, textoToast, duracion).show();
    }

    static boolean isWriteExternalStoragePermissionGranted(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean bandera;
            try {
                bandera = Integer.parseInt(Activity.class.getMethod("checkSelfPermission", new Class[]{String.class}).invoke(ctx, new Object[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).toString()) == 0;
            } catch (Exception e) {
                bandera = false;
            }
            if (!bandera) {
                return true;
            }
        }
        return false;
    }

    static void importDevSettings(Context ctx, Uri uri){
        try {
            if(isWriteExternalStoragePermissionGranted(ctx)){
                Brian.startPermissionDialogActivity(ctx);
            }else{
                InputStream FID = ctx.getContentResolver().openInputStream(uri);
                File archivoSalida = new File(ctx.getFilesDir(), "mobileconfig"+File.separator+"mc_overrides.json");
                if (!archivoSalida.exists()){
                    archivoSalida.createNewFile();
                }
                FileOutputStream FOS = new FileOutputStream(archivoSalida);
                byte[] buf = new byte[1024];
                int len;
                while ((len = FID.read(buf)) > 0) {
                    FOS.write(buf, 0, len);
                }
                showToast(ctx,"Imported file", Toast.LENGTH_LONG);
                FID.close();
                FOS.close();
            }
        }catch (Exception e){
            showToast(ctx,"Error: Could not import developer mode settings",Toast.LENGTH_LONG);
        }
    }

    static void startPermissionDialogActivity(Context ctx) {
        Intent intent = new Intent(ctx, PermissionDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        String[] strListaMenu = {"Open Developer Menu", "Create developer mode backup", "Restore developer mode backup"};
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setItems(strListaMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        InstagramMainActivity instagramMainActivity = mainActivity;
                        DeveloperOptionsLauncher.loadAndLaunchDeveloperOptions(v.getContext(), instagramMainActivity.All(), mainActivity, instagramMainActivity.A0H);
                        return;
                    case 1:
                        try {
                            if(Brian.isWriteExternalStoragePermissionGranted(v.getContext())){
                                Brian.startPermissionDialogActivity(v.getContext());
                            }else{
                                File archivoDev = new File(v.getContext().getFilesDir(), "mobileconfig"+File.separator+"mc_overrides.json");
                                if(archivoDev.exists()){
                                    File rutaSalida = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+File.separator+"InstaDev"+File.separator+"Developer_Mode_Backups"+File.separator);
                                    if(!rutaSalida.exists()){
                                        rutaSalida.mkdirs();
                                    }
                                    File archivoSalida = new File(rutaSalida, "BackupDev_"+new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date())+".json");
                                    if (!archivoSalida.exists()){
                                        archivoSalida.createNewFile();
                                    }
                                    FileInputStream FID = new FileInputStream(archivoDev);
                                    FileOutputStream FOS = new FileOutputStream(archivoSalida);
                                    byte[] buf = new byte[1024];
                                    int len;
                                    while ((len = FID.read(buf)) > 0) {
                                        FOS.write(buf, 0, len);
                                    }
                                    Brian.showToast(v.getContext(),"File exported in"+ archivoSalida.getPath(), Toast.LENGTH_LONG);
                                    FID.close();
                                    FOS.close();
                                }else{
                                    Brian.showToast(v.getContext(), "There is no configuration file to export", Toast.LENGTH_LONG);
                                }
                            }
                        }catch (Exception e){
                            Brian.showToast(v.getContext(),"Error: Could not export developer mode settings", Toast.LENGTH_LONG);
                        }
                        return;
                    case 2:
                        Intent intent = new Intent(v.getContext(), RestoreBackupDev.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                        return;
                    default:
                        return;
                }
            }
        });
        builder.create();
        builder.show();
        return true;
    }
}
