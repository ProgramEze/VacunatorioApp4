package com.ezequieldiaz.vacunatorioapp4.ui.registro;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarPacienteFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<String> resultado = new MutableLiveData<>();

    public AgregarPacienteFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getResultado() {
        return resultado;
    }

    public void guardarPaciente(String nombre, String apellido, String dni, String fechaNacimiento, String genero) {
        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDni(dni);
        paciente.setFechaDeNacimiento(fechaNacimiento);
        paciente.setGenero(genero);

        String token = ApiClient.leerToken(getApplication());

        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        Call<Void> call = api.registrarPaciente(token, dni, nombre, apellido, fechaNacimiento, genero);
        Log.d("fechaDeNacimiento", fechaNacimiento.toString());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue("Paciente guardado correctamente");
                } else {
                    //  Captura y muestra el error
                    int errorCode = response.code();
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody().string();
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error al leer el cuerpo del error", e);
                    }
                    if (errorCode == 400 && errorBody != null) {
                        resultado.setValue(errorBody);
                    } else {
                        resultado.setValue("Error al guardar el tutor: " + errorCode + " - " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Error al guardar paciente: " + t.getMessage());
                resultado.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
