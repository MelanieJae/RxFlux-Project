package com.example.melanieh.traveltabrxflux;

import android.app.Application;

import com.hardsoftstudio.rxflux.RxFlux;

/**
 * Created by melanieh on 8/2/17.
 */

public class FluxApplication extends Application {

    public RxFlux initializeRxFlux() {
        return RxFlux.init(this);
    }

}
