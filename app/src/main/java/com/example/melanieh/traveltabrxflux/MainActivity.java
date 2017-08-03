package com.example.melanieh.traveltabrxflux;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hardsoftstudio.rxflux.RxFlux;
import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.dispatcher.RxBus;
import com.hardsoftstudio.rxflux.store.RxStoreChange;
import com.hardsoftstudio.rxflux.util.SubscriptionManager;

public class MainActivity extends AppCompatActivity implements ViewDispatch{

    private ToggleButton wifiToggle;
    private TextView connectMsgTV;
    private ClickActionsCreator clickActionsCreator;
    private ConnectionStore connectionStore;
    private Dispatcher dispatcher;
    public RxFlux rxFlux;
    private static final String TAG = "Main Activity";

    // request code for runtime persmission needed for COARSE LOCATION
    // permission for ReactiveWifi in the action creator class
    private int REQUEST_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((FluxApplication)getApplication()).initializeRxFlux();

        wifiToggle = (ToggleButton) findViewById(R.id.wifi_connect_btn);
        connectMsgTV = (TextView) findViewById(R.id.connected_msg);

        // get instances ofRXFlux architecture components (actioncreator, dispatcher and stores)
        dispatcher = Dispatcher.getInstance(RxBus.getInstance());
        clickActionsCreator = ClickActionsCreator.get(getApplicationContext(), dispatcher, SubscriptionManager.getInstance());
        connectionStore = ConnectionStore.get(dispatcher);

        wifiToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                clickActionsCreator.createWifiConnection();
            }
        });

        // permissions request prior to connecting to api
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

        }
    }

        /**
         * runtime permissions for ACCESS_FINE_LOCATION/ACCESS_COARSE_LOCATION
         **/

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
            if (requestCode == REQUEST_LOCATION) {
                if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // This is just a global runtime permission grant for the COARSE location permission
                    // for Reactive Wifi to observse wifi access points and state changes
                    // in the action creator class. Nothing is executed here.

                } else {
                    // Permission was denied or request was cancelled
                    Log.e(TAG, "User did not grant permission to " +
                            "access available wi-fi networks at the current location");
                }
            }
        }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onRxStoreChanged(RxStoreChange change) {
        if (change != null) {
            connectMsgTV.setText("Connected to TravelTab Wi-fi");
        }
    }

    @Override
    public void onRxError(RxError error) {

    }

    @Override
    public void onRxViewRegistered() {
        // register anything that is not an activity (e.g. customview or fragment)

    }

    @Override
    public void onRxViewUnRegistered() {
        // unregister anything that is not an activity (e.g. customview or fragment)


    }

    @Override
    public void onRxStoresRegister() {
        connectionStore.register();
    }

}
