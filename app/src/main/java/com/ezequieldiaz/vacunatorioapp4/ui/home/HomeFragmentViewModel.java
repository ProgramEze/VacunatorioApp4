package com.ezequieldiaz.vacunatorioapp4.ui.home;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ezequieldiaz.vacunatorioapp4.util.SingleLiveEvent;

public class HomeFragmentViewModel extends ViewModel {
    private final SingleLiveEvent<Navegacion> navegacion = new SingleLiveEvent<>();

    public LiveData<Navegacion> getNavegacion() {
        return navegacion;
    }

    public void irAFragment(Navegacion navegacion) {
        Log.d("debug_nav", "navegacion changed: " + navegacion);
        this.navegacion.setValue(navegacion);
    }

    public static class Navegacion {
        public int destino;
        public Bundle args;

        public Navegacion(int destino, Bundle args) {
            this.destino = destino;
            this.args = args;
        }
    }
}
