package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.app.Application;
import android.content.Context;

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

    public LiveData<String> getMensajeError() {
        if (mMensajeError == null) {
            mMensajeError = new MutableLiveData<>();
        }
        return mMensajeError;
    }

    public void cargarFechas(int mes, int anio) {
        ApiClient.MisEndPoints mep = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication().getApplicationContext());
        if (token != null) {
            Call<List<FechasResponse>> call = mep.getDisponibilidadMes(token, anio, mes);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<FechasResponse>> call, Response<List<FechasResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        mFechas.postValue(response.body());
                    } else {
                        mFechas.postValue(new ArrayList<>());
                        mMensajeError.postValue("Error al cargar disponibilidades: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<FechasResponse>> call, Throwable t) {
                    mFechas.postValue(new ArrayList<>());
                    mMensajeError.postValue("Fallo al cargar disponibilidades: " + t.getMessage());
                }
            });
        } else {
            mMensajeError.postValue("Token nulo");
        }
    }
}

