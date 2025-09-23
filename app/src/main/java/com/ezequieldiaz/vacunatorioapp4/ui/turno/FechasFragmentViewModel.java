package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ezequieldiaz.vacunatorioapp4.model.FechasResponse;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// DisponibilidadFragmentViewModel.java
public class FechasFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<List<FechasResponse>> mFechas;
    private String darTurnoOCargarTurno;
    private MutableLiveData<String> mMensajeError;

    public FechasFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<FechasResponse>> getMFechas() {
        if (mFechas == null) {
            mFechas = new MutableLiveData<>();
        }
        return mFechas;
    }

    public String getDarTurnoOCargarTurno() {
        return darTurnoOCargarTurno;
    }

    public void cargarFechas(int mes, int anio) {
        String token = ApiClient.leerToken(getApplication());
        Call<List<FechasResponse>> call = ApiClient.getEndPoints().getDisponibilidadMes(token, anio, mes);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<FechasResponse>> call, Response<List<FechasResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    darTurnoOCargarTurno = "Dar turno";
                    mFechas.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<FechasResponse>> call, Throwable t) {
                Log.e("FechasViewModel", "Error al cargar fechas: " + t.getMessage());
            }
        });
    }

    public void cargarTurnos(int dniPaciente, int mes, int anio) {
        String token = ApiClient.leerToken(getApplication());
        Call<List<FechasResponse>> call = ApiClient.getEndPoints().getTurnosPorPaciente(token, dniPaciente, anio, mes);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<FechasResponse>> call, Response<List<FechasResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    darTurnoOCargarTurno = "Cargar turno";
                    mFechas.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<FechasResponse>> call, Throwable t) {
                Log.e("FechasViewModel", "Error al cargar turnos: " + t.getMessage());
            }
        });
    }
}

