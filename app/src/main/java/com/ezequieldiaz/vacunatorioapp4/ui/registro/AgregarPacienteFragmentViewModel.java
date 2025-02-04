package com.ezequieldiaz.vacunatorioapp4.ui.registro;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarPacienteFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<String> resultadoGuardado = new MutableLiveData<>();

    public AgregarPacienteFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getResultadoGuardado() {
        return resultadoGuardado;
    }

    public void guardarPaciente(String nombre, String apellido, String dni, String telefono, String email) {
        // Crear un objeto Tutor (suponiendo que el paciente es un tutor)
        Tutor tutor = new Tutor();
        tutor.setNombre(nombre);
        tutor.setApellido(apellido);
        tutor.setDni(dni);
        tutor.setEmail(email);
        tutor.setTelefono(telefono);

        // Obtener el token de autenticación
        String token = ApiClient.leerToken(getApplication());

        // Llamar a la API para guardar el tutor
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        Call<Void> call = api.registrarTutor(token, dni, nombre, apellido, telefono, email);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultadoGuardado.setValue("Paciente/Tutor guardado correctamente");
                } else {
                    Log.d("salida", "Error al guardar el paciente/tutor");
                    Log.d("salida", "Código de estado: " + response.code());
                    Log.d("salida", "Mensaje de error: " + response.message());
                    resultadoGuardado.setValue("Error al guardar el paciente/tutor");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                resultadoGuardado.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}