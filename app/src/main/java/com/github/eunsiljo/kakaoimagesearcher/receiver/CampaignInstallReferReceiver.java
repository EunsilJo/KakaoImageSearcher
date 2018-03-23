package com.github.eunsiljo.kakaoimagesearcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.analytics.CampaignTrackingReceiver;

/**
 * Created by jjo on 2018. 3. 24..
 */

public class CampaignInstallReferReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        new CampaignTrackingReceiver().onReceive(context, intent);
    }
}
