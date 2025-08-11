package com.ezequieldiaz.vacunatorioapp4.ui.aplicacion;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;
import com.ezequieldiaz.vacunatorioapp4.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AplicacionFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<String> mFecha;
    private MutableLiveData<List<String>> mHorarios;
    private MutableLiveData<Turno> mTurno;
    private MutableLiveData<Boolean> mConfirmar;
    private MutableLiveData<Boolean> mLimpiar;
    private MutableLiveData<String> mMensaje;
    private SingleLiveEvent<Long> mShowDatePickerEvent;
    private SharedPreferences sharedPreferences;

    public AplicacionFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getmFecha() {
        if (mFecha == null) {
            mFecha = new MutableLiveData<>();
        }
        return mFecha;
    }

    public MutableLiveData<List<String>> getmHorarios() {
        if (mHorarios == null) {
            mHorarios = new MutableLiveData<>();
        }
        return mHorarios;
    }

    public MutableLiveData<Turno> getmTurno() {
        if (mTurno == null) {
            mTurno = new MutableLiveData<>();
        }
        return mTurno;
    }

    public MutableLiveData<Boolean> getmConfirmar() {
        if (mConfirmar == null) {
            mConfirmar = new MutableLiveData<>();
        }
        return mConfirmar;
    }

    public MutableLiveData<Boolean> getmLimpiar() {
        if (mLimpiar == null) {
            mLimpiar = new MutableLiveData<>();
        }
        return mLimpiar;
    }

    public MutableLiveData<String> getmMensaje() {
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public SingleLiveEvent<Long> getShowDatePickerEvent() {
        if(mShowDatePickerEvent == null){
            mShowDatePickerEvent = new SingleLiveEvent<>();
        }
        return mShowDatePickerEvent;
    }

    public void cargarHorarios() {
        ArrayList<String> horarios = new ArrayList<>();
        horarios.add("Seleccione el horario de la cita");
        horarios.add("08:00");
        horarios.add("08:15");
        horarios.add("08:30");
        horarios.add("08:45");
        horarios.add("09:00");
        horarios.add("09:15");
        horarios.add("09:30");
        horarios.add("09:45");
        horarios.add("10:00");
        horarios.add("10:15");
        horarios.add("10:30");
        horarios.add("10:45");
        horarios.add("11:00");
        horarios.add("11:15");
        horarios.add("11:30");
        horarios.add("11:45");
        horarios.add("12:00");
        horarios.add("12:15");
        horarios.add("12:30");
        horarios.add("12:45");
        horarios.add("13:00");
        horarios.add("13:15");
        horarios.add("13:30");
        horarios.add("13:45");
        horarios.add("14:00");
        horarios.add("14:15");
        horarios.add("14:30");
        horarios.add("14:45");
        horarios.add("15:00");
        horarios.add("15:15");
        horarios.add("15:30");
        horarios.add("15:45");
        horarios.add("16:00");
        mHorarios.setValue(horarios);
    }

    public boolean fechaClickeada(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            long fechaSeleccionada = Calendar.getInstance().getTimeInMillis();
            mShowDatePickerEvent.setValue(fechaSeleccionada);
            return true;
        }
        return false;

    }

    public void fechaSeleccionada(int year, int month, int dayOfMonth) {
        String fechaElegida = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
        mFecha.setValue(fechaElegida);
    }

    public void cargarTurno(String fecha) {
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<Turno> call = apiService.obtenerTurnoPorFecha(token, fecha);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Turno> call, Response<Turno> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mTurno.setValue(response.body());
                    mConfirmar.setValue(true); // Mostrar botón
                } else {
                    mMensaje.setValue("No se encontró un turno para esa fecha");
                    mConfirmar.setValue(false); // Ocultar botón
                }
            }

            @Override
            public void onFailure(Call<Turno> call, Throwable throwable) {
                mMensaje.setValue("Error al obtener el turno");
                mConfirmar.setValue(false); // Ocultar botón
            }
        });
    }

    public void confirmarAplicacion() {
        String token = ApiClient.leerToken(getApplication());
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        if (token != null) {
            Call<Void> call = api.confirmarAplicacion(token, mTurno.getValue().getAplicacionId());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        mMensaje.setValue(response.message());
                        mConfirmar.setValue(false); // Ocultar el botón
                        Log.d("cancelar", "Vacuna aplicada correctamente");
                        mLimpiar.setValue(true);
                    } else {
                        mMensaje.setValue("No se pudo cancelar el turno");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    mMensaje.setValue("Error: " + throwable.getMessage());
                    Log.d("cancelar", "Falla: " + throwable.getMessage());
                }
            });
        }
    }
}