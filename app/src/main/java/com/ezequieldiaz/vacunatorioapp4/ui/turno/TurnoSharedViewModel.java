package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TurnoSharedViewModel extends ViewModel {
    private final MutableLiveData<String> fechaSeleccionada = new MutableLiveData<>();
    private final MutableLiveData<String> horaSeleccionada = new MutableLiveData<>();

    public LiveData<String> getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public LiveData<String> getHoraSeleccionada() {
        return horaSeleccionada;
    }

    public void setFechaHora(String fecha, String hora) {
        fechaSeleccionada.setValue(fecha);
        horaSeleccionada.setValue(hora);
    }
}
