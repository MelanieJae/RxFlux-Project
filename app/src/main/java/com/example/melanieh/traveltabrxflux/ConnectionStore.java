package com.example.melanieh.traveltabrxflux;

import com.hardsoftstudio.rxflux.action.RxAction;
import com.hardsoftstudio.rxflux.dispatcher.Dispatcher;
import com.hardsoftstudio.rxflux.store.RxStore;
import com.hardsoftstudio.rxflux.store.RxStoreChange;

/**
 * Created by melanieh on 8/2/17.
 *
 * This is the specific store class for wi-fi connections in the RxFlux pattern.
 */

public class ConnectionStore extends RxStore implements ConnectionStoreInterface {

    public static ConnectionStore storeInstance;

    private ConnectionStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public static ConnectionStore get(Dispatcher dispatcher) {
        if (storeInstance == null) {
            storeInstance = new ConnectionStore(dispatcher);
        }
        return storeInstance;
    }

    // This method will be called every time the dispatcher post a RxAction into the bus
    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case ActionsInterface.WIFI_CONNECT:
                createConnection();
                break;
        }
        postChange(new RxStoreChange(ConnectionStoreInterface.STORE_ID, action));

    }

    @Override
    public void createConnection() {
    }

}
