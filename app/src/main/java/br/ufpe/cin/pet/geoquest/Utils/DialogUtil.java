package br.ufpe.cin.pet.geoquest.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by mras on 20/06/16.
 */
public class DialogUtil {
    private static void dialogBox(Context context, String title, String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title)
                          .setMessage(message)
                          .setCancelable(false)
                          .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                              }
                          });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
