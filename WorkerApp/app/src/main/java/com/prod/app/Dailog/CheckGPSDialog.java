package com.prod.app.Dailog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prod.app.GeoLocation.DialogClickListener;
import com.prod.app.GeoLocation.GPSCheckPoint;
import com.prod.app.Interfaces.GpsOnListener;
import com.prod.app.R;


public class CheckGPSDialog {

    Activity activity;
    GpsOnListener gpsOnListner;
    AlertDialog.Builder alertDialog;
    Dialog dialog;
    DialogClickListener dialogClickListner;
    int count = 0;

    public CheckGPSDialog(Activity context) {
        this.activity = context;
        this.gpsOnListner = (GpsOnListener) activity;
    }

    public void showDialog(DialogClickListener listner) {
        try {
            dialogClickListner = listner;
            alertDialog = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View v = inflater.inflate(R.layout.use_location_dialog, null);

            alertDialog.setView(v);
            if (dialog == null) {
                dialog = alertDialog.create();
            }

            LinearLayout positive_button = (LinearLayout) v.findViewById(R.id.positive_button);
            LinearLayout negative_button = (LinearLayout) v.findViewById(R.id.negative_button);

            TextView title = (TextView) v.findViewById(R.id.title);
            TextView message = (TextView) v.findViewById(R.id.message);
            TextView suggetion = (TextView) v.findViewById(R.id.suggetion);

            TextView negative_txt = (TextView) v.findViewById(R.id.negative_txt);
            TextView positive_txt = (TextView) v.findViewById(R.id.positive_txt);



            positive_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogClickListner.positiveListener(activity, dialog);

                    /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(intent);
                    gpsOnHandler();*/


                }
            });

            negative_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogClickListner.negativeListener(activity, dialog);
                    // gpsOnListner.gpsPermissionDenied(1);
                }
            });

            dialog.show();


        } catch (Exception e) {

        }

    }

    private void gpsOnHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isGPSEnabled = GPSCheckPoint.gpsProviderEnable(activity);
                boolean isNetworkEnabled = GPSCheckPoint.networkProviderEnable(activity);
                if (!isGPSEnabled && !isNetworkEnabled) {
                    if (count < 3) {
                        gpsOnHandler();
                        count++;
                    } else {
                        gpsOnListner.gpsStatus(true);
                        count = 3;
                    }

                } else {
                    gpsOnListner.gpsStatus(true);
                }
            }
        }, 1000);
    }
}