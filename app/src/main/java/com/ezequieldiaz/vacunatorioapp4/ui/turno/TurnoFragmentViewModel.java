package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;
import com.ezequieldiaz.vacunatorioapp4.util.SingleLiveEvent;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TurnoFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<List<TipoDeVacuna>> mListaTipo;
    private MutableLiveData<List<String>> mRelaciones;
    private MutableLiveData<String> mFecha;
    private MutableLiveData<List<String>> mHorarios;
    private MutableLiveData<Boolean> mEsPaciente;
    private MutableLiveData<Paciente> mPaciente;
    private MutableLiveData<Tutor> mTutor;
    private SharedPreferences sharedPreferences;

    // LiveData para señalizar al Fragment que muestre el DatePickerDialog
    private SingleLiveEvent<Long> mShowDatePickerEvent = new SingleLiveEvent<>();

    public TurnoFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TipoDeVacuna>> getMListaTipo() {
        if (mListaTipo == null) {
            mListaTipo = new MutableLiveData<>();
        }
        return mListaTipo;
    }

    public LiveData<List<String>> getMRelaciones() {
        if (mRelaciones == null) {
            mRelaciones = new MutableLiveData<>();
        }
        return mRelaciones;
    }

    public LiveData<String> getMFecha() {
        if (mFecha == null) {
            mFecha = new MutableLiveData<>();
        }
        return mFecha;
    }

    public LiveData<List<String>> getMHorarios() {
        if (mHorarios == null) {
            mHorarios = new MutableLiveData<>();
        }
        return mHorarios;
    }

    public LiveData<Paciente> getMPaciente() {
        if (mPaciente == null) {
            mPaciente = new MutableLiveData<>();
        }
        return mPaciente;
    }

    public LiveData<Tutor> getMTutor() {
        if (mTutor == null) {
            mTutor = new MutableLiveData<>();
        }
        return mTutor;
    }

    public LiveData<Boolean> getMEsPaciente() {
        if (mEsPaciente == null) {
            mEsPaciente = new MutableLiveData<>();
        }
        return mEsPaciente;
    }

    // Getter para el evento del selector de fecha
    public SingleLiveEvent<Long> getShowDatePickerEvent() {
        return mShowDatePickerEvent;
    }

    public void cargarTipos() {
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<List<TipoDeVacuna>> call = apiService.getTiposDeVacunas(token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TipoDeVacuna>> call, Response<List<TipoDeVacuna>> response) {
                if (response.isSuccessful()) {
                    mListaTipo.setValue(response.body());
                } else {
                    Log.e("TurnoFragmentViewModel", "Error al obtener tipos de vacunas: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TipoDeVacuna>> call, Throwable t) {
                Log.e("TurnoFragmentViewModel", "Error al obtener tipos de vacunas: " + t.getMessage());
            }
        });
    }

    public void cargarRelaciones() {
        ArrayList<String> relaciones = new ArrayList<>();
        relaciones.add("Madre");
        relaciones.add("Padre");
        relaciones.add("Tutor");
        relaciones.add("Otro");
        mRelaciones.setValue(relaciones);
    }

    public void cargarHorarios() {
        ArrayList<String> horarios = new ArrayList<>();
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

    public void cargarSpinners() {
        cargarTipos();
        cargarRelaciones();
        cargarHorarios();
    }

    // Método llamado desde el Fragment cuando se toca el campo de fecha
    public void onDateInputClicked() {
        // Puedes pasar la fecha actual o cualquier otra fecha inicial que desees
        long initialDate = Calendar.getInstance().getTimeInMillis();
        mShowDatePickerEvent.setValue(initialDate);
    }

    // Método para actualizar la fecha seleccionada en el ViewModel
    public void onDateSelected(int year, int month, int dayOfMonth) {
        // Formatea la fecha como desees, por ejemplo dd/MM/yyyy
        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
        mFecha.setValue(selectedDate);
    }

    public void esPaciente(String dni){
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<Paciente> call = apiService.getPaciente(token, Integer.parseInt(dni));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                if (response.isSuccessful()) {
                    mEsPaciente.setValue(true);
                } else {
                    mEsPaciente.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Paciente> call, Throwable throwable) {
                mEsPaciente.setValue(false);
            }
        });
    }


    public void buscarDNIPaciente(String dni){
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<Paciente> call = apiService.getPaciente(token, Integer.parseInt(dni));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                if (response.isSuccessful()) {
                    mPaciente.setValue(response.body());
                } else {
                    Log.e("TurnoFragmentViewModel", "Error al obtener un paciente: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Paciente> call, Throwable throwable) {

            }
        });
    }

    public void buscarDNITutor(String dni){
        ApiClient.MisEndPoints apiService = ApiClient.getEndPoints();
        String token = ApiClient.leerToken(getApplication());
        Call<Tutor> call = apiService.getTutor(token, Integer.parseInt(dni));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Tutor> call, Response<Tutor> response) {
                if (response.isSuccessful()) {
                    mTutor.setValue(response.body());
                } else {
                    Log.e("TurnoFragmentViewModel", "Error al obtener el tutor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Tutor> call, Throwable throwable) {

            }
        });
    }

    public void guardarTurno(Turno turno) {
        try {
            turno.setPacienteId(mPaciente.getValue().getId());
            turno.setTutorId(mTutor.getValue().getId());
            String token = ApiClient.leerToken(getApplication());
            sharedPreferences = getApplication().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String matricula = sharedPreferences.getString("matricula", "00000000");
            Log.d("salida", turno.getRelacionTutor()+"");
            if (token != null) {
                ApiClient.MisEndPoints api = ApiClient.getEndPoints();
                Call<Turno> call = api.registrarTurno(token, turno.getPacienteId(), turno.getTipoDeVacunaId(), turno.getTutorId(), matricula, 0, turno.getCita(), turno.getRelacionTutor());
                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Turno> call, Response<Turno> response) {
                        if (response.isSuccessful()) {
                            Log.d("salida", response.isSuccessful()+"");
                            Toast.makeText(getApplication(), "Turno dado de alta con exito", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(), "Falla en el dado de alta del Turno", Toast.LENGTH_LONG).show();
                            Log.d("salida", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Turno> call, Throwable throwable) {
                        Log.d("salida", "Falla: " + throwable.getMessage());
                    }
                });
            }
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();

        }
    }
}
