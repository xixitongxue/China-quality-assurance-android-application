package com.zhibaowang.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by zhaoyuntao on 2017/11/30.
 */

public class ZDialogUtils {
    public static void show(Context context, String message, String string_button_ok, DialogInterface.OnClickListener onClickListener_ok, String string_button_cancel, DialogInterface.OnClickListener onClickListener_cancel) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(string_button_ok, onClickListener_ok).setNegativeButton(string_button_cancel, onClickListener_cancel).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        dialog = builder.create();
        dialog.setMessage(message);
        dialog.show();
    }
}
