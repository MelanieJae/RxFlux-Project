package com.example.melanieh.traveltabrxflux;


import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.github.pwittchen.reactivewifi.ReactiveWifi;
import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.action.RxActionCreator;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by melanieh on 8/2/17.
 *
 * This class creates the actions and posts them to the Dispatcher in an RxFlux pattern.
 */

public class ClickActionsCreator extends RxActionCreator implements ActionsInterface {

    private static ClickActionsCreator instance;
    private Context context;

    private ClickActionsCreator (Context context, Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
        this.context = context;
    }

    public static ClickActionsCreator get(Context context, Dispatcher dispatcher, SubscriptionManager manager) {
        if (instance == null) {
            instance = new ClickActionsCreator(context, dispatcher, manager);
        }
        return instance;
    }


    public void createWifiConnection() {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // add to manager's available networks
        wifiMgr.setWifiEnabled(true);

        // set up connection to TravelTabWifi

        WifiConfiguration conf = new WifiConfiguration();
        String networkSSID = BuildConfig.SSID;
        String networkPass = BuildConfig.PASSKEY;
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes

        conf.wepKeys[0] = "\"" + networkPass + "\"";
        conf.wepTxKeyIndex = 0;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        List<WifiConfiguration> list = wifiMgr.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiMgr.disconnect();
                wifiMgr.enableNetwork(i.networkId, true);
                wifiMgr.reconnect();
                wifiMgr.addNetwork(conf);

                break;
            }
        }

        final RxAction action = newRxAction(WIFI_CONNECT);
        // checks for existing actions to cancel them before starting a new one.
        if (hasRxAction(action)) return; // Return or cancel previous


        addRxAction(action, ReactiveWifi.observeWifiAccessPointChanges(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());

        postRxAction(action);

    }


}
