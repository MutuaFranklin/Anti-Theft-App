package com.track.function;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeaturesFunction {

    private Context tContext;
    public FeaturesFunction(Context context) {
        tContext = context;
    }

    public void toaster(String message) {
      //  Toast.makeText(tContext, message, Toast.LENGTH_LONG).show();
    }

    public AlertDialog.Builder alertDialog(String title, String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(tContext);
        bld.setTitle(title);
        bld.setMessage(message);

        bld.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        bld.create().show();
        return bld;
    }

    public ProgressDialog progressDialog(String title, String body) {
        ProgressDialog loadingDialog = new ProgressDialog(tContext, 0);
        loadingDialog.setIndeterminate(true);
        loadingDialog.setMessage(body);
        loadingDialog.setCancelable(true);
        loadingDialog.show();
        return loadingDialog;
    }

    public String getSimPhoneNumber() {
        try {
            if (ContextCompat.checkSelfPermission(tContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                toaster("Check SMS permissions");
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) tContext.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    return telephonyManager.getLine1Number();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @SuppressLint("HardwareIds")
    public String getImei() {
        try {
            if (ContextCompat.checkSelfPermission(tContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                toaster("Check SMS permissions");
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) tContext.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    return telephonyManager.getImei();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDeviceName() {
        return Build.MODEL;
    }

    public String getModelNumber() {
        return Build.DEVICE;
    }

    public String getTimeDate() {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return time.format(new Date());
    }
}
