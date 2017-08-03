package com.example.melanieh.traveltabrxflux;

import com.hardsoftstudio.rxflux.action.RxError;
import com.hardsoftstudio.rxflux.store.RxStoreChange;

/**
 * Created by melanieh on 8/3/17.
 */

public interface ViewDispatch {

    void onRxStoreChanged(RxStoreChange change);

    void onRxError(RxError error);

    void onRxViewRegistered();

    void onRxViewUnRegistered();

    void onRxStoresRegister();

}
