package com.trinfosoft.teacherassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Trilokynath Wagh on 12/01/2018.
 */

public class BootService extends BroadcastReceiver{
    MainActivity act = new MainActivity();
    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Boot Complete",Toast.LENGTH_LONG).show();

        /****** For Start Activity *****
         Intent i = new Intent(context, MainActivity.class);
         i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         context.startActivity(i);*/

        /***** For start Service  ****/
        Intent myIntent = new Intent(context, Notification_Service.class);
        context.startService(myIntent);

    }

}
