package com.ezequieldiaz.vacunatorioapp4.ui.home;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeFragmentViewModel extends ViewModel {
    private MutableLiveData<Navegacion> navegacion = new MutableLiveData<>();

    public LiveData<Navegacion> getNavegacion() {
        return navegacion;
    }

    public void irAFragment(Navegacion navegacion) {
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