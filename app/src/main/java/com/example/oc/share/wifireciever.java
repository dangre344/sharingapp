package com.example.oc.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

public class wifireciever extends BroadcastReceiver {
    private WifiP2pManager manager;
    private Channel channel;
    private MainActivity activity;

    public wifireciever(WifiP2pManager manager, Channel channel,
                                       MainActivity activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {

    String action = intent.getAction();
    if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
        int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
        if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
            activity.setIsWifiP2pEnabled(true);
        } else {
            activity.setIsWifiP2pEnabled(false);
            activity.resetData();
        }
    } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
        if (manager != null) {
            manager.requestPeers(channel, (PeerListListener) activity.getFragmentManager()
                    .findFragmentById(R.id.fragment_list));
        }
    } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
        if (manager == null) {
            return;
        }
        NetworkInfo networkInfo = (NetworkInfo) intent
                .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
        if (networkInfo.isConnected()) {
            DeviceDetailFragment fragment = (DeviceDetailFragment) activity
                    .getFragmentManager().findFragmentById(R.id.fragment_detail);
            manager.requestConnectionInfo(channel, fragment);
        } else {
            activity.resetData();
        }
    }
}
}
